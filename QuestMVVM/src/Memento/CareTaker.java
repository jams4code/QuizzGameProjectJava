/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Memento;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * This class is the CareTaker for the Memento.
 * It creates a LinkedList in which we can store Mementos.
 * @author Jamal&MehdiEPFC
 */
public class CareTaker {
    private final Queue<Memento> mementos = new LinkedList<>();
    /**
     * This method add a memento in the list of the Instance.
     * @param mem The memento to add.
     */
    public void addMemento(Memento mem) {
        mementos.add(mem);
    }
    /**
     * This method return the first memento added.
     * @return a memento.
     */
    public Memento getMemento() {
        return mementos.poll();
    }
    /**
     * This method check if the careTaker contains a memento or not.
     * @return true if and only if there is at least one memento. False, if the list is empty.
     */
    public Boolean isEmpty(){
        return mementos.isEmpty();
    }
}
