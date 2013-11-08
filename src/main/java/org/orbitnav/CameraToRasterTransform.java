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
package org.orbitnav;

import javafx.geometry.Point2D;

/**
 * Performs a coordinate transformation from a 3D scene to the 2D coordinates of the raster on which the scene is
 * displayed.
 */
public interface CameraToRasterTransform {

    /**
     * Transforms a 3D coordinate from the scene into 2D coordinates.
     *
     * @param x x coordinate in the 3D scene
     * @param y y coordinate in the 3D scene
     * @param z z coordinate in the 3D scene
     * @return 2D raster coordinate
     */
    Point2D transform(double x, double y, double z);

    /**
     * Transforms the radius of a sphere centered at coordinates (<code>x</code>, <code>y</code>, <code>z</code>) in
     * the 3D scene to the equivalent radius of a circle on the raster.
     *
     * @param x x coordinate of the center of the sphere in the 3D scene
     * @param y y coordinate of the center of the sphere in the 3D scene
     * @param z z coordinate of the center of the sphere in the 3D scene
     * @param radius radius of the sphere in the 3D scene
     * @return radius of the sphere on the 2D raster
     */
    double transformRadius(double x, double y, double z, double radius);

}
