package org.arcball.internal;

import org.arcball.NavigationBehavior;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;

/**
 * Zoom interaction triggered by scroll events (eg. mouse wheel scroll).
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionScrollZoom extends InteractionScroll {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionScrollZoom(DoubleProperty distanceFromOrigin) {
        setNavigationBehavior(NavigationBehavior.scroll(NavigationBehavior.Response.ZOOM));
        this.distanceFromOrigin.bindBidirectional(distanceFromOrigin);
    }
    
    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
    
    public DoubleProperty zoomCoefficientProperty() { return zoomCoefficient; }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED

    protected EventHandler<ScrollEvent> getScrollHandler() { return scrollHandler; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 0);
    private final DoubleProperty zoomCoefficient = new SimpleDoubleProperty(this, "zoomCoefficient", 0.002);

    private final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent se) {
            if (scrollEventMatches(se)) {
                final double coeff = zoomCoefficient.get();
                distanceFromOrigin.set((1.0 - (coeff * se.getDeltaY())) * distanceFromOrigin.get());
            }
        }
    };    
    
}
