package org.arcball;

import org.arcball.internal.InteractionXZTurntable;
import org.arcball.internal.InteractionScrollZoom;
import org.arcball.internal.InteractionPan;

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
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

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
    
    public void encompassBounds(Bounds bounds, double animationDurationMillis) {
        // find the center of the bounds
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        double cy = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        double cz = (bounds.getMinZ() + bounds.getMaxZ()) / 2.0;
        // find the "radius", as the maximum of the depth, height and width divided by 2
        double r = Math.max(bounds.getDepth(), Math.max(bounds.getHeight(), bounds.getWidth())) / 2.0;
        // configure camera
        if (getCamera() instanceof PerspectiveCamera) {
          PerspectiveCamera pCamera = (PerspectiveCamera)getCamera();
          double fov = pCamera.getFieldOfView() * Math.PI / 180.0;
          double d = r / Math.tan(fov / 2.0);
          if (animationDurationMillis <= 0.0) {
              setDistanceFromOrigin(1.1 * d);
              setOriginX(cx);
              setOriginY(cy);
              setOriginZ(cz);
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
    
    public ReadOnlyObjectProperty<CameraTo2DTransform> viewTransformProperty() { return viewTransform; }
    public CameraTo2DTransform getViewTransform() { return viewTransform.get(); }
    
    public ReadOnlyObjectProperty<Transform> rotationOnlyComponentProperty() { return null; }
    public Transform getRotationOnlyComponent() { return null; }
    
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
    
    private final ObjectProperty<CameraTo2DTransform> viewTransform =
            new SimpleObjectProperty<CameraTo2DTransform>(this, "viewTransform", new MyCameraTo2DTransform());
    
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
        
        // bind all changes in the properties to the viewChangeListener
        originX.addListener(viewChangeListener);
        originY.addListener(viewChangeListener);
        originZ.addListener(viewChangeListener);
        xRotation.addListener(viewChangeListener);
        zRotation.addListener(viewChangeListener);
        distanceFromOrigin.addListener(viewChangeListener);        
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
    
    private final ChangeListener<Number> viewChangeListener = new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
            PerspectiveCamera pCamera = (PerspectiveCamera)getCamera();
            double width = subscene.getWidth();
            double height = subscene.getHeight();
            double fov;
            if (pCamera.isVerticalFieldOfView()) {
                double fovrad = pCamera.getFieldOfView() * Math.PI / 180.0;
                double hfovrad = 2.0 * Math.atan((width / height) * Math.tan(fovrad / 2.0));
                fov = hfovrad * 180.0 / Math.PI;
            } else {
                fov = pCamera.getFieldOfView();
            }
            viewTransform.set(new MyCameraTo2DTransform(getOriginX(), getOriginY(), getOriginZ(),
                    getXRotation(), getZRotation(), getDistanceFromOrigin(), fov, width, height));
        }
    }; 
        
    private static final class MyCameraTo2DTransform implements CameraTo2DTransform {
        public MyCameraTo2DTransform() { this(0, 0, 0, 0, 0, 0, 45.0, 1024, (768.0/2.0)); }
        public MyCameraTo2DTransform(
                double originX, double originY, double originZ,
                double xRotation, double zRotation, double distanceFromOrigin,
                double fov, double width, double height
            ) {
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
            this.xRotation = xRotation;
            this.zRotation = zRotation;
            this.distanceFromOrigin = distanceFromOrigin;
            this.fov = fov;
            this.width = width;
            this.height = height;
        }
        @Override public Point2D transform(double x, double y, double z) {
            affine.setToIdentity();
            affine.appendTranslation(0, 0, distanceFromOrigin);
            affine.appendRotation(-xRotation, 0, 0, 0, 1, 0, 0);
            affine.appendRotation(-zRotation, 0, 0, 0, 0, 0, 1);
            affine.appendTranslation(-originX, -originY, -originZ);
            Point3D p3d = affine.transform(x, y, z);
            final double f = 1.0 / Math.tan(fov * Math.PI / 180.0 / 2.0);
            final double w2 = width / 2.0;
            final double xprime = f * w2 * p3d.getX() / p3d.getZ() + w2;
            final double yprime = f * w2 * p3d.getY() / p3d.getZ() + (height / 2.0);
            return new Point2D(xprime, yprime);
        }
        @Override public double transformRadius(double x, double y, double z, double radius) {
            affine.setToIdentity();
            affine.appendTranslation(0, 0, distanceFromOrigin);
            affine.appendRotation(-xRotation, 0, 0, 0, 1, 0, 0);
            affine.appendRotation(-zRotation, 0, 0, 0, 0, 0, 1);
            affine.appendTranslation(-originX, -originY, -originZ);
            Point3D p3d = affine.transform(x, y, z);
            final double f = 1.0 / Math.tan(fov * Math.PI / 180.0 / 2.0);
            final double w2 = width / 2.0;
            final double xprime      = f * w2 * p3d.getX() / p3d.getZ() + w2;
            final double xprimeprime = f * w2 * (p3d.getX() + radius) / p3d.getZ() + w2;
            return Math.abs(xprimeprime - xprime);
        }
        private final double originX;
        private final double originY;
        private final double originZ;
        private final double xRotation;
        private final double zRotation;
        private final double distanceFromOrigin;
        private final double fov;
        private final double width;
        private final double height;
        private final Affine affine = new Affine();
    }
    
}
