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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.orbitnav.NavigationBehavior;

/**
 * Base class for {@link Interaction Interactions}, providing concrete implementations of the interaction methods.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public abstract class InteractionBase implements Interaction {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    @Override public void attachToHost(Host host) {
        assert(this.host == null);
        this.host = host;
        width.bind(host.widthProperty());
        height.bind(host.heightProperty());
    }
    
    @Override public void detachFromHost(Host host) {
        assert(this.host == host);
        this.host = null;
        width.unbind();
        height.unbind();
    }

    @Override public ObjectProperty<NavigationBehavior> navigationBehaviorProperty() { return navigationBehavior; }
    @Override public NavigationBehavior getNavigationBehavior() { return navigationBehavior.get(); }
    @Override public void setNavigationBehavior(NavigationBehavior nb) { navigationBehavior.set(nb); }

    /**
     * Property bound to the width of the interaction's current host.
     * @return property bound to the width of the interaction's current host
     */
    public ReadOnlyDoubleProperty widthProperty() { return width; }

    /**
     * Returns the width of the interaction's current host.
     * @return the width of the current host
     */
    public double getWidth() { return width.get(); }

    /**
     * Property bound to the height of the interaction's current host.
     * @return property bound to the height of the interaction's current host
     */
    public ReadOnlyDoubleProperty heightProperty() { return height; }

    /**
     * Returns the height of the interaction's current host.
     * @return height of the current host
     */
    public double getHeight() { return height.get(); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final ObjectProperty<NavigationBehavior> navigationBehavior =
            new SimpleObjectProperty<>(this, "navigationBehavior", null);
    private Host host;
    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", 1.0);
    private final DoubleProperty height = new SimpleDoubleProperty(this, "height", 1.0);
    
}
