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

package org.arcball;

import java.util.HashMap;
import java.util.Map;

import org.arcball.internal.Attachable;
import org.arcball.internal.InteractionBase;
import org.arcball.internal.InteractionDragArcball;
import org.arcball.internal.InteractionDragPan;
import org.arcball.internal.InteractionDragXZTurntable;
import org.arcball.internal.InteractionDragZoom;
import org.arcball.internal.InteractionHost;
import org.arcball.internal.InteractionScrollZoom;
import org.arcball.internal.NoGarbageProperty;
import org.arcball.internal.PerspectiveSceneToRaster;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public final class OrbitalCameraRig implements Attachable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public OrbitalCameraRig() {
        attachParameterListeners();
        attachArcballEnabledListener();
        // TODO:
    }

    public void attachToHost(InteractionHost host) {
        assert(this.host == null);
        this.host = host;
        for (InteractionBase ib : interactionMap.values()) {
            attachInteractionToHost(ib);
        }
    }
    
    public void detachFromHost(InteractionHost host) {
        assert(this.host == host);
        for (InteractionBase ib : interactionMap.values()) {
            detachInteractionFromHost(ib);
        }
        this.host = null;
    }
        
    public void addNavigationBehavior(NavigationBehavior nb) { 
        removeNavigationBehavior(nb);    // removes any previous match for these input conditions
        InteractionBase ib = createInteraction(nb);
        attachInteractionToHost(ib);
        interactionMap.put(nb, ib);
        navigationBehaviorsList.add(nb);
    }
    
    public void removeNavigationBehavior(NavigationBehavior nb) { 
        for (NavigationBehavior n : navigationBehaviorsList) {
            if (nb.inputConditionsMatch(n)) {
                detachInteractionFromHost(interactionMap.get(n));
                interactionMap.remove(n);
                navigationBehaviorsList.remove(n);
                break;
            }
        }
    }
    
    public void clearNavigationBehaviors() {
        for (InteractionBase ib : interactionMap.values()) {
            detachInteractionFromHost(ib);
        }
        interactionMap.clear();
        navigationBehaviorsList.clear();
    }
    
    public ReadOnlyListProperty<NavigationBehavior> navigationBehaviorsListProperty() { 
        return navigationBehaviorsList; 
    }
    
    public BooleanProperty arcballEnabledProperty() { return arcballEnabled; }
    
    public boolean isArcballEnabled() { return arcballEnabled.get(); }
    
    public void setArcballEnabled(boolean value) { arcballEnabled.set(value); }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final BooleanProperty arcballEnabled =
            new SimpleBooleanProperty(this, "arcballEnabled", false);
    private final ListProperty<NavigationBehavior> navigationBehaviorsList = 
            new SimpleListProperty<NavigationBehavior>(this, "navigationBehaviorsList");
    
    private final DoubleProperty originX = new SimpleDoubleProperty(this, "originX", 0);
    private final DoubleProperty originY = new SimpleDoubleProperty(this, "originY", 0);
    private final DoubleProperty originZ = new SimpleDoubleProperty(this, "originZ", 0);
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 10);
    private final DoubleProperty rotationAngle = new SimpleDoubleProperty(this, "rotationAngle", 0);
    private final DoubleProperty rotationAxisX = new SimpleDoubleProperty(this, "rotationAxisX", 0);
    private final DoubleProperty rotationAxisY = new SimpleDoubleProperty(this, "rotationAxisY", 0);
    private final DoubleProperty rotationAxisZ = new SimpleDoubleProperty(this, "rotationAxisZ", 0);
    private final DoubleProperty xArcballRotation = new SimpleDoubleProperty(this, "xArcballRotation", 0);
    private final DoubleProperty zArcballRotation = new SimpleDoubleProperty(this, "zArcballRotation", 0);
    
    private final ReadOnlyObjectProperty<PerspectiveCamera> camera =
            new SimpleObjectProperty<PerspectiveCamera>(this, "camera", new PerspectiveCamera(true));
    private final NoGarbageProperty<Transform> transformRotationOnly =
            new NoGarbageProperty<Transform>(this, "transformRotationOnly", new Affine());
    private final NoGarbageProperty<Transform> transformCamera =
            new NoGarbageProperty<Transform>(this, "transformCamera", new Affine());
    private final NoGarbageProperty<CameraToRasterTransform> transformToRaster =
            new NoGarbageProperty<CameraToRasterTransform>(this, "transformToRaster",
                    new PerspectiveSceneToRaster());
        
    private final Map<NavigationBehavior, InteractionBase> interactionMap = 
            new HashMap<NavigationBehavior, InteractionBase>(); 
    
    private InteractionHost host = null;
    
    private void updateTransformRotationOnly() {
        final Affine xform = (Affine)transformRotationOnly.get();
        xform.setToIdentity();
        if (!arcballEnabled.get()) {
            // TODO: set rotation angle and axis from turntable rotations
        } else {
            // TODO: find closest matching turntable rotations from angle and axis
        }
        xform.appendRotation(rotationAngle.get(), 0, 0, 0,
                             rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get());
        transformRotationOnly.fireChangedEvent();
    }
    
    private void updateTransformCamera() {
        final Affine xform = (Affine)transformCamera.get();
        xform.setToIdentity();
        xform.appendTranslation(originX.get(), originY.get(), originZ.get());
        xform.append(transformRotationOnly.get());
        xform.appendTranslation(0, 0, -distanceFromOrigin.get());
        transformCamera.fireChangedEvent();
    }
    
    private void updateTransformToRaster() {
        final PerspectiveSceneToRaster s2r = (PerspectiveSceneToRaster)transformToRaster.get();
        if (host != null) {
            s2r.setParameters(camera.get(), transformCamera.get(), host);
        }
        transformToRaster.fireChangedEvent();
    }
    
    private void updateTransforms() {
        updateTransformRotationOnly();
        updateTransformCamera();
        updateTransformToRaster();
        camera.get().getTransforms().setAll(transformCamera.get());
    }
    
    private InteractionBase createInteraction(NavigationBehavior nb) {
        InteractionBase ib = null;
        switch (nb.getInput()) {
        case SCROLL:
            switch (nb.getResponse()) {
            case PAN:
                break;  // TODO
            case ZOOM:
                ib = new InteractionScrollZoom(distanceFromOrigin);
                break;
            case ROTATE:
                break; // TODO
            }
            break;
        case DRAG:
            switch (nb.getResponse()) {
            case PAN:
                ib = new InteractionDragPan(originX, originY, originZ, transformRotationOnly, distanceFromOrigin, 
                        camera);
                break;
            case ZOOM:
                ib = new InteractionDragZoom(distanceFromOrigin);
                break;
            case ROTATE:
                if (isArcballEnabled()) {
                    ib = new InteractionDragArcball(rotationAngle, rotationAxisX, rotationAxisY, rotationAxisZ);
                } else {
                    ib = new InteractionDragXZTurntable(xArcballRotation, zArcballRotation);
                }
                break;
            }
        }
        if (ib != null) {
            ib.setNavigationBehavior(nb);
        }
        return ib;
    }
    
    private void attachInteractionToHost(InteractionBase ib) {
        if (host != null) {
            ib.attachToHost(host);
        }
    }
    
    private void detachInteractionFromHost(InteractionBase ib) {
        if (host != null) {
            ib.detachFromHost(host);
        }
    }
    
    private void attachParameterListeners() {
        final ChangeListener<Number> parameterNumberListener = new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
                updateTransforms();
            }
        };
        originX.addListener(parameterNumberListener);
        originY.addListener(parameterNumberListener);
        originZ.addListener(parameterNumberListener);
        distanceFromOrigin.addListener(parameterNumberListener);
        xArcballRotation.addListener(parameterNumberListener);
        zArcballRotation.addListener(parameterNumberListener);
        rotationAngle.addListener(parameterNumberListener);
        rotationAxisX.addListener(parameterNumberListener);
        rotationAxisY.addListener(parameterNumberListener);
        rotationAxisZ.addListener(parameterNumberListener);
    }
    
    private void attachArcballEnabledListener() {
        arcballEnabled.addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ob, Boolean olde, Boolean newe) {
                // when the arcball status changes, we have to traverse the map of interactions and interchange
                //  arcball and xz-turntable style drags
                for (NavigationBehavior nb : interactionMap.keySet()) {
                    if ((nb.getResponse() == NavigationBehavior.Response.ROTATE) &&
                        (nb.getInput() == NavigationBehavior.Input.DRAG)) 
                    {
                        // detach existing rotation from the host
                        detachInteractionFromHost(interactionMap.get(nb));
                        // create new rotation
                        InteractionBase ib;
                        if (newe == true) {
                            ib = new InteractionDragArcball(rotationAngle, 
                                    rotationAxisX, rotationAxisY, rotationAxisZ);
                        } else {
                            ib = new InteractionDragXZTurntable(xArcballRotation, zArcballRotation);
                        }
                        // replace rotation in the map
                        interactionMap.put(nb, ib);
                        // attach new rotation to the host
                        attachInteractionToHost(ib);
                    }
                }
            }
        });
    }
    
}
