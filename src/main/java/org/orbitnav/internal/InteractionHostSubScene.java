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

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Camera;
import javafx.scene.SubScene;

/**
 * Adaptor class to convert a <code>SubScene</code> to an {@link org.orbitnav.internal.InteractionHost InteractionHost}.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public class InteractionHostSubScene implements InteractionHost {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionHostSubScene(SubScene subscene) {
        this.subscene = subscene;
    }
    
    @Override public <T extends Event> void addEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        subscene.addEventHandler(eventType, eventHandler);
    }

    @Override public <T extends Event> void removeEventHandler(EventType<T> eventType,
            EventHandler<? super T> eventHandler) 
    {
        subscene.removeEventHandler(eventType, eventHandler);
    }

    @Override public ReadOnlyDoubleProperty widthProperty() { return subscene.widthProperty(); }

    @Override public ReadOnlyDoubleProperty heightProperty() { return subscene.heightProperty(); }

    @Override public double getWidth() { return subscene.getWidth(); }

    @Override public double getHeight() { return subscene.getHeight(); }
    
    @Override public void setCamera(Camera camera) { subscene.setCamera(camera); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private final SubScene subscene;    
    
}
