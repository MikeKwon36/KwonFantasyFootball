package com.kwon.mike.pr2;

/**
 * Created by Todo on 2/11/2016.
 */
public class PlayerStatistics {
    private int mCompletionPercentage = 0;
    private int mCatchRatio = 0;
    private int mStrength_Speed = 0;
    private boolean mHomefieldAdvantage = false;

    //Constructor for PlayerStatistics object that's embedded in every player object... purpose is
    // to assign player statistics at runtime via a separate class to allow for easy modifications in the future
    public PlayerStatistics() {
    }

    //Getter methods include switch statements to return the appropriate statistic based on the Player object calling it
    public int getCompletionPercentage(Player player) {
        switch (player.getmName().toUpperCase()){
            case "PEYTON MANNING":
                mCompletionPercentage = 60;
                break;
            case "TOM BRADY":
                mCompletionPercentage = 50;
                break;
            case "CAM NEWTON":
                mCompletionPercentage = 40;
                break;
            default:
                mCompletionPercentage = 0;
                break;
        }
        return mCompletionPercentage;
    }
    public int getCatchRatio(Player player) {
        switch (player.getmName().toUpperCase()){
            case "ANTONIO BROWN":
                mCatchRatio = 60;
                break;
            case "MIKE KWON":
                mCatchRatio = 50;
                break;
            case "BRANDON MARSHALL":
                mCatchRatio = 40;
                break;
            case "TODD GURLEY":
                mCatchRatio = 30;
                break;
            case "ADRIAN PETERSON":
                mCatchRatio = 20;
                break;
            case "DOUG MARTIN":
                mCatchRatio = 10;
                break;
            default:
                mCatchRatio = 0;
                break;
        }
        return mCatchRatio;
    }
    public int getStrength_Speed(Player player) {
        switch (player.getmName()){
            case "DOUG MARTIN":
                mStrength_Speed = 90;
                break;
            case "ADRIAN PETERSON":
                mStrength_Speed = 80;
                break;
            case "TODD GURLEY":
                mStrength_Speed = 70;
                break;
            case "CAM NEWTON":
                mStrength_Speed = 60;
                break;
            case "BRANDON MARSHALL":
                mStrength_Speed = 60;
                break;
            case "MIKE KWON":
                mStrength_Speed = 50;
                break;
            case "TOM BRADY":
                mStrength_Speed = 50;
                break;
            case "ANTONIO BROWN":
                mStrength_Speed = 40;
                break;
            case "PEYTON MANNING":
                mStrength_Speed = 40;
                break;
            default:
                mStrength_Speed = 0;
                break;
        }
        return mStrength_Speed;
    }
    public boolean getHomefieldAdvantage(Player player, String stadium) {
        if(player.getmName().toUpperCase().equals("BRANDON MARSHALL") && stadium.equals("MetLife Stadium, NY/NJ")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("PEYTON MANNING") && stadium.equals("MileHigh Stadium, CO")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("CAM NEWTON") && stadium.equals("Bank of America Stadium, NC")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("TOM BRADY") && stadium.equals("Gillette Stadium, MA")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("ANTONIO BROWN") && stadium.equals("Heinz Field, PA")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("DOUG MARTIN") && stadium.equals("Raymond James Stadium, FL")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("ADRIAN PETERSON") && stadium.equals("U.S. Bank Stadium, MN")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("MIKE KWON") && stadium.equals("Fauver Stadium, ROC")){mHomefieldAdvantage=true;}
        else if(player.getmName().toUpperCase().equals("TODD GURLEY") && stadium.equals("Los Angeles Entertainment Center, CA")){mHomefieldAdvantage=true;}
        return mHomefieldAdvantage;
    }

    //Setter methods made private (wasn't sure if I should just delete completely)
    private void setCatchRatio(int pCatchRatio) {
        this.mCatchRatio = pCatchRatio;
    }
    private void setStrength_Speed(int pStrength_Speed) {
        this.mStrength_Speed = pStrength_Speed;
    }
    private void setmCompletionPercentage(int pCompletionPercentage) {
        this.mCompletionPercentage = pCompletionPercentage;
    }
    private void setHomefieldAdvantage(boolean pHomefieldAdvantage) {
        this.mHomefieldAdvantage = pHomefieldAdvantage;
    }
}
