package org.arcball;

import org.arcball.internal.MousePositionTracker;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public final class TurntableCameraRig extends Group {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public TurntableCameraRig() {
        buildTree();
    }
    
    public void setCameraForScene(Scene scene) {
        this.scene = scene;
        scene.setCamera(camera);
        turntable.attachSceneEventHandlers(scene);
        zoom.attachSceneEventHandlers(scene);
        pan.attachSceneEventHandlers(scene);
    }
    
    public void removeCameraFromScene(Scene scene) {
        turntable.detachSceneEventHandlers(scene);
        zoom.detachSceneEventHandlers(scene);
        pan.detachSceneEventHandlers(scene);
        this.scene = null;
        scene.setCamera(null);
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private static MouseButton TURNTABLE_BUTTON = MouseButton.PRIMARY;
    private static MouseButton PAN_BUTTON       = MouseButton.SECONDARY;
    private static double      TURNTABLE_COEFF  = 0.4;
    private static double      ZOOM_COEFF       = 0.001;
    private static double      PAN_COEFF        = 0.0007;
    
    private       Scene             scene     = null;
    private final PerspectiveCamera camera    = new PerspectiveCamera(true);
    private final Translate         panTran   = new Translate(0, 0, 0);
    private final Rotate            rotateZ   = new Rotate(0, Rotate.Z_AXIS);
    private final Rotate            rotateX   = new Rotate(0, Rotate.X_AXIS);
    private final Translate         zOffset   = new Translate(0, 0, -10);
    private final Turntable         turntable = new Turntable(rotateX, rotateZ);
    private final Zoom              zoom      = new Zoom(zOffset);
    private final Pan               pan       = new Pan(panTran, rotateZ, rotateX, zOffset);
    
    private void buildTree() {
        this.getTransforms().addAll(panTran, rotateZ, rotateX, zOffset);
        this.getChildren().add(camera);
    }

    /** Handle turntable rotation. */
    private static final class Turntable {
        public Turntable(Rotate rotateX, Rotate rotateZ) {
            this.rotateX = rotateX;
            this.rotateZ = rotateZ;
        }
        public void attachSceneEventHandlers(Scene scene) {
            scene.addEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
            scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        }
        public void detachSceneEventHandlers(Scene scene) {
            scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
            scene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        }
        private final EventHandler<MouseEvent> pressHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                if (me.getButton() == TURNTABLE_BUTTON) {
                    mpt.mousePress(me.getSceneX(), me.getSceneY());
                }
            }
        };
        private final EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                if (me.getButton() == TURNTABLE_BUTTON) {
                    mpt.mouseDrag(me.getSceneX(), me.getSceneY());
                    // update camera rotation
                    rotateX.setAngle(rotateX.getAngle() - (mpt.getDeltaY() * TURNTABLE_COEFF));
                    rotateZ.setAngle(rotateZ.getAngle() + (mpt.getDeltaX() * TURNTABLE_COEFF));
                }
            }
        };
        private final Rotate rotateX;
        private final Rotate rotateZ;
        private final MousePositionTracker mpt = new MousePositionTracker();
    }
    
    /** Handle zooming. */
    private static final class Zoom {
        public Zoom(Translate zOffset) {
            this.zOffset = zOffset;
        }
        public void attachSceneEventHandlers(Scene scene) {
            scene.addEventHandler(ScrollEvent.SCROLL, scrollHandler);
        }
        public void detachSceneEventHandlers(Scene scene) {
            scene.removeEventHandler(ScrollEvent.SCROLL, scrollHandler);
        }
        private final EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent se) {
                zOffset.setZ((1.0 - (ZOOM_COEFF * se.getDeltaY())) * zOffset.getZ());
            }
        };
        private final Translate zOffset;
    }
    
    /** Handle pan. */
    private static final class Pan {
        public Pan(Translate panTran, Rotate rotateZ, Rotate rotateX, Translate zOffset) {
            this.panTran = panTran;
            this.rotateZ = rotateZ;
            this.rotateX = rotateX;
            this.zOffset = zOffset;
        }
        public void attachSceneEventHandlers(Scene scene) {
            scene.addEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
            scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        }
        public void detachSceneEventHandlers(Scene scene) {
            scene.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
            scene.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        }
        private final EventHandler<MouseEvent> pressHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                if (me.getButton() == PAN_BUTTON) {
                    mpt.mousePress(me.getSceneX(), me.getSceneY());
                 }
            }
        };
        private final EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                if (me.getButton() == PAN_BUTTON) {
                    // update position delta
                    mpt.mouseDrag(me.getSceneX(), me.getSceneY());
                    // z distance of camera
                    zDistance = zOffset.getZ();
                    // find local x and y vectors for the camera
                    Point3D camX = rotateZ.transform(rotateX.transform(xvec));
                    Point3D camY = rotateZ.transform(rotateX.transform(yvec));
                    // x shift
                    panTran.setX(panTran.getX() + (PAN_COEFF * mpt.getDeltaX() * zDistance * camX.getX()));
                    panTran.setY(panTran.getY() + (PAN_COEFF * mpt.getDeltaX() * zDistance * camX.getY()));
                    panTran.setZ(panTran.getZ() + (PAN_COEFF * mpt.getDeltaX() * zDistance * camX.getZ()));
                    // y shift
                    panTran.setX(panTran.getX() + (PAN_COEFF * mpt.getDeltaY() * zDistance * camY.getX()));
                    panTran.setY(panTran.getY() + (PAN_COEFF * mpt.getDeltaY() * zDistance * camY.getY()));
                    panTran.setZ(panTran.getZ() + (PAN_COEFF * mpt.getDeltaY() * zDistance * camY.getZ()));                    
                }
            }
        };
        private final Translate panTran;
        private final Rotate rotateZ;
        private final Rotate rotateX;
        private final Translate zOffset;
        private final Point3D xvec = new Point3D(1, 0, 0);
        private final Point3D yvec = new Point3D(0, 1, 0);
        private final MousePositionTracker mpt = new MousePositionTracker();
        private double zDistance;
    }
    
}
