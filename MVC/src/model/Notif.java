/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author mehdi
 */
public enum Notif {
        /**
         *  Phase d'initialisation.
         */
        INIT,

        /**
         * Ajout d'un match dans le tournoi.
         */
        MATCH_ADDED,

        /**
         * Supression d'un match dans le tournoi.
         */
        MATCH_DELETED,

        /**
         * Modification d'un match sur le tournoi.
         */
        MATCH_UPDATED,
        
        TOURNOI_SELECTED,
        TOURNOI_UNSELECTED,
        MATCH_UNSELECTED,
        MATCH_SELECTED,
        PLAYER_SELECTED;
}
