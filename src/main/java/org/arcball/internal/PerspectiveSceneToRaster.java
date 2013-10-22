package org.arcball.internal;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import org.arcball.CameraToRasterTransform;

/**
 * A {@link org.arcball.CameraToRasterTransform CameraToRasterTransform} that uses a rigid body transformation plus
 * a <code>PerspectiveCamera</code>.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class PerspectiveSceneToRaster implements CameraToRasterTransform {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public PerspectiveSceneToRaster() { }

    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation,
                              double width, double height)
    {
        this.transformRotationTranslation = transformRotationTranslation;
        this.w2 = width / 2.0;
        this.h2 = height / 2.0;
        final double fov = Util.getHorizontalFieldOfView(camera, width, height);
        final double focalLength = 1.0 / Math.tan(Math.toRadians(fov / 2.0));
        this.flcoeff = focalLength * this.w2;
    }
    
    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation, Scene scene) {
        setParameters(camera, transformRotationTranslation, scene.getWidth(), scene.getHeight());
    }
    
    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation, SubScene subscene) {
        setParameters(camera, transformRotationTranslation, subscene.getWidth(), subscene.getHeight());
    }
    
    @Override public Point2D transform(double x, double y, double z) {
        try {
            final Point3D p3d = transformRotationTranslation.inverseTransform(x, y, z);
            final double c = flcoeff / p3d.getZ();
            return new Point2D(c * p3d.getX() + w2, c * p3d.getY() + h2);
        } catch (NonInvertibleTransformException ex) { return new Point2D(0, 0); /* should not occur! */ }
    }
    
    @Override public double transformRadius(double x, double y, double z, double radius) {
        try {
            final Point3D p3d = transformRotationTranslation.inverseTransform(x, y, z);
            return Math.abs(flcoeff * radius / p3d.getZ());
        } catch (NonInvertibleTransformException ex) { return 0; /* should not occur! */ }
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private Transform transformRotationTranslation;
    private double w2;
    private double h2;
    private double flcoeff;
        
}
