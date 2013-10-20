package org.arcball;

import javafx.geometry.Point2D;

public interface CameraTo2DTransform {
    Point2D transform(double x, double y, double z);
    double transformRadius(double x, double y, double z, double radius);
}
