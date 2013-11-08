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
 * Minimal adaptor class for a {@link DragHandler DragHandler}.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public class DragHandlerAdaptor implements DragHandler {
    @Override public void handleClick(MouseEvent mouseEvent) { }
    @Override public void handleDrag(MouseEvent mouseEvent, double deltaX, double deltaY) { }
}
