/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Question;
import viewmodel.ViewModelMatch;
import viewmodel.ViewModelMatchGame;

/**
 *
 * @author @author Jamal&MehdiEPFC
 */
public class ViewMatchGame extends VBox{
    
    /*  VBox Title    : TOPZONE*/
    private final VBox topZone = new VBox();
    private final Label title = new Label("Réponses aux questionnaire"); 
    
    /*  VBox Question   : QUESTZONE*/
    private final VBox questZone = new VBox();

    // Question
    // current Question
    private final Text question = new Text();
    private final Label numQuest = new Label();
    
    //hint Fonctionne mais a ajuster avec css
    /* Hint */
    private final VBox hintZone = new VBox();
    private final Text hint = new Text();
    private final Button btHint = new Button("Hint");
    private final SimpleBooleanProperty hintOrNo = new SimpleBooleanProperty();
    private Boolean showHint = false;
    /* Points */
    private final Label points = new Label();
    private final Label pointsGagne = new Label();
    /*  VBox Reponse    REPONSEZONE*/
    private final VBox reponseZone = new VBox();
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private List<RadioButton> reponses = new ArrayList();
    private SimpleIntegerProperty badReponse = new SimpleIntegerProperty(-1);
    /*  VBox Validation  VALIDATIONZONE*/
    private final VBox validationZone = new VBox();
    private final Button btValider = new Button("Valider");
    private final Button btAnnuler = new Button("Annuler");
    /* VM   */
    private ViewModelMatchGame vm;
    private final Image icon = new Image("/images/LOGO.png");
    
    public ViewMatchGame(Stage secondaryStage,ViewModelMatchGame vm){
        this.vm = vm;
        configComponents();
        configListeners();
        configBindings();
        loadButton();
        Scene scene = new Scene(this, 300,500);
        scene.getStylesheets().add("view/Styles.css");
        secondaryStage.setTitle("Question game- MJ");
        secondaryStage.setScene(scene);
        secondaryStage.getIcons().add(icon);
    }
    public void configBindings(){
        question.textProperty().bind(vm.questionProperty());
        toggleBind();
        points.textProperty().bind(vm.pointstrProperty());
        pointsGagne.textProperty().bind(vm.cptPointstrProperty());
        numQuest.textProperty().bind(vm.numCurrentQuestLbProperty());
        hint.textProperty().bind(vm.hintProperty());
        hintOrNo.bind(vm.hintOrNoProperty());
        badReponse.bind(vm.badRepProperty());
    }
    public void configListeners(){
        this.btValider.setOnAction((ActionEvent event)->{
            vm.bindRepSelectedPropTo(new SimpleIntegerProperty((int)toggleGroup.getSelectedToggle().getUserData()));
            toggleGroup.selectToggle(null);
            
            if(!vm.next()){
                Stage stage = (Stage) this.getScene().getWindow();
                stage.close();
            }
            selectRightRb();
         });
        this.btHint.setOnAction((ActionEvent event)->{
            showHint = true;
            loadButton();
            vm.usedHint();
         });
        this.hintOrNo.addListener((o,oldValue,newValue) -> {
                loadButton();
        });
        
        this.btAnnuler.setOnAction((ActionEvent event)->{
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
         });
    }
    public void configComponents(){
        configTopZone();
        configQuestZone();
        configReponseZone();
        configValidationZone();
        configWindow();
    }
    public void configTopZone(){
        topZone.setAlignment(Pos.CENTER);
        topZone.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        topZone.getChildren().setAll(title, numQuest);
    }
    public void configQuestZone(){
        questZone.setAlignment(Pos.CENTER);
        question.setWrappingWidth(250);
        questZone.getChildren().setAll(question,points, hintZone, reponseZone, pointsGagne);
    } 
    public void loadButton(){
        if(hintOrNo.get() && !showHint){
            hintZone.setAlignment(Pos.CENTER);
            this.hintZone.getChildren().setAll(btHint);
        }
        if(showHint){
            hintZone.getChildren().setAll(hint);
            showHint = false;
        }
        if(!hintOrNo.get() && !showHint){
            hintZone.setAlignment(Pos.CENTER);
            this.hintZone.getChildren().clear();
        }
    }
    private void configHintZone(){
        hintZone.setAlignment(Pos.CENTER);
        hintZone.getStyleClass().add("hint");
    }
    public void configReponseZone(){
        for (int i = 0; i < 4; i++) {
            RadioButton r = new RadioButton();
            r.setUserData(i+1);
            r.setToggleGroup(toggleGroup);
            reponses.add(r);
        }
        reponseZone.getChildren().setAll((reponses));
    }
    public void configValidationZone(){
        validationZone.setAlignment(Pos.CENTER);
        validationZone.getChildren().setAll(btValider, btAnnuler);
    }
    public void configWindow(){
        this.getChildren().setAll(topZone,questZone,reponseZone,validationZone);
    }

    private void toggleBind() {
        for (int i = 0; i < 4; i++) {
            RadioButton r = reponses.get(i);
            r.textProperty().bind(vm.reponseTextProperty(i));
            if(i == badReponse.get()){
                r.setSelected(true);
            }
        }
    }
    private void selectRightRb() {
        for (int i = 0; i < 4; i++) {
            RadioButton r = reponses.get(i);
            if(i == badReponse.get()){
                r.setDisable(true);
                r.setStyle("color:red;");
            }
            else
                r.setDisable(false);
        }
    }
}
