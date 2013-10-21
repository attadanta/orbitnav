package org.arcball;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Transform;

public interface CameraRig {
    void attachToScene(Scene scene);
    void detachFromScene(Scene scene);
    void attachToSubScene(SubScene subscene);
    void detachFromSubScene(SubScene subscene);
    void encompassBounds(Bounds bounds, double animationDurationMillis);
    
    ReadOnlyObjectProperty<PerspectiveCamera> cameraProperty();
    ReadOnlyObjectProperty<CameraToRasterTransform> transformSceneToRasterProperty();
    ReadOnlyObjectProperty<Transform> transformRotationOnlyProperty();
    ReadOnlyObjectProperty<Transform> transformRotationTranslationProperty();
}
