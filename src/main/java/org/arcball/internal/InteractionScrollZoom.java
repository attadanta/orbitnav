package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.ScrollEvent;

public final class InteractionScrollZoom {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionScrollZoom(DoubleProperty distanceFromOrigin) {
        this.distanceFromOrigin.bindBidirectional(distanceFromOrigin);
    }
    
    public void attachToScene(Scene scene) { scene.addEventHandler(ScrollEvent.SCROLL, scrollHandler); }
    
    public void detachFromScene(Scene scene) { scene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler); }

    public void attachToSubScene(SubScene sscene) { sscene.addEventHandler(ScrollEvent.SCROLL, scrollHandler); }
    
    public void detachFromSubScene(SubScene sscene) { sscene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler); }

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
