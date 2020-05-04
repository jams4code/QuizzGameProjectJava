/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import element.Elem;
import static element.Elements.loadElemsFromFile;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
/**
 * categories.
 * @author Jamal&MehdiEPFC
 */
public class Categories {
    private static List<Elem> e = loadElemsFromFile("Questions.JSON");
    /**
     * Extrait toutes les categories au meme niveau que ce soit categorie ou sous categorie
     * 
     */
    public static List<Component> loadLinearCategories(){
        List<Component> element = new ArrayList<>();
        Category all = new Category("All");
        element.add(all);
        for(Elem e : e){
            Category c = new Category(e.name);
            element.add(c);
            all.addToChild(c);
            extractCatAndQuest(c,element,e.subElems);
        }
        return element;
    }
    /**
     * Extrait les categorie avec les question associés
     * @param cat
     * @param element
     * @param subelements 
     */
    private static void extractCatAndQuest(Category cat,List<Component> element,List<Elem> subelements){
        for (Elem el : subelements){
            if(el.subElems == null){
                Question q = new Question(el.name,el.numCorrectResponse,el.points,el.responses,el.fakeHint,el.hint,cat);
                cat.addToChild(q);
                
            }else{
                Category c = new Category(el.name,cat);
                cat.addToChild(c);
                element.add(c);
                extractCatAndQuest(c,element,el.subElems);
            }
        }
        
    }
    /**
     * Fonction de debuggage
     * @param e 
     */
    public static void afficheTab(List<Component> e){
        for(Component element : e){
            afficheChild("",element);
        }    
    }
    /**
     * Fonction de debuggage
     * @param h
     * @param e 
     */
    private static void afficheChild(String h,Component e){
        System.out.println(h+e.getName());
        if(e instanceof Category){
            for(Component el : ((Category) e).getChilds()){
                    afficheChild(h+"-",el);
            }
        }
    }
    /**
     * Extrait toutes les categories au meme niveau que ce soit categorie ou sous categorie
     * @return 
     */
    public static List<Component> loadLinearStruct(){
        List<Component> element = new ArrayList<>();
        Category all = new Category("All");
        element.add(all);
        for(Elem e : e){
            Category c = new Category(e.name);
            element.add(c);
            all.addToChild(c);
            extractCatAndQuestForLinear(c,element,e.subElems);
        }
        return element;
    }
    /**
     * Extrait seulement les categorie 
     * @param cat
     * @param element
     * @param subelements 
     */
    private static void extractCatAndQuestForLinear(Category cat,List<Component> element,List<Elem> subelements){
        for (Elem el : subelements){
            if(el.subElems != null){
                Category c = new Category(el.name,cat);
                cat.addToChild(c);
                element.add(c);
                extractCatAndQuestForLinear(c,element,el.subElems);
            }
        }
    }
    /**
     * 
     * @param c
     * @param quest
     * @return 
     */
    public static Component getCategoryOf(ObservableList<Component> c , Component quest){
        Component find = null;
        List<Component> lc = Categories.loadLinearCategories();
        for(Component cate :lc ){
            if(((Category)cate).containsQuestion(quest)){
                find = cate;
            }
        }
        for(Component cate :c ){
            if(find.getName()==cate.getName()){
                find = cate;
            }
        }
        return find;
    }

}
