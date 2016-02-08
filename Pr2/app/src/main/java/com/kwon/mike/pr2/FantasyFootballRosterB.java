package com.kwon.mike.pr2;

import java.util.ArrayList;

/**
 * Created by Todo on 2/8/2016.
 */
public class FantasyFootballRosterB {
    private static FantasyFootballRosterB mInstance;
    private static ArrayList<Player> mFantasyFootballRosterBArray;

    //Roster B Singleton ArrayList of Player objects instantiated in private constructor
    private FantasyFootballRosterB() {
        mFantasyFootballRosterBArray = new ArrayList<Player>();
    }

    //getInstance() method call instantiates and returns an instance of the FantasyFootballRosterB class
    public static FantasyFootballRosterB getInstance() {
        if(mInstance==null){
            mInstance = new FantasyFootballRosterB();
        }
        return mInstance;
    }

    public void addPlayerB(Player player){
        mFantasyFootballRosterBArray.add(player);
    }
    public void removePlayerB(Player player){
        mFantasyFootballRosterBArray.remove(player);
    }
    public void removePlayerB(int index){
        mFantasyFootballRosterBArray.remove(index);
    }
    public Player getPlayerB(int index){
        return mFantasyFootballRosterBArray.get(index);
    }
    public ArrayList<Player> getFullRosterB(){
        return mFantasyFootballRosterBArray;
    }
}
