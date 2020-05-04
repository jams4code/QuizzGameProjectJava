/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Mehdi&Jams4EPFC
 */
public class AlertBox {
    public static void display(String title, String message){
        display(title,message,"Ok, je ne recommencerai pas.");
    }
    public static void display(String title, String message,String button){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        window.setMinHeight(300);
        
        Label label = new Label();
        label.setText(message);
        
        Button closeButton = new Button(button);
        closeButton.setOnAction(e -> window.close());
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("view/Styles.css");
        window.setScene(scene);
        window.showAndWait();
    }
    
    public static void endGame(String pl1, String pl2, String res){
        display("Fin du match", "Résultat du match : " + res, "Fin");
    }
    
}
