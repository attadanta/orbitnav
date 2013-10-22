package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;

/**
 * Abstract base class for all dragging interactions.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public abstract class InteractionDrag {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public void attachToScene(Scene scene) { 
        dragHelper.attachToScene(scene); 
        width.bind(scene.widthProperty());
        height.bind(scene.heightProperty());
    }
    
    public void attachToSubScene(SubScene subscene) { 
        dragHelper.attachToSubScene(subscene);
        width.bind(subscene.widthProperty());
        height.bind(subscene.heightProperty());
    }
    
    public void detachFromScene(Scene scene) { 
        dragHelper.detachFromScene(scene);
        width.unbind();
        height.unbind();
    }
    
    public void detachFromSubScene(SubScene subscene) { 
        dragHelper.detachFromSubScene(subscene);
        width.unbind();
        height.unbind();
    }
    
    public ObjectProperty<MouseButton> triggerButtonProperty() { return triggerButton; }
    public MouseButton getTriggerButton() { return triggerButton.get(); }
    public void setTriggerButton(MouseButton button) { triggerButton.set(button); }
    
    public ReadOnlyDoubleProperty widthProperty() { return width; }
    public double getWidth() { return width.get(); }
    
    public ReadOnlyDoubleProperty heightProperty() { return height; }
    public double getHeight() { return height.get(); }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected abstract DragHandler getDragHandler();
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final ObjectProperty<MouseButton> triggerButton =
            new SimpleObjectProperty<MouseButton>(this, "triggerButton", MouseButton.PRIMARY);
    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", 1.0);
    private final DoubleProperty height = new SimpleDoubleProperty(this, "height", 1.0);
    private final DragHelper dragHelper = new DragHelper(triggerButton, getDragHandler());
    
}
