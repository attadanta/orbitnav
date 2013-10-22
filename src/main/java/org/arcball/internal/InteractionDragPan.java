package org.arcball.internal;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * Panning drag interaction.
 * 
 * @author Jonathan Merritt (<a href="mailto:j.s.merritt@gmail.com">j.s.merritt@gmail.com</a>)
 */
public final class InteractionDragPan extends InteractionDrag {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    /**
     * Creates a new instance of <code>InteractionPan</code>.
     * 
     * @param originX origin x property
     * @param originY origin y property
     * @param originZ origin z property
     * @param viewRotation rotation-only component of the viewing transformation
     * @param distanceFromOrigin distance from origin
     * @param camera camera property
     */
    public InteractionDragPan(DoubleProperty originX, DoubleProperty originY, DoubleProperty originZ,
                          ReadOnlyObjectProperty<Transform> viewRotation, ReadOnlyDoubleProperty distanceFromOrigin,
                          ReadOnlyObjectProperty<PerspectiveCamera> camera)
    {
        // bind basic properties
        this.originX.bindBidirectional(originX);
        this.originY.bindBidirectional(originY);
        this.originZ.bindBidirectional(originZ);
        this.viewRotation.bind(viewRotation);
        this.distanceFromOrigin.bind(distanceFromOrigin);
        this.camera.bind(camera);
        
        // set the default trigger button
        setTriggerButton(DEFAULT_TRIGGER_BUTTON);
        
        // listen for changes to the camera (to update the pan scale coefficient)
        final ChangeListener<PerspectiveCamera> cameraChangeListener = new ChangeListener<PerspectiveCamera>() {
            @Override public void changed(ObservableValue<? extends PerspectiveCamera> ob, 
                    PerspectiveCamera oldCamera, PerspectiveCamera newCamera)
            {
                coeffDirty = true;
            }
        };
        // listen for other changes that affect the pan scale coefficient
        final ChangeListener<Number> coeffParamListener = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
                coeffDirty = true;
            }
        };
        // attach listeners to properties that affect the pan scale coefficient
        widthProperty().addListener(coeffParamListener);
        heightProperty().addListener(coeffParamListener);
        this.distanceFromOrigin.addListener(coeffParamListener);
        this.camera.addListener(cameraChangeListener);
    }
        
    public DoubleProperty originXProperty() { return originX; }
    
    public DoubleProperty originYProperty() { return originY; }
    
    public DoubleProperty originZProperty() { return originZ; }
    
    public ObjectProperty<Transform> viewRotationProperty() { return viewRotation; }
    
    public DoubleProperty distanceFromOriginProperty() { return distanceFromOrigin; }
    
    public ObjectProperty<PerspectiveCamera> cameraProperty() { return camera; }
    
    //------------------------------------------------------------------------------------------------------- PROTECTED
    
    protected DragHandler getDragHandler() {
        return new DragHandlerAdaptor() {
            @Override public void handleDrag(MouseEvent me, double deltaX, double deltaY) {
                updateCoeff();
                // find local x and y vector shifts for the camera
                final Point3D dxVec = viewRotation.get().transform(STARTING_X_VEC).multiply(coeff * deltaX);
                final Point3D dyVec = viewRotation.get().transform(STARTING_Y_VEC).multiply(coeff * deltaY);
                // perform shifts along x and y
                originX.set(originX.get() - dxVec.getX() - dyVec.getX());
                originY.set(originY.get() - dxVec.getY() - dyVec.getY());
                originZ.set(originZ.get() - dxVec.getZ() - dyVec.getZ());
            }        
        }; 
    }    
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final static MouseButton DEFAULT_TRIGGER_BUTTON = MouseButton.SECONDARY;
    private final static Point3D STARTING_X_VEC = new Point3D(1, 0, 0);
    private final static Point3D STARTING_Y_VEC = new Point3D(0, 1, 0);

    private final DoubleProperty originX = new SimpleDoubleProperty(this, "originX", 0);
    private final DoubleProperty originY = new SimpleDoubleProperty(this, "originY", 0);
    private final DoubleProperty originZ = new SimpleDoubleProperty(this, "originZ", 0);
    private final ObjectProperty<Transform> viewRotation = 
            new SimpleObjectProperty<Transform>(this, "viewRotation", new Affine());
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 10);
    private final ObjectProperty<PerspectiveCamera> camera = 
            new SimpleObjectProperty<PerspectiveCamera>(this, "camera", null);
    
    private boolean coeffDirty = true;
    private double coeff;
    
    private void updateCoeff() {
        if (coeffDirty) {
            PerspectiveCamera pCam = camera.get();
            if (pCam != null) {
                final double hfovRad = Math.toRadians(Util.getHorizontalFieldOfView(pCam, getWidth(), getHeight()));
                coeff = 2.0 * distanceFromOrigin.get() * Math.tan(hfovRad / 2.0) / getWidth();
            }
            coeffDirty = false;
        }
    }
            
}
