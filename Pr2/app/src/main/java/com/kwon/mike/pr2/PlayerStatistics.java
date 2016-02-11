package com.kwon.mike.pr2;

/**
 * Created by Todo on 2/11/2016.
 */
public class PlayerStatistics {
    int mCompletionPercentage = 0;
    int mCatchRatio = 0;
    int mStrength_Speed = 0;
    boolean mHomefieldAdvantage = false;

    //Constructor for PlayerStatistics object that's embedded in every player object... purpose is
    // to assign player statistics at runtime via a separate class to allow for easy modifications in the future
    public PlayerStatistics() {
    }

    //Getter methods include switch statements to return the appropriate statistic based on the Player object calling it
    public int getCompletionPercentage(Player player) {
        switch (player.getmName()){
            case "Peyton Manning":
                mCompletionPercentage = 60;
                break;
            case "Tom Brady":
                mCompletionPercentage = 50;
                break;
            case "Cam Newton":
                mCompletionPercentage = 40;
                break;
            default:
                mCompletionPercentage = 0;
                break;
        }
        return mCompletionPercentage;
    }
    public int getCatchRatio(Player player) {
        switch (player.getmName()){
            case "Antonio Brown":
                mCatchRatio = 60;
                break;
            case "Mike Kwon":
                mCatchRatio = 50;
                break;
            case "Brandon Marshall":
                mCatchRatio = 40;
                break;
            case "Todd Gurley":
                mCatchRatio = 30;
                break;
            case "Adrian Peterson":
                mCatchRatio = 20;
                break;
            case "Doug Martin":
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
            case "Doug Martin":
                mStrength_Speed = 90;
                break;
            case "Adrian Peterson":
                mStrength_Speed = 80;
                break;
            case "Todd Gurley":
                mStrength_Speed = 70;
                break;
            case "Cam Newton":
                mStrength_Speed = 60;
                break;
            case "Brandon Marshall":
                mStrength_Speed = 60;
                break;
            case "Mike Kwon":
                mStrength_Speed = 50;
                break;
            case "Tom Brady":
                mStrength_Speed = 50;
                break;
            case "Antonio Brown":
                mStrength_Speed = 40;
                break;
            case "Peyton Manning":
                mStrength_Speed = 40;
                break;
            default:
                mStrength_Speed = 0;
                break;
        }
        return mStrength_Speed;
    }
    public boolean getHomefieldAdvantage(Player player, String stadium) {
        if(player.getmName().equals("Brandon Marshall") && stadium.equals("MetLife Stadium, NY/NJ")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Peyton Manning") && stadium.equals("MileHigh Stadium, CO")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Cam Newton") && stadium.equals("Bank of America Stadium, NC")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Tom Brady") && stadium.equals("Gillette Stadium, MA")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Antonio Brown") && stadium.equals("Heinz Field, PA")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Doug Martin") && stadium.equals("Raymond James Stadium, FL")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Adrian Peterson") && stadium.equals("U.S. Bank Stadium, MN")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Mike Kwon") && stadium.equals("Fauver Stadium, ROC")){mHomefieldAdvantage=true;}
        else if(player.getmName().equals("Todd Gurley") && stadium.equals("Los Angeles Entertainment Center, CA")){mHomefieldAdvantage=true;}
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
