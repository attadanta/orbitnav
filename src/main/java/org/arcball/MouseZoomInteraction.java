package org.arcball;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Translate;

public final class MouseZoomInteraction {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public void attachToScene(Scene scene, Translate cameraZOffset) {
        this.scene         = scene;
        this.cameraZOffset = cameraZOffset;
        scene.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
    }
    
    public void detachFromScene(Scene scene) {
        if (this.scene == null) {
            throw new IllegalArgumentException("not attached to a scene");
        }
        if (this.scene != scene) {
            throw new IllegalArgumentException("attempted to detach from a different scene");
        }
        scene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
        this.scene = null;
        this.cameraZOffset = null;
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private static double DEFAULT_SCALE_COEFFICIENT = 0.001;
    
    private double    scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;
    private Scene     scene            = null;
    private Translate cameraZOffset    = null;
    
    private final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
        @Override public void handle(ScrollEvent scrollEvent) {
            cameraZOffset.setZ((1.0 - (scaleCoefficient * scrollEvent.getDeltaY())) * cameraZOffset.getZ());
        }
    };
        
}
