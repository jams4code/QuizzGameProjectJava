package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * 
 * @author Jams
 */
public class Federation  {
    private final ObservableList <Tournoi> tournois = FXCollections.observableArrayList();
    private int numTournoiSelected = 1;

    /**
     *
     */
    public Federation(){
        initListTrnm();
    }
    /**
     * 
     */
    private void initListTrnm(){
        this.addTrnm(new Tournoi("Premier Tournoi"));
        this.addTrnm(new Tournoi("Deuxieme Tournoi"));
    }
    /**
     * Ajoute un tournoi dans la liste des tournois
     * @param t Un tournoi.
     */
    public void addTrnm(Tournoi t){
        tournois.add(t);
        int index = tournois.indexOf(t);
    }

    /**
     *
     * @param index
     */
    public void select(int index){
       numTournoiSelected = index;
    }

    /**
     *
     * @return
     */
    public List<Tournoi> getTournois() {
        return tournois;
    }
    /**
     * Renvoie la liste de tournoi en chaine de caractère.
     * @return La liste de tournoi en String.
     */
    public ObservableList<String> getTournoisName() {
        return tournois.stream().map(d -> d.getNom()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
    }

    /**
     *
     */
    public void unselect(){
        numTournoiSelected = -1;
    }

    /**
     *
     * @return
     */
    public int nbTrnm(){
        return tournois.size();
    }

    /**
     *
     * @return
     */
    public Tournoi selectTrnm(int index){
        return tournois.get(index);
    }

}
