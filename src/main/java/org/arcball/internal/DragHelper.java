package org.arcball.internal;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public final class DragHelper {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public DragHelper(MouseButton triggerButton, DragHandler dragHandler) {
        this.triggerButton = triggerButton;
        this.dragHandler = dragHandler;
    }
    
    public void attachToScene(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
    }
    
    public void attachToSubScene(SubScene subscene) {
        subscene.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        subscene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);        
    }
    
    public void detachFromScene(Scene scene) {
        scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseDragHandler);
    }
    
    public void detachFromSubScene(SubScene subscene) {
        subscene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        subscene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseDragHandler);        
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private MouseButton triggerButton;
    private DragHandler dragHandler;
    private final MousePositionTracker mpt = new MousePositionTracker();
    
    private final EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton) {
                mpt.mousePress(me.getSceneX(), me.getSceneY());
            }
        }
    };
    
    private final EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton) {
                mpt.mouseDrag(me.getSceneX(), me.getSceneY());
                dragHandler.handleDrag(mpt.getDeltaX(), mpt.getDeltaY());
            }
        }
    };
    
}
