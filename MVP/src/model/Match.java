package model;

import java.util.ArrayList;
import java.util.List;
import static model.Match.Resultat.GAIN_JOUEUR1;

/**
 * Cette classe représente un match d'un duel quelconque.
 * @author Jamal&Mehdi4EPFC
 */
public class Match {
 
    /**
     * Enumeration des différents résultats possible d'un match.
     */
    public enum Resultat {

        /**
         * Victoire du joueur 1.
         */
        GAIN_JOUEUR1,

        /**
         * Victoire du joueur 2.
         */
        GAIN_JOUEUR2,

        /**
         * Match nul entre les deux joueurs.
         */
        MATCH_NUL;
    }
    private Player p1;
    private Player p2;
    private Resultat r;

    /**
     * Constructeur de la classe Match. Crée une instance de la classe Match.
     * @param p1 Le joueur 1 du match.
     * @param p2 Le joueur 2 du match.
     * @param r Le résultat du match entre les deux joueurs.
     */
    public Match(Player p1, Player p2, Resultat r) {
        this.p1 = p1;
        this.p2 = p2;
        this.r = r;
    }
    /**
     * Permet d'avoir le joueur 1.
     * @return le joueur 1.
     */
    public Player getP1() {
        return p1;
    }

    /**
     * Permet d'avoir le joueur 2.
     * @return le joueur 2.
     */
    public Player getP2() {
        return p2;
    }

    /**
     * Permet d'avoir la valeur de "r" le Résultat.
     * @return la valeur du résultat.
     */
    public String getR() {
        return r.name();
    }

    /**
     * Permet de cahnger la valeur de "p1" le second joueurs.
     * @param p1 Le premier joueur.
     */
    public void setP1(Player p1) {
        this.p1 = p1;
    }

    /**
     *  Permet de cahnger la valeur de "p2" le second joueurs.
     * @param p2 Le second joueur.
     */
    public void setP2(Player p2) {
        this.p2 = p2;
    }

    /**
     * Permet de changer la valeur de "r" le résultat.
     * @param r Le nouveau résultat.
     */
    public void setR(Resultat r) {
        this.r = r;
    }
    
    /**
     * Methode qui permet de vérifier si deux objets sont équivalent.
     * @param o Un objet avec lequel on va comparer le match actuel.
     * @return Vrai, Si les matchs se jouent entre les mêmes joueurs.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Match){
            Match m = (Match)o;
            return (m.p1.getNom() == this.p1.getNom())  && (m.p2.getNom() == this.p2.getNom())
                        || (m.p2.getNom() == this.p1.getNom()) && (m.p1.getNom() == this.p2.getNom());
        }
        return false;
    }
    public boolean isValidMatch(){
        return ! this.p1.equals(p2);
    }
    public String getP1Name(){
        return p1.getNom();
    }
    public String getP2Name(){
        return p2.getNom();
    }  
    public static List<String> getRInList(){
        List<String> r = new ArrayList();
        r.add(Resultat.GAIN_JOUEUR1.name());
        r.add(Resultat.GAIN_JOUEUR2.name());
        r.add(Resultat.MATCH_NUL.name());
        return r;
    }
}
