package org.arcball.example;

import java.io.File;

import org.arcball.CameraRig;
import org.arcball.Pane3D;
import org.arcball.TurntableCameraRig;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public final class ArcballSampleApp extends Application {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox baseVBox = new VBox();
        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuView = new Menu("View");
        MenuItem viewAll = new MenuItem("View all");
        viewAll.setOnAction(viewAllHandler);
        CheckMenuItem viewProperties = new CheckMenuItem("View properties");
        viewPropertiesPane.visibleProperty().bind(viewProperties.selectedProperty());
        menuView.getItems().addAll(viewAll, viewProperties);
        menuBar.getMenus().addAll(menuFile, menuView);
        baseVBox.getChildren().add(menuBar);
        
        Scene scene = new Scene(baseVBox, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);

        VBox.setVgrow(pane3D, Priority.ALWAYS);
        baseVBox.getChildren().add(pane3D);
                
        Group world = new Group();
        pane3D.getRoot().getChildren().add(world);
        
        Group geometryGroup = buildGeometry();
        world.getChildren().add(geometryGroup);
        pane3D.getCameraRig().encompassBounds(geometryGroup.getBoundsInParent(), 0);
        
        TurntableCameraRig tcr = (TurntableCameraRig)pane3D.getCameraRig();
        tcr.setZRotation(-36);
        tcr.setXRotation(75);
        updateViewPropertiesPane();

        pane3D.getChildren().add(viewPropertiesPane);
        pane3D.widthProperty().addListener(pane3DsizeListener);
        pane3D.heightProperty().addListener(pane3DsizeListener);
        
        primaryStage.setTitle("Arcball Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Apparently this main() method should be ignored in correctly-deployed JavaFX applications.  It serves as a
     * fall-back in the case of applications that can't be launched through deployment artifacts.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    //--------------------------------------------------------------------------------------------- PRIVATE / PROTECTED
    
    private EventHandler<ActionEvent> viewAllHandler = new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
            pane3D.viewAll(500);
        }
    };
        
    private ChangeListener<Number> pane3DsizeListener = new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ob, Number oldValue, Number newValue) {
            Region.layoutInArea(viewPropertiesPane, 10, pane3D.getHeight()-500-10, 
                    500, 500, 
                    0, null, false, false, HPos.LEFT, VPos.BOTTOM, true);
        }
    };
    
    private static Group buildGeometry() {
        Group geometryGroup = new Group();
        
        Group atomGroup = new Group();
        Group bondGroup = new Group();
        PDBSource pdbSource = new PDBSource(new File("1BNA.pdb"));
        atomGroup.getChildren().addAll(pdbSource.getAtoms(0.4));
        bondGroup.getChildren().addAll(pdbSource.getBonds(0.2));
        geometryGroup.getChildren().add(atomGroup);
        geometryGroup.getChildren().add(bondGroup);
        
        return geometryGroup;
    }
    
    private void updateViewPropertiesPane() {
        viewPropertiesPane.clearProperties();
        CameraRig cameraRig = pane3D.getCameraRig();
        if (cameraRig instanceof TurntableCameraRig) {
            TurntableCameraRig tcr = (TurntableCameraRig)cameraRig;
            viewPropertiesPane.addDoubleProperty("originX", tcr.originXProperty());
            viewPropertiesPane.addDoubleProperty("originY", tcr.originYProperty());
            viewPropertiesPane.addDoubleProperty("originZ", tcr.originZProperty());
            viewPropertiesPane.addDoubleProperty("zRotation", tcr.zRotationProperty());
            viewPropertiesPane.addDoubleProperty("xRotation", tcr.xRotationProperty());
            viewPropertiesPane.addDoubleProperty("distanceFromOrigin", tcr.distanceFromOriginProperty());
        }
    }

    private final Pane3D pane3D = new Pane3D();
    private final ViewPropertiesPane viewPropertiesPane = new ViewPropertiesPane();
    
    private static final int DEFAULT_WIDTH  = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    
}
