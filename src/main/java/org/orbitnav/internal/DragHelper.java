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
package org.orbitnav.internal;

import org.orbitnav.NavigationBehavior;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;
import static javafx.scene.input.MouseEvent.MOUSE_DRAGGED;

/**
 * Drag helper class.  Keeps track of mouse press and drag events for a <code>Scene</code> or <code>SubScene</code>.
 * <p>
 * In order to respond to drag events for view changes, it is typically necessary to know only the change in 
 * coordinates that is produced as the mouse is dragged.  This class keeps track of only those coordinate changes,
 * reporting them to a {@link org.orbitnav.internal.DragHandler DragHandler} as required.  The helper can be attached
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
        this.navigationBehavior.addListener((o, old, value) -> { assert(value.isMouseDrag()); } );
        
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
        host.addEventHandler(MOUSE_PRESSED, mousePressHandler);
        host.addEventHandler(MOUSE_DRAGGED, mouseDragHandler);
    }

    /**
     * Detaches from a host to stop receving mouse events.
     * 
     * @param host host from which to detach
     */
    public void detachFromHost(InteractionHost host) {
        assert(this.host == host);
        host.removeEventHandler(MOUSE_PRESSED, mousePressHandler);
        host.removeEventHandler(MOUSE_DRAGGED, mouseDragHandler);
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
            new SimpleObjectProperty<>(this, "navigationBehavior", null);
    private DragHandler dragHandler; // TODO: dragHandler should be final, but problems arise with lambdas
    private double x;
    private double y;
    private InteractionHost host = null;
    
    private final EventHandler<MouseEvent> mousePressHandler = (m) -> {
    	final NavigationBehavior nb = navigationBehavior.get();
    	if ((nb == null) || (nb.inputEventMatches(m))) {
    		x = m.getSceneX();
    		y = m.getSceneY();
    		dragHandler.handleClick(m);
    	}
    };

    private final EventHandler<MouseEvent> mouseDragHandler = (m) -> {
    	final NavigationBehavior nb = navigationBehavior.get();
    	if ((nb == null) || (nb.inputEventMatches(m))) {
    		final double oldX = x;
    		final double oldY = y;
    		x = m.getSceneX();
    		y = m.getSceneY();
    		dragHandler.handleDrag(m, x - oldX, y - oldY);
    	}
    };
    
}
