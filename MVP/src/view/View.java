/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.List;
import model.Match;


/**
 *
 * @author 2805jaabdelkhalek
 */
public interface View {
    
    void loadTrnm(List<String> trnm);
    
    void loadRegPl(List<String> regPl);
    
    void loadMatch(List<Match> matchs);
    
    public void fillCBoxes(List<String> pl1,List<String> pl2,List<String> res);
    
    public void fillCBoxes(List<String> pl2,List<String> res);
    
    public void fillCBoxesOnUpd(String pl1, String pl2, List<String> res);
    
    public void selectTrnm(int index);
    
    public void selectMatch(int index);
    
    public void loadingButtonSelected();
    
    public void loadingButtonAdd();
    
    public void initMatchEditZone();
    
    public void modifyDisabilityPl1(boolean state);
    
    public void modifyDisabilityPl2(boolean state);
    
    public void modifyDisabilityRes(boolean state);
    
    public void setCbbx(String pl1,String pl2,String res);
    public void setCbbx(String pl2,String res);
    
    public void defaultValues();
}
