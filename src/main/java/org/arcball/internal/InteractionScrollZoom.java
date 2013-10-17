package org.arcball.internal;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Translate;

public final class InteractionScrollZoom {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionScrollZoom(Translate zOffset) {
        this.zOffset = zOffset;
    }
    
    public void attachToScene(Scene scene) { scene.addEventHandler(ScrollEvent.SCROLL, scrollHandler); }
    
    public void detachFromScene(Scene scene) { scene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler); }

    public void attachToSubScene(SubScene sscene) { sscene.addEventHandler(ScrollEvent.SCROLL, scrollHandler); }
    
    public void detachFromSubScene(SubScene sscene) { sscene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler); }
    
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final Translate zOffset;
    private double coeff = 0.002;

    private final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent se) {
            zOffset.setZ((1.0 - (coeff * se.getDeltaY())) * zOffset.getZ());
        }
    };
    
}
