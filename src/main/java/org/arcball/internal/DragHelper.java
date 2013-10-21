package org.arcball.internal;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Drag helper class.  Keeps track of mouse press and drag events for a <code>Scene</code> or <code>SubScene</code>.
 * <p>
 * In order to respond to drag events for view changes, it is typically necessary to know only the change in 
 * coordinates that is produced as the mouse is dragged.  This class keeps track of only those coordinate changes,
 * reporting them to a {@link org.arcball.internal.DragHandler DragHandler} as required.  The helper can be attached
 * to a single <code>Scene</code> or <code>SubScene</code> at one time.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class DragHelper {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    /**
     * Creates a new instance of a <code>DragHelper</code>.
     * @param triggerButton the mouse button required to trigger the drag
     * @param dragHandler the handler attached to this helper
     */
    public DragHelper(MouseButton triggerButton, DragHandler dragHandler) {
        this.triggerButton = triggerButton;
        this.dragHandler = dragHandler;
    }
    
    /**
     * Attaches this drag helper to a scene.
     * @param scene scene to which the helper should be attached
     */
    public void attachToScene(Scene scene) {
        assert((this.scene == null) && (this.subscene == null));
        this.scene = scene;
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
    }
    
    /**
     * Attaches this drag helper to a subscene.
     * @param subscene subscene to which the helper should be attached
     */
    public void attachToSubScene(SubScene subscene) {
        assert((this.scene == null) && (this.subscene == null));
        this.subscene = subscene;
        subscene.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        subscene.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);        
    }
    
    /**
     * Detaches this drag helper from a scene.  The <code>scene</code> must match the scene to which the helper was
     * previously attached using the {@link #attachToScene(Scene) attachToScene} method.
     * @param scene scene from which to detach the helper
     */
    public void detachFromScene(Scene scene) {
        assert((this.scene == scene) && (this.subscene == null));
        scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseDragHandler);
        this.scene = null;
    }
    
    /**
     * Detaches this drag helper from a subscene.  The <code>subscene</code> must match the subscene to which the
     * helper was previously attached using the {@link #attachToSubScene(SubScene) attachToSubScene} method.
     * @param subscene scene from which to detach the helper
     */
    public void detachFromSubScene(SubScene subscene) {
        assert((this.subscene == subscene) && (this.scene == null));
        subscene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        subscene.removeEventHandler(MouseEvent.MOUSE_PRESSED, mouseDragHandler);
        this.subscene = null;
    }
    
    /**
     * Sets the button which will trigger the drags that are monitored by this helper.
     * @param b triggering mouse button
     */
    public void setTriggerButton(MouseButton b) { triggerButton = b; }
    
    /**
     * Gets the button which triggers the drags that are monitored by this helper.
     * @return triggering mouse button
     */
    public MouseButton getTriggerButton() { return triggerButton; } 
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private MouseButton triggerButton;
    private final DragHandler dragHandler;
    private double x;
    private double y;
    private Scene scene = null;
    private SubScene subscene = null;
    
    private final EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton) {
                x = me.getSceneX();
                y = me.getSceneY();
            }
        }
    };
    
    private final EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton) {
                final double oldX = x;
                final double oldY = y;
                x = me.getSceneX();
                y = me.getSceneY();
                dragHandler.handleDrag(x - oldX, y - oldY);
            }
        }
    };
        
}
