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

import org.orbitnav.internal.geom.MutableQuat3D;
import org.orbitnav.internal.geom.MutableVec3D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.MouseEvent;

/**
 * Arcball drag interaction.
 *
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionDragArcball extends InteractionDrag {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public InteractionDragArcball(DoubleProperty rotationAngle,
            DoubleProperty rotationAxisX, DoubleProperty rotationAxisY, DoubleProperty rotationAxisZ)
    {
        this.rotationAngle.bindBidirectional(rotationAngle);
        this.rotationAxisX.bindBidirectional(rotationAxisX);
        this.rotationAxisY.bindBidirectional(rotationAxisY);
        this.rotationAxisZ.bindBidirectional(rotationAxisZ);
        
        ChangeListener<Number> whChangeListener = (o, old, value) -> updateArcballCenterAndRadius();
        widthProperty().addListener(whChangeListener);
        heightProperty().addListener(whChangeListener);
    }

    //------------------------------------------------------------------------------------------------------- PROTECTED

    protected DragHandler getDragHandler() { return dragHandler; }

    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final DoubleProperty rotationAxisX = new SimpleDoubleProperty(this, "rotationAxisX", 0);
    private final DoubleProperty rotationAxisY = new SimpleDoubleProperty(this, "rotationAxisY", 0);
    private final DoubleProperty rotationAxisZ = new SimpleDoubleProperty(this, "rotationAxisZ", 0);
    private final DoubleProperty rotationAngle = new SimpleDoubleProperty(this, "rotationAngle", 0);

    private double centerX;  // centerX = width / 2
    private double centerY;  // centerY = height / 2
    private double radius;   // radius = max(width, height) / 3
    
    private final MutableQuat3D startQuat = new MutableQuat3D();
    private final MutableQuat3D deltaQuat = new MutableQuat3D();
    private final MutableQuat3D finalQuat = new MutableQuat3D();
    private final MutableVec3D startArcballVector = new MutableVec3D();    
    private final MutableVec3D currentArcballVector = new MutableVec3D();
    private final MutableVec3D rotationAxis = new MutableVec3D();
    
    private void updateArcballCenterAndRadius() {
        centerX = getWidth() / 2.0;
        centerY = getHeight() / 2.0;
        radius = Math.max(getWidth(), getHeight()) / 3.0;
    }
    
    private void projectScenePointToSphere(MutableVec3D result, double sceneX, double sceneY) {
        final double xp = (sceneX - centerX) / radius;
        final double yp = (sceneY - centerY) / radius;
        final double l2 = xp * xp + yp * yp;
        if (l2 <= 1.0) {
            result.set(xp, yp, -Math.sqrt(1.0 - l2));
        } else {
            final double l = Math.sqrt(l2);
            result.set(xp / l, yp / l, 0);
        }
    }

    private final DragHandler dragHandler = new DragHandler() {
        @Override public void handleClick(MouseEvent me) {
            projectScenePointToSphere(startArcballVector, me.getSceneX(), me.getSceneY());
            startQuat.setAxisAngleDegrees(rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get(),
                    rotationAngle.get());
        }
        @Override public void handleDrag(MouseEvent me, double deltaX, double deltaY) {
            projectScenePointToSphere(currentArcballVector, me.getSceneX(), me.getSceneY());
            // find the quaternion rotation between the initial arcball vector and the current arcball vector
            rotationAxis.cross(startArcballVector, currentArcballVector);
            final double arcballDot = MutableVec3D.dot(startArcballVector, currentArcballVector);
            final double angleRadians = -Math.acos(Math.min(1.0, arcballDot));
            deltaQuat.setAxisAngleRadians(rotationAxis, angleRadians);
            // set the current rotation
            if (angleRadians != 0) {
                finalQuat.set(startQuat);
                finalQuat.multiplyBy(deltaQuat);
                finalQuat.normalize();
                finalQuat.getAxis(rotationAxis);
                rotationAxisX.set(rotationAxis.getX());
                rotationAxisY.set(rotationAxis.getY());
                rotationAxisZ.set(rotationAxis.getZ());
                rotationAngle.set(Util.normalizeAngle(finalQuat.getAngleDegrees()));
            }
        }
    };

}
