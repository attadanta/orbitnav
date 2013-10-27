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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.arcball.NavigationBehavior;

public abstract class InteractionBase implements Attachable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC

    public void attachToHost(InteractionHost host) {
        width.bind(host.widthProperty());
        height.bind(host.heightProperty());
    }
    
    public void detachFromHost(InteractionHost host) {
        width.unbind();
        height.unbind();
    }
        
    public ObjectProperty<NavigationBehavior> navigationBehaviorProperty() { return navigationBehavior; }
    public NavigationBehavior getNavigationBehavior() { return navigationBehavior.get(); }
    public void setNavigationBehavior(NavigationBehavior nb) { navigationBehavior.set(nb); }
    
    public ReadOnlyDoubleProperty widthProperty() { return width; }
    public double getWidth() { return width.get(); }
    
    public ReadOnlyDoubleProperty heightProperty() { return height; }
    public double getHeight() { return height.get(); }    
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final ObjectProperty<NavigationBehavior> navigationBehavior =
            new SimpleObjectProperty<NavigationBehavior>(this, "navigationBehavior", null);
    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", 1.0);
    private final DoubleProperty height = new SimpleDoubleProperty(this, "height", 1.0);    
    
}
