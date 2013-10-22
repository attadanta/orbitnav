package org.arcball.internal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
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
public final class DragHelper implements Attachable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    /**
     * Creates a new instance of a <code>DragHelper</code>.
     * @param triggerButton the mouse button required to trigger the drag
     * @param dragHandler the handler attached to this helper
     */
    public DragHelper(ObjectProperty<MouseButton> triggerButton, DragHandler dragHandler) {
        this.triggerButton.bind(triggerButton);
        this.dragHandler = dragHandler;
    }

    /**
     * Attaches to a host to receive mouse events.
     * 
     * @param host host to which attachment should be made
     */
    public void attachToHost(InteractionHost host) {
        assert(this.host == null);
        this.host = host;
        host.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        host.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
    }

    /**
     * Detaches from a host to stop receving mouse events.
     * 
     * @param host host from which to detach
     */
    public void detachFromHost(InteractionHost host) {
        assert(this.host == host);
        host.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
        host.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDragHandler);
        this.host = null;
    }
    
    /**
     * Sets the button which will trigger the drags that are monitored by this helper.
     * @param b triggering mouse button
     */
    public void setTriggerButton(MouseButton b) { triggerButton.set(b); }
    
    /**
     * Gets the button which triggers the drags that are monitored by this helper.
     * @return triggering mouse button
     */
    public MouseButton getTriggerButton() { return triggerButton.get(); }
    
    /**
     * Returns the trigger button property.
     * @return trigger button property
     */
    public ObjectProperty<MouseButton> triggerButtonProperty() { return triggerButton; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private ObjectProperty<MouseButton> triggerButton = 
            new SimpleObjectProperty<MouseButton>(this, "triggerButton", MouseButton.PRIMARY);
    private final DragHandler dragHandler;
    private double x;
    private double y;
    private InteractionHost host = null;
    
    private final EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton.get()) {
                x = me.getSceneX();
                y = me.getSceneY();
                dragHandler.handleClick(me);
            }
        }
    };
    
    private final EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            if (me.getButton() == triggerButton.get()) {
                final double oldX = x;
                final double oldY = y;
                x = me.getSceneX();
                y = me.getSceneY();
                dragHandler.handleDrag(me, x - oldX, y - oldY);
            }
        }
    };
        
}
