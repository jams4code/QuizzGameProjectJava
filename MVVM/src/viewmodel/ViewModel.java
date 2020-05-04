/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

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
import model.*;
import model.Match.Resultat;
import static model.Match.resFromString;
import view.View;

/**
 * Controleur
 * @author Jamal&Mehdi4EPFC
 */
public class ViewModel {
    Federation fd;
    private final IntegerProperty numTrnmSelected = new SimpleIntegerProperty(-1);
    private final IntegerProperty numMatchSelected = new SimpleIntegerProperty(-1);
    private BooleanProperty loadingButton =new SimpleBooleanProperty(true);//true = add; false= upd/del
    private StringProperty pl1 = new SimpleStringProperty("Player one");
    private StringProperty pl2 = new SimpleStringProperty("Player two");
    private StringProperty res = new SimpleStringProperty(Resultat.MATCH_NUL.name());
    private final BooleanProperty pl1Editable = new SimpleBooleanProperty(true);
    private final BooleanProperty pl2Editable = new SimpleBooleanProperty(true);
    private final BooleanProperty resEditable = new SimpleBooleanProperty(true);
    
    public ViewModel(Federation fede) {
        this.fd = fede;
        configApplicativeLogic();
    }
    
    private void configApplicativeLogic(){
        numTrnmSelected.addListener((o, old, newValue)->{
                int index = newValue.intValue();
                if(index >= 0 && index < fd.nbTrnm()){
                    pl1.set("Player one");
                    pl2.set("Player two");
                    res.set(Resultat.MATCH_NUL.name());
                    pl1Editable.set(false);
                    loadingButton.set(true);
                }
            });
        numMatchSelected.addListener((o, old, newValue)->{
            int index = newValue.intValue();
            if(index >= 0 && index < fd.selectTrnm(numTrnmSelected.get()).nbMatchs()){
                loadingButton.set(false);
                pl1.set(fd.selectTrnm(numTrnmSelected.get()).selectedMatch(numMatchSelected.get()).getP1Name());
                pl2.set(fd.selectTrnm(numTrnmSelected.get()).selectedMatch(numMatchSelected.get()).getP2Name());
                res.set(fd.selectTrnm(numTrnmSelected.get()).selectedMatch(numMatchSelected.get()).getR());
            }        
            });
        pl1.addListener((o, old, newValue)->{
            pl2Editable.set(false);
            resEditable.set(false);
        });
    }
    
    public void bindTrnmSelectedPropTo(ReadOnlyIntegerProperty prop){
        numTrnmSelected.bind(prop);
    }
    
    public void bindMatchSelectedPropTo(ReadOnlyIntegerProperty prop){
        numMatchSelected.bind(prop);
    }
    public SimpleListProperty<String> trnmProperty(){
        return new SimpleListProperty<>(fd.getTournoisName());
    }
    public SimpleListProperty<Match> matchProperty(){
        return new SimpleListProperty<>(fd.selectTrnm(numTrnmSelected.get()).getMatchs());
    }
    public SimpleListProperty<String> pl1Property(){
        return new SimpleListProperty<>(fd.selectTrnm(numTrnmSelected.get()).getPlayersName());
    }
    public SimpleListProperty<String> pl2Property(){
        if(fd.selectTrnm(numTrnmSelected.get()).getPlayersName().contains(pl1))
            return new SimpleListProperty<>(fd.selectTrnm(numTrnmSelected.get()).versus(pl1.get()));
        return new SimpleListProperty<>(fd.selectTrnm(numTrnmSelected.get()).getPlayersName());
    }
    public SimpleListProperty<String> resProperty(){
        return new SimpleListProperty<>(Match.getRInList());
    }
    public BooleanProperty pl1EditableProperty(){
        return pl1Editable;
    }
    public BooleanProperty pl2EditableProperty(){
        return pl2Editable;
    }
    public BooleanProperty resEditableProperty(){
        return resEditable;
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
    public void addMatch(){
        Match m = stringToMatch(pl1.get(), pl2.get(), res.get());
        fd.selectTrnm(numTrnmSelected.get()).addMatch(m);
    }
    private Match stringToMatch(String pl1, String pl2, String res){
        return new Match(new Player(pl1), new Player(pl2), Resultat.valueOf(res));
    }
    public void updMatch(){
        Match newM = stringToMatch(pl1.get(), pl2.get(), res.get());
        Match oldM = fd.selectTrnm(numTrnmSelected.get()).selectedMatch(numMatchSelected.get());
        fd.selectTrnm(numTrnmSelected.get()).updateMatch(oldM, newM);
    }
    public void delMatch(){
        Match m = stringToMatch(pl1.get(), pl2.get(), res.get());
        fd.selectTrnm(numTrnmSelected.get()).deleteMatch(m);
    }
//    public void getOpponent(String name){
//        Player p = new Player(name);
//        t = fd.selectedTrnm();
//        t.getOpponent(p);
//    }
}
