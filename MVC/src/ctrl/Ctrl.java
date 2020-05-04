/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctrl;

import java.util.List;
import model.*;
import model.Match.Resultat;
import view.View;

/**
 * Controleur
 * @author Jamal&Mehdi4EPFC
 */
public class Ctrl {
    Federation fd;
    Tournoi t;
    /**
     *
     */
    public Ctrl(Federation fede) {
        this.fd = fede;
    }
    public void TrnmSelected(int index){
        if(index >= 0 && index < fd.nbTrnm()){
            fd.select(index);
        }
        else
            fd.unselect();
    }
    public void matchSelected(int index){
        t = fd.selectedTrnm();
        if(index >= 0 && index < t.nbMatchs()){
            t.select(index);
        }
        else
            t.unselect();
    }
    public void addMatch(String p1,String p2,String res){
        Match m = new Match(new Player(p1),new Player(p2),Resultat.valueOf(res));
        t = fd.selectedTrnm();
        t.addMatch(m);     
    }
    public void delSelectedMatch(String p1,String p2,String res){
        Match m = new Match(new Player(p1),new Player(p2),Resultat.valueOf(res));
        t=fd.selectedTrnm();
        t.deleteMatch(m);
    }
    public void updateSelectedMatch(String op1,String op2,String ores,String np1,String np2,String nres){
        Match old = new Match(new Player(op1),new Player(op2),Resultat.valueOf(ores));
        Match newM = new Match(new Player(np1),new Player(np2),Resultat.valueOf(nres));;
        t = fd.selectedTrnm();
        t.updateMatch(old, newM);
    }
    
    public void getOpponent(String name){
        Player p = new Player(name);
        t = fd.selectedTrnm();
        t.getOpponent(p);
    }
}
