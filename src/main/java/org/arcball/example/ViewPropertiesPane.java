package org.arcball.example;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

public final class ViewPropertiesPane extends Pane {

    //---------------------------------------------------------------------------------------------------------- PUBLIC
    
    public ViewPropertiesPane() {
        init();
    }
    
    public void addDoubleProperty(String name, ReadOnlyDoubleProperty p) {
        DoubleProperty localProperty = new SimpleDoubleProperty(this, p.getName(), 0);
        localProperty.bind(p);
        
        Text nameText = new Text(name);
        GridPane.setHalignment(nameText, HPos.RIGHT);
        Text propText = new Text();
        GridPane.setHalignment(propText, HPos.RIGHT);
        propText.textProperty().bindBidirectional(localProperty, new NumberStringConverter("##0.0"));
        
        gridPane.addRow(lastRow, nameText, propText);
        lastRow += 1;
    }
    
    public void clearProperties() {
        gridPane.getChildren().clear();
        lastRow = 0;
    }
    
    //--------------------------------------------------------------------------------------------------------- PRIVATE
    
    private void init() {
        setDisable(true);
        
        gridPane.setStyle("-fx-background-color: #888888AA; -fx-border-color: #555555;" +
                          "-fx-border-radius: 10; -fx-border-width: 2; -fx-background-radius: 10;" +
                          "-fx-padding: 6; -fx-hgap: 5;" +
                          "-fx-effect: dropshadow(gaussian, #00000055, 12, 0.5, 2, 2)");
        gridPane.getColumnConstraints().add(new ColumnConstraints());
        gridPane.getColumnConstraints().add(new ColumnConstraints(50));
        
        getChildren().add(gridPane);
    }
    
    private final GridPane gridPane = new GridPane();
    private int lastRow = 0;
    
}
