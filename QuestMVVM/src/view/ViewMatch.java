/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Match;
import viewmodel.ViewModelMatch;

/**
 *
 * @author Jamal&MehdiEPFC
 */
public class ViewMatch extends VBox {
    
    //Title
    private final VBox top = new VBox();
    private final Label title = new Label("Construction questionnaire");   
    private final HBox parent = new HBox();
    //Vbox Question disponibles
    private final VBox QuestDispoZone = new VBox();
    private final ComboBox<String> Category = new ComboBox<String>();
    private final Label lbMatch = new Label();
    private final Label lbQuestDispo = new Label();
    private final ListView<String> QuestDispo= new ListView<>();
    private final Label PointsDispo = new Label(/*"Points disponibles :"*/);
    //Vbox Construction Question
    private final VBox QuestZone = new VBox();
    private final Text question = new Text();
    private final Label points = new Label();
    private final VBox ReponseZone = new VBox();
    //Reponse list
    private final List<RadioButton > reponses = new ArrayList();
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final Button btAddQuest = new Button("->");
    private final Button btRemQuest = new Button("<-");
    private final Button btValider = new Button("Valider");
    private final Button btAnnuler = new Button("Annuler");
    //Vbox Question Choisies
    private final VBox QuestChoisiZone = new VBox();
    private final Label lbQuestChoisies = new Label();
    private final ListView<String> QuestChoisi = new ListView<>();
    private final Label pointChoisi= new Label();
    
    private final HBox validation = new HBox();
    
    //------------------------------------------------------------------// 
    private ViewModelMatch vm;
    private final Image icon = new Image("/images/LOGO.png");   
    public ViewMatch(Stage secondaryStage,ViewModelMatch vm){
        this.vm = vm;
        configComponents();
        configBindings();
        configListeners();
        Scene scene = new Scene(this, 800,700);
        scene.getStylesheets().add("view/Styles.css");
        secondaryStage.setTitle("Question game- MJ");
        secondaryStage.setScene(scene);
        secondaryStage.getIcons().add(icon);
    }
    public void configComponents(){
        configTop();
        configQuestDispo();
        configQuestZone();
        configReponseZone();
        configQuestChoisis();
        configWindow();
    }
    public void configTop(){
        top.setAlignment(Pos.CENTER);
        top.setMaxWidth(Double.MAX_VALUE);
        this.top.getChildren().setAll(title);
    }
    public void configQuestDispo(){
//        PointsDispo.setText(this.PointsDispo.getText()+ this.pointsRestant.get()+ "points");
        this.QuestDispoZone.getChildren().setAll(lbMatch,Category,lbQuestDispo,QuestDispo,PointsDispo);
    }
    public void configQuestZone(){
        question.setWrappingWidth(250);
        QuestZone.setAlignment(Pos.CENTER);
        QuestZone.getStyleClass().add("zoneR");
        QuestZone.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        QuestZone.setPadding(new Insets(50,10,50,10));
        btAddQuest.setAlignment(Pos.CENTER);
        btRemQuest.setAlignment(Pos.CENTER);
        QuestZone.getChildren().setAll(question,points,ReponseZone,btAddQuest,btRemQuest);
    }
    
    public void configReponseZone(){
        for (int i = 0; i < 4; i++) {
            RadioButton r = new RadioButton();
            r.setUserData(i+1);
            r.setDisable(true);
            r.setToggleGroup(toggleGroup);
            reponses.add(r);
        }
        ReponseZone.getChildren().addAll(reponses);
        ReponseZone.getStyleClass().add("zoneR");
    }
    public void configQuestChoisis(){
        this.QuestChoisiZone.getChildren().setAll(lbQuestChoisies,QuestChoisi,pointChoisi);
    }
    public void configWindow(){
        this.setAlignment(Pos.CENTER);
        this.validation.getChildren().setAll(this.btValider,this.btAnnuler);
        this.parent.getChildren().setAll(QuestDispoZone,QuestZone,QuestChoisiZone);
        this.title.getStyleClass().add("title");
        validation.setAlignment(Pos.CENTER);
        this.getChildren().setAll(top,parent,validation);
    }
    public void configBindings(){
        vm.bindQuestChoisiSelectedPropTo(this.getListViewQuestChoi().selectedIndexProperty());
        vm.bindQuestDispoSelectedPropTo(this.getListViewQuestDispo().selectedIndexProperty());
        pointChoisi.textProperty().bind(vm.ptChoisiLabelProperty());
        PointsDispo.textProperty().bind(vm.ptDispoLabelProperty());
        question.textProperty().bind(vm.questionSelectedProperty());
        lbQuestChoisies.textProperty().bind(vm.nbQuestProperty());
        lbQuestDispo.textProperty().bind(vm.nbQuestDispoProperty());
        points.textProperty().bind(vm.pointsProperty());
        this.lbMatch.textProperty().bind(vm.titleProperty());
        configBindCategory();
        toggleSelected();
        bouttonPropertyBinding();
    }
    public void configBindCategory(){
        this.Category.itemsProperty().bind(vm.catStructStrProperty());
        vm.bindCategorieSelectedPropTo(getComboboxCategory().selectedIndexProperty());
        getComboboxCategory().selectFirst();
        this.QuestDispo.itemsProperty().bind(vm.getQuestionsDispo());
        vm.bindQuestionChosenIndexPropTo(getListViewQuestDispo().selectedIndexProperty());
        QuestChoisi.itemsProperty().bind(vm.questChosenProperty());
        vm.bindQuestionAvailableIndexPropTo(getListViewQuestChoi().selectedIndexProperty());
    }
    public void bouttonPropertyBinding(){
        this.btAddQuest.disableProperty().bind(vm.enableLeftProperty());   
        this.btRemQuest.disableProperty().bind(vm.enableRightProperty());
    }
    private SelectionModel<String> getComboboxCategory() {
        return Category.getSelectionModel();
   }
    private SelectionModel<String> getListViewQuestDispo() {
        return QuestDispo.getSelectionModel();
   }
     private SelectionModel<String> getListViewQuestChoi() {
        return QuestChoisi.getSelectionModel();
    }
     private void configListeners(){
         
         btAddQuest.setOnAction((ActionEvent event)->{
             vm.addToQuestionChoisi();
             getListViewQuestDispo().clearSelection();
         });
         btRemQuest.setOnAction((ActionEvent event)->{
             vm.addToQuestionDispo();
             this.getListViewQuestChoi().clearSelection();
         });
         btValider.setOnAction((ActionEvent event)->{
            vm.launchGame();
            exitWindow();
         });
         btAnnuler.setOnAction((ActionEvent event)->{
             exitWindow();
         });
     }
     private void toggleSelected(){
         for (int i = 0; i < 4; i++) {
             RadioButton r = reponses.get(i);
             r.selectedProperty().bind(vm.reponseProperty(i));
             r.textProperty().bind(vm.reponseTextProperty(i));
         }
     }
     private void exitWindow(){
        Stage stage = (Stage) this.getScene().getWindow();
        stage.close();
     }
}
