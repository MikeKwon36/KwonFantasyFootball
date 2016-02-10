package com.kwon.mike.pr2;

import java.util.ArrayList;

/**
 * Created by Todo on 2/6/2016.
 */
public class FantasyFootballRosterA {
    private static FantasyFootballRosterA mInstance;
    private static ArrayList<Player> mFantasyFootballRosterAArray;

    //Roster A Singleton ArrayList of Player objects instantiated in private constructor
    private FantasyFootballRosterA(){
        mFantasyFootballRosterAArray = new ArrayList<Player>();
        Player titleA = new Player("--Roster A--","","","",0);
        mFantasyFootballRosterAArray.add(titleA);
    }

    //getInstance() method call instantiates and returns an instance of the FantasyFootballRosterA class
    public static FantasyFootballRosterA getInstance(){
        if(mInstance==null){
            mInstance = new FantasyFootballRosterA();
        }
        return mInstance;
    }

    public static void addPlayerA(Player player){
        mFantasyFootballRosterAArray.add(player);
    }
    public static void removePlayerA(Player player){
        mFantasyFootballRosterAArray.remove(player);
    }
    public static void removePlayerA(int index){
        mFantasyFootballRosterAArray.remove(index);
    }
    public static Player getPlayerA(int index){
        return mFantasyFootballRosterAArray.get(index);
    }
    public static ArrayList<Player> getFullRosterA(){
        return mFantasyFootballRosterAArray;
    }
}
