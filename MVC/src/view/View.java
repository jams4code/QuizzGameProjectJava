package view;

import ctrl.Ctrl;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Notif;
import model.Player;

/**
 *
 * @author Jamal&MehdiEPFC
 */
public class View extends VBox implements Observer{
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
    private final Ctrl ctrl;
    private final Image icon = new Image("/images/LOGO.png");
    
    public View(Stage primaryStage, Ctrl ctrl){
        this.ctrl = ctrl;
        configComponents();
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
    public void fillTrnmList(List<String> trnm){
        listTrnm.getItems().setAll(trnm);
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
    public void loadingButtonSelected(){
        if(edit.get(edit.size()-1).getChildren().contains(this.btAddMatch))
            edit.remove(edit.size()-1);
        this.borderPaning(btUpdMatch);
        this.borderPaning(btDelMatch);
        matchEditZone.getChildren().setAll(edit);
    }
    public void loadingButtonAdd(Federation f){
        if(edit.get(edit.size()-1).getChildren().contains(this.btDelMatch)){
            edit.remove(edit.size()-1);
            edit.remove(edit.size()-1);
        }           
        this.borderPaning(btAddMatch);
        matchEditZone.getChildren().setAll(edit);
        defaultValues(f);
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
    
    public void defaultValues(Federation f){               
        pl1.setValue("Player 1");              
        pl2.setValue("Player 2");
        res.setValue("MATCH_NUL");
        pl2.setDisable(true);
        res.setDisable(true);
    }
    public void fillCbbx(List<String> p1,List<String> p2,List<String> res){
        pl1.getItems().setAll(p1);
        pl2.getItems().setAll(p2);
        this.res.getItems().setAll(res);
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
        configListenersMatchListZone();
        configListenersMatchEditZone();
    }
    private void configListenerTrnmZone(){
        getListViewTrnm().selectedIndexProperty()
            .addListener(o -> {
                ctrl.TrnmSelected(getListViewTrnm().getSelectedIndex());
            });
        listTrnm.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) 
                getListViewMatch().clearSelection();
        });
    }
    private SelectionModel<String> getListViewTrnm() {
        return listTrnm.getSelectionModel();
    }
     private void configListenersMatchListZone(){
        getListViewMatch().selectedIndexProperty()
                .addListener(o -> {
                    ctrl.matchSelected(getListViewMatch().getSelectedIndex());
                });
    }
    private void configListenersMatchEditZone(){
        btAddMatch.setOnAction(o ->{
            if(addable(pl1.getValue(),pl2.getValue())){
                ctrl.addMatch(pl1.getValue(),pl2.getValue(),res.getValue());
            } else {
                AlertBox.display("Erreur", "Opération impossible");
            }
        });
        btDelMatch.setOnAction(o->{
            String p1 = getListViewMatch().getSelectedItem().getP1Name();
            String p2 =  getListViewMatch().getSelectedItem().getP2Name();
            String resultat = getListViewMatch().getSelectedItem().getR();
            ctrl.delSelectedMatch(p1,p2,resultat);
        });
        btUpdMatch.setOnAction(o->{
            String p1 = getListViewMatch().getSelectedItem().getP1Name();
            String p2 =  getListViewMatch().getSelectedItem().getP2Name();
            String resultat = getListViewMatch().getSelectedItem().getR();
            if(addable(pl1.getValue(),pl2.getValue())){
                ctrl.updateSelectedMatch(p1,p2,resultat,pl1.getValue(),pl2.getValue(),res.getValue());}
        });
        pl1.setOnAction(o->{
            if(!pl1.getValue().equals("Player 1")){
                pl2.setValue("Player 2");
                ctrl.getOpponent(pl1.getValue());}
        });
    }
    public boolean addable(String p1,String p2){
        return !p1.equals("Player 1")&&!p2.equals("Player 2")&&!p2.equals("No oppenent");
    }
    public void displayMatch(List<Match> m){
        ObservableList<Match> dataMatch = FXCollections.observableArrayList(m);
        listMatch.getItems().setAll(dataMatch);
    }
    private SelectionModel<Match> getListViewMatch() {
        return listMatch.getSelectionModel();
   }
    private void initSelectionPlayers(Federation f){
        if(!f.selectedTrnm().versus().isEmpty()){
            pl2.getItems().setAll(f.selectedTrnm().versus());
            pl2.setDisable(false);
            res.setDisable(false);
        }
        else{
            if(!getListViewMatch().isEmpty()){
                pl2.setValue(f.selectedTrnm().selectedMatch().getP2Name());
                res.setDisable(false);
            }
            else {
            pl2.setValue("No oppenent");
            pl2.setDisable(true);
            res.setDisable(true);
        }
        }
    }
    
    
    @Override
    public void update(Observable o, Object args) {
        Federation f = (Federation) o;
        Notif status = (Notif) args;
        switch(status){
            case INIT:
                loadingButtonAdd(f);
                pl1.setDisable(true);
               fillTrnmList(f.getTournoisName());               
                break;
            case MATCH_ADDED:
                displayMatch(f.selectedTrnm().getMatchs());
                defaultValues(f);
                break;
            case MATCH_DELETED:
                displayMatch(f.selectedTrnm().getMatchs());
                break;
           case MATCH_UPDATED:
                displayMatch(f.selectedTrnm().getMatchs());
                break;
            case TOURNOI_SELECTED:
                pl1.setDisable(false);
                displayLstPL(f.selectedTrnm().getPlayersName());
                displayMatch(f.selectedTrnm().getMatchs());
                fillCbbx(f.selectedTrnm().getPlayersName(),f.selectedTrnm().getPlayersName(),Match.getRInList());
                defaultValues(f);
                break;
            case MATCH_SELECTED:
                pl1.setValue(f.selectedTrnm().selectedMatch().getP1().getNom());
                pl2.setValue(f.selectedTrnm().selectedMatch().getP2().getNom());
                res.setValue(f.selectedTrnm().selectedMatch().getR());
                initSelectionPlayers(f);
                loadingButtonSelected();
                break;
            case MATCH_UNSELECTED:
                loadingButtonAdd(f);               
                getListViewMatch().select(-1);
                break;
            case PLAYER_SELECTED:
                initSelectionPlayers(f);
                break;
            case TOURNOI_UNSELECTED:
                getListViewTrnm().select(-1);
                break;
       }
    }
    private void displayLstPL(List<String> player){
           listPlReg.getItems().setAll(player); 
    }
    
}
