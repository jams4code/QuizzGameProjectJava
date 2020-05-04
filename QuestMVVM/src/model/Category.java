/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Jamal&MehdiEPFC
 */
public class Category extends Component {
    private List<Component> childs = new ArrayList<>();
    /**
     * Constructeur d'une categorie avec le parent mentionné
     */
    public Category(String name,Category parent){
        super(name,parent);
    }
    /**
     * Constructeur Categorie avec parent non mentionné donc parent null(Pour la categorie mere)
     */
    public Category(String name){
        this(name,null);
    }
    /**
     * Pour ajouter une question ou une sous categorie a une categorie
     */
    public void addToChild(Component element){
        childs.add(element);
    }
    /**
     * Pour retirer une question ou une sous categorie a une categorie
     */
    public boolean removeFromChilds(Component element){
        boolean removed  = childs.remove(element);
        for(Component c : childs){
            if(c instanceof Category && removed == false){
                removed = ((Category)c).removeFromChilds(element);
            }
        }
        return removed;
    }
    /**
     * Pour determiner si une question se trouve dans une categorie
     */
    public boolean containsQuestion(Component element){
        boolean contained = false;
        for(Component c : childs){
            if(contained == false && element.getName() == c.getName()){
                contained = true;
            }
        }
        return contained;
    }
    /**
     * Obtenir la list des souscategorie et question d 'une question
     */
    public List<Component> getChilds() {
        return childs;
    }
    /**
     * Obtenir un des composant dans la liste des enfants dont l'indice est index
     */
    public Component getChild(int index){
        return childs.get(index);
    }
    /**
     * Permet d'extraire toutes les questions d'une categories et de ses sous categories
     */
    public List<Component> getQuestions(){
        List<Component> question = new ArrayList<>();
        for(Component e : childs){
            if(e instanceof Question){
                question.add(e);
            }
            else if(e instanceof Category){
                question.addAll(((Category) e).getQuestions());
            }
        }
        return question;
    } 
    /**
     * Calcul le nombre de points de toutes les questions de la categorie et sous categories
     */
    public int getPointsAllQuestion(){
        int point = 0;
        List<Component> questions = this.getQuestions();
        for(Component qu : questions){
            point += ((Question)qu).getPoint();
        }
        return point;
    }
}
