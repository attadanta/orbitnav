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

import javafx.scene.input.MouseEvent;

/**
 * Drag handler.  Handles dragging events for 3D interaction.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public interface DragHandler {
    
    /**
     * Handle an initial click event.
     * @param mouseEvent the mouse event invoking the click
     */
    void handleClick(MouseEvent mouseEvent);
    
    /**
     * Handle a drag event.
     * @param mouseEvent the mouse event invoking the drag
     * @param deltaX the change in mouse (scene) x coordinate
     * @param deltaY the change in mouse (scene) y coordinate
     */
    void handleDrag(MouseEvent mouseEvent, double deltaX, double deltaY);
    
}
