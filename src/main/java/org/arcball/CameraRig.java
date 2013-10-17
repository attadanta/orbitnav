package org.arcball;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SubScene;

public interface CameraRig {
    void attachToScene(Scene scene);
    void detachFromScene(Scene scene);
    void attachToSubScene(SubScene subscene);
    void detachFromSubScene(SubScene subscene);
    public void encompassBounds(Bounds bounds);
}
