package org.arcball;

import org.arcball.internal.InteractionXZTurntable;
import org.arcball.internal.InteractionScrollZoom;
import org.arcball.internal.InteractionPan;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class TurntableCameraRig extends Group implements CameraRig {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public TurntableCameraRig() {
        buildTree();
    }
    
    public void attachToScene(Scene scene) {
        this.scene = scene;
        scene.setCamera(getCamera());
        turntable.attachToScene(scene);
        zoom.attachToScene(scene);
        pan.attachToScene(scene);
    }
    
    public void detachFromScene(Scene scene) {
        turntable.detachFromScene(scene);
        zoom.detachFromScene(scene);
        pan.detachFromScene(scene);
        scene.setCamera(null);
        this.scene = null;
    }
    
    public void attachToSubScene(SubScene subscene) {
        this.subscene = subscene;
        turntable.attachToSubScene(subscene);
        zoom.attachToSubScene(subscene);
        pan.attachToSubScene(subscene);
        subscene.setCamera(getCamera());
    }
    
    public void detachFromSubScene(SubScene subscene) {
        turntable.detachFromSubScene(subscene);
        zoom.detachFromSubScene(subscene);
        pan.detachFromSubScene(subscene);
        subscene.setCamera(null);
        this.subscene = null;
    }
    
    public ObjectProperty<Camera> cameraProperty() { return camera; }
    public void setCamera(Camera camera) { this.camera.set(camera); }
    public Camera getCamera() { return camera.get(); }
    
    public DoubleProperty originXProperty() { return originX; }
    public void setOriginX(double originX) { this.originX.set(originX); }
    public double getOriginX() { return originX.get(); }
    
    public DoubleProperty originYProperty() { return originY; }
    public void setOriginY(double originY) { this.originY.set(originY); }
    public double getOriginY() { return originY.get(); }
    
    public DoubleProperty originZProperty() { return originZ; }
    public void setOriginZ(double originZ) { this.originZ.set(originZ); }
    public double getOriginZ() { return originZ.get(); }
    
    public DoubleProperty zRotationProperty() { return zRotation; }
    public void setZRotation(double zRotation) { this.zRotation.set(zRotation); }
    public double getZRotation() { return zRotation.get(); }
    
    public DoubleProperty xRotationProperty() { return xRotation; }
    public void setXRotation(double xRotation) { this.xRotation.set(xRotation); }
    public double getXRotation() { return xRotation.get(); }    
    
    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
    public void setDistanceFromOrigin(double distance) { distanceFromOrigin.set(distance); }
    public double getDistanceFromOrigin() { return distanceFromOrigin.get(); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private Scene scene = null;
    private SubScene subscene = null;
    
    private final ObjectProperty<Camera> camera     = new SimpleObjectProperty<Camera>(this, "camera", 
                                                                                       new PerspectiveCamera(true));
    private final DoubleProperty originX            = new SimpleDoubleProperty(this, "originX", 0);
    private final DoubleProperty originY            = new SimpleDoubleProperty(this, "originY", 0);
    private final DoubleProperty originZ            = new SimpleDoubleProperty(this, "originZ", 0);
    private final DoubleProperty zRotation          = new SimpleDoubleProperty(this, "zRotation", 0);
    private final DoubleProperty xRotation          = new SimpleDoubleProperty(this, "xRotation", 0);
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 10);
    
    private final InteractionXZTurntable turntable = new InteractionXZTurntable(xRotation, zRotation);
    private final InteractionScrollZoom  zoom      = new InteractionScrollZoom(distanceFromOrigin);
    private final InteractionPan         pan       = new InteractionPan(originX, originY, originZ, zRotation,
                                                                        xRotation, distanceFromOrigin);
    
    private void buildTree() {
        // camera change listener
        camera.addListener(cameraChangeListener);
        // pan translation
        Translate panTranslation = new Translate();
        panTranslation.xProperty().bind(originX);
        panTranslation.yProperty().bind(originY);
        panTranslation.zProperty().bind(originZ);
        // z rotation
        Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
        rotateZ.angleProperty().bind(zRotation);
        // x rotation
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateX.angleProperty().bind(xRotation);
        // z Offset translation
        Translate zOffset = new Translate();
        zOffset.zProperty().bind(distanceFromOrigin.negate());
        // set up transforms
        this.getTransforms().addAll(panTranslation, rotateZ, rotateX, zOffset);
        this.getChildren().add(getCamera());
    }
    
    private final ChangeListener<Camera> cameraChangeListener = new ChangeListener<Camera>() {
        @Override public void changed(
                ObservableValue<? extends Camera> observable, Camera oldCamera, Camera newCamera
        ) {
            if (scene != null) {
                scene.setCamera(getCamera());
            }
            if (subscene != null) {
                subscene.setCamera(getCamera());
            }
        }
    };
    
}
