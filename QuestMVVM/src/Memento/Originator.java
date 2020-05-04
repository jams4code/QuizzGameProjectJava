/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Memento;

import model.Question;

/**
 * The Originator is a class that creates a memento. It creates a state first and create a memento after.
 * @author Jamal&MehdiEPFC
 */
public class Originator {

    /**
     * It creates a state first and creates a Memento from this state.
     * @param indexQuest The index of the current question.
     * @param indexBadRep The index of the bad answer selected by the player.
     * @return the instance of the memento created.
     */
    public Memento createMemento(int indexQuest, int indexBadRep){
    	State state = new State(indexQuest, indexBadRep);
        return new Memento(state);
    }
    /**
     * It's a getter to get the value of the index saved.
     * @param mem the memento
     * @return the value of the index of the question.
     */
    public int restoreStateIndexQuest(Memento mem){
            State state = mem.getState();
        return state.getIndexQuestion();
    }
    /**
     * It's a getter to get the value of the wrong answer selected
     * @param mem the memento
     * @return the value of the index of the selected wrong answer saved.
     */
    public int restoreStateBad(Memento mem){
        State state = mem.getState();
        return state.getIndexBadRep();
    }
    /**
     * It represents a state of the game while playing. 
     * We chose to save the index of the question and the index of the wrong selected answer.
     * With those two elements, we can easily go from one state to an old one.
     */
    class State{
    	private int indexQuestion;
    	private int indexBadRep;
        /**
         * Creates an instance of a state.
         * @param currentQuestionIndex The number that represent the index of the current question.
         * @param selectedResponse The wrong answer selected by the player.
         */
    	private State(int currentQuestionIndex, int selectedResponse){
    		this.indexQuestion = currentQuestionIndex;
    		this.indexBadRep = selectedResponse;
    	}
        /**
         * This method is a getter for the index value of the question saved.
         * @return the value of the attribut.
         */
    	private int getIndexQuestion(){
    		return this.indexQuestion;
    	}
        /**
         * this method is a getter for the index value of the wrong answer selected saved.
         * @return the value of the attribut.
         */
    	private int getIndexBadRep(){
    		return this.indexBadRep;
    	}
    }
}
