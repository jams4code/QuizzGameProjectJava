package view;

import viewmodel.ViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Federation;
import model.Match;
import model.Player;

/**
 *
 * @author Jamal&MehdiEPFC
 */
public class View extends VBox{
    //VBox Tournament zone - Au dessus
    private final VBox trnmZone = new VBox();
    private final ListView<String> listTrnm = new ListView<>();
    private final Label lbTrnm = new Label("Tournoi :");
    //HBox Zone en dessous de Tournament zone qui inclus PlRegZone + MathZone
    private final HBox bottomZone = new HBox();
    //VBox PlReg zone contenue dans HBox(BottomZone) A coté de mathZone
    private final VBox plRegZone = new VBox();
    private final ListView<String> listPlReg = new ListView<>();
    private final Label lbPlReg = new Label("Inscrits :");
    //VBox MatchZone contenue dans HBox(BottomZone) a coté de plRegZone
    private final VBox matchZone = new VBox();
    private final VBox matchListZone = new VBox();
    private final HBox matchEditZone = new HBox();
    private final List<BorderPane> edit = new ArrayList();
    private final TableView<Match> listMatch = new TableView<>();
    private final TableColumn <Match,String> plOne = new TableColumn<Match,String>("Player One");
    private final TableColumn <Match,String> plTwo = new TableColumn<Match,String>("Player Two");
    private final TableColumn <Match,String> resu = new TableColumn<Match,String>("Result");
    private final Label lbMatch = new Label("Matchs :");
    private final Label lbPr1 = new Label("Player 1:");
    private final Label lbPr2 = new Label("Player 2:");
    private final Label lbRes = new Label("Result :");
    private final Button btAddMatch = new Button("Add");
    private final Button btUpdMatch = new Button("Update");
    private final Button btDelMatch = new Button("Delete");
    private final ComboBox<String> pl1 = new ComboBox<String>();
    private final ComboBox<String> pl2 = new ComboBox<String>();
    private final ComboBox<String> res = new ComboBox<String>();
    private final BooleanProperty loadingButton = new SimpleBooleanProperty();
    private final StringProperty player1 = new SimpleStringProperty();
    private final StringProperty player2 = new SimpleStringProperty();
    private final StringProperty Results = new SimpleStringProperty();
    private final ViewModel viewModel;
    private final Image icon = new Image("/images/LOGO.png");
    
    public View(Stage primaryStage, ViewModel viewModel){
        this.viewModel = viewModel;
        configComponents();
        configBindings();
        configListeners();
        Scene scene = new Scene(this, 800,700);
        scene.getStylesheets().add("view/Styles.css");
        primaryStage.setTitle("Gestion de tournois - MJ");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        
    }
    private void configComponents(){
        configTrnmZone();
        configPlRegZone();
        configMatchZone();
        configBottomZone();
        configWindow();
    }
    private void configTrnmZone(){
        trnmZone.getChildren().addAll(lbTrnm,listTrnm);
    }
    private void configPlRegZone(){
        listPlReg.setFocusTraversable(false);
        listPlReg.setMouseTransparent(true);
        plRegZone.getChildren().addAll(lbPlReg,listPlReg);
    }
    private void configMatchZone(){
        configMatchListZone();
        configMatchEditZone();
        matchZone.setMinWidth(200);
        matchZone.getChildren().addAll(matchListZone,matchEditZone);
    }
     private void configMatchListZone(){
        plOne.setCellValueFactory(new PropertyValueFactory<>("P1Name"));
        plTwo.setCellValueFactory(new PropertyValueFactory<>("P2Name"));
        resu.setCellValueFactory(new PropertyValueFactory<>("R"));
        resu.setMinWidth(120);
        listMatch.getColumns().addAll(plOne, plTwo,resu);
        matchListZone.getChildren().addAll(lbMatch,listMatch);
    }


    private void borderPaning(ComboBox c,Label l){
        BorderPane v = new BorderPane();
        v.setTop(l);
        v.setBottom(c);
        edit.add(v);
    }
    private void borderPaning(Button b){
        BorderPane v = new BorderPane();
        v.setBottom(b);
        edit.add(v);
        
    }
    private void configMatchEditZone(){
        borderPaning(pl1,lbPr1);
        borderPaning(pl2,lbPr2);
        borderPaning(res,lbRes);
        matchEditZone.getChildren().addAll(edit);
    }
    
    private void configBottomZone(){
        bottomZone.setSpacing(10);
        bottomZone.setAlignment(Pos.CENTER);
        bottomZone.getChildren().addAll(plRegZone,matchZone);
    }
    private void configWindow(){
        this.setPadding(new Insets(15));
        this.setSpacing(5);
        this.getChildren().addAll(trnmZone,bottomZone);
        
    }
    private void configListeners(){
        configListenerTrnmZone();
        configListenerMatchZone();
        configListenersMatchEditZone();
    }
    public void configListenerTrnmZone(){
        getListViewTrnm().selectedIndexProperty()
            .addListener(o -> {
                listMatch.itemsProperty().bind(viewModel.matchProperty());
                listPlReg.itemsProperty().bind(viewModel.pl1Property());
//                pl1.itemsProperty().bind(viewModel.pl1Property());
//                pl2.itemsProperty().bindBidirectional(viewModel.pl2Property());
//                res.itemsProperty().bindBidirectional(viewModel.resProperty());
                loadButtons(this.loadingButton.get());
            });
    }
    public void configListenerMatchZone(){
        getListViewMatch().selectedIndexProperty().addListener(o -> {
                loadButtons(this.loadingButton.get());
        });
    }
    private void configListenersMatchEditZone(){
        configBtAddMatch();
        this.btDelMatch.setOnAction((ActionEvent event) -> {
            viewModel.delMatch();
        });
        this.btUpdMatch.setOnAction((ActionEvent event) -> {
            viewModel.updMatch();
        });
    }
    public void loadButtons(boolean loadAdd){
        if(loadAdd){
            if(edit.get(edit.size()-1).getChildren().contains(this.btDelMatch)){
                edit.remove(edit.size()-1);
                edit.remove(edit.size()-1);
            }           
            this.borderPaning(btAddMatch);
            matchEditZone.getChildren().setAll(edit);
        }else{
            if(edit.get(edit.size()-1).getChildren().contains(this.btAddMatch))
                edit.remove(edit.size()-1);
            this.borderPaning(btUpdMatch);
            this.borderPaning(btDelMatch);
            matchEditZone.getChildren().setAll(edit);
        }
    }
    
    private void removeDelBtc() {
        
    }
  
    
     private SelectionModel<Match> getListViewMatch() {
        return listMatch.getSelectionModel();
   }
     private SelectionModel<String> getListViewTrnm() {
        return listTrnm.getSelectionModel();
    }
    private void configBtAddMatch(){
        btAddMatch.setOnAction((ActionEvent event) -> {
            viewModel.addMatch();
        });
    }
    private void configBindings(){
        configDataBindings();
        configActionsBindings();
        configViewModelBindings();
    }
    private void configDataBindings(){
        listTrnm.itemsProperty().bind(viewModel.trnmProperty());
        loadingButton.bind(viewModel.loadingButtonProperty());
        pl1.valueProperty().bindBidirectional(viewModel.pl1StringProperty());
        pl2.valueProperty().bindBidirectional(viewModel.pl2StringProperty());
        res.valueProperty().bindBidirectional(viewModel.resStringProperty());
        pl1.itemsProperty().bindBidirectional(viewModel.pl1Property());
        pl2.itemsProperty().bindBidirectional(viewModel.pl2Property());
        res.itemsProperty().bindBidirectional(viewModel.resProperty());
    }
    private void configActionsBindings(){
        pl1.disableProperty().bind(viewModel.pl1EditableProperty());
        pl2.disableProperty().bind(viewModel.pl2EditableProperty());
        res.disableProperty().bind(viewModel.resEditableProperty());
    }
    private void configViewModelBindings(){
        viewModel.bindTrnmSelectedPropTo(this.getListViewTrnm().selectedIndexProperty());
        viewModel.bindMatchSelectedPropTo(this.getListViewMatch().selectedIndexProperty());
    }

    
}
