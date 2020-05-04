package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import model.Match.Resultat;

/**
 * Cette classe représente un tournoi d'un jeu quelconque.
 * @author Jamal&Mehdi4EPFC
 */
public class Tournoi extends Observable {

    /**
     *  Enumération des différents états possible d'un tournoi.
     */
    private ObservableList <Match> matchs = FXCollections.observableArrayList();
    private ObservableList <Player> players = FXCollections.observableArrayList();
    private String nom; 

    /**
     * Constructeur de la classe Tournoi. Crée une instance de Tournoi qui porte le nom donnée en paramètre.
     * @param nom Le nom du tournoi.
     */
    public Tournoi(String nom) {
        this.nom = nom;
        this.players = randomPlList();
        matchMaker();
        
    }
    /**
     * Fonction qui permet de changer le nom du tournoi.
     * @param nom Le nouveau nom du tournoi.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    /**
     * Fonction qui permet d'avoir les matchs.
     * @return Un set avec les matchs du tournoi.
     */
    public ObservableList<Match> getMatchs() {
        return matchs;
    }
    /**
     * Fonction qui permet d'avoir les participants du tournoi.
     * @return Un set avec les participants du tournoi.
     */
    public List<Player> getPlayers() {
        return players;
    }
    /**
     * Fonction qui permet d'avoir les participants du tournoi.
     * @return Un set avec les participants du tournoi.
     */
    public ObservableList<String> getPlayersName() {
        return players.stream().map(d->d.getNom()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
    }
    /**
     * Fonction qui permet d'avoir le nom du tournoi.
     * @return Le nom du tournoi.
     */
    public String getNom() {
        return nom;
    }
    public Match selectedMatch(int index){
        return this.matchs.get(index);
    }
    public boolean hasMatch(Match m){
        return matchs.contains(m);
    }
    public int nbMatchs(){
        return matchs.size();
    }   
    public boolean addMatch(Match m){
        boolean canAdd =false;
        if(!hasMatch(m) && isValidMatch(m)){
            matchs.add(m);
            canAdd = true;
        }
        return canAdd;
    }
    
    public boolean isValidMatch(Match m){
        return m.isValidMatch() && players.contains(m.getP1()) && players.contains(m.getP2());
    }
    public boolean deleteMatch(Match m){
        boolean deleted = false; 
        //supprime si la valeur qui est identique a m
        if(matchs.removeIf(q -> q.equals(m))){
            deleted = true;
        }
        return deleted;
    }
    public boolean updateMatch(Match oldM, Match newM){
       Match tmp = oldM;
       deleteMatch(oldM);
       boolean added = true;
       if(!addMatch(newM)){
           addMatch(tmp);
           added = false;
       }
       else{
           
       }
       return added;
    }
    private List<Player> membreList(){
        List<Player>membreList = new ArrayList<>();
        membreList.add(new Player("Mehdi"));
        membreList.add(new Player("Jamal"));
        membreList.add(new Player("Bruno"));
        membreList.add(new Player("Benoit"));
        membreList.add(new Player("Alain"));
        membreList.add(new Player("Jon"));
        membreList.add(new Player("Van"));
        membreList.add(new Player("Moussa"));
        membreList.add(new Player("Carré"));
        membreList.add(new Player("Bertrand"));
        membreList.add(new Player("Sven"));
        membreList.add(new Player("Tom"));
        membreList.add(new Player("Bob l'eponge"));
        return membreList;
    }
    private ObservableList<Player>randomPlList(){
        List<Player>l = membreList();
        ObservableList<Player>randomPlList = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            int nbRandom = randomNumber(0, l.size()-1);
            Player tmpPl = l.get(nbRandom);
            if(!randomPlList.contains(tmpPl)){
                randomPlList.add(tmpPl);
            }
        }
        return randomPlList;
    }
    private int randomNumber(int min, int max){
        Random rand = new Random();
        int nombreAleatoire = rand.nextInt(max - min + 1) + min;
        return nombreAleatoire;
    }
    private void matchMaker(){
        int nbMatch = nbMatchPossible(players);
        List<Resultat>rList = new ArrayList<>();
        rList.add(Resultat.MATCH_NUL);
        rList.add(Resultat.GAIN_JOUEUR1);
        rList.add(Resultat.GAIN_JOUEUR2);
        for (int i = 0; i < nbMatch; i++) {
            int selectPl1 = randomNumber(0, this.players.size()-1);
            int selectPl2 = randomNumber(0, this.players.size()-1);
            Player p1 = this.players.get(selectPl1);
            Player p2 = this.players.get(selectPl2);
            int selectR = randomNumber(0,2);
            Resultat r = rList.get(selectR);
            Match match = new Match(p1, p2, r);
            addMatch(match);
        }
    }
    private int nbMatchPossible(List<Player>l){
        int n = l.size(); //Nb player
        int k = 2;//Match ce joue par 2
        return Factoriel(n)/(Factoriel(k)*Factoriel(n-k));//Combinatoire
    }
    private int Factoriel(int n) {
       return n > 1?n * Factoriel(n-1):1;
    }

    public ObservableList<Player>versus(Player p){
        ObservableList<Player>versus = canMatch(p);
        versus.removeIf(m -> m.equals(p));
        return versus;
    }
    private ObservableList<Player>canMatch(Player p){
        ObservableList<Player>matched = FXCollections.observableArrayList();
        matched.addAll(players);
        for (int i = 0; i < matchs.size(); i++) {
            if(matchs.get(i).getP1().equals(p)){
                matched.remove(matchs.get(i).getP2());
            } else if(matchs.get(i).getP2().equals(p)){
                matched.remove(matchs.get(i).getP1());
            }
        }
        return matched;
    }
   public ObservableList<String> versus(String pl){
       Player p = new Player(pl); 
       return versus(p).stream().map(x->x.getNom()).collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
   }
}
