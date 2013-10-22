package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Turntable drag interaction.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionDragXZTurntable extends InteractionDrag {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDragXZTurntable(DoubleProperty xRotation, DoubleProperty zRotation) {
        this.xRotation.bindBidirectional(xRotation);
        this.zRotation.bindBidirectional(zRotation);
        
        setTriggerButton(DEFAULT_TRIGGER_BUTTON);
    }

    public DoubleProperty xRotationProperty() { return xRotation; }
    
    public DoubleProperty zRotationProperty() { return zRotation; }
    
    public DoubleProperty rotationCoefficientProperty() { return rotationCoefficient; }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected DragHandler getDragHandler() {
        return new DragHandlerAdaptor() {
            @Override public void handleDrag(MouseEvent mouseEvent, double deltaX, double deltaY) {
                final double oldXRot = xRotation.get();
                final double oldZRot = zRotation.get();
                final double coeff = rotationCoefficient.get();
                final double zRotationSign = (oldXRot > 180.0) ? (1.0) : (-1.0);
                final double newXRotation = oldXRot - (coeff * deltaY);
                final double newZRotation = oldZRot - (zRotationSign * coeff * deltaX); 
                xRotation.set(Util.normalizeAngle(newXRotation));
                zRotation.set(Util.normalizeAngle(newZRotation));
            }        
        };
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static final MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.PRIMARY;
    
    private final DoubleProperty xRotation = new SimpleDoubleProperty(this, "xRotation", 0);
    private final DoubleProperty zRotation = new SimpleDoubleProperty(this, "yRotation", 0);
    private final DoubleProperty rotationCoefficient = new SimpleDoubleProperty(this, "rotationCoefficient", 0.4);
    
}
