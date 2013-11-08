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
package org.orbitnav;

import org.orbitnav.internal.AxisTriad;
import org.orbitnav.internal.InteractionHostSubScene;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
 * A Pane that contains and automatically re-sizes a SubScene.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class Pane3D extends Pane {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public Pane3D() {
        init();
    }
    
    public void viewAll(double animationDurationMillis) {
        cameraRig.encompassBounds(getRoot().getBoundsInParent(), animationDurationMillis);
    }
    
    public ObjectProperty<Group> rootProperty() { return root; }
    public void setRoot(Group root) { this.root.set(root); }
    public Group getRoot() { return root.get(); }
    
    public ObjectProperty<Paint> fillProperty() { return fill; }
    public void setFill(Paint fill) { this.fill.set(fill); }
    public Paint getFill() { return fill.get(); }
    
    //public ObjectProperty<CameraRig> cameraRigProperty() { return cameraRig; }
    //public void setCameraRig(CameraRig cameraRig) { this.cameraRig.set(cameraRig); }
    //public CameraRig getCameraRig() { return cameraRig.get(); }
    
    public ReadOnlyObjectProperty<CameraToRasterTransform> transformToRasterProperty() { 
        return cameraRig.transformToRasterProperty();
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final ObjectProperty<Group> root = new SimpleObjectProperty<Group>(this, "root", new Group());
    private final ObjectProperty<Paint> fill = new SimpleObjectProperty<Paint>(this, "fill", Color.DARKGREY);
    
    private final SubScene subScene = new SubScene(getRoot(), 8, 8, true, SceneAntialiasing.BALANCED);
    
    private final Group axisRoot = new Group();
    private final SubScene axisSubscene = new SubScene(axisRoot, 128, 128, true, SceneAntialiasing.BALANCED);
    
    private final OrbitalCameraRig cameraRig = new OrbitalCameraRig();
    
    private void init() {
        subScene.fillProperty().bind(fill);
        getChildren().addAll(subScene, axisSubscene);
        widthProperty().addListener(widthChangeListener);
        heightProperty().addListener(heightChangeListener);
        //setCameraRig(new ArcballCameraRig());
        
        cameraRig.setArcballEnabled(true);
        cameraRig.attachToHost(new InteractionHostSubScene(subScene));
        cameraRig.addNavigationBehavior(NavigationBehavior.drag(MouseButton.PRIMARY, NavigationBehavior.Response.ROTATE));
        cameraRig.addNavigationBehavior(NavigationBehavior.drag(MouseButton.SECONDARY, NavigationBehavior.Response.PAN));
        cameraRig.addNavigationBehavior(NavigationBehavior.drag(MouseButton.MIDDLE, NavigationBehavior.Response.ZOOM));
        cameraRig.addNavigationBehavior(NavigationBehavior.scroll(NavigationBehavior.Response.ZOOM));
        cameraRig.setArcballEnabled(false);
        
        PerspectiveCamera axisCamera = new PerspectiveCamera(true);
        axisCamera.setTranslateZ(-5);
        axisSubscene.setCamera(axisCamera);
        axisSubscene.setDisable(true);
        final AxisTriad axisTriad = new AxisTriad();
        cameraRig.transformRotationOnlyProperty().addListener(new ChangeListener<Transform>() {
            @Override public void changed(ObservableValue<? extends Transform> ob, Transform old, Transform newt) {
                try {
                    axisTriad.getTransforms().setAll(newt.createInverse());
                } catch (NonInvertibleTransformException ex) {
                    // should never happen 
                    throw new RuntimeException(ex);
                }
            }
        });
        axisRoot.getChildren().add(axisTriad);
    }
            
    private final ChangeListener<Number> widthChangeListener = new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ob, Number oldWidth, Number newWidth) {
            subScene.setWidth(Math.max(1, getWidth()));
        }
    };
    
    private final ChangeListener<Number> heightChangeListener = new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ob, Number oldHeight, Number newHeight) {
            subScene.setHeight(Math.max(1, getHeight()));
        }
    };
        
}
