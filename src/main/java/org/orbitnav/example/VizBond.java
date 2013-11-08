package org.orbitnav.example;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Affine;

import org.biojava.bio.structure.Bond;

public final class VizBond extends Group {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public VizBond(Bond bond, double radius) {
        this.bond = bond;
        this.radius = radius;
        init();
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static final int NUM_CYLINDER_DIVISIONS = 8;
    
    private void init() {
        
        double aloc[] = bond.getAtomA().getCoords();
        double bloc[] = bond.getAtomB().getCoords();
        Point3D a = new Point3D(aloc[0], aloc[1], aloc[2]);
        Point3D b = new Point3D(bloc[0], bloc[1], bloc[2]);
        Point3D center = a.midpoint(b);
        double length = a.distance(b);

        Cylinder cylinder = new Cylinder(radius, length, NUM_CYLINDER_DIVISIONS);
        Affine align = new Affine();
        align.append(LookAt.lookAt(center, b, new Point3D(0, 0, 1)));
        align.appendRotation(90, 0, 0, 0, 1, 0, 0);
        cylinder.getTransforms().add(align);
        getChildren().add(cylinder);
    }
        
    private final Bond bond;
    private final double radius;
    
}
