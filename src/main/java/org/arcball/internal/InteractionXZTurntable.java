package org.arcball.internal;

import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;

public final class InteractionXZTurntable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionXZTurntable(Rotate rotateX, Rotate rotateZ) {
        this.rotateX = rotateX;
        this.rotateZ = rotateZ;
    }
    
    public void attachToScene(Scene scene) { dragHelper.attachToScene(scene); }
    
    public void detachFromScene(Scene scene) { dragHelper.detachFromScene(scene); }
    
    public void attachToSubScene(SubScene subscene) { dragHelper.attachToSubScene(subscene); }
    
    public void detachFromSubScene(SubScene subscene) { dragHelper.detachFromSubScene(subscene); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final Rotate rotateX;
    private final Rotate rotateZ;
    private MouseButton triggerButton = MouseButton.PRIMARY;
    private double coeff = 0.4;
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            rotateX.setAngle(rotateX.getAngle() - (coeff * deltaY));
            rotateZ.setAngle(rotateZ.getAngle() + (coeff * deltaX));                    
        }
    };
    
    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
}
