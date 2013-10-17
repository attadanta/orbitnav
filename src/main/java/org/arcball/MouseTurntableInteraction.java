package org.arcball;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public final class MouseTurntableInteraction {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public void attachToScene(Scene scene, Rotate cameraRotate) {
        this.scene        = scene;
        this.cameraRotate = cameraRotate;
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
        this.cameraRotate = null;
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private static double      DEFAULT_SCALE_COEFFICIENT = 0.0075;
    private static MouseButton DEFAULT_MOUSE_BUTTON      = MouseButton.PRIMARY;
    
    private double      scaleCoefficient = DEFAULT_SCALE_COEFFICIENT;
    private MouseButton mouseButton      = DEFAULT_MOUSE_BUTTON;
    
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
                // update camera rotation
            }
        }
    };
    
    private Scene  scene        = null;
    private Rotate cameraRotate = null;
    private double mousePosX;
    private double mousePosY;
    private double oldMousePosX;
    private double oldMousePosY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    
}
