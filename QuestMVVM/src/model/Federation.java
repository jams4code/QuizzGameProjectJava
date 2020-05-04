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
 * A federation represents a list of Tournaments.
 * @author Jamal&MehdiEPFC
 */
public class Federation  {
    private final ObservableList <Tournoi> tournois = FXCollections.observableArrayList();

    /**
     * It creates an instance of Federation and initialise the list of tournaments that this federation contain.
     */
    public Federation(){
        initListTrnm();
    }
    /**
     * Initialise the list of tournaments.
     */
    private void initListTrnm(){
        this.addTrnm(new Tournoi("Premier Tournoi"));
        this.addTrnm(new Tournoi("Deuxieme Tournoi"));
        this.addTrnm(new Tournoi("Troisième Tournoi"));
    }
    /**
     * Add a tournament in the list
     * @param t the tournament to add.
     */
    public void addTrnm(Tournoi t){
        tournois.add(t);
        int index = tournois.indexOf(t);
    }

    /**
     * Getter for the tournament's list.
     * @return the tournaments.
     */
    public ObservableList<Tournoi> getTournois() {
        return tournois;
    }
    /**
     * Cast each tournament in string and return the ObservableList of tournaments.
     * @return the list of tournaments in String.
     */
    public ObservableList<String> getTournoisName() {
        return tournois.stream().map(d -> d.getNom()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
    }

    /**
     * Check the number of tournaments that the federation contain.
     * @return the number of tournaments.
     */
    public int nbTrnm(){
        return tournois.size();
    }

    /**
     * Select a tournament from the list.
     * @return the tournament that correspond to the index.
     */
    public Tournoi selectTrnm(int index){
        return tournois.get(index);
    }

}
