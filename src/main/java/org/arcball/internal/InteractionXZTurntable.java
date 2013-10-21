package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;

public final class InteractionXZTurntable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionXZTurntable(DoubleProperty xRotation, DoubleProperty zRotation) {
        this.xRotation.bindBidirectional(xRotation);
        this.zRotation.bindBidirectional(zRotation);
    }
    
    public void attachToScene(Scene scene) { dragHelper.attachToScene(scene); }
    
    public void detachFromScene(Scene scene) { dragHelper.detachFromScene(scene); }
    
    public void attachToSubScene(SubScene subscene) { dragHelper.attachToSubScene(subscene); }
    
    public void detachFromSubScene(SubScene subscene) { dragHelper.detachFromSubScene(subscene); }

    public DoubleProperty xRotationProperty() { return xRotation; }
    
    public DoubleProperty zRotationProperty() { return zRotation; }
    
    public ObjectProperty<MouseButton> triggerButtonProperty() { return triggerButton; }
    
    public DoubleProperty rotationCoefficientProperty() { return rotationCoefficient; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static final MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.PRIMARY;
    
    private final DoubleProperty xRotation = new SimpleDoubleProperty(this, "xRotation", 0);
    private final DoubleProperty zRotation = new SimpleDoubleProperty(this, "yRotation", 0);
    private final ObjectProperty<MouseButton> triggerButton = 
            new SimpleObjectProperty<MouseButton>(this, "triggerButton", DEFAULT_TRIGGER_BUTTON);
    private final DoubleProperty rotationCoefficient = new SimpleDoubleProperty(this, "rotationCoefficient", 0.4);
    
    private final DragHandler dragHandler = new DragHandler() {
        public void handleDrag(double deltaX, double deltaY) {
            final double coeff = rotationCoefficient.get();
            final double zRotationSign = (xRotation.get() > 180.0) ? (1.0) : (-1.0);
            final double newXRotation = xRotation.get() - (coeff * deltaY);
            final double newZRotation = zRotation.get() - (zRotationSign * coeff * deltaX); 
            xRotation.set(Util.normalizeAngle(newXRotation));
            zRotation.set(Util.normalizeAngle(newZRotation));
        }
    };
    
    private final DragHelper dragHelper = new DragHelper(triggerButton, dragHandler);
    
}
