/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

import java.util.List;
import model.Federation;
import model.Match;
import model.Match.Resultat;
import model.Player;
import view.View;

/**
 *
 * @author 2805jaabdelkhalek
 */
public class Presenter {
    private final Federation FD;
    private View view;
    private int numTrnm = -1;
    private int numMatch = -1;
    public Presenter(Federation fd){
        this.FD = fd;
    }
    
    public void setView(View view){
        this.view = view;
        this.view.loadTrnm(FD.getTournoisName());
        this.view.initMatchEditZone();
    }
    public void trnmSelected(int index){
        if(index >= 0 && index < FD.nbTrnm()){
            this.numTrnm = index;
            load();
            view.loadingButtonAdd();
        } else {
            this.numTrnm = -1;
        }
    }
    public void matchSelected(int index){
        if(index >= 0 && index < FD.selectedTrnm(numTrnm).nbMatchs()){
            this.numMatch = index;
            Match selMat = FD.selectedTrnm(numTrnm).getMatch(numMatch);
            view.loadingButtonSelected();
            String pl1 = FD.selectedTrnm(numTrnm).getMatch(numMatch).getP1Name();
            String pl2 = FD.selectedTrnm(numTrnm).getMatch(numMatch).getP2Name();
            List<String> res = Match.getRInList();
            view.fillCBoxesOnUpd(pl1, pl2, res);
            
        } else {
            view.selectMatch(-1);
        }
    }
    public void load(){
        view.modifyDisabilityPl1(false);
        view.loadRegPl(FD.getTournois().get(numTrnm).getPlayersName());
        view.loadMatch(FD.getTournois().get(numTrnm).getMatchs());
        loadCBox();
  
    }
    public void loadCBox(){
        List<String>pl = FD.selectedTrnm(numTrnm).getPlayersName();
        List<String>res = Match.getRInList();
        view.fillCBoxes(pl, pl, res);
    }
    public Match stringToMatch(String pl1, String pl2, String res){
        return new Match(new Player(pl1), new Player(pl2), Resultat.valueOf(res));
    }
    public void addMatch(String pl1, String pl2, String res){
        Match m = stringToMatch(pl1, pl2, res);
        FD.selectedTrnm(numTrnm).addMatch(m);
        view.loadMatch(FD.selectedTrnm(numTrnm).getMatchs());
        view.defaultValues();
    }
    public void playerSelected(String pl1){
        List<String> opponents = FD.selectedTrnm(numTrnm).versus(pl1);
        if(!opponents.isEmpty()){
            List<String>res = Match.getRInList();
            view.fillCBoxes(opponents, res);
            view.modifyDisabilityPl2(false);
            view.modifyDisabilityRes(false);
        }
        else
        {
            view.setCbbx("no opponent",Match.Resultat.MATCH_NUL.name());
            view.modifyDisabilityPl2(true);
            view.modifyDisabilityRes(true);
        }
    }
    public void updateSelectedMatch(String opl1,String opl2,String olRes,String npl1,String npl2,String nres){
        Match old = stringToMatch(opl1,opl2,olRes);
        Match n = stringToMatch(npl1,npl2,nres);
        FD.selectedTrnm(numTrnm).updateMatch(old, n);
        view.loadMatch(FD.getTournois().get(numTrnm).getMatchs());
    }
    public void delSelectedMatch(String dpl1,String dpl2,String dres){
        Match toDel = stringToMatch(dpl1,dpl2,dres);
        FD.selectedTrnm(numTrnm).deleteMatch(toDel);
         view.loadMatch(FD.getTournois().get(numTrnm).getMatchs());
    }
                
                
    
}
