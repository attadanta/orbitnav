package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

/**
 * Zoom interaction triggered by scroll events (eg. mouse wheel scroll).
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionScrollZoom implements Attachable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionScrollZoom(DoubleProperty distanceFromOrigin) {
        this.distanceFromOrigin.bindBidirectional(distanceFromOrigin);
    }
    
    public void attachToHost(InteractionHost host) {
        host.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
    }
    
    public void detachFromHost(InteractionHost host) {
        host.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
    }

    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
    
    public DoubleProperty zoomCoefficientProperty() { return zoomCoefficient; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 0);
    private final DoubleProperty zoomCoefficient = new SimpleDoubleProperty(this, "zoomCoefficient", 0.002);

    private final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent se) {
            final double coeff = zoomCoefficient.get();
            distanceFromOrigin.set((1.0 - (coeff * se.getDeltaY())) * distanceFromOrigin.get());
        }
    };
    
}
