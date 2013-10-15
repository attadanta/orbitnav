package org.arcball;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class ArcballCameraRig extends Group {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public ArcballCameraRig() {
        buildTree();
    }

    public void setCameraForScene(Scene scene) {
        scene.setCamera(camera);
        translateInteraction.attachToScene(scene, cameraTranslate);
        turntableInteraction.attachToScene(scene, cameraRotate);
        zoomInteraction.attachToScene(scene, cameraZOffset);
    }
    
    public void removeCameraFromScene(Scene scene) {
        scene.setCamera(null);
        translateInteraction.detachFromScene(scene);
        turntableInteraction.detachFromScene(scene);
        zoomInteraction.detachFromScene(scene);
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private void buildTree() {
        this.getTransforms().addAll(cameraTranslate, cameraRotate, cameraZOffset);
        this.getChildren().add(camera);
    }
    
    private final PerspectiveCamera camera          = new PerspectiveCamera(true);
    private final Translate         cameraTranslate = new Translate(0, 0, 0);
    private final Rotate            cameraRotate    = new Rotate(0, Rotate.X_AXIS);
    private final Translate         cameraZOffset   = new Translate(0, 0, -10);
    
    private final MouseTranslateInteraction translateInteraction = new MouseTranslateInteraction();
    private final MouseTurntableInteraction turntableInteraction = new MouseTurntableInteraction();
    private final MouseZoomInteraction      zoomInteraction      = new MouseZoomInteraction();
    
}
