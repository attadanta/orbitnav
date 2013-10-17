package org.arcball;

import org.arcball.internal.InteractionXZTurntable;
import org.arcball.internal.InteractionScrollZoom;
import org.arcball.internal.InteractionPan;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class TurntableCameraRig extends Group {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public TurntableCameraRig() {
        buildTree();
    }
    
    public void attachToScene(Scene scene) {
        scene.setCamera(camera);
        turntable.attachToScene(scene);
        zoom.attachToScene(scene);
        pan.attachToScene(scene);
    }
    
    public void detachFromScene(Scene scene) {
        turntable.detachFromScene(scene);
        zoom.detachFromScene(scene);
        pan.detachFromScene(scene);
        scene.setCamera(null);
    }
    
    public void attachToSubScene(SubScene subscene) {
        turntable.attachToSubScene(subscene);
        zoom.attachToSubScene(subscene);
        pan.attachToSubScene(subscene);
        subscene.setCamera(camera);
    }
    
    public void detachFromSubScene(SubScene subscene) {
        turntable.detachFromSubScene(subscene);
        zoom.detachFromSubScene(subscene);
        pan.detachFromSubScene(subscene);
        subscene.setCamera(null);
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final PerspectiveCamera camera    = new PerspectiveCamera(true);
    private final Translate         panTran   = new Translate(0, 0, 0);
    private final Rotate            rotateZ   = new Rotate(0, Rotate.Z_AXIS);
    private final Rotate            rotateX   = new Rotate(0, Rotate.X_AXIS);
    private final Translate         zOffset   = new Translate(0, 0, -10);
    
    private final InteractionXZTurntable turntable = new InteractionXZTurntable(rotateX, rotateZ);
    private final InteractionScrollZoom  zoom      = new InteractionScrollZoom(zOffset);
    private final InteractionPan         pan       = new InteractionPan(panTran, rotateZ, rotateX, zOffset);
    
    private void buildTree() {
        this.getTransforms().addAll(panTran, rotateZ, rotateX, zOffset);
        this.getChildren().add(camera);
    }
        
}
