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

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * A hacked <code>ReadOnlyObjectProperty</code> that doesn't generate garbage.
 * <p>
 * A typical <code>Property</code> requires new objects to be created and set.  This property instead allows a mutable
 * object to be stored, and changes fired manually.  This is primarily intended to avoid creating lots of different
 * <code>Affine</code> and {@link org.arcball.internal.PerspectiveSceneToRaster PerspectiveSceneToRaster} objects
 * in transformations.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 *
 * @param <T> type of the property
 */
public final class NoGarbageProperty<T> extends ReadOnlyObjectProperty<T> {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public NoGarbageProperty(Object bean, String name, T init) {
        this.bean = bean;
        this.name = name;
        this.value = init;
    }
    
    public void fireChangedEvent() {
        for (ChangeListener<? super T> l : changeListeners) { l.changed(this, value, value); }
    }
    
    @Override public T get() { return value; }
    @Override public Object getBean() { return bean; }
    @Override public String getName() { return name; }    
    @Override public void addListener(ChangeListener<? super T> l) { changeListeners.add(l); }
    @Override public void removeListener(ChangeListener<? super T> l) { changeListeners.remove(l); }
    @Override public void addListener(InvalidationListener l) { /* invalidationListeners.add(l); */ }
    @Override public void removeListener(InvalidationListener l) { /* invalidationListeners.remove(l); */ }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final T value;
    private final Object bean;
    private final String name;
    private final List<ChangeListener<? super T>> changeListeners = new ArrayList<ChangeListener<? super T>>();
    
}
