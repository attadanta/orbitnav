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

package org.orbitnav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.orbitnav.internal.*;
import org.orbitnav.internal.Host;
import org.orbitnav.internal.geom.MutableAxisAngle3D;
import org.orbitnav.internal.geom.MutableTurntable3D;
import org.orbitnav.internal.geom.MutableVec3D;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

public final class OrbitalCameraRig implements Attachable {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public OrbitalCameraRig() {
        attachParameterListeners();
        attachArcballEnabledListener();
        updateTransforms();
        // TODO:
    }

    public void attachToHost(Host host) {
        assert(this.host == null);
        this.host = host;
        this.host.setCamera(camera.get());
        for (Interaction ic : interactionMap.values()) {
            attachInteractionToHost(ic);
        }
    }
    
    public void detachFromHost(Host host) {
        assert(this.host == host);
        this.host.setCamera(null);
        for (Interaction ic : interactionMap.values()) {
            detachInteractionFromHost(ic);
        }
        this.host = null;
    }
        
    public void addNavigationBehavior(NavigationBehavior nb) { 
        removeNavigationBehavior(nb);    // removes any previous match for these input conditions
        Interaction ic = createInteraction(nb);
        attachInteractionToHost(ic);
        interactionMap.put(nb, ic);
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
        for (Interaction ic : interactionMap.values()) {
            detachInteractionFromHost(ic);
        }
        interactionMap.clear();
        navigationBehaviorsList.clear();
    }
    
    public void encompassBounds(Bounds bounds, double animationDurationMillis) {
        // find the center of the bounds
        double cx = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        double cy = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        double cz = (bounds.getMinZ() + bounds.getMaxZ()) / 2.0;
        // find the "radius", as the maximum of the depth, height and width divided by 2
        double r = Math.max(bounds.getDepth(), Math.max(bounds.getHeight(), bounds.getWidth())) / 2.0;
        // configure camera
        if (camera.get() instanceof PerspectiveCamera) {
          PerspectiveCamera pCamera = (PerspectiveCamera)camera.get();
          double fov = pCamera.getFieldOfView() * Math.PI / 180.0;
          double d = r / Math.tan(fov / 2.0);
          if (animationDurationMillis <= 0.0) {
              distanceFromOrigin.set(1.1 * d);
              setOrigin(cx, cy, cz);
          } else {
              Duration tf = Duration.millis(animationDurationMillis);
              Timeline timeline = new Timeline();
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(distanceFromOrigin, 1.1 * d)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originX, cx)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originY, cy)));
              timeline.getKeyFrames().add(new KeyFrame(tf, new KeyValue(originZ, cz)));
              timeline.play();
          }
          pCamera.setNearClip(0.05 * d);
          pCamera.setFarClip(10.0 * d);
        }
    }    
    
    public void setOrigin(double x, double y, double z) {
        originX.set(x);
        originY.set(y);
        originZ.set(z);
    }
    
    public ReadOnlyListProperty<NavigationBehavior> navigationBehaviorsListProperty() { 
        return navigationBehaviorsList; 
    }
    
    public BooleanProperty arcballEnabledProperty() { return arcballEnabled; }
    
    public boolean isArcballEnabled() { return arcballEnabled.get(); }
    
    public void setArcballEnabled(boolean value) { arcballEnabled.set(value); }
    
    public ReadOnlyObjectProperty<Transform> transformRotationOnlyProperty() { return transformRotationOnly; }
    
    public ReadOnlyObjectProperty<CameraToRasterTransform> transformToRasterProperty() { return transformToRaster; }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private final BooleanProperty arcballEnabled =
            new SimpleBooleanProperty(this, "arcballEnabled", false);
    private final ListProperty<NavigationBehavior> navigationBehaviorsList = 
            new SimpleListProperty<NavigationBehavior>(this, "navigationBehaviorsList",
                    javafx.collections.FXCollections.observableList(new ArrayList<NavigationBehavior>()));
    
    private final DoubleProperty originX = new SimpleDoubleProperty(this, "originX", 0);
    private final DoubleProperty originY = new SimpleDoubleProperty(this, "originY", 0);
    private final DoubleProperty originZ = new SimpleDoubleProperty(this, "originZ", 0);
    private final DoubleProperty distanceFromOrigin = new SimpleDoubleProperty(this, "distanceFromOrigin", 10);
    private final DoubleProperty rotationAngle = new SimpleDoubleProperty(this, "rotationAngle", 0);
    private final DoubleProperty rotationAxisX = new SimpleDoubleProperty(this, "rotationAxisX", 0);
    private final DoubleProperty rotationAxisY = new SimpleDoubleProperty(this, "rotationAxisY", 0);
    private final DoubleProperty rotationAxisZ = new SimpleDoubleProperty(this, "rotationAxisZ", 0);
    private final DoubleProperty xTurntableRotation = new SimpleDoubleProperty(this, "xTurntableRotation", 0);
    private final DoubleProperty zTurntableRotation = new SimpleDoubleProperty(this, "zTurntableRotation", 0);
    
    private final ReadOnlyObjectProperty<PerspectiveCamera> camera =
            new SimpleObjectProperty<PerspectiveCamera>(this, "camera", new PerspectiveCamera(true));
    private final NoGarbageProperty<Transform> transformRotationOnly =
            new NoGarbageProperty<Transform>(this, "transformRotationOnly", new Affine());
    private final NoGarbageProperty<Transform> transformCamera =
            new NoGarbageProperty<Transform>(this, "transformCamera", new Affine());
    private final NoGarbageProperty<CameraToRasterTransform> transformToRaster =
            new NoGarbageProperty<CameraToRasterTransform>(this, "transformToRaster",
                    new PerspectiveSceneToRaster());
        
    private final Map<NavigationBehavior, Interaction> interactionMap = new HashMap<>();
    
    private Host host = null;
    
    private final MutableTurntable3D turntableRotation = new MutableTurntable3D();
    private final MutableAxisAngle3D axisAngleRotation = new MutableAxisAngle3D();
    
    private void updateTransformRotationOnly() {
        if (!arcballEnabled.get()) {
            // set rotation angle and axis from turntable rotations
            turntableRotation.setDegrees(xTurntableRotation.get(), zTurntableRotation.get());
            axisAngleRotation.set(turntableRotation);
            final MutableVec3D axis = axisAngleRotation.getAxis();
            rotationAxisX.set(axis.getX());
            rotationAxisY.set(axis.getY());
            rotationAxisZ.set(axis.getZ());
            rotationAngle.set(axisAngleRotation.getAngleDegrees());
        } else {
            // find closest matching turntable rotations from angle and axis
            axisAngleRotation.setDegrees(rotationAxisX.get(), rotationAxisY.get(), rotationAxisZ.get(), 
                                         rotationAngle.get());
            turntableRotation.setToClosestAxisAngle(axisAngleRotation);
        }
        axisAngleRotation.getAffine((Affine)transformRotationOnly.get());
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
    
    private Interaction createInteraction(NavigationBehavior nb) {
        Interaction ic = null;

        if (nb.isMouseDrag()) {
            switch (nb.getActivity()) {
                case PAN:
                    ic = new InteractionDragPan(
                            originX, originY, originZ, transformRotationOnly, distanceFromOrigin, camera
                    );
                    break;
                case ZOOM:
                    ic = new InteractionDragZoom(distanceFromOrigin);
                    break;
                case ROTATE:
                    if (isArcballEnabled()) {
                        ic = new InteractionDragArcball(rotationAngle, rotationAxisX, rotationAxisY, rotationAxisZ);
                    } else {
                        ic = new InteractionDragXZTurntable(xTurntableRotation, zTurntableRotation);
                    }
                    break;
            }
        } else if (nb.isGestureRotate()) {
            // TODO:
        } else if (nb.isGestureScroll()) {
            switch (nb.getActivity()) {
                case PAN:   // TODO
                    break;
                case ZOOM:
                    ic = new InteractionScrollZoom(distanceFromOrigin);
                    break;
                case ROTATE:    // TODO
                    break;
            }
        } else if (nb.isGestureZoom()) {
            // TODO:
        }

        if (ic != null) {
            ic.setNavigationBehavior(nb);  // associates modifiers, mouse buttons, etc.
        }
        return ic;
    }
    
    private void attachInteractionToHost(Interaction ic) {
        if (host != null) {
            ic.attachToHost(host);
        }
    }
    
    private void detachInteractionFromHost(Interaction ic) {
        if (host != null) {
            ic.detachFromHost(host);
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
        xTurntableRotation.addListener(parameterNumberListener);
        zTurntableRotation.addListener(parameterNumberListener);
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
                    if ((nb.getActivity() == NavigationBehavior.Activity.ROTATE) && nb.isMouseDrag()) {
                        // detach existing rotation from the host
                        detachInteractionFromHost(interactionMap.get(nb));
                        // create new rotation
                        Interaction ic;
                        if (newe == true) {
                            ic = new InteractionDragArcball(rotationAngle,
                                    rotationAxisX, rotationAxisY, rotationAxisZ);
                        } else {
                            ic = new InteractionDragXZTurntable(xTurntableRotation, zTurntableRotation);
                        }
                        // replace rotation in the map
                        interactionMap.put(nb, ic);
                        // attach new rotation to the host
                        attachInteractionToHost(ic);
                    }
                }
            }
        });
    }
    
}
