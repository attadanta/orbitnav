package org.arcball.example;

import org.arcball.ArcballCameraRig;
import org.arcball.TurntableCameraRig;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public final class ArcballSampleApp extends Application {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
        scene.setFill(Color.GREY);
        
        Group world = new Group();
        root.getChildren().add(world);
        
        Group geometryGroup = buildGeometry();
        world.getChildren().add(geometryGroup);
        
        //ArcballCameraRig cameraRig = new ArcballCameraRig();
        //cameraRig.setCameraForScene(scene);
        TurntableCameraRig cameraRig = new TurntableCameraRig();
        cameraRig.setCameraForScene(scene);
        
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
        greenMaterial.setSpecularColor(Color.GREEN);
        
        Box cube = new Box(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
        cube.setMaterial(greenMaterial);
        geometryGroup.getChildren().add(cube);
        
        return geometryGroup;
    }
    
    private final Group root = new Group();

    private static final int DEFAULT_WIDTH  = 1024;
    private static final int DEFAULT_HEIGHT = 768;
    private static final double CUBE_SIZE = 1;
    
}
