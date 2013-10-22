package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public final class InteractionArcball {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionArcball(DoubleProperty rotationAngle,
            DoubleProperty rotationAxisX, DoubleProperty rotationAxisY, DoubleProperty rotationAxisZ)
    {
        this.rotationAngle.bindBidirectional(rotationAngle);
        this.rotationAxisX.bindBidirectional(rotationAxisX);
        this.rotationAxisY.bindBidirectional(rotationAxisY);
        this.rotationAxisZ.bindBidirectional(rotationAxisZ);
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
    
    private final MutableVec arcballVector = new MutableVec();
    private final MutableVec temp_arcballVec = new MutableVec();
    private final MutableQuat initialQuat = new MutableQuat();
    private final MutableQuat quat = new MutableQuat();
    
    private final DragHelper dragHelper = new DragHelper(triggerButton, new DragHandlerAdaptor() {
        @Override public void handleClick(MouseEvent me) {
            projectScenePointToSphere(arcballVector, me.getSceneX(), me.getSceneY());
            initialQuat.setAxisAngle(rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get(),
                    rotationAngle.get());
        }
        @Override public void handleDrag(MouseEvent me, double deltaX, double deltaY) {
            projectScenePointToSphere(temp_arcballVec, me.getSceneX(), me.getSceneY());
            quatBetweenVecs(quat, arcballVector, temp_arcballVec);
            if (quat.getAngleRadians() != 0) {
                incrementRotation(quat);
            }
            //arcballVector.set(temp_arcballVec);
        }
    });
    
    private void projectScenePointToSphere(MutableVec result, double sceneX, double sceneY) {
        final double centerX = width.get() / 2.0;
        final double centerY = height.get() / 2.0;
        final double radius = Math.max(width.get(), height.get()) / 3.0;
        final double xp = (sceneX - centerX) / radius;
        final double yp = (sceneY - centerY) / radius;
        final double l2 = xp * xp + yp * yp;
        result.set(xp, yp, (l2 <= 1.0) ? -Math.sqrt(1.0 - l2) : 0.0);
        result.normalize();
    }
    
    private final MutableVec temp_axis = new MutableVec();
    private void quatBetweenVecs(MutableQuat result, MutableVec v1, MutableVec v2) {
        temp_axis.setToCross(v1, v2);
        final double angle = -Math.acos(Math.min(1.0, v1.dot(v2)));
        result.setAxisAngle(temp_axis, Math.toDegrees(angle));
    }
    
    private final MutableQuat curRot = new MutableQuat();
    private final MutableVec newAxis = new MutableVec();
    private void incrementRotation(MutableQuat quat) {
        //curRot.setAxisAngle(rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get(), rotationAngle.get());
        curRot.set(initialQuat);
        curRot.setMultiplyBy(quat);
        curRot.normalize();
        curRot.getAxis(newAxis);
        final double newAngle = curRot.getAngleDegrees();
        rotationAxisX.set(newAxis.getX());
        rotationAxisY.set(newAxis.getY());
        rotationAxisZ.set(newAxis.getZ());
        rotationAngle.set(newAngle);
    }
    
    /**
     * Mutable 3D vector.
     */
    private static final class MutableVec {
        public void set(MutableVec v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }
        public void set(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public void normalize() {
            final double l = Math.sqrt(x * x + y * y + z * z);
            x /= l;
            y /= l;
            z /= l;
        }
        public void setToCross(MutableVec a, MutableVec b) {
            x = a.y * b.z - a.z * b.y;
            y = a.z * b.x - a.x * b.z;
            z = a.x * b.y - a.y * b.x;
        }
        public double dot(MutableVec v) { return (x * v.x + y * v.y + z * v.z); }
        public double getX() { return x; }
        public double getY() { return y; }
        public double getZ() { return z; }
        private double x;
        private double y;
        private double z;
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
            setAxisAngle(axis.getX(), axis.getY(), axis.getZ(), angleDegrees);
        }
        public double getAngleDegrees() { return Math.toDegrees(getAngleRadians()); }
        public double getAngleRadians() { return 2.0 * Math.acos(a); }
        public void getAxis(MutableVec result) {
            final double o = 1.0 / Math.sin(Math.acos(a));
            result.set(b * o, c * o, d * o);
        }
        public void setMultiplyBy(MutableQuat q) {
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
