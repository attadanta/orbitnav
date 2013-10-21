package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;

public final class InteractionXZTurntable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionXZTurntable(DoubleProperty xRotation, DoubleProperty zRotation) {
        this.xRotation = xRotation;
        this.zRotation = zRotation;
    }
    
    public void attachToScene(Scene scene) { dragHelper.attachToScene(scene); }
    
    public void detachFromScene(Scene scene) { dragHelper.detachFromScene(scene); }
    
    public void attachToSubScene(SubScene subscene) { dragHelper.attachToSubScene(subscene); }
    
    public void detachFromSubScene(SubScene subscene) { dragHelper.detachFromSubScene(subscene); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty xRotation;
    private final DoubleProperty zRotation;
    private MouseButton triggerButton = MouseButton.PRIMARY;
    private double coeff = 0.4;
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            xRotation.set(xRotation.get() - (coeff * deltaY));
            zRotation.set(zRotation.get() - (coeff * deltaX));
        }
    };
    
    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
}
