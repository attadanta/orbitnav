package org.arcball.example;

import java.io.File;

import org.arcball.Pane3D;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
        menuView.getItems().add(viewAll);
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
    
    private static Group buildGeometry() {
        Group geometryGroup = new Group();
        
        Group atomGroup = new Group();
        PDBSource pdbSource = new PDBSource(new File("1BNA.pdb"));
        atomGroup.getChildren().addAll(pdbSource.getAtoms(0.5));
        geometryGroup.getChildren().add(atomGroup);
        
        return geometryGroup;
    }

    private final Pane3D pane3D = new Pane3D();
    
    private static final int DEFAULT_WIDTH  = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final double CUBE_SIZE = 1;
    
}
