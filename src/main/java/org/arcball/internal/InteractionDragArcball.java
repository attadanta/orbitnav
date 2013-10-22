package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Arcball interaction.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionDragArcball {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDragArcball(DoubleProperty rotationAngle,
            DoubleProperty rotationAxisX, DoubleProperty rotationAxisY, DoubleProperty rotationAxisZ)
    {
        this.rotationAngle.bindBidirectional(rotationAngle);
        this.rotationAxisX.bindBidirectional(rotationAxisX);
        this.rotationAxisY.bindBidirectional(rotationAxisY);
        this.rotationAxisZ.bindBidirectional(rotationAxisZ);
        
        ChangeListener<Number> whChangeListener = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ob, Number oldNumber, Number newNumber) {
                updateArcballCenterAndRadius();
            }
        };
        width.addListener(whChangeListener);
        height.addListener(whChangeListener);
    }
    
    public void attachToScene(Scene scene) {
        dragHelper.attachToScene(scene);
        width.bind(scene.widthProperty());
        height.bind(scene.heightProperty());
    }
    
    public void attachToSubScene(SubScene subscene) {
        dragHelper.attachToSubScene(subscene);
        width.bind(subscene.widthProperty());
        height.bind(subscene.heightProperty());
    }
    
    public void detachFromScene(Scene scene) {
        dragHelper.detachFromScene(scene);
        width.unbind();
        height.unbind();
    }
    
    public void detachFromSubScene(SubScene subscene) {
        dragHelper.detachFromSubScene(subscene);
        width.unbind();
        height.unbind();
    }
    
    public ObjectProperty<MouseButton> triggerButtonProperty() { return triggerButton; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static final MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.PRIMARY;
    
    private final DoubleProperty rotationAxisX = new SimpleDoubleProperty(this, "rotationAxisX", 0);
    private final DoubleProperty rotationAxisY = new SimpleDoubleProperty(this, "rotationAxisY", 0);
    private final DoubleProperty rotationAxisZ = new SimpleDoubleProperty(this, "rotationAxisZ", 0);
    private final DoubleProperty rotationAngle = new SimpleDoubleProperty(this, "rotationAngle", 0);
    private final ObjectProperty<MouseButton> triggerButton =
            new SimpleObjectProperty<MouseButton>(this, "triggerButton", DEFAULT_TRIGGER_BUTTON);

    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", 1.0);
    private final DoubleProperty height = new SimpleDoubleProperty(this, "height", 1.0);
    private double centerX;  // centerX = width / 2
    private double centerY;  // centerY = height / 2
    private double radius;   // radius = max(width, height) / 3
    
    private final MutableQuat startQuat = new MutableQuat();
    private final MutableQuat deltaQuat = new MutableQuat();
    private final MutableQuat finalQuat = new MutableQuat();
    private final MutableVec startArcballVector = new MutableVec();    
    private final MutableVec currentArcballVector = new MutableVec();
    private final MutableVec rotationAxis = new MutableVec();

    private final DragHelper dragHelper = new DragHelper(triggerButton, new DragHandlerAdaptor() {
        @Override public void handleClick(MouseEvent me) {
            projectScenePointToSphere(startArcballVector, me.getSceneX(), me.getSceneY());
            startQuat.setAxisAngle(rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get(), rotationAngle.get());
        }
        @Override public void handleDrag(MouseEvent me, double deltaX, double deltaY) {
            projectScenePointToSphere(currentArcballVector, me.getSceneX(), me.getSceneY());
            // find the quaternion rotation between the initial arcball vector and the current arcball vector
            rotationAxis.setToCross(startArcballVector, currentArcballVector);
            final double angleRadians = -Math.acos(Math.min(1.0, startArcballVector.dot(currentArcballVector)));
            deltaQuat.setAxisAngle(rotationAxis, Math.toDegrees(angleRadians));
            // set the current rotation
            if (angleRadians != 0) {
                finalQuat.set(startQuat);
                finalQuat.multiplyBy(deltaQuat);
                finalQuat.normalize();
                finalQuat.getAxis(rotationAxis);
                rotationAxisX.set(rotationAxis.x);
                rotationAxisY.set(rotationAxis.y);
                rotationAxisZ.set(rotationAxis.z);
                rotationAngle.set(Util.normalizeAngle(finalQuat.getAngleDegrees()));
            }
        }
    });
    
    private void updateArcballCenterAndRadius() {
        centerX = width.get() / 2.0;
        centerY = height.get() / 2.0;
        radius = Math.max(width.get(), height.get()) / 3.0;
    }
    
    private void projectScenePointToSphere(MutableVec result, double sceneX, double sceneY) {
        final double xp = (sceneX - centerX) / radius;
        final double yp = (sceneY - centerY) / radius;
        final double l2 = xp * xp + yp * yp;
        if (l2 <= 1.0) {
            result.set(xp, yp, -Math.sqrt(1.0 - l2));
        } else {
            final double l = Math.sqrt(l2);
            result.set(xp / l, yp / l, 0);
        }
    }
        
    /**
     * Mutable 3D vector.
     */
    private static final class MutableVec {
        public void set(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public void setToCross(MutableVec a, MutableVec b) {
            x = a.y * b.z - a.z * b.y;
            y = a.z * b.x - a.x * b.z;
            z = a.x * b.y - a.y * b.x;
        }
        public double dot(MutableVec v) { return (x * v.x + y * v.y + z * v.z); }
        public double x;
        public double y;
        public double z;
    }
    
    /**
     * Mutable quaternion.
     * Stored as a + b*i + c*j + d*k.
     */
    private static final class MutableQuat {
        public void set(MutableQuat q) {
            a = q.a;
            b = q.b;
            c = q.c;
            d = q.d;
        }
        public void setAxisAngle(double x, double y, double z, double angleDegrees) {
            final double a2 = Math.toRadians(angleDegrees) / 2.0;
            a = Math.cos(a2);
            final double coeff = Math.sin(a2) / Math.sqrt(x * x + y * y + z * z);
            b = x * coeff;
            c = y * coeff;
            d = z * coeff;
        }
        public void setAxisAngle(MutableVec axis, double angleDegrees) {
            setAxisAngle(axis.x, axis.y, axis.z, angleDegrees);
        }
        public double getAngleDegrees() { return Math.toDegrees(getAngleRadians()); }
        public double getAngleRadians() { return 2.0 * Math.acos(a); }
        public void getAxis(MutableVec result) {
            final double o = 1.0 / Math.sin(Math.acos(a));
            result.set(b * o, c * o, d * o);
        }
        public void multiplyBy(MutableQuat q) {
            final double a1 = a;
            final double b1 = b;
            final double c1 = c;
            final double d1 = d;
            a = a1 * q.a - b1 * q.b - c1 * q.c - d1 * q.d;
            b = a1 * q.b + b1 * q.a + c1 * q.d - d1 * q.c;
            c = a1 * q.c - b1 * q.d + c1 * q.a + d1 * q.b;
            d = a1 * q.d + b1 * q.c - c1 * q.b + d1 * q.a;
        }
        public void normalize() {
            final double l = Math.sqrt(b * b + c * c + d * d);
            b /= l;
            c /= l;
            d /= l;
        }
        private double a;
        private double b;
        private double c;
        private double d;
    }
    
}
