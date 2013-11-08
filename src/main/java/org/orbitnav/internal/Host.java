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

/**
 * A host for {@link Attachable Attachable} and {@link Interaction Interaction} classes.
 *
 * <p>
 * Hosts are normally JavaFX <code>Scene</code> or <code>SubScene</code> objects, wrapped by the
 * {@link HostScene HostScene} and {@link HostSubScene HostSubScene} classes.  Hosts provide event dispatching for
 * <code>InputEvent</code>s, which {@link Interaction Interactions} respond to.  They also provide
 * {@link #widthProperty() width} and {@link #heightProperty() height}
 * information, which is then used by the interactions to scale responses appropriately.  Finally, hosts also possess
 * a JavaFX camera.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public interface Host {

    /**
     * Adds an event handler to this host.
     *
     * <p>
     * Event handlers are typically used to receive <code>InputEvent</code>s from the host.
     *
     * @param eventType type of event to receive
     * @param eventHandler handler for the events
     * @param <T> the event class
     * @see #removeEventHandler(javafx.event.EventType, javafx.event.EventHandler)
     */
    <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

    /**
     * Removes an event handler from this host.
     *
     * @param eventType type of event to receive
     * @param eventHandler handler for the events
     * @param <T> the event class
     * @see #addEventHandler(javafx.event.EventType, javafx.event.EventHandler)
     */
    <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler);

    /**
     * Returns the width property.
     * @return the width property
     */
    ReadOnlyDoubleProperty widthProperty();

    /**
     * Returns the width of the host.
     * @return width
     */
    double getWidth();

    /**
     * Returns the height property.
     * @return the height of the host
     */
    ReadOnlyDoubleProperty heightProperty();

    /**
     * Returns the height of the host.
     * @return the height of the host
     */
    double getHeight();

    /**
     * Sets the camera that should be used by this host.
     * @param camera camera that the host should use
     */
    void setCamera(Camera camera);
}
