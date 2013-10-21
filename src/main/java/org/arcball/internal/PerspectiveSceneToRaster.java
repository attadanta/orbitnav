package org.arcball.internal;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import org.arcball.CameraToRasterTransform;

public final class PerspectiveSceneToRaster implements CameraToRasterTransform {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public PerspectiveSceneToRaster() { }

    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation,
                              double width, double height)
    {
        this.camera = camera;
        this.transformRotationTranslation = transformRotationTranslation;
        this.width = width;
        this.height = height;
    }
    
    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation, Scene scene) {
        setParameters(camera, transformRotationTranslation, scene.getWidth(), scene.getHeight());
    }
    
    public void setParameters(PerspectiveCamera camera, Transform transformRotationTranslation, SubScene subscene) {
        setParameters(camera, transformRotationTranslation, subscene.getWidth(), subscene.getHeight());
    }
    
    @Override public Point2D transform(double x, double y, double z) {
        Point3D p3d = null;
        try {
            p3d = transformRotationTranslation.inverseTransform(x, y, z);
        } catch (NonInvertibleTransformException ex) { /* should not occur! */ }
        final double w2 = width / 2.0;
        final double h2 = height / 2.0;
        final double c = focalLength() * w2 / p3d.getZ();
        final double xprime = c * p3d.getX() + w2;
        final double yprime = c * p3d.getY() + h2;
        return new Point2D(xprime, yprime);
    }
    
    @Override public double transformRadius(double x, double y, double z, double radius) {
        Point3D p3d = null;
        try {
            p3d = transformRotationTranslation.inverseTransform(x, y, z);
        } catch (NonInvertibleTransformException ex) { /* should not occur! */ }
        final double w2 = width / 2.0;
        final double c = focalLength() * w2 / p3d.getZ();
        final double xprime = c * p3d.getX() + w2;
        final double xpprime = c * (p3d.getX() + radius) + w2;
        return Math.abs(xpprime - xprime);
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private PerspectiveCamera camera;
    private Transform transformRotationTranslation;
    private double width;
    private double height;
    
    private double focalLength() {
        final double fov = Util.getHorizontalFieldOfView(camera, width, height);
        return 1.0 / Math.tan(Math.toRadians(fov / 2.0));
    }
    
}
