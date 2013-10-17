package org.arcball;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Translate;

public final class MouseTranslateInteraction {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    public void attachToScene(Scene scene, Translate cameraTranslate) {
        this.scene           = scene;
        this.cameraTranslate = cameraTranslate;
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
    }
    
    public void detachFromScene(Scene scene) {
        if (this.scene == null) {
            throw new IllegalArgumentException("not attached to a scene");
        }
        if (this.scene != scene) {
            throw new IllegalArgumentException("attempted to detach from a different scene");
        }
        scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
        this.scene = null;
        this.cameraTranslate = null;
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private static double      DEFAULT_SCALE_COEFFICIENT = 0.01;
    private static MouseButton DEFAULT_MOUSE_BUTTON      = MouseButton.SECONDARY;
    
    private double      scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;
    private MouseButton mouseButton      = DEFAULT_MOUSE_BUTTON;
    private Scene       scene            = null;
    private Translate   cameraTranslate  = null;
    
    private final EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == mouseButton) {
                mousePosX = mouseEvent.getSceneX();
                mousePosY = mouseEvent.getSceneY();
            }
        }
    };
    
    private final EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
            if (mouseEvent.getButton() == mouseButton) {
                // calculate mouse position delta
                oldMousePosX = mousePosX;
                oldMousePosY = mousePosY;
                mousePosX    = mouseEvent.getSceneX();
                mousePosY    = mouseEvent.getSceneY();
                mouseDeltaX  = mousePosX - oldMousePosX;
                mouseDeltaY  = mousePosY - oldMousePosY;
                // update camera translation
                cameraTranslate.setX(cameraTranslate.getX() - (mouseDeltaX * scaleCoefficient));
                cameraTranslate.setY(cameraTranslate.getY() - (mouseDeltaY * scaleCoefficient));
            }
        }
    };
    
    private double mousePosX;
    private double mousePosY;
    private double oldMousePosX;
    private double oldMousePosY;
    private double mouseDeltaX;
    private double mouseDeltaY;
}
