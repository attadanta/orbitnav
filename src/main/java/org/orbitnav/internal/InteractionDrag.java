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

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import static javafx.scene.input.MouseEvent.*;
import org.orbitnav.NavigationBehavior;

/**
 * Abstract base class for all dragging interactions.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public abstract class InteractionDrag extends InteractionBase {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDrag() {
        super();
        // it only makes sense for mouseDrag navigation behavior to be assigned; so assert() that
        navigationBehaviorProperty().addListener((o, old, value) -> { assert(value.isMouseDrag()); });
    }
    
    @Override public void attachToHost(Host host) {
        super.attachToHost(host);
        host.addEventHandler(MOUSE_PRESSED, mousePressHandler);
        host.addEventHandler(MOUSE_DRAGGED, mouseDragHandler);
    }
    
    @Override public void detachFromHost(Host host) {
        host.removeEventHandler(MOUSE_PRESSED, mousePressHandler);
        host.removeEventHandler(MOUSE_DRAGGED, mouseDragHandler);
        super.detachFromHost(host);
    }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED

    /**
     * Returns the handler for drag events.
     *
     * <p>
     * This method must be implemented by sub-classes.  This method is called to obtain a
     * {@link DragHandler DragHandler} that will respond to dragging events produced by the {@link Host Host}.
     *
     * <p>
     * This method will be called multiple times by <code>InteractionDrag</code> (once per event handler invocation),
     * so the implementation should ideally just return a final field.
     *
     * @return drag handler
     */
    protected abstract DragHandler getDragHandler();
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private double x;
    private double y;
    private double oldX;
    private double oldY;

    private final EventHandler<MouseEvent> mousePressHandler = (m) -> {
        final NavigationBehavior nb = getNavigationBehavior();
        if (nb.inputEventMatches(m)) {
            x = m.getSceneX();
            y = m.getSceneY();
            getDragHandler().handleClick(m);
        }
    };

    private final EventHandler<MouseEvent> mouseDragHandler = (m) -> {
        final NavigationBehavior nb = getNavigationBehavior();
        if (nb.inputEventMatches(m)) {
            oldX = x;
            oldY = y;
            x = m.getSceneX();
            y = m.getSceneY();
            getDragHandler().handleDrag(m, x - oldX, y - oldY);
        }
    };

}
