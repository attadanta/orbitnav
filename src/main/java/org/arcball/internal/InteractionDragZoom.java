package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Zoom drag interaction.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public class InteractionDragZoom extends InteractionDrag {
    
    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDragZoom(DoubleProperty distanceFromOrigin) {
        this.distanceFromOrigin.bindBidirectional(distanceFromOrigin);
        setTriggerButton(DEFAULT_TRIGGER_BUTTON);
    }
    
    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
    
    public DoubleProperty zoomCoefficientProperty() { return zoomCoefficient; }    
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected DragHandler getDragHandler() {
        return new DragHandlerAdaptor() {
            @Override public void handleDrag(MouseEvent me, double deltaX, double deltaY) {
                final double coeff = zoomCoefficient.get();
                distanceFromOrigin.set((1.0 + (coeff * deltaY)) * distanceFromOrigin.get());
            }
        };
    }
        
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final static MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.MIDDLE;
    
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 0);
    private final DoubleProperty zoomCoefficient = new SimpleDoubleProperty(this, "zoomCoefficient", 0.003);
    
}
