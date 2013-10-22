package org.arcball.internal;

import org.arcball.NavigationBehavior;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
     * @param navigationBehavior the navigation behavior required to initiate the drag
     * @param dragHandler the handler attached to this helper
     */
    public DragHelper(ObjectProperty<NavigationBehavior> navigationBehavior, DragHandler dragHandler) {
        // it only makes sense for DRAG navigation behavior to be assigned; so assert() that
        this.navigationBehavior.addListener(new ChangeListener<NavigationBehavior>() {
            @Override public void changed(ObservableValue<? extends NavigationBehavior> ob, 
                    NavigationBehavior oldnb, NavigationBehavior newnb)
            {
                assert(newnb.getInput() == NavigationBehavior.Input.DRAG);
            }
        });        
        
        this.navigationBehavior.bind(navigationBehavior);
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
     * Sets the navigation behavior.
     * @param nb navigation behavior
     */
    public void setNavigationBehavior(NavigationBehavior nb) { navigationBehavior.set(nb); }

    /**
     * Gets the navigation behavior.
     * @return navigation behavior
     */
    public NavigationBehavior getNavigationBehavior() { return navigationBehavior.get(); }
    
    /**
     * Returns the navigation behavior property.
     * @return navigation behavior property
     */
    public ObjectProperty<NavigationBehavior> navigationBehaviorProperty() { return navigationBehavior; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private ObjectProperty<NavigationBehavior> navigationBehavior = 
            new SimpleObjectProperty<NavigationBehavior>(this, "navigationBehavior", null);
    private final DragHandler dragHandler;
    private double x;
    private double y;
    private InteractionHost host = null;
    
    private final EventHandler<MouseEvent> mousePressHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            final NavigationBehavior nb = navigationBehavior.get();
            if ((nb == null) || (nb.mouseEventMatches(me))) {
                x = me.getSceneX();
                y = me.getSceneY();
                dragHandler.handleClick(me);
            }
        }
    };
    
    private final EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent me) {
            final NavigationBehavior nb = navigationBehavior.get();
            if ((nb == null) || (nb.mouseEventMatches(me))) {
                final double oldX = x;
                final double oldY = y;
                x = me.getSceneX();
                y = me.getSceneY();
                dragHandler.handleDrag(me, x - oldX, y - oldY);
            }
        }
    };
    
}
