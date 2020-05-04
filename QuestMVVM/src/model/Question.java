/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jamal&MehdiEPFC
 */
public class Question extends Component {
    private int numCorrect;
    private List<String> reponses = new ArrayList();
    private int point;
    private String fakeHint;
    private String hint;
    private String sndHint;
    public Question(String name, int numCorrect, int point,List<String>reponse,String hint,String fakeHint,Category parent) {
        super(name,parent);
        this.numCorrect = numCorrect;
        this.point = point;
        this.reponses=reponse;
        this.fakeHint = fakeHint;
        this.hint = hint;
    }
    //Renvoie une question avec les mêmes paramètre que la question courante
    public Question(Question quest){
        this(quest.getName(), quest.numCorrect, quest.point, quest.reponses, quest.hint, quest.fakeHint, quest.getParent());
    }
    public int getNumCorrect() {
        return numCorrect;
    }
    public List<String> getReponses() {
        return reponses;
    }
    public Question getQuestion(){
        return this;
    }
    public int getPoint() {
        return point;
    }
    // 1 chance sur 5 que ce soit un faux joker
    public String setAndGetHint(){
        sndHint = hint; //Bon hint
        if(point >= 3){
            int nb = generateRandom(); //génére un chiffre de 1 à 5. Si = 5 fake hint
            if(nb == 5){
                sndHint = fakeHint;
            }
        }
        return sndHint;
    }
    public boolean isFake(){
        return sndHint == hint;
    }
    // A placer dans le bon fichier !! To continue ...
    /* Générer un chiffre entre 1 et 5 , afin de l'utiliser 
    * Dans la fonction setFakeOrNot(int index) 
    * Si le chiffre vaut 5, On va générer un fake  joker(1/5 = 0.2 => 20% chance que ça arrive)
    */
    public static int generateRandom(){
        Random rand = new Random();
        int min = 1;
        int max = 5;
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
