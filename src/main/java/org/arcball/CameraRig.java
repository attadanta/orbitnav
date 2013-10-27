/**
 * Copyright 2013 Dr Jonathan S Merritt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with 
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the 
 * specific language governing permissions and limitations under the License.
 */
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
