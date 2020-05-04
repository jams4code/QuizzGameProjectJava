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
public class Federation extends Observable implements Observer {
    private List <Tournoi> tournois = new ArrayList<>();
    private int numTournoiSelected = -1;

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
        tournois.get(index).addObserver(this);
    }

    /**
     *
     * @param index
     */
    public void select(int index){
       numTournoiSelected = index;
       notif(Notif.TOURNOI_SELECTED);
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
    public List<String> getTournoisName() {
        return tournois.stream().map(d -> d.getNom()).collect(toList());
    }

    /**
     *
     */
    public void unselect(){
        numTournoiSelected = -1;
        notif(Notif.TOURNOI_UNSELECTED);
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
    public Tournoi selectedTrnm(){
        return tournois.get(numTournoiSelected);
    }

    /**
     *
     * @param typeNotif
     */
    public void notif(Notif typeNotif) {
       setChanged();
       notifyObservers(typeNotif);
    }

    @Override
    public void update(Observable o, Object args) {
        Tournoi t = (Tournoi) o;
        Notif status = (Notif) args;
        notif(status);
    }

}
