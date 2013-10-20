package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;

public final class InteractionPan {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionPan(DoubleProperty originX, DoubleProperty originY, DoubleProperty originZ,
                          DoubleProperty zRotation, DoubleProperty xRotation, DoubleProperty distanceFromOrigin)
    {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.zRotation = zRotation;
        this.xRotation = xRotation;
        this.distanceFromOrigin = distanceFromOrigin;
        
        rotateX.angleProperty().bind(this.xRotation);
        rotateZ.angleProperty().bind(this.zRotation);
    }
        
    public void attachToScene(Scene scene) { dragHelper.attachToScene(scene); }
    
    public void detachFromScene(Scene scene) { dragHelper.detachFromScene(scene); }
    
    public void attachToSubScene(SubScene subscene) { dragHelper.attachToSubScene(subscene); }
    
    public void detachFromSubScene(SubScene subscene) { dragHelper.detachFromSubScene(subscene); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty originX;
    private final DoubleProperty originY;
    private final DoubleProperty originZ;
    private final DoubleProperty zRotation;
    private final DoubleProperty xRotation;
    private final DoubleProperty distanceFromOrigin;
    
    private MouseButton triggerButton = MouseButton.SECONDARY;
    private double coeff = 0.0007;
    private final static Point3D startingXVec = new Point3D(1, 0, 0);
    private final static Point3D startingYVec = new Point3D(0, 1, 0);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            // camera's z distance
            final double zDistance = distanceFromOrigin.get();
            // find local x and y vector shifts for the camera
            final Point3D dxVec = 
                    rotateZ.transform(rotateX.transform(startingXVec)).multiply(coeff * zDistance * deltaX);
            final Point3D dyVec = 
                    rotateZ.transform(rotateX.transform(startingYVec)).multiply(coeff * zDistance * deltaY);
            // perform shifts along x and y
            originX.set(originX.get() - dxVec.getX() - dyVec.getX());
            originY.set(originY.get() - dxVec.getY() - dyVec.getY());
            originZ.set(originZ.get() - dxVec.getZ() - dyVec.getZ());
        }
    };

    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
}
