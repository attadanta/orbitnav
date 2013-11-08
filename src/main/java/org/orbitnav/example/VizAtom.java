package org.orbitnav.example;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import org.biojava.bio.structure.Atom;

public final class VizAtom extends Group {
    
    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public VizAtom(Atom atom, double radius) {
        this.atom = atom;
        this.radius = radius;
        init();
    }
    
    public boolean containsNode(Node n) { return (n == sphere); }
    
    public String getElementName() { return atom.getElement().name(); }
    
    public double getX() { return atom.getCoords()[0]; }
    public double getY() { return atom.getCoords()[1]; }
    public double getZ() { return atom.getCoords()[2]; }
    
    public double getRadius() { return radius; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE

    private static final int NUM_SPHERE_DIVISIONS = 24;
    
    private static final PhongMaterial MAT_CARBON     = new PhongMaterial(Color.web("#909090"));
    private static final PhongMaterial MAT_NITROGEN   = new PhongMaterial(Color.web("#3050F8"));
    private static final PhongMaterial MAT_OXYGEN     = new PhongMaterial(Color.web("#FF0D0D"));
    private static final PhongMaterial MAT_PHOSPHORUS = new PhongMaterial(Color.web("#FF8000"));
    
    private static final PhongMaterial[] MATERIALS = { null,
        null, null, null, null, null, MAT_CARBON, MAT_NITROGEN, MAT_OXYGEN, null, null,
        null, null, null, null, MAT_PHOSPHORUS
    };
    
    private void init() {
        sphere = new Sphere(radius, NUM_SPHERE_DIVISIONS);
        sphere.setTranslateX(getX());
        sphere.setTranslateY(getY());
        sphere.setTranslateZ(getZ());
        PhongMaterial material = getMaterial();
        if (material != null) {
            sphere.setMaterial(material);
        }
        getChildren().add(sphere);
    }
    
    private PhongMaterial getMaterial() {
        int atomicNumber = atom.getElement().getAtomicNumber();
        PhongMaterial material = null;
        if (atomicNumber < MATERIALS.length) {
            material = MATERIALS[atomicNumber];
        }
        return material;
    }

    private Sphere sphere;
    private final Atom atom;
    private final double radius;
    
}
