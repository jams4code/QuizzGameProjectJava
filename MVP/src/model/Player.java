package model;

/**
 * Cette classe représente un joueur.
 * @author Jamal&Mehdi4EPFC
 */
public class Player {
    private String nom;

    /**
     * Constructeur de la classe Player. Crée une instance de la classe avec le nom donné en paramètre.
     * @param nom Le nom du joueur.
     */
    public Player(String nom) {
        this.nom = nom;
    }

    /**
     * Permet d'obtenir le nom du joueur.
     * @return le nom du joueur.
     */
    public String getNom() {
        return nom;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Player){
            Player p = (Player)o;
            return p.nom.compareTo(this.nom) == 0;
        }
        return false;
    }
    @Override
    public String toString(){
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
