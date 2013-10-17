package org.arcball.internal;

import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class InteractionPan {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionPan(Translate panTran, Rotate rotateZ, Rotate rotateX, Translate zOffset) {
        this.panTran = panTran;
        this.rotateZ = rotateZ;
        this.rotateX = rotateX;
        this.zOffset = zOffset;
    }
    
    public void attachToScene(Scene scene) { dragHelper.attachToScene(scene); }
    
    public void detachFromScene(Scene scene) { dragHelper.detachFromScene(scene); }
    
    public void attachToSubScene(SubScene subscene) { dragHelper.attachToSubScene(subscene); }
    
    public void detachFromSubScene(SubScene subscene) { dragHelper.detachFromSubScene(subscene); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final Translate panTran;
    private final Rotate rotateZ;
    private final Rotate rotateX;
    private final Translate zOffset;
    private MouseButton triggerButton = MouseButton.SECONDARY;
    private double coeff = 0.0007;
    private final static Point3D startingXVec = new Point3D(1, 0, 0);
    private final static Point3D startingYVec = new Point3D(0, 1, 0);
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            // camera's z distance
            double zDistance = zOffset.getZ();
            // find local x and y vector shifts for the camera
            Point3D dxVec = rotateZ.transform(rotateX.transform(startingXVec)).multiply(coeff * zDistance * deltaX);
            Point3D dyVec = rotateZ.transform(rotateX.transform(startingYVec)).multiply(coeff * zDistance * deltaY);
            // perform shifts along x and y
            panTran.setX(panTran.getX() + dxVec.getX() + dyVec.getX());
            panTran.setY(panTran.getY() + dxVec.getY() + dyVec.getY());
            panTran.setZ(panTran.getZ() + dxVec.getZ() + dyVec.getZ());
        }
    };

    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
}
