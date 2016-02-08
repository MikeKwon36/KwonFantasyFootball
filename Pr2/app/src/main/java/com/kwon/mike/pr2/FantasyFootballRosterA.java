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
    }

    //getInstance() method call instantiates and returns an instance of the FantasyFootballRosterA class
    public static FantasyFootballRosterA getInstance(){
        if(mInstance==null){
            mInstance = new FantasyFootballRosterA();
        }
        return mInstance;
    }

    public void addPlayerA(Player player){
        mFantasyFootballRosterAArray.add(player);
    }
    public void removePlayerA(Player player){
        mFantasyFootballRosterAArray.remove(player);
    }
    public void removePlayerA(int index){
        mFantasyFootballRosterAArray.remove(index);
    }
    public Player getPlayerA(int index){
        return mFantasyFootballRosterAArray.get(index);
    }
    public ArrayList<Player> getFullRosterA(){
        return mFantasyFootballRosterAArray;
    }
}
