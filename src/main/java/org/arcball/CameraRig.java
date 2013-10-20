package org.arcball;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Transform;

public interface CameraRig {
    void attachToScene(Scene scene);
    void detachFromScene(Scene scene);
    void attachToSubScene(SubScene subscene);
    void detachFromSubScene(SubScene subscene);
    void encompassBounds(Bounds bounds, double animationDurationMillis);
    
    ReadOnlyObjectProperty<CameraTo2DTransform> viewTransformProperty();
    CameraTo2DTransform getViewTransform();
    ReadOnlyObjectProperty<Transform> rotationOnlyComponentProperty();
    Transform getRotationOnlyComponent();
}
