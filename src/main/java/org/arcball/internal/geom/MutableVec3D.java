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
package org.arcball.internal.geom;

/**
 * Mutable 3D vector.
 * <p>
 * This class is intended only for fast internal vector calculation use.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class MutableVec3D {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Sets the vector.
     * @param x x component
     * @param y y component
     * @param z z component
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x component of the vector.
     * @return x component
     */
    public double getX() { return x; }
    
    /**
     * Returns the y component of the vector.
     * @return y component
     */
    public double getY() { return y; }
    
    /**
     * Returns the z component of the vector.
     * @return z component
     * @return
     */
    public double getZ() { return z; }
    
    /**
     * Returns the dot product of a pair of vectors.
     * @param a vector in the dot product
     * @param b vector in the dot product
     * @return dot product
     */
    public static double dot(MutableVec3D a, MutableVec3D b) { return (a.x * b.x + a.y * b.y + a.z * b.z); }

    /**
     * Sets this vector equal to the cross product of two vectors.
     * @param a first vector in the cross product
     * @param b second vector in the cross product
     */
    public void cross(MutableVec3D a, MutableVec3D b) {
        x = a.y * b.z - a.z * b.y;
        y = a.z * b.x - a.x * b.z;
        z = a.x * b.y - a.y * b.x;        
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private double x;
    private double y;
    private double z;
    
}
