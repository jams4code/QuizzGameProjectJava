package Mediator;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Federation;
import model.Match;
import model.Question;
import model.Tournoi;
import view.AlertBox;
import view.ViewMatch;
import view.ViewMatchGame;
import viewmodel.ViewModel;
import viewmodel.ViewModelMatch;
import viewmodel.ViewModelMatchGame;

/**
 * Mediator class encapsulate all the interaction between the objects of the model.
 * @author Jamal&MehdiEPFC
 */
public class MediatorViewModel {
    private String name;
    private List<ViewModel> canal = new ArrayList<>();
    private MediatorViewModel(){}
    private static MediatorViewModel instance;
    /**
     * This method check if an instance of the mediator have already been created. 
     * @return If not existing, it creates a new one. If existing, It return the existing instance.
     */
    public static MediatorViewModel getInstance(){
        if(instance == null){
            instance = new MediatorViewModel();
        }
        return instance;
    }
    /**
     * Set the name of the mediator.
     * @param n the name to set.
     */
    public void setName(String n){
        this.name = n;
    }
    /**
     * Get the name of the mediator.
     * @return the name of the mediator.
     */
    public String getName(){
        return name;
    }



    
    /**
     * Allow to add a view model to the list
     * @param vm the view model to att
     */
    public void addViewModel(ViewModel vm){
        canal.add(vm);
    }
    /**
     * Allow to remove a view model to the list
     * @param vm the view model to remove
     */
    public void removeViewModel(ViewModel vm){
        canal.remove(vm);
    }
    /**
     * Allow to send an object to a view model.
     * @param vm the view model that will receive the object.
     * @param o the object to send.
     */
    public void send(Object o,ViewModel vm){
        vm.receive(o);
    }
    /**
     * Launch a match.
     * @param fd The federation which contains the tournament from where the players of the match are.
     * @param trnm The Tournament from where the players of the game are.
     * @param m The match that is going to be played.
     */
    public void matchMake(Federation fd, Tournoi trnm, Match m){
        Stage secondary = new Stage();
        ViewModelMatch vM = new ViewModelMatch(getInstance(), fd, trnm, m);
        ViewMatch view = new ViewMatch(secondary, vM);
        secondary.initModality(Modality.APPLICATION_MODAL);
        secondary.showAndWait();
    }
    /**
     * Launch a game for a match with the player 1's selected questions for the player 2.
     * @param m The match for which the game is launched.
     * @param q The selected questions that will be in the game.
     */
    public void matchGame(Match m,ObservableList<Question> q){
        Stage tertiary = new Stage();
        ViewModelMatchGame vM = new ViewModelMatchGame(m,q);
        ViewMatchGame view = new ViewMatchGame(tertiary, vM);
        tertiary.initModality(Modality.APPLICATION_MODAL);
        tertiary.showAndWait();
        m.setResultScored(vM.getScore());
        displayMatchResult(m);
    }
    /**
     * Display the result of the game through an alertbox.
     * @param m The match that's just ended.
     */
    public void displayMatchResult(Match m){
        String pl1 = m.getP1Name();
        String pl2 = m.getP2Name();
        String res = m.getR();
        AlertBox.endGame(pl1, pl2, res);
    }
   
   
}