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
package org.orbitnav.internal.geom;

import javafx.scene.transform.Affine;

/**
 * Mutable axis-angle representation of a rotation.
 * <p>
 * This class is intended only for fast internal calculation use.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class MutableAxisAngle3D {
    
    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Sets this axis-angle rotation representation using an angle in degrees.
     * @param x x component of the axis
     * @param y y component of the axis
     * @param z z component of the axis
     * @param angleDegrees angle of rotation in degrees
     */
    public void setDegrees(double x, double y, double z, double angleDegrees) {
        setRadians(x, y, z, Math.toRadians(angleDegrees));
    }
    
    /**
     * Sets this axis-angle rotation representation using an angle in radians.
     * @param x x component of the axis
     * @param y y component of the axis
     * @param z z component of the axis
     * @param angleRadians angle of rotation in radians
     */
    public void setRadians(double x, double y, double z, double angleRadians) {
        final double l = Math.sqrt(x * x + y * y + z * z);
        axis.set(x / l, y / l, z / l);
        angle = angleRadians;
    }
    
    /**
     * Sets this axis-angle rotation equal to a rotation stored in a quaternion.
     * @param q quaternion containing the rotation to copy
     */
    public void set(MutableQuat3D q) {
        q.getAxis(axis);
        angle = q.getAngleRadians();
    }
    
    /**
     * Sets this axis-angle rotation equal to a turntable rotation.
     * @param t turntable containing the rotation to copy
     */
    public void set(MutableTurntable3D t) {
        t.getAxisAngle(this);
    }
    
    /**
     * Returns the axis of this axis-angle rotation.
     * <p>
     * The returned axis is the actual mutable vector storing the axis, not a copy.
     * @return axis
     */
    public MutableVec3D getAxis() { return axis; }

    /**
     * Returns the angle in degrees.
     * @return angle in degrees
     */
    public double getAngleDegrees() { return Math.toDegrees(angle); }
    
    /**
     * Returns the angle in radians.
     * @return angle in radians
     */
    public double getAngleRadians() { return angle; }
    
    /**
     * Returns this axis-angle rotation as an <code>Affine</code> transformation.
     * @param result <code>Affine</code> object to set to this axis-angle rotation
     */
    public void getAffine(Affine result) {
        result.setToIdentity();
        result.appendRotation(getAngleDegrees(), 0, 0, 0, axis.getX(), axis.getY(), axis.getZ());
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    /**
     * Rotation axis.
     */
    private MutableVec3D axis = new MutableVec3D();
    
    /**
     * Rotation angle (radians).
     */
    private double angle;

}
