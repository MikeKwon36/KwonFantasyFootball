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

    public Player() {}
    public Player(String mName, String mPosition, String mTeam, String mBio, int mImage) {
        this.mName = mName;
        this.mPosition = mPosition;
        this.mTeam = mTeam;
        this.mBio = mBio;
        this.mImage = mImage;
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

    @Override
    public String toString() {
        return getmName();
    }
}
