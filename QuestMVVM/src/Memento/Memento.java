/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Memento;

import Memento.Originator.State;
import model.Question;

/**
 * The Memento class represent a state.
 * @author Jamal&MehdiEPFC
 */
public class Memento {
    
    private final State state;
    /**
     * This is the constructor. It creates an instance of a memento with the given state.
     * @param state The state to save in the memento.
     */
    public Memento(State state){
        this.state = state;
    }
    /**
     * This method return the state of the memento.
     * @return the state of the memento.
     */
    public State getState() {
        return this.state;
    }
    
    
}
