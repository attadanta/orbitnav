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

import static org.orbitnav.NavigationBehavior.Response.ROTATE;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import static javafx.scene.input.MouseButton.PRIMARY;
import javafx.scene.input.MouseEvent;

/**
 * Turntable drag interaction.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionDragXZTurntable extends InteractionDrag {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDragXZTurntable(DoubleProperty xRotation, DoubleProperty zRotation) {
        this.xRotation.bindBidirectional(xRotation);
        this.zRotation.bindBidirectional(zRotation);
        
        setNavigationBehavior(NavigationBehavior.drag(PRIMARY, ROTATE));        
    }

    public DoubleProperty xRotationProperty() { return xRotation; }
    
    public DoubleProperty zRotationProperty() { return zRotation; }
    
    public DoubleProperty rotationCoefficientProperty() { return rotationCoefficient; }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected DragHandler getDragHandler() {
        return new DragHandlerAdaptor() {
            @Override public void handleDrag(MouseEvent mouseEvent, double deltaX, double deltaY) {
                final double oldXRot = xRotation.get();
                final double oldZRot = zRotation.get();
                final double coeff = rotationCoefficient.get();
                final double zRotationSign = (oldXRot > 180.0) ? (1.0) : (-1.0);
                final double newXRotation = oldXRot - (coeff * deltaY);
                final double newZRotation = oldZRot - (zRotationSign * coeff * deltaX); 
                xRotation.set(Util.normalizeAngle(newXRotation));
                zRotation.set(Util.normalizeAngle(newZRotation));
            }        
        };
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty xRotation = new SimpleDoubleProperty(this, "xRotation", 0);
    private final DoubleProperty zRotation = new SimpleDoubleProperty(this, "yRotation", 0);
    private final DoubleProperty rotationCoefficient = new SimpleDoubleProperty(this, "rotationCoefficient", 0.4);
    
}
