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

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Affine;

/**
 * Represents an axis triad (x, y and z axes).  The axes are drawn as cylinders.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class AxisTriad extends Group {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Creates a new <code>AxisTriad</code> that has axes of length 1 and radius of 0.025.
     */
    public AxisTriad() { this(1.0, 0.025); }
    
    /**
     * Creates a new <code>AxisTriad</code>.
     * @param axisLength length of each of the x, y and z axes
     * @param axisRadius radius of the cylinder used to represent the axes
     */
    public AxisTriad(double axisLength, double axisRadius) {
        createGeometry(axisLength, axisRadius);
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private final static int CYLINDER_DIVISIONS = 16;

    private void createGeometry(double axisLength, double axisRadius) {
        
        // x axis
        final Cylinder xAxis = new Cylinder(axisRadius, axisLength, CYLINDER_DIVISIONS);
        final Affine xAxisTransform = new Affine();
        xAxisTransform.appendTranslation(axisLength / 2.0, 0, 0);
        xAxisTransform.appendRotation(-90, 0, 0, 0, 0, 0, 1);
        xAxis.getTransforms().add(xAxisTransform);
        xAxis.setMaterial(createDiffuseMaterial(Color.RED));
        
        // y axis
        final Cylinder yAxis = new Cylinder(axisRadius, axisLength, CYLINDER_DIVISIONS);
        final Affine yAxisTransform = new Affine();
        yAxisTransform.appendTranslation(0, axisLength / 2.0, 0);
        yAxis.getTransforms().add(yAxisTransform);
        yAxis.setMaterial(createDiffuseMaterial(Color.GREEN));
        
        // z axis
        final Cylinder zAxis = new Cylinder(axisRadius, axisLength, CYLINDER_DIVISIONS);
        final Affine zAxisTransform = new Affine();
        zAxisTransform.appendTranslation(0, 0, axisLength / 2.0);
        zAxisTransform.appendRotation(90, 0, 0, 0, 1, 0, 0);
        zAxis.getTransforms().add(zAxisTransform);
        zAxis.setMaterial(createDiffuseMaterial(Color.BLUE));
        
        // add axes to the enclosing group
        getChildren().addAll(xAxis, yAxis, zAxis);
        
    }
    
    private PhongMaterial createDiffuseMaterial(Color color) {
        final PhongMaterial material = new PhongMaterial(color);
        material.setSpecularColor(Color.BLACK);
        return material;
    }
        
}
