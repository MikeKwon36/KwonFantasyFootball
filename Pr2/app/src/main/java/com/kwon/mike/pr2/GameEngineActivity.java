package com.kwon.mike.pr2;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameEngineActivity extends AppCompatActivity {

    private TextView mTeamAScore,mTeamBScore,mQuarter,mStadium,mWeather,mGameFacilitator;
    private TextView mPlayerA_1, mPlayerA_1TD, mPlayerA_2, mPlayerA_2TD, mPlayerA_3, mPlayerA_3TD;
    private TextView mPlayerB_1, mPlayerB_1TD, mPlayerB_2, mPlayerB_2TD, mPlayerB_3, mPlayerB_3TD;
    private Random mGameEngine;
    private Cursor mCursor;
    private DBSQLiteOpenHelper mHelper;
    private boolean mGameComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_engine);

        mTeamAScore = (TextView) findViewById(R.id.xmlScoreBoardTeamAScore);
        mTeamBScore = (TextView) findViewById(R.id.xmlScoreBoardTeamBScore);
        mQuarter = (TextView) findViewById(R.id.xmlScoreBoardQuarter);
        mStadium = (TextView) findViewById(R.id.xmlStadium);
        mWeather = (TextView) findViewById(R.id.xmlWeather);
        mGameFacilitator = (TextView) findViewById(R.id.xmlGameFacilitator);
        mPlayerA_1 = (TextView) findViewById(R.id.xmlPlayerA_1);
        mPlayerA_1TD = (TextView) findViewById(R.id.xmlPlayerA_1_TDs);
        mPlayerA_2 = (TextView) findViewById(R.id.xmlPlayerA_2);
        mPlayerA_2TD = (TextView) findViewById(R.id.xmlPlayerA_2_TDs);
        mPlayerA_3 = (TextView) findViewById(R.id.xmlPlayerA_3);
        mPlayerA_3TD = (TextView) findViewById(R.id.xmlPlayerA_3_TDs);
        mPlayerB_1 = (TextView) findViewById(R.id.xmlPlayerB_1);
        mPlayerB_1TD = (TextView) findViewById(R.id.xmlPlayerB_1_TDs);
        mPlayerB_2 = (TextView) findViewById(R.id.xmlPlayerB_2);
        mPlayerB_2TD = (TextView) findViewById(R.id.xmlPlayerB_2_TDs);
        mPlayerB_3 = (TextView) findViewById(R.id.xmlPlayerB_3);
        mPlayerB_3TD = (TextView) findViewById(R.id.xmlPlayerB_3_TDs);
        mGameEngine = new Random();
        mCursor = mHelper.getPlayerList();

        //Set initial game parameters by generating Stadium and Weather conditions, setting the
        // player rosters, setting the mGameFacilitator TextView/Button text, and setting the
        // mGamePhase boolean to false
        populateGameConditions();
        populateGameRosters();
        mStadium.setText(StadiumWeatherVariables.getVariable(mGameEngine.nextInt(9)));
        mWeather.setText(StadiumWeatherVariables.getVariable(9 + mGameEngine.nextInt(3)));
        mGameFacilitator.setText("[Click here to Kick-off]");
        mGameComplete = false;

        mGameFacilitator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGameComplete==false){
                    
                } else {

                }
            }
        });
    }

    //Method to populate all possible random game conditions for Stadium and Weather
    private void populateGameConditions(){
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumCA));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumCAR));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumDEN));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumFL));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumMIN));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumNE));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumNYC));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumPIT));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.stadiumROC));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.weatherBlizzard));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.weatherRainy));
        StadiumWeatherVariables.getmInstance().add(getResources().getString(R.string.weatherSunny));
    }

    //Method to populate player names based off the completed fantasy rosters
    private void populateGameRosters(){
        mPlayerA_1.setText(FantasyFootballRosterA.getPlayerA(1).getmName().toString());
        mPlayerA_2.setText(FantasyFootballRosterA.getPlayerA(2).getmName().toString());
        mPlayerA_3.setText(FantasyFootballRosterA.getPlayerA(3).getmName().toString());
        mPlayerB_1.setText(FantasyFootballRosterB.getPlayerB(1).getmName().toString());
        mPlayerB_2.setText(FantasyFootballRosterB.getPlayerB(2).getmName().toString());
        mPlayerB_3.setText(FantasyFootballRosterB.getPlayerB(3).getmName().toString());
    }
}
