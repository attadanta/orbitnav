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
package org.arcball.internal;

import org.arcball.NavigationBehavior;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Abstract base class for all dragging interactions.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public abstract class InteractionDrag extends InteractionBase {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDrag() {
        super();
        // it only makes sense for DRAG navigation behavior to be assigned; so assert() that
        navigationBehaviorProperty().addListener(new ChangeListener<NavigationBehavior>() {
            @Override public void changed(ObservableValue<? extends NavigationBehavior> ob, 
                    NavigationBehavior oldnb, NavigationBehavior newnb)
            {
                assert(newnb.getInput() == NavigationBehavior.Input.DRAG);
            }
        });        
    }
    
    @Override public void attachToHost(InteractionHost host) {
        super.attachToHost(host);
        dragHelper.attachToHost(host);
    }
    
    @Override public void detachFromHost(InteractionHost host) {
        super.detachFromHost(host);
        dragHelper.detachFromHost(host);
    }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected abstract DragHandler getDragHandler();
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final DragHelper dragHelper = new DragHelper(navigationBehaviorProperty(), getDragHandler());
    
}
