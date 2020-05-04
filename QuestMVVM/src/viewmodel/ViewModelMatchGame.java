
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import Mediator.MediatorViewModel;
import Memento.CareTaker;
import Memento.Memento;
import Memento.Originator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Match;
import model.Question;
import static model.Question.generateRandom;
import view.AlertBox;

/**
 *
 * @author 2805jaabdelkhalek
 */
public class ViewModelMatchGame extends ViewModel{
    Match currentMatch;
    /* Question */
    private ObservableList<Question> questions;
    private Question currentQuest;
    private SimpleStringProperty questProperty = new SimpleStringProperty();
    private IntegerProperty index = new SimpleIntegerProperty(0);
    private int nbQuestions;
    private SimpleIntegerProperty numCurrentQuest = new SimpleIntegerProperty(0);
    private SimpleStringProperty numCurrentQuestLb = new SimpleStringProperty(numCurrentQuest + "/" + nbQuestions);
    
    /* Hint */
    private SimpleStringProperty hint = new SimpleStringProperty();
    private SimpleBooleanProperty hintOrNo = new SimpleBooleanProperty();
    private Boolean showHint = false ;
    
    /* Info Game */
    private StringProperty pl1 = new SimpleStringProperty();
    private StringProperty pl2 = new SimpleStringProperty();
    private StringProperty res = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty("Match : ");
    /* Réponse */
    private ObservableList<String> reponses = FXCollections.observableArrayList();
    private SimpleStringProperty r1 = new SimpleStringProperty();
    private SimpleStringProperty r2 = new SimpleStringProperty();
    private SimpleStringProperty r3 = new SimpleStringProperty();
    private SimpleStringProperty r4 = new SimpleStringProperty();
    private final SimpleIntegerProperty numReponseSelected = new SimpleIntegerProperty(-1);
    private final SimpleIntegerProperty numBonneReponse = new SimpleIntegerProperty(0);
    private SimpleListProperty<SimpleStringProperty> reponsesProperty = new SimpleListProperty(FXCollections.observableArrayList(r1, r2, r3, r4));
    /* Points */
    private final SimpleIntegerProperty nbTotalPoints = new SimpleIntegerProperty();
    private final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
    private final SimpleStringProperty pointstr = new SimpleStringProperty(0 +" points");
    private SimpleIntegerProperty cptPoint = new SimpleIntegerProperty(0);
    private final SimpleStringProperty cptPointStr = new SimpleStringProperty("- points Gagnés : " + 0 +"/"+nbTotalPoints.get());
    /* Memento */
    private CareTaker careTaker = new CareTaker();
    private Originator creator = new Originator();
    private Boolean lastRepWasCorrect = false;
    private final SimpleIntegerProperty badRep = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty jokerOrNo = new SimpleBooleanProperty();
    private int saveCurrentIndex;
    private boolean lastQuestWasJoker = false;
    /* Points Total possible avec les questions passées -- Point total possible a la current position*/
    private int pointsTotalCurrent = 0;
    private final SimpleIntegerProperty numRepSelected = new SimpleIntegerProperty();

    public ViewModelMatchGame(Match match,ObservableList<Question> q){
        super("Reponse");
        currentMatch = match;
        questions = q;
        nbQuestions = questions.size();
        nbTotalPoints.set(getTotalPoint(q));
        listener();
        settingQuestion();
    }
    public void hintOrNo(){
        if(points.get() >= 3){
            hintOrNo.set(true);
        } else {
            hintOrNo.set(false);
        }
    }
    public void jokerOrNo(){
        if(canJoker()){
            jokerOrNo.set(true);
        }else {
            jokerOrNo.set(false);
        }
    }
    public SimpleBooleanProperty hintOrNoProperty(){
        return hintOrNo;
    }
    public SimpleBooleanProperty jokerOrNoProperty(){
        return jokerOrNo;
    }
    public SimpleStringProperty numCurrentQuestLbProperty(){
        return numCurrentQuestLb;  
    }
    public SimpleListProperty<Question> getQuestions(){
        return new SimpleListProperty<>(questions);
    }
    public SimpleStringProperty questionProperty(){
        return questProperty;
    }
    public SimpleStringProperty hintProperty(){
        return hint;
    }
    public SimpleStringProperty rlProperty(){
        return r1;
    }
    public SimpleStringProperty r2Property(){
        return r2;
    }
    public SimpleStringProperty r3Property(){
        return r3;
    }
    public SimpleStringProperty r4Property(){
        return r4;
    }
    public SimpleStringProperty pointstrProperty(){
        return pointstr;
    }
    public SimpleStringProperty cptPointstrProperty(){
        return cptPointStr;
    }
    public SimpleIntegerProperty badRepProperty(){
        return badRep;
    }
    public void settingQuestion(){
        /* Questions */
        currentQuest = questions.get(index.get());
        questProperty.set(currentQuest.getName());
        numCurrentQuest.set(index.get()) ;
        numCurrentQuestLb.set(numCurrentQuest.get()+1 + "/" + nbQuestions);
        /* Réponses */
        reponses.setAll(currentQuest.getReponses());
        r1.set(reponses.get(0));
        r2.set(reponses.get(1));
        r3.set(reponses.get(2));
        r4.set(reponses.get(3));
        numBonneReponse.set(currentQuest.getNumCorrect());
        /* Points */
        points.set(currentQuest.getPoint());
        pointstr.set(points.get() + " points");
        cptPointStr.set("- points Gagnés : " + cptPoint.get() +"/"+nbTotalPoints.get());
        pointsTotalCurrent += points.get();
        /* Hint */
        hint.set(currentQuest.setAndGetHint());
        this.showHint = false;
        hintOrNo();
        jokerOrNo();
    }
    public int getScore(){
        return checkPoints();
    }
    public int getTotal(){
        return this.nbTotalPoints.get();
    }
    public SimpleStringProperty reponseTextProperty(int index){
        return reponsesProperty.get(index);
    }
    public boolean next(){
        cptPoint.set(cptPoint.get() + CalculPointQuestion());
        int isWin = checkPoints();
        if(isWin != -1){
            return false;
        }
        if(lastQuestWasJoker){
            index.set(saveCurrentIndex +1);
            lastQuestWasJoker = false;
        } else if(canJoker()){
            saveCurrentIndex = this.index.get();
            joker();
            lastQuestWasJoker = true;
        } else {
            index.set(index.get()+1);
        }
        return true;
    }
    // 1F(0) 2V(1) 
    public void bindRepSelectedPropTo(ReadOnlyIntegerProperty prop){
        numRepSelected.bind(prop);
    }    
    public void listener(){
        index.addListener((o,oldValue,newVal)->{
            int index = newVal.intValue();
            if(index>=0 && index < questions.size() && newVal.intValue() > oldValue.intValue()){
                badRep.set(-1);
                settingQuestion();
            }
            else if(index >= questions.size()){}
            
        });
    }

    public void joker(){
        Memento mem = careTaker.getMemento();
        this.badRep.set(creator.restoreStateBad(mem)-1);
        this.index.set(creator.restoreStateIndexQuest(mem));
        settingQuestion();
    }
    public int getTotalPoint(ObservableList<Question> q){
        int point = 0;
        for(Question qu : q){
            point += qu.getPoint();
        }
        return point;
    }
    /**
     * Version temporaire:
     * @return  -1 si pas encore de resultat
     *          0 si match null
     *          1 si pl1 est gagnant
     *          2 si pl2 est gagnant
     */
    public int checkPoints(){
        int winner = -1;
        int pointsTot = nbTotalPoints.get(); //Le nombre de point total du questionnaire
        int pointsRest = pointsTot - pointsTotalCurrent; //Le nombre de point encore possible de gagner
        int pointsMid = (pointsTot/2); //La moitié du nombre total de points
        int pointsWin = cptPoint.get(); //Les points gagnés par le player 2
        if(pointsWin > (pointsMid)){
            winner = 2;
        } else if((pointsWin + pointsRest) < pointsMid){
            winner = 1;
        } else if(pointsRest == 0 && pointsWin == pointsMid){
            winner = 0;
        }
        return winner;
    }
    public int CalculPointQuestion(){
        int pointWin = 0;
        if(numRepSelected.get() == numBonneReponse.get()){
            this.lastRepWasCorrect = true;
            if(showHint){
                if(currentQuest.isFake()){
                    pointWin = 2;
                } else {
                    pointWin = 1;
                }
            } else {
                //Points de la question
                pointWin = this.points.get();
            }
        } else {
            this.lastRepWasCorrect = false;
            Memento mem = creator.createMemento(index.get(), numRepSelected.get());
            careTaker.addMemento(mem);
        }
        return pointWin;
    }
    public void usedHint(){
        showHint = true;
    }
    public boolean canJoker(){
        int nb = generateRandom();
        return lastRepWasCorrect && !careTaker.isEmpty() && nb ==5;
    }
    
}