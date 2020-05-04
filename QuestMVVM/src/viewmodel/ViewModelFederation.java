/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import Mediator.MediatorViewModel;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.*;
import model.Match.Resultat;
import static model.Match.resFromString;
import view.AlertBox;
import view.ViewFederation;
import view.ViewMatch;

/**
 * Controleur
 * @author Jamal&Mehdi4EPFC
 */
public class ViewModelFederation extends ViewModel {
    Federation fd;
    Tournoi trnmSelected;
    private SimpleListProperty<String> tournoi = new SimpleListProperty<>();
    private SimpleListProperty<String> players = new SimpleListProperty<>();
    private SimpleListProperty<Match> matchs = new SimpleListProperty<>();
    private SimpleListProperty<String> players1 = new SimpleListProperty<>();
    private SimpleListProperty<String> players2 = new SimpleListProperty<>();
    private SimpleListProperty<String> results = new SimpleListProperty<>();
    private StringProperty pl1 = new SimpleStringProperty("Player one");
    private StringProperty pl2 = new SimpleStringProperty("Player two");
    private StringProperty res = new SimpleStringProperty(Resultat.MATCH_NUL.name());
    
    /*------------------------------------------------------------------------------------------------*/
    
    private final SimpleIntegerProperty numTrnmSelected = new SimpleIntegerProperty(-1);
    private final IntegerProperty numMatchSelected = new SimpleIntegerProperty(-1);
    private final BooleanProperty pl1DisableProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty pl2DisableProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty resDisablePrpoerty = new SimpleBooleanProperty(true);
    private BooleanProperty loadingButton =new SimpleBooleanProperty(true);//true = add; false= upd/del

    /*--------------------------------------------------------------------------------------------------*/
    
    
    public ViewModelFederation(Federation fede){
            super("Federation");
            MediatorViewModel.getInstance().setName("ok");
            this.fd = fede;
            configBindings();
            configApplicativeLogic();
            configListListeners();
    }
    private void configBindings(){
        tournoi.bind(obserToSimliString(fd.getTournoisName()));
        results.set(Match.getRInList());
    }
    public void bindTrnmSelectedPropTo(ReadOnlyIntegerProperty prop){
        numTrnmSelected.bind(prop);
    }
    public void configApplicativeLogic(){
        numTrnmSelected.addListener((o,old,newValue)->{
            int index = newValue.intValue();
            if(index>=0 && index<fd.nbTrnm()){
                players.bind(obserToSimliString(getTrnmSelected().getPlayersName()));
                matchs.setValue(getTrnmSelected().getMatchs());
                initPlayerSelection();
                
            }
//            else{
//                pl1Editable.set(true);
//                pl2Editable.set(true);
//                resEditable.set(true);
//            }
        });
        numMatchSelected.addListener((o,old,newValue)->{
            int index = newValue.intValue();
            if(index>=0 && index<getTrnmSelected().nbMatchs()){
                loadingButton.set(false);
                pl1.setValue(getMatchSelected().getP1Name());
                pl2.setValue(getMatchSelected().getP2Name()); 
                res.setValue(getMatchSelected().getR());
                pl1DisableProperty.set(true);
            }
            else{
                initPlayerSelection();
            }
        });
        pl1.addListener((o, old, newValue)->{
            opponentsLoading(newValue);
        });
    }
    public void configListListeners(){
        fd.getTournois().addListener(new ListChangeListener<Object>() {
            @Override
            public void onChanged(Change<?> change)
            {
                tournoi.clear();
                for(Tournoi t : fd.getTournois() )
                {
                    tournoi.add(t.getNom());
                }
            }
        });
    }
    public void bindMatchSelectedPropTo(ReadOnlyIntegerProperty prop){
        numMatchSelected.bind(prop);
    }
    /**
        convertis les observable lists<Match> en simpleList<Match>
    */
    private SimpleListProperty<Match> obserToSimliMatch(ObservableList<Match> ob){
        return new SimpleListProperty<>(ob);
    }
    /**
    *    Attribue les valeurs initiaux aux optionbox<br/>
    *    remplit l'optionbox du joueur 1<br/>
    *    Active l'edition du player 1<br/>
    *    Met la Zone d'edition en Mode ajout<br/>
    */
    private void initPlayerSelection(){
        playerSet("Player One","Player two"," ");
        players1.set(players);
        pl1DisableProperty.set(false);
        pl2DisableProperty.set(true);
        this.resDisablePrpoerty.set(true);
        loadingButton.set(true);    
    }
    /**
        Permet de configurer les valeurs de pl1,pl2 et res avec les valeurs en parametre 
    */
    private void playerSet(String play1,String play2,String resu){
        pl1.set(play1);
        pl2.set(play2);
        res.set(resu);
    }
    /**
        recoit un String en parametre qui est le nom d un joueur et renvoie 
        la liste de tout les joueurs avec lesquels ce joueur peux encore jouer
    */
    public SimpleListProperty<String> getOpponent(String name){
        return this.obserToSimliString(getTrnmSelected().versus(name));
    }
    /**
        reenvoie l'index du tournoi selectionné
    */
    private int getNumTrnmSelected() {
        return numTrnmSelected.get();
    }
    /**
        reenvoie l'index du match selectionne
    */
    private int getNumMatchSelected() {
        return numMatchSelected.get();
    }
    /**
        renvoie le tournoi selectionné
    */
    private Tournoi getTrnmSelected(){
        return fd.selectTrnm(getNumTrnmSelected());
    }
    /**
        renvoie le match selectionne
    */
    private Match getMatchSelected() {
        return getTrnmSelected().selectedMatch(getNumMatchSelected());
    }
    /**
    * en fonction du joueur 1 l'optionbox est remplie avec les opposant possible du 
    * joueur 1
    */
    private void opponentsLoading(String pl1){
        if(pl1.compareTo("Player One")!=0){
            if(!getOpponent(pl1).isEmpty()){
                players2.set(getOpponent(pl1));
                    this.pl2.set("Player two");
                pl2DisableProperty.set(false);
                resDisablePrpoerty.set(false);
            }else{
                players2.clear();
                pl2.set("No Opponents");
                pl2DisableProperty.set(true);
            }
        }
    }
    public SimpleListProperty<String> tournoiProperty(){
        return tournoi;
    }
    public SimpleListProperty<String> playersProperty(){
        return players;
    }
    public SimpleListProperty<Match> matchProperty(){
        return matchs;
    }
    public SimpleListProperty<String> players1Property(){
        return players1;
    }
    public SimpleListProperty<String> players2Property(){
        return players2;
    }
    public SimpleListProperty<String> resultsProperty(){
        return results;
    }
    public BooleanProperty pl1EditableProperty(){
        return pl1DisableProperty;
    }
    public BooleanProperty pl2EditableProperty(){
        return pl2DisableProperty;
    }
    public BooleanProperty resEditableProperty(){
        return resDisablePrpoerty;
    }
    public BooleanProperty loadingButtonProperty(){
        return loadingButton;
    }
    public StringProperty pl1StringProperty(){
        return pl1;
    }
    public StringProperty pl2StringProperty(){
        return pl2;
    }
    public StringProperty resStringProperty(){
        return res;
    }
     public boolean addMatch(){
        if(! res.get().equals(" ")){
            Match m = stringToMatch(pl1.get(), pl2.get(), res.get());
            return getTrnmSelected().addMatch(m);
        }else{
            AlertBox.display("Opération impossible", "Vous ne pouvez pas ajouter un match avec un resultat vide");
            return false;
        }
        
    }
    public void updMatch(){
        Match newM = stringToMatch(pl1.get(), pl2.get(), res.get());
        Match oldM = fd.selectTrnm(numTrnmSelected.get()).selectedMatch(numMatchSelected.get());
        getTrnmSelected().updateMatch(oldM, newM);
    }
    public void delMatch(){
        Match m = stringToMatch(pl1.get(), pl2.get(), res.get());
        getTrnmSelected().deleteMatch(m);
    }
    public void matchMake(){
        if(this.getNumTrnmSelected()>=0){
            if(players.contains(pl1.get()) && players.contains(pl2.get())){
                res.set(" ");
                Match m = stringToMatch(pl1.get(),pl2.get());
                mdvm.matchMake(this.fd, this.getTrnmSelected(), m);//prendre le resultat et ajouter
//                this.pl1.set(m.getP1Name());
//                this.pl2.set(m.getP2Name());
//                this.res.set(m.getR());
                if(m.played()){
                    getTrnmSelected().addMatch(m);
                    initPlayerSelection();
                }
                
            }
        }
    }
    
}

