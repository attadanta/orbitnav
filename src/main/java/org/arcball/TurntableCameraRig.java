package org.arcball;

import org.arcball.internal.InteractionDragXZTurntable;
import org.arcball.internal.InteractionHost;
import org.arcball.internal.InteractionHostScene;
import org.arcball.internal.InteractionHostSubScene;
import org.arcball.internal.InteractionScrollZoom;
import org.arcball.internal.InteractionDragPan;
import org.arcball.internal.NoGarbageProperty;
import org.arcball.internal.PerspectiveSceneToRaster;
import org.arcball.internal.geom.MutableAxisAngle3D;
import org.arcball.internal.geom.MutableTurntable3D;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

public final class TurntableCameraRig implements CameraRig {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public TurntableCameraRig() {
        buildListeners();
    }
    
    public void attachToScene(Scene scene) {
        assert((this.scene == null) && (this.subscene == null));
        this.scene = scene;
        scene.setCamera(camera.get());
        host = new InteractionHostScene(scene);
        attachToHost();
    }
    
    public void detachFromScene(Scene scene) {
        assert((this.scene == scene) && (this.subscene == null));
        detachFromHost();
        scene.setCamera(null);
        this.scene = null;
        this.host = null;
    }
    
    public void attachToSubScene(SubScene subscene) {
        assert((this.scene == null) && (this.subscene == null));
        this.subscene = subscene;
        host = new InteractionHostSubScene(subscene);
        attachToHost();
        subscene.setCamera(camera.get());
    }
    
    public void detachFromSubScene(SubScene subscene) {
        assert((this.scene == null) && (this.subscene == subscene));
        detachFromHost();
        subscene.setCamera(null);
        this.subscene = null;
        this.host = null;
    }
    
    public void encompassBounds(Bounds bounds, double animationDurationMillis) {
        // find the center of the bounds
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        double cy = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        double cz = (bounds.getMinZ() + bounds.getMaxZ()) / 2.0;
        // find the "radius", as the maximum of the depth, height and width divided by 2
        double r = Math.max(bounds.getDepth(), Math.max(bounds.getHeight(), bounds.getWidth())) / 2.0;
        // configure camera
        if (camera.get() instanceof PerspectiveCamera) {
          PerspectiveCamera pCamera = (PerspectiveCamera)camera.get();
          double fov = pCamera.getFieldOfView() * Math.PI / 180.0;
          double d = r / Math.tan(fov / 2.0);
          if (animationDurationMillis <= 0.0) {
              distanceFromOrigin.set(1.1 * d);
              setOrigin(cx, cy, cz);
          } else {
              Duration tf = Duration.millis(animationDurationMillis);
              Timeline timeline = new Timeline();
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(distanceFromOriginProperty(), 1.1 * d)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originXProperty(), cx)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originYProperty(), cy)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originZProperty(), cz)));
              timeline.play();
          }
          pCamera.setNearClip(0.05 * d);
          pCamera.setFarClip(10.0 * d);
        }
    }
    
    public void setOrigin(double x, double y, double z) {
        originX.set(x);
        originY.set(y);
        originZ.set(z);
    }
    
    public ReadOnlyObjectProperty<PerspectiveCamera> cameraProperty() { return camera; }
    
    public DoubleProperty originXProperty() { return originX; }
    
    public DoubleProperty originYProperty() { return originY; }
    
    public DoubleProperty originZProperty() { return originZ; }
    
    public DoubleProperty zRotationProperty() { return zRotation; }
    
    public DoubleProperty xRotationProperty() { return xRotation; }
    
    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
        
    public DoubleProperty rotationCoefficientProperty() { return turntable.rotationCoefficientProperty(); }
    
    public DoubleProperty zoomCoefficientProperty() { return zoom.zoomCoefficientProperty(); }
    
    public ReadOnlyObjectProperty<CameraToRasterTransform> transformSceneToRasterProperty() { 
        return transformSceneToRaster; 
    }
    
    public ReadOnlyObjectProperty<Transform> transformRotationOnlyProperty() { 
        return transformRotationOnly; 
    }
    
    public ReadOnlyObjectProperty<Transform> transformRotationTranslationProperty() {
        return transformRotationTranslation;
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty originX            = new SimpleDoubleProperty(this, "originX", 0);
    private final DoubleProperty originY            = new SimpleDoubleProperty(this, "originY", 0);
    private final DoubleProperty originZ            = new SimpleDoubleProperty(this, "originZ", 0);
    private final DoubleProperty zRotation          = new SimpleDoubleProperty(this, "zRotation", 0);
    private final DoubleProperty xRotation          = new SimpleDoubleProperty(this, "xRotation", 0);
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 10);

    private final ObjectProperty<PerspectiveCamera> camera = 
            new SimpleObjectProperty<PerspectiveCamera>(this, "camera", new PerspectiveCamera(true));
    private final NoGarbageProperty<Transform> transformRotationOnly = 
            new NoGarbageProperty<Transform>(this, "transformRotationOnly", new Affine());
    private final NoGarbageProperty<Transform> transformRotationTranslation = 
            new NoGarbageProperty<Transform>(this, "transformRotationTranslation", new Affine());
    private final NoGarbageProperty<CameraToRasterTransform> transformSceneToRaster =
            new NoGarbageProperty<CameraToRasterTransform>(this, "transformSceneToRaster",
                    new PerspectiveSceneToRaster());
    
    private final InteractionDragXZTurntable turntable = new InteractionDragXZTurntable(xRotation, zRotation);
    private final InteractionScrollZoom  zoom      = new InteractionScrollZoom(distanceFromOrigin);
    private final InteractionDragPan         pan       = new InteractionDragPan(originX, originY, originZ, 
            transformRotationOnly, distanceFromOrigin, camera);

    private Scene scene = null;
    private SubScene subscene = null;
    private InteractionHost host = null;
    
    private final MutableTurntable3D turntableRotation = new MutableTurntable3D();
    private final MutableAxisAngle3D axisAngleRotation = new MutableAxisAngle3D(); 
    
    private void updateTransformRotationOnly() {
        turntableRotation.setDegrees(xRotation.get(), zRotation.get());
        axisAngleRotation.set(turntableRotation);
        axisAngleRotation.getAffine((Affine)transformRotationOnly.get());
        transformRotationOnly.fireChangedEvent();
    }    
    
    private void updateTransformRotationTranslation() {
        final Affine xform = (Affine)transformRotationTranslation.get();
        xform.setToIdentity();
        xform.appendTranslation(originX.get(), originY.get(), originZ.get());
        xform.append(transformRotationOnly.get());
        xform.appendTranslation(0, 0, -distanceFromOrigin.get());
        transformRotationTranslation.fireChangedEvent();
    }
        
    private void updateTransformSceneToRaster() {
        final PerspectiveSceneToRaster s2r = (PerspectiveSceneToRaster)transformSceneToRaster.get();
        if (host != null) {
            s2r.setParameters(camera.get(), transformRotationTranslation.get(), host);
        }
        transformSceneToRaster.fireChangedEvent();
    }
    
    private void updateTransforms() {
        updateTransformRotationOnly();
        updateTransformRotationTranslation();
        updateTransformSceneToRaster();
        camera.get().getTransforms().setAll(transformRotationTranslation.get());
    }
    
    private void buildListeners() {
        final ChangeListener<Number> triggerUpdateTransformsFromNumber = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
                updateTransforms();
            }
        };
        originX.addListener(triggerUpdateTransformsFromNumber);
        originY.addListener(triggerUpdateTransformsFromNumber);
        originZ.addListener(triggerUpdateTransformsFromNumber);
        zRotation.addListener(triggerUpdateTransformsFromNumber);
        xRotation.addListener(triggerUpdateTransformsFromNumber);
        distanceFromOrigin.addListener(triggerUpdateTransformsFromNumber);
        camera.get().fieldOfViewProperty().addListener(triggerUpdateTransformsFromNumber);
    }
    
    private void attachToHost() {
        assert(host != null);
        turntable.attachToHost(host);
        zoom.attachToHost(host);
        pan.attachToHost(host);        
    }
    
    private void detachFromHost() {
        assert(host != null);
        turntable.detachFromHost(host);
        zoom.detachFromHost(host);
        pan.detachFromHost(host);
    }
}
