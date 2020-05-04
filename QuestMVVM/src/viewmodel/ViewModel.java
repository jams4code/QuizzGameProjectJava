/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewmodel;

import Mediator.MediatorViewModel;
import element.Elem;
import java.util.List;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import model.Match;
import model.Player;

/**
 *
 * @author mehdi
 */
public abstract class ViewModel {
    public String name;
    public MediatorViewModel mdvm;
    ViewModel(String nom){
        name = nom;
        mdvm = Mediator.MediatorViewModel.getInstance();
    }
    public String getname(){
        return name;
    }
    public void send(Object o,ViewModel vm){
        mdvm.send(o, vm);
    }
    public void receive(Object o){
    }
    public void sendList(List<Elem> e,ViewModel vm){
        vm.receiveList(e);
    }
    public void receiveList(List<Elem> e){}
    protected SimpleListProperty<String> obserToSimliString(ObservableList<String> ob){
        return new SimpleListProperty<>(ob);
    }
    public Match stringToMatch(String pl1, String pl2, String res){
        return new Match(new Player(pl1), new Player(pl2), Match.Resultat.valueOf(res));
    }
    public Match stringToMatch(String pl1, String pl2){
        return new Match(new Player(pl1), new Player(pl2));
    }    
}
