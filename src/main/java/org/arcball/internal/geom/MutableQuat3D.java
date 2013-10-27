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
 * Mutable quaternion.
 * <p>
 * This class is intended only for fast internal calculation use.
 * <p>
 * The internal quaternion data is stored as <code>a + b*i + c*j + d*k</code>.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class MutableQuat3D {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Sets the components of this quaternion equal to another.
     * @param q quaternion from which to copy components
     */
    public void set(MutableQuat3D q) {
        a = q.a;
        b = q.b;
        c = q.c;
        d = q.d;
    }
    
    /**
     * Sets this quaternion equal to an axis-angle rotation, with the angle specified in degrees.
     * @param x x component of the rotation axis
     * @param y y component of the rotation axis
     * @param z z component of the rotation axis
     * @param angleDegrees angle of rotation in degrees
     */
    public void setAxisAngleDegrees(double x, double y, double z, double angleDegrees) {
        setAxisAngleRadians(x, y, z, Math.toRadians(angleDegrees));
    }
    
    /**
     * Sets this quaternion equal to an axis-angle rotation, with the angle specified in degrees.
     * @param axis rotation axis
     * @param angleDegrees angle of rotation in degrees
     */
    public void setAxisAngleDegrees(MutableVec3D axis, double angleDegrees) {
        setAxisAngleDegrees(axis.getX(), axis.getY(), axis.getZ(), angleDegrees);
    }
    
    /**
     * Sets this quaternion equal to an axis-angle rotation, with the angle specified in radians.
     * @param x x component of the rotation axis
     * @param y y component of the rotation axis
     * @param z z component of the rotation axis
     * @param angleRadians angle of rotation in radians
     */
    public void setAxisAngleRadians(double x, double y, double z, double angleRadians) {
        final double a2 = angleRadians / 2.0;
        final double coeff = Math.sin(a2) / Math.sqrt(x * x + y * y + z * z);
        a = Math.cos(a2);
        b = x * coeff;
        c = y * coeff;
        d = z * coeff;
    }
    
    /**
     * Sets this quaternion equal to an axis-angle rotation, with the angle specified in radians.
     * @param axis rotation axis
     * @param angleRadians angle of rotation in radians
     */
    public void setAxisAngleRadians(MutableVec3D axis, double angleRadians) {
        setAxisAngleRadians(axis.getX(), axis.getY(), axis.getZ(), angleRadians);
    }
    
    /**
     * Returns the rotation angle represented by this quaternion, in degrees.
     * @return rotation angle in degrees
     */
    public double getAngleDegrees() { return Math.toDegrees(getAngleRadians()); }
    
    /**
     * Returns the rotation angle represented by this quaternion, in radians.
     * @return rotation angle in radians
     */
    public double getAngleRadians() { return 2.0 * Math.acos(a); }
    
    /**
     * Sets the <code>result</code> vector to the axis of rotation represented by this quaternion.
     * @param result vector that will be set to the axis of rotation
     */
    public void getAxis(MutableVec3D result) {
        final double coeff = 1.0 / Math.sin(Math.acos(a));
        result.set(b * coeff, c * coeff, d * coeff);
    }
    
    /**
     * Multiplies this quaternion by <code>q</code> and sets its value to the result of the multiplication.
     * @param q quaternion to post-muliply this quaternion
     */
    public void multiplyBy(MutableQuat3D q) {
        final double a1 = a;
        final double b1 = b;
        final double c1 = c;
        final double d1 = d;
        a = a1 * q.a - b1 * q.b - c1 * q.c - d1 * q.d;
        b = a1 * q.b + b1 * q.a + c1 * q.d - d1 * q.c;
        c = a1 * q.c - b1 * q.d + c1 * q.a + d1 * q.b;
        d = a1 * q.d + b1 * q.c - c1 * q.b + d1 * q.a;        
    }
    
    /**
     * Normalizes this quaternion.  Values <code>b</code>, <code>c</code> and <code>d</code> are divided by the
     * vector length.
     */
    public void normalize() {
        final double l = Math.sqrt(b * b + c * c + d * d);
        b /= l;
        c /= l;
        d /= l;
    }    
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private double a;
    private double b;
    private double c;
    private double d;
    
}
