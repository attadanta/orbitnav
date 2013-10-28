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

import org.arcball.internal.Util;

/**
 * Mutable representation of a turntable rotation.
 * <p>
 * This class is intended only for fast internal calculation use.
 * <p>
 * This class represents a rotation as a pair of concatenated rotations.  First, a rotation about the z-axis, followed
 * by a rotation about the x-axis.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class MutableTurntable3D {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Sets this turntable rotation (angles expressed in radians).
     * @param xRotation x rotation angle (radians)
     * @param yRotation z rotation angle (radians)
     */
    public void setRadians(double xRotation, double zRotation) {
        xRot = Util.normalizeAngle(xRotation);
        zRot = Util.normalizeAngle(zRotation);
    }
    
    /**
     * Sets this turntable rotation (angles expressed in degrees).
     * @param xRotation x rotation angle (degrees)
     * @param yRotation z rotation angle (degrees)
     */
    public void setDegrees(double xRotation, double zRotation) {
        setRadians(Math.toRadians(xRotation), Math.toRadians(zRotation));
    }
    
    /**
     * Sets this turntable rotation to the closest axis-angle representation.
     * @param a axis-angle representation
     */
    public void setToClosestAxisAngle(MutableAxisAngle3D a) {
        final MutableVec3D axis = a.getAxis();
        zRot = 2.0 * Math.atan2(axis.getY(), axis.getX());
        xRot = 2.0 * Math.acos(Math.cos(a.getAngleRadians() / 2.0) / Math.cos(zRot / 2.0));
    }
    
    /**
     * Gets this turntable rotation as an axis-angle rotation.
     * @param result axis-angle rotation to be set equal to this turntable rotation
     */
    public void getAxisAngle(MutableAxisAngle3D result) {
        // TODO: Rodrigues' formula?
        qtemp.setAxisAngleRadians(0, 0, 1, zRot);
        qtemp.concatXRotationRadians(xRot);
        result.set(qtemp);
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private double xRot;
    private double zRot;
    private final MutableQuat3D qtemp = new MutableQuat3D();
    
}
