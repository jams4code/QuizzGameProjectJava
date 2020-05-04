/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import Mediator.MediatorViewModel;
import element.Elem;
import javafx.beans.property.BooleanProperty;
import static javafx.beans.property.BooleanProperty.booleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.Categories;
import model.Category;
import model.Component;
import model.Federation;
import model.Match;
import model.Question;
import model.Tournoi;
import view.AlertBox;


/**
 *
 * @author Jamal&Mehdi4EPFC
 */
public class ViewModelMatch extends ViewModel {
    Federation fd;
    Tournoi trnmSelected;
    Match focusedMatch;
    Question focusedQuest;
    Category catCh;
    /*Informations relatives au choix des joueurs :*/
    private StringProperty pl1 = new SimpleStringProperty();
    private StringProperty pl2 = new SimpleStringProperty();
    private StringProperty res = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty("Match : "); 
    /*Informations relatives a la liste des questions :*/
    /*-------------View Category -----------------*/
    private ObservableList<Component> catAndQuestAvailable = FXCollections.observableArrayList(Categories.loadLinearCategories());
    private ObservableList<Component> catStruct = FXCollections.observableArrayList(Categories.loadLinearStruct());
    private ListProperty<String> catStructStr = new SimpleListProperty(FXCollections.observableArrayList());
    private ListProperty<Component> questAv = new SimpleListProperty(FXCollections.observableArrayList());
    private ListProperty<String> questAvStr = new SimpleListProperty(FXCollections.observableArrayList());
    private SimpleIntegerProperty pointsDispo = new SimpleIntegerProperty(0);
    /*-------------View Category -----------------*/
    private ObservableList<Question> questChosen = FXCollections.observableArrayList();
    private ListProperty<String> questChosenStrs = new SimpleListProperty(FXCollections.observableArrayList());
    private SimpleIntegerProperty pointsChoisi = new SimpleIntegerProperty(0);
    private static int random = 1;
    private SimpleStringProperty ptDispoLabel = new SimpleStringProperty("Points Disponibles :"+ pointsDispo.get()+"points");
    private SimpleStringProperty ptChoisiLabel =new SimpleStringProperty(pointsChoisi.get()+"/10");
    private SimpleIntegerProperty nbQuest = new SimpleIntegerProperty(0);
    private SimpleStringProperty nbQuestLabel = new SimpleStringProperty("Questions Choisies : " + nbQuest.get());
    private SimpleIntegerProperty nbQuestDispo = new SimpleIntegerProperty(0);
    private SimpleStringProperty nbQuestDispoLabel = new SimpleStringProperty("Questions Disponibles : " + nbQuestDispo.get());
    /*Informations relatives a la questions focus :*/
    private SimpleStringProperty questionSelected = new SimpleStringProperty();
    private SimpleStringProperty r1 = new SimpleStringProperty();
    private SimpleStringProperty r2 = new SimpleStringProperty();
    private SimpleStringProperty r3 = new SimpleStringProperty();
    private SimpleStringProperty r4 = new SimpleStringProperty();
    private SimpleListProperty<SimpleStringProperty> reponses = new SimpleListProperty(FXCollections.observableArrayList(r1, r2, r3, r4));
    private final SimpleIntegerProperty numReponse = new SimpleIntegerProperty(0);
    /*-------------View Category -----------------*/
    private IntegerProperty numCatCh = new SimpleIntegerProperty();
    private IntegerProperty numQuestCh = new SimpleIntegerProperty();
    private IntegerProperty numQuestAv = new SimpleIntegerProperty();

    private final SimpleIntegerProperty numReponseSelected = new SimpleIntegerProperty(-1);
    private final SimpleIntegerProperty points = new SimpleIntegerProperty(0);
    private final SimpleStringProperty pointstr = new SimpleStringProperty("- " + 0 +" points -");
    //toggleReponse
    private SimpleListProperty<SimpleBooleanProperty> selectedReponse = new SimpleListProperty(FXCollections.observableArrayList());
    
    private BooleanProperty enableLeftProperty = new SimpleBooleanProperty(false);
    private BooleanProperty enableRightProperty = new SimpleBooleanProperty(false);

    
    public ViewModelMatch(MediatorViewModel mdvm,Federation fede, Tournoi tournoi, Match m) {
        super("Match");
        this.fd = fede;
        this.trnmSelected = tournoi;
        this.focusedMatch = m;
        title.set("- Match : "+m.getP1Name()+" -VS- "+m.getP2Name() + " -") ;
        initSelectedReponse();
        defaultSetting();
        configListeners();
        refreshLabels();
    } 
    public void defaultSetting(){
        for(Component c : catStruct){
            catStructStr.add(c.getName());
        }
        numCatCh.set(0);
    }
    private void configListeners(){
        numCatCh.addListener((o,old,newVal)->{
            int index = newVal.intValue();
            if(index >=0 && index <catAndQuestAvailable.size()){
                catCh = (Category) catAndQuestAvailable.get(index); 
                loadingQuest();
            }
        });
        numQuestCh.addListener((o,old,newVal)->{
            int index = newVal.intValue();
            if(index >=0 && index < questAv.size()){
                focusedQuest = (Question) questAv.get(index);
                settingQuestion();
                enableRightProperty.setValue(true);
                enableLeftProperty.setValue(false);
            }else{
                focusedQuest = null;
                initQuestion();
                enableRightProperty.setValue(false);
                if(numQuestAv.get() > -1)
                    numQuestAv.setValue(-1);

            }
        });
        numQuestAv.addListener((o,old,newVal)->{
            int index = newVal.intValue();
            if(index>=0 && index <questChosen.size()){
                focusedQuest = questChosen.get(index);
                settingQuestion();
                enableLeftProperty.setValue(true);
                enableRightProperty.setValue(false);
            }
            else{
                focusedQuest = null;
                initQuestion();
                enableLeftProperty.set(false);
                if(numQuestCh.get() > -1)
                    numQuestCh.setValue(-1);
            }
        });
        questAv.addListener(new ListChangeListener<Object>(){
            @Override
            public void onChanged(ListChangeListener.Change<?> change) {
                questAvStr.clear();
                for(Component c :questAv){
                    questAvStr.add(c.getName());
                }
            }
        });
        questChosen.addListener(new ListChangeListener<Object>(){
            @Override
            public void onChanged(ListChangeListener.Change<?> change) {
                questChosenStrs.clear();
                for(Question c :questChosen){
                    questChosenStrs.add(c.getName());
                }
            }
        });
    }
    public void settingQuestion(){
        questionSelected.set(focusedQuest.getName());
        r1.set(focusedQuest.getReponses().get(0));
        r2.set(focusedQuest.getReponses().get(1));
        r3.set(focusedQuest.getReponses().get(2));
        r4.set(focusedQuest.getReponses().get(3));
        numReponse.set(focusedQuest.getNumCorrect());
        setSelectedReponse();
        points.set(focusedQuest.getPoint());
        this.pointstr.set(points.get()+" points");
    }
    public void initQuestion(){
        questionSelected.setValue("Veuillez Choisir une question");
        r1.set("");
        r2.set("");
        r3.set("");
        r4.set("");
        numReponse.set(-1);
        points.set(0);
        this.pointstr.set(points.get()+" points");
        initSelectedReponse();
    }
    public void bindQuestDispoSelectedPropTo(ReadOnlyIntegerProperty prop){
        numQuestAv.bind(prop);
    }
    public void bindQuestChoisiSelectedPropTo(ReadOnlyIntegerProperty prop){
        numQuestCh.bind(prop);
    }
    public SimpleIntegerProperty pointsRestantProperty(){
        return pointsDispo;
    }
    public SimpleIntegerProperty pointsTotalProperty(){
        return pointsChoisi;
    }
    public SimpleStringProperty ptDispoLabelProperty(){
        return ptDispoLabel;
    }
    public SimpleStringProperty ptChoisiLabelProperty(){
        return ptChoisiLabel;
    }
    public SimpleStringProperty questionSelectedProperty(){
        return questionSelected;
    }
    public ListProperty<String> getQuestionsDispo(){
        return questAvStr;
    }
    public ListProperty<String> getQuestionChoisi(){
        return questChosenStrs;
    }
    public SimpleStringProperty pointsProperty(){
        return this.pointstr;
    }
    public SimpleStringProperty titleProperty(){
        return title;
    }
    public ListProperty<String> questChosenProperty(){
        return questChosenStrs;
    }
    public ListProperty<String> catStructStrProperty(){
        return catStructStr;
    }
    public void bindCategorieSelectedPropTo(ReadOnlyIntegerProperty prop){
        numCatCh.bind(prop);
    }
    public void bindQuestionChosenIndexPropTo(ReadOnlyIntegerProperty prop)
    {
        numQuestCh.bind(prop);
    }
    public void bindQuestionAvailableIndexPropTo(ReadOnlyIntegerProperty prop)
    {
        numQuestAv.bind(prop);
    }
    public BooleanProperty enableLeftProperty(){
        return enableLeftProperty;
    }
    public BooleanProperty enableRightProperty(){
        return enableRightProperty;
    }
    /**
     * Permet d'ajouter a la liste des questions disponibles 
     */
    public void addToQuestionDispo(){
        if(focusedQuest != null){
            Category cat = (Category)Categories.getCategoryOf(catAndQuestAvailable, focusedQuest);
            cat.addToChild(focusedQuest);
            questChosen.remove(focusedQuest);
            loadingQuest();
        }
    }
    public void addToQuestionChoisi(){
        int nextScore = points.get() + pointsChoisi.get();
        if(nextScore<=10){
            if(catCh != null && focusedQuest != null){
                questChosen.add(focusedQuest);
                catCh.removeFromChilds(focusedQuest);
                loadingQuest();
            }
        }else if(nextScore>10  && points.get()<10){
            AlertBox.display("Attention", "En ajoutant cette question vous allez dépasser le nombre de points maximum du questionnaire : " + nextScore + " au lieu de 10.\n Veuillez choisir une question qui vaut " + (10-pointsChoisi.get()),"ok");
        } else {
            AlertBox.display("Attention", "Le total de points a ete atteint,\nVous ne pouvez plus ajouter d'autre questions","ok");
        }
        
    }
    private String getSelectedList(ObservableList<String> ls,int index){
        return ls.get(index);
    }
    public void loadingQuest(){
        if(catCh != null){
            ObservableList<Component> quests = FXCollections.observableArrayList(catCh.getQuestions());
            questAv.setValue(quests);
            refreshLabels();
        }
    }
    public void refreshLabels(){
        this.pointsDispo.set(PointOf(questAv));
        this.pointsChoisi.set(PointOfQ(questChosen));
        this.nbQuest.set(getNbQuest(questChosenStrs));
        this.nbQuestDispo.set(getNbQuest(questAvStr));
        this.ptDispoLabel.set("- Points Disponibles : "+ pointsDispo.get()+" points -");
        this.ptChoisiLabel.set("- Points questionnaire : " + pointsChoisi.get()+"/10 -");
        this.nbQuestLabel.set("- Questions Choisies : " + nbQuest.get() + " -");
        this.nbQuestDispoLabel.set("- Questions Disponibles : " + nbQuestDispo.get() + " -");
    }
    public int PointOf(ObservableList<Component> points){
        int point = 0;
        for(Component ob : points){
            Component compo = (Component)ob;
            Question c = (Question)compo;
            point +=c.getPoint();
        }
        return point;
    }
    public int PointOfQ(ObservableList<Question> points){
        int point = 0;
        for(Question ob : points){
            Question c = (Question)ob;
            point +=c.getPoint();
        }
        return point;
    }
    public int getNbQuest(ObservableList<String> list){
        return list.size();
    }
    public SimpleStringProperty nbQuestProperty(){
        return nbQuestLabel;
    }
    public SimpleStringProperty nbQuestDispoProperty(){
        return nbQuestDispoLabel;
    }
    public Match getResult(){
        return this.stringToMatch(pl1.get(), pl2.get(), res.get());
    }
    public void launchGame(){
        mdvm.matchGame(focusedMatch,questChosen);
    }
    private void initSelectedReponse(){
        if(selectedReponse.isEmpty()){
            for (int i = 0; i < 4; i++) {
                selectedReponse.add(new SimpleBooleanProperty(false));
            }
        }else{
            for (int i = 0; i < 4; i++) {
                selectedReponse.get(i).set(false);
            }
        }
    }
    private void setSelectedReponse(){
        for (int i = 0; i < selectedReponse.getSize(); i++) {
            if(i == numReponse.get()-1){
                selectedReponse.get(i).setValue(true);
            }else {
                selectedReponse.get(i).setValue(false);
            }
        }
    }
    public BooleanProperty reponseProperty(int index){
        if(index >=0){
            return selectedReponse.get(index);
        } else {
            throw new RuntimeException("Reponse Property");
        }
    }
    public SimpleStringProperty reponseTextProperty(int index){
        if(index >=0){
            return reponses.get(index);
        }else {
            throw new RuntimeException("Reponse text property");
        }
        
    }
    
}   
/*
*/