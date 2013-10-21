package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Transform;

public final class InteractionPan {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionPan(DoubleProperty originX, DoubleProperty originY, DoubleProperty originZ,
                          ObjectProperty<Transform> rotationComponent, DoubleProperty distanceFromOrigin,
                          DoubleProperty hfov)
    {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.rotation = rotationComponent;
        this.distanceFromOrigin = distanceFromOrigin;
        this.hfov = hfov;
        
        this.distanceFromOrigin.addListener(coeffUpdater);
        this.hfov.addListener(coeffUpdater);
        this.width.addListener(coeffUpdater);
    }
        
    public void attachToScene(Scene scene) { 
        dragHelper.attachToScene(scene);
        width.bind(scene.widthProperty());
    }
    
    public void detachFromScene(Scene scene) { 
        dragHelper.detachFromScene(scene);
        width.unbind();
    }
    
    public void attachToSubScene(SubScene subscene) {
        dragHelper.attachToSubScene(subscene);
        width.bind(subscene.widthProperty());
    }
    
    public void detachFromSubScene(SubScene subscene) { 
        dragHelper.detachFromSubScene(subscene);
        width.unbind();
    }
    
    public void setTriggerButton(MouseButton button) { triggerButton = button; }
    
    public MouseButton getTriggerButton() { return triggerButton; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final static MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.SECONDARY;
    private final static Point3D STARTING_X_VEC = new Point3D(1, 0, 0);
    private final static Point3D STARTING_Y_VEC = new Point3D(0, 1, 0);
    
    private final DoubleProperty originX;
    private final DoubleProperty originY;
    private final DoubleProperty originZ;
    private final ObjectProperty<Transform> rotation;
    private final DoubleProperty distanceFromOrigin;
    private final DoubleProperty hfov;
    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", 1.0);
    private double coeff;
    
    private MouseButton triggerButton = DEFAULT_TRIGGER_BUTTON;
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            // find local x and y vector shifts for the camera
            final Point3D dxVec = rotation.get().transform(STARTING_X_VEC).multiply(coeff * deltaX);
            final Point3D dyVec = rotation.get().transform(STARTING_Y_VEC).multiply(coeff * deltaY);
            // perform shifts along x and y
            originX.set(originX.get() - dxVec.getX() - dyVec.getX());
            originY.set(originY.get() - dxVec.getY() - dyVec.getY());
            originZ.set(originZ.get() - dxVec.getZ() - dyVec.getZ());
        }
    };

    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
    private final ChangeListener<Number> coeffUpdater = new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
            final double fovRad = hfov.get() * Math.PI / 180.0;
            coeff = 2.0 * distanceFromOrigin.get() * Math.tan(fovRad / 2.0) / width.get();
        }
    };
    
}
