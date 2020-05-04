/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.ArrayList;
import java.util.List;
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
import model.Match;
import presenter.Presenter;

/**
 *
 * @author 2805jaabdelkhalek
 */
public class ViewImplements extends VBox implements View{
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
    private final Label lbMatch = new Label("Matchs :");
    private final Label lbPr1 = new Label("Player 1:");
    private final Label lbPr2 = new Label("Player 2:");
    private final Label lbRes = new Label("Result :");
    private final Button btAddMatch = new Button("Add");
    private final Button btUpdMatch = new Button("Update");
    private final Button btDelMatch = new Button("Delete");
    private final ComboBox<String> pl1 = new ComboBox<>();
    private final ComboBox<String> pl2 = new ComboBox<>();
    private final ComboBox<String> res = new ComboBox<>();
    private final Image icon = new Image("/images/LOGO.png");
    private Presenter presenter;
    private final TableView<Match> listMatch = new TableView<>();
    private final TableColumn <Match,String> plOne = new TableColumn<>("Player One");
    private final TableColumn <Match,String> plTwo = new TableColumn<>("Player Two");
    private final TableColumn <Match,String> resu = new TableColumn<>("Result");
    
    public ViewImplements(Stage primaryStage){
        configComponents();
        configListeners();
        Scene scene = new Scene(this, 800,700);
        scene.getStylesheets().add("view/Styles.css");
        primaryStage.setTitle("Gestion de tournois - MJ");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
    }
    
    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
    }
    
    public void configComponents(){
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
    private SelectionModel<String> getListViewTrnm() {
        return listTrnm.getSelectionModel();
    }
    private SelectionModel<Match> getListViewMatch() {
        return listMatch.getSelectionModel();
   }
    private void configMatchListZone(){
        plOne.setCellValueFactory(new PropertyValueFactory<>("P1Name"));
        plTwo.setCellValueFactory(new PropertyValueFactory<>("P2Name"));
        resu.setCellValueFactory(new PropertyValueFactory<>("R"));
        resu.setMinWidth(120);
        listMatch.getColumns().addAll(plOne, plTwo,resu);
        matchListZone.getChildren().addAll(lbMatch,listMatch);
    }
  private void configListeners(){
        configListenerTrnmZone();
        configListenersMatchListZone();
        configListenersMatchEditZone();
    }
     private void configListenerTrnmZone(){
        getListViewTrnm().selectedIndexProperty()
            .addListener(o -> {
                presenter.trnmSelected(getListViewTrnm().getSelectedIndex());
            });
        listTrnm.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) 
                getListViewMatch().clearSelection();
        });
    }
     private void configListenersMatchListZone(){
        getListViewMatch().selectedIndexProperty()
                .addListener(o -> {
                    presenter.matchSelected(getListViewMatch().getSelectedIndex());
                });
    }
     private void configListenersMatchEditZone(){
        btAddMatch.setOnAction(o ->{
            if(addable(pl1.getValue(),pl2.getValue())){
                presenter.addMatch(pl1.getValue(),pl2.getValue(),res.getValue());
            } else {
                AlertBox.display("Erreur", "Opération impossible");
            }
        });
        btUpdMatch.setOnAction(o->{
            String p1 = getListViewMatch().getSelectedItem().getP1Name();
            String p2 =  getListViewMatch().getSelectedItem().getP2Name();
            String resultat = getListViewMatch().getSelectedItem().getR();
            if(addable(pl1.getValue(),pl2.getValue())){
                presenter.updateSelectedMatch(p1,p2,resultat,pl1.getValue(),pl2.getValue(),res.getValue());}
        });
        btDelMatch.setOnAction(o->{
            String p1 = getListViewMatch().getSelectedItem().getP1Name();
            String p2 =  getListViewMatch().getSelectedItem().getP2Name();
            String resultat = getListViewMatch().getSelectedItem().getR();
            presenter.delSelectedMatch(p1,p2,resultat);
        });
        pl1.setOnAction(o->{
            if(!pl1.getValue().equals("Player 1")){
                pl2.setValue("Player 2");
                presenter.playerSelected(pl1.getValue());
            }
        });
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
     public boolean addable(String p1,String p2){
        return !p1.equals("Player 1")&&!p2.equals("Player 2")&&!p2.equals("no opponent");
    }
    @Override
    public void loadTrnm(List<String> trnm) {
        this.listTrnm.getItems().setAll(trnm);
    }

    @Override
    public void loadRegPl(List<String> regPl) {
        this.listPlReg.getItems().setAll(regPl);
    }

    @Override
    public void loadMatch(List<Match> matchs) {
        this.listMatch.getItems().setAll(matchs);
    }

    @Override
    public void selectTrnm(int index) {
        this.getListViewTrnm().select(index);
    }

    @Override
    public void selectMatch(int index) {
        this.getListViewMatch().select(index);
        if(index == -1){
            this.loadingButtonAdd();
        }
    }
    public void defaultValues(){
        pl1.setValue("Player 1");
        pl2.setValue("Player 2");
        res.setValue("MATCH_NUL");
        pl1.setDisable(false);
        pl2.setDisable(true);
        res.setDisable(true);
        
    }
    
    @Override
    public void initMatchEditZone() {
       this.loadingButtonAdd();
       pl1.setDisable(true);
    }
    //LoadingButton
    @Override
    public void loadingButtonSelected(){
        if(edit.get(edit.size()-1).getChildren().contains(this.btAddMatch)){
            edit.remove(edit.size()-1);
        }
        this.borderPaning(btUpdMatch);
        this.borderPaning(btDelMatch);
        matchEditZone.getChildren().setAll(edit);
    }
    public void loadingButtonAdd(){
        if(edit.get(edit.size()-1).getChildren().contains(this.btDelMatch)){
            edit.remove(edit.size()-1);
            edit.remove(edit.size()-1);
        }
        this.borderPaning(btAddMatch);
        matchEditZone.getChildren().setAll(edit);
        defaultValues();
    }

    //Disability Function
    @Override
    public void modifyDisabilityPl1(boolean state) {
        pl1.setDisable(state);
    }

    @Override
    public void modifyDisabilityPl2(boolean state) {
        pl2.setDisable(state);
    }

    @Override
    public void modifyDisabilityRes(boolean state) {
        res.setDisable(state);
    }

    //Combo box function
    @Override
    public void setCbbx(String pl1, String pl2, String res) {
        this.pl1.setValue(pl1);
        this.setCbbx(pl2, res);
    }
    
    @Override
    public void setCbbx( String pl2, String res) {
        this.pl2.setValue(pl2);
        this.res.setValue(res);
    }

    @Override
    public void fillCBoxes(List<String> pl2, List<String> res) {
        this.pl2.getItems().setAll(pl2);
        this.res.getItems().setAll(res);
    }
    
    @Override
    public void fillCBoxes(List<String> pl1,List<String> pl2,List<String> res){
        this.pl1.getItems().setAll(pl1);
        this.fillCBoxes(pl2, res);
        
    }
    
    public void fillCBoxesOnUpd(String pl1, String pl2, List<String> res){
        this.pl1.setValue(pl1);
        this.pl2.setValue(pl2);
        this.res.getItems().setAll(res);
        modifyDisabilityPl1(true);
        modifyDisabilityPl2(true);
        modifyDisabilityRes(false);

    }
    
}
