package com.kwon.mike.pr2;

/**
 * Created by Todo on 2/4/2016.
 */
public class Player {
    private String mName;
    private String mPosition;
    private String mTeam;
    private String mBio;
    private int mImage;
    private PlayerStatistics mPlayerStats;

    public Player() {}
    public Player(String pName, String pPosition, String pTeam, String pBio, int pImage) {
        this.mName = pName;
        this.mPosition = pPosition;
        this.mTeam = pTeam;
        this.mBio = pBio;
        this.mImage = pImage;
    }
    public String getmName() {
        return mName;
    }
    public void setmName(String mName) {
        this.mName = mName;
    }
    public String getmPosition() {
        return mPosition;
    }
    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }
    public String getmTeam() {
        return mTeam;
    }
    public void setmTeam(String mTeam) {
        this.mTeam = mTeam;
    }
    public String getmBio() {
        return mBio;
    }
    public void setmBio(String mBio) {
        this.mBio = mBio;
    }
    public int getmImage() {
        return mImage;
    }
    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    //call to getPlayerStats instantiates a PlayerStatistics object to allow a Player object to
    // access the individual statistics set by the PlayerStatistics class
    public PlayerStatistics getPlayerStats() {
        mPlayerStats = new PlayerStatistics();
        return mPlayerStats;
    }
    private void setPlayerStats(PlayerStatistics pPlayerStats) {
        this.mPlayerStats = pPlayerStats;
    }

    @Override
    public String toString() {
        return getmName();
    }
}
