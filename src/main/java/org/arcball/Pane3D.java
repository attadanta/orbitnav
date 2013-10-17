package org.arcball;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A Pane that contains and automatically re-sizes a SubScene.
 * 
 * @author Jonathan Merritt <j.s.merritt@gmail.com>
 */
public final class Pane3D extends Pane {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public Pane3D() {
        init();
    }
    
    public ObjectProperty<Group> rootProperty() { return root; }
    public void setRoot(Group root) { this.root.set(root); }
    public Group getRoot() { return root.get(); }
    
    public ObjectProperty<Paint> fillProperty() { return fill; }
    public void setFill(Paint fill) { this.fill.set(fill); }
    public Paint getFill() { return fill.get(); }
    
    public ObjectProperty<CameraRig> cameraRigProperty() { return cameraRig; }
    public void setCameraRig(CameraRig cameraRig) { this.cameraRig.set(cameraRig); }
    public CameraRig getCameraRig() { return cameraRig.get(); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final ObjectProperty<Group> root = new SimpleObjectProperty<Group>(this, "root", new Group());
    private final ObjectProperty<Paint> fill = new SimpleObjectProperty<Paint>(this, "fill", Color.DARKGRAY);
    private final ObjectProperty<CameraRig> cameraRig = new SimpleObjectProperty<CameraRig>(this, "cameraRig", null);
    
    private final SubScene subScene = new SubScene(getRoot(), 8, 8, true, true);
    
    private void init() {
        subScene.fillProperty().bind(fill);
        getChildren().add(subScene);
        widthProperty().addListener(widthChangeListener);
        heightProperty().addListener(heightChangeListener);
        cameraRigProperty().addListener(rigChangeListener);
        setCameraRig(new TurntableCameraRig());
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
    
    private final ChangeListener<CameraRig> rigChangeListener = new ChangeListener<CameraRig>() {
        @Override public void changed(ObservableValue<? extends CameraRig> ob, CameraRig oldRig, CameraRig newRig) {
            if (oldRig != null) {
                oldRig.detachFromSubScene(subScene);
            }
            if (newRig != null) {
                newRig.attachToSubScene(subScene);
            }
        }
    };
    
}
