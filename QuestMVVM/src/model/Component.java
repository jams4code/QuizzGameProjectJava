/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Jamal&MehdiEPFC
 */
public abstract class Component {
    private String name;
    private Category parent;
    Component(String name,Category parent){
        this.name = name;
        this.parent = parent;
    }
    public String getName() {
        return name;
    }
    public Category getParent() {
        return parent;
    }
}
