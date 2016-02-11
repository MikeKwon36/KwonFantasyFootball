package com.kwon.mike.pr2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import junit.framework.Assert;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Todo on 2/8/2016.
 */
public class KwonProject2UnitTests {

    @Test
    public void testIfPlayerStatisticsGetCompletionCorrect(){
        Player test = new Player("Peyton Manning","QB","Denver Broncos","Bio",1);
        int expectedValue = 60;
        int actualValue = test.getPlayerStats().getCompletionPercentage(test);
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfPlayerStatisticsGetCatchRatioCorrect(){
        Player test = new Player("Antonio Brown","WR","Pittsburgh Steelers","Bio",1);
        int expectedValue = 60;
        int actualValue = test.getPlayerStats().getCatchRatio(test);
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfPlayerStatisticsGetStrengthCorrect(){
        Player test = new Player("Doug Martin","RB","Tampa Bay Buccaneers","Bio",1);
        int expectedValue = 90;
        int actualValue = test.getPlayerStats().getStrength_Speed(test);
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfPlayerStatisticsGetHomefieldCorrect(){
        Player test = new Player("Doug Martin","RB","Tampa Bay Buccaneers","Bio",1);
        Boolean expectedValue = true;
        Boolean actualValue = test.getPlayerStats().getHomefieldAdvantage(test, "Raymond James Stadium, FL");
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfPlayerClassGetNameMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        String expectedValue = "Mike";
        String actualValue = test.getmName();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIfPlayerClassGetPositionMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        String expectedValue = "WR";
        String actualValue = test.getmPosition();
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfPlayerClassGetTeamMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        String expectedValue = "NYJ";
        String actualValue = test.getmTeam();
        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testIfRosterAAddPlayerMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterA.getInstance().addPlayerA(test);
        Assert.assertTrue(FantasyFootballRosterA.getInstance().getFullRosterA().get(FantasyFootballRosterA.getInstance().getFullRosterA().size() - 1).equals(test));
    }

    @Test
    public void testIfRosterARemovePlayerMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterA.getInstance().addPlayerA(test);
        FantasyFootballRosterA.getInstance().removePlayerA(test);
        Assert.assertFalse(FantasyFootballRosterA.getInstance().getFullRosterA().contains(test));
    }

    @Test
    public void testIfRosterAGetPlayerMethodCorrect(){
        Player expectedObject = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterA.getInstance().addPlayerA(expectedObject);
        Player actualObject = FantasyFootballRosterA.getInstance().getPlayerA(FantasyFootballRosterA.getInstance().getFullRosterA().size()-1);
        Assert.assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testIfRosterBAddPlayerMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterB.getInstance().addPlayerB(test);
        Assert.assertTrue(FantasyFootballRosterB.getInstance().getFullRosterB().get(FantasyFootballRosterB.getInstance().getFullRosterB().size() - 1).equals(test));
    }

    @Test
    public void testIfRosterBRemovePlayerMethodCorrect(){
        Player test = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterB.getInstance().addPlayerB(test);
        FantasyFootballRosterB.getInstance().removePlayerB(test);
        Assert.assertFalse(FantasyFootballRosterB.getInstance().getFullRosterB().contains(test));
    }

    @Test
    public void testIfRosterBGetPlayerMethodCorrect(){
        Player expectedObject = new Player("Mike","WR","NYJ","Bio",1);
        FantasyFootballRosterB.getInstance().addPlayerB(expectedObject);
        Player actualObject = FantasyFootballRosterB.getInstance().getPlayerB(FantasyFootballRosterB.getInstance().getFullRosterB().size() - 1);
        Assert.assertEquals(expectedObject, actualObject);
    }

}
