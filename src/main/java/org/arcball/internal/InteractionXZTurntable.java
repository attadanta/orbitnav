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
    
    public void setTriggerButton(MouseButton button) { dragHelper.setTriggerButton(button); }
    
    public MouseButton getTriggerButton() { return dragHelper.getTriggerButton(); }
    
    public void setRotationCoefficient(double coefficient) { coeff = coefficient; }
    
    public double getRotationCoefficient() { return coeff; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static final MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.PRIMARY;
    
    private final DoubleProperty xRotation;
    private final DoubleProperty zRotation;
    private double coeff = 0.4;
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            final double zRotationSign = (xRotation.get() > 180.0) ? (1.0) : (-1.0);
            xRotation.set(xRotation.get() - (coeff * deltaY));
            zRotation.set(zRotation.get() - (zRotationSign * coeff * deltaX));
        }
    };
    
    private final DragHelper dragHelper = new DragHelper(DEFAULT_TRIGGER_BUTTON, dragHandler);
    
}
