package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import model.Match.Resultat;

/**
 * Cette classe représente un tournoi d'un jeu quelconque.
 * @author Jamal&Mehdi4EPFC
 */
public class Tournoi extends Observable {

    /**
     *  Enumération des différents états possible d'un tournoi.
     */
    private List <Match> matchs;
    private List <Player> players;
    private String nom; 
    private int numMatchSelected = -1;
    Notif notif = null;
    private Player selectedPlayer;

    /**
     * Constructeur de la classe Tournoi. Crée une instance de Tournoi qui porte le nom donnée en paramètre.
     * @param nom Le nom du tournoi.
     */
    public Tournoi(String nom) {
        this.nom = nom;
        this.players = randomPlList();
        this.matchs = new ArrayList<>();
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
    public List<Match> getMatchs() {
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
    public List<String> getPlayersName() {
        return players.stream().map(d -> d.getNom()).collect(toList());
    }
    /**
     * Fonction qui permet d'avoir le nom du tournoi.
     * @return Le nom du tournoi.
     */
    public String getNom() {
        return nom;
    }
    public Match selectedMatch(){
        return this.matchs.get(numMatchSelected);
    }
    public void select(int index){
        numMatchSelected = index;
        notif(Notif.MATCH_SELECTED);
    }
    public void unselect(){
        numMatchSelected = -1;
        notif(Notif.MATCH_UNSELECTED);
    }
    public boolean hasMatch(Match m){
        return matchs.contains(m);
    }
    public int nbMatchs(){
        return matchs.size();
    }   
    public boolean addMatch(Match m){
        boolean canAdd =false;
        if(!hasMatch(m) && m.isValidMatch()){
            matchs.add(m);
            notif(Notif.MATCH_ADDED);
            canAdd = true;
        }
        return canAdd;
    }
    public boolean deleteMatch(Match m){
        boolean deleted = false; 
        //supprime si la valeur qui est identique a m
        if(matchs.removeIf(q -> q.equals(m))){
            notif (Notif.MATCH_DELETED);
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
           notif(Notif.MATCH_UPDATED);
       }
       return added;
    }
    public void notif(Notif typeNotif) {
       setChanged();
       notifyObservers(typeNotif);
        
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
    private List<Player>randomPlList(){
        List<Player>l = membreList();
        List<Player>randomPlList = new ArrayList<>();
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

    public List<Player>versus(Player p){
        List<Player>versus = canMatch(p);
        versus.removeIf(m -> m.equals(p));
        return versus;
    }
    private List<Player>canMatch(Player p){
        List<Player>matched = new ArrayList<>();
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
    public void getOpponent(Player p){
        selectedPlayer=p;
        notif(Notif.PLAYER_SELECTED);
    }
   public List<String> versus(){
       return versus(selectedPlayer).stream().map(x->x.getNom()).collect(toList());
   }
}
