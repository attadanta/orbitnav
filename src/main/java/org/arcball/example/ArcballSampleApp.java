package org.arcball.example;

import org.arcball.Pane3D;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

public final class ArcballSampleApp extends Application {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox baseVBox = new VBox();
        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        menuBar.getMenus().add(menuFile);
        baseVBox.getChildren().add(menuBar);
        
        Scene scene = new Scene(baseVBox, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);

        Pane3D pane3D = new Pane3D();
        VBox.setVgrow(pane3D, Priority.ALWAYS);
        baseVBox.getChildren().add(pane3D);
                
        Group world = new Group();
        pane3D.getRoot().getChildren().add(world);
        
        Group geometryGroup = buildGeometry();
        world.getChildren().add(geometryGroup);
        
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
    
    private static Group buildGeometry() {
        Group geometryGroup = new Group();
        
        PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.BLACK);
        
        Box cube = new Box(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
        cube.setMaterial(greenMaterial);
        geometryGroup.getChildren().add(cube);
        
        return geometryGroup;
    }

    private static final int DEFAULT_WIDTH  = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final double CUBE_SIZE = 1;
    
}
