package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import static java.util.stream.Collectors.toList;
/**
 * 
 * @author Jams
 */
public class Federation {
    private List <Tournoi> tournois = new ArrayList<>();

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
        this.addTrnm(new Tournoi("Troisième Tournoi"));
    }
    /**
     * Ajoute un tournoi dans la liste des tournois
     * @param t Un tournoi.
     */
    public void addTrnm(Tournoi t){
        tournois.add(t);
        int index = tournois.indexOf(t);
    }

    public Tournoi selectedTrnm(int index){
        return tournois.get(index);
    }
    public List<Tournoi> getTournois() {
        return tournois;
    }
    /**
     * Renvoie la liste de tournoi en chaine de caractère.
     * @return La liste de tournoi en String.
     */
    public List<String> getTournoisName() {
        return tournois.stream().map(d -> d.getNom()).collect(toList());
    }

    /**
     *
     */
   

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
    

}
