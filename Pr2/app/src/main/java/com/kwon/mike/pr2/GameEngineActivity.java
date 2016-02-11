package com.kwon.mike.pr2;

import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
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
    private int mGamePhase, mCalcTeamAScoreSum, mCalcA1TD, mCalcA2TD, mCalcA3TD, mCalcTeamBScoreSum, mCalcB1TD, mCalcB2TD, mCalcB3TD;

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
        mHelper = DBSQLiteOpenHelper.getInstance(GameEngineActivity.this);
        mCursor = mHelper.getPlayerList();

        //Set initial game parameters by generating Stadium and Weather conditions, setting the
        // player rosters, setting the mGameFacilitator TextView/Button text, and initializing the
        // mGamePhase integer to zero
        populateGameConditions();
        populateGameRosters();
        initializeScoreTrackers();
        mQuarter.setText(String.valueOf(mGamePhase));
        mStadium.setText(StadiumWeatherVariables.getVariable(mGameEngine.nextInt(9)));
        mWeather.setText(StadiumWeatherVariables.getVariable(9 + mGameEngine.nextInt(3)));
        mGameFacilitator.setText(getResources().getString(R.string.gameEngineKickOff));

        //mGameFacilitator click intiates the game sequence and then returns the user to the
        // Main Activity upon completion (and confirming click)
        mGameFacilitator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Game simulation is curated by a countdown timer that updates player statistics
                // and the team's score at timed intervals.  Player statistics are generated by
                // applying their individual multipliers against randomly generated scores.
                if (mGamePhase == 0) {
                    mGameFacilitator.setText(getResources().getString(R.string.gameEngineInGame));
                    new CountDownTimer(25000, 5000) {
                        public void onTick(long millisUntilFinished) {
                            mGamePhase += 1;
                            mQuarter.setText(String.valueOf(mGamePhase));
                            mCalcA1TD += (mCalcA1TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcA2TD += (mCalcA2TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcA3TD += (mCalcA3TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcB1TD += (mCalcB1TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcB2TD += (mCalcB2TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcB3TD += (mCalcB3TD + (mGameEngine.nextInt(2)*mGameEngine.nextInt(2)));
                            mCalcTeamAScoreSum = (7 * mCalcA1TD) + (7 * mCalcA2TD) + (7 * mCalcA3TD);
                            mCalcTeamBScoreSum = (7 * mCalcB1TD) + (7 * mCalcB2TD) + (7 * mCalcB3TD);
                            mPlayerA_1TD.setText(String.valueOf(mCalcA1TD));
                            mPlayerA_2TD.setText(String.valueOf(mCalcA2TD));
                            mPlayerA_3TD.setText(String.valueOf(mCalcA3TD));
                            mPlayerB_1TD.setText(String.valueOf(mCalcB1TD));
                            mPlayerB_2TD.setText(String.valueOf(mCalcB2TD));
                            mPlayerB_3TD.setText(String.valueOf(mCalcB3TD));
                            mTeamAScore.setText(String.valueOf(mCalcTeamAScoreSum));
                            mTeamBScore.setText(String.valueOf(mCalcTeamBScoreSum));
                        }

                        public void onFinish() {
                            mQuarter.setText("F");
                            if (mCalcTeamAScoreSum > mCalcTeamBScoreSum) {
                                mGameFacilitator.setText(getResources().getString(R.string.gameEngineAwins));
                            } else if (mCalcTeamAScoreSum < mCalcTeamBScoreSum) {
                                mGameFacilitator.setText(getResources().getString(R.string.gameEngineBwins));
                            } else if (mCalcTeamAScoreSum == mCalcTeamBScoreSum) {
                                mGameFacilitator.setText(getResources().getString(R.string.gameEngineTie));
                            }
                        }
                    }.start();
                } else {
                    Intent intent = new Intent(GameEngineActivity.this, MainActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
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
        mPlayerA_1TD.setText(String.valueOf(mCalcA1TD));
        mPlayerA_2TD.setText(String.valueOf(mCalcA2TD));
        mPlayerA_3TD.setText(String.valueOf(mCalcA3TD));
        mPlayerB_1TD.setText(String.valueOf(mCalcB1TD));
        mPlayerB_2TD.setText(String.valueOf(mCalcB2TD));
        mPlayerB_3TD.setText(String.valueOf(mCalcB3TD));
    }

    //Method to initialize all scoring tracker variables to zero
    private void initializeScoreTrackers(){
        mGamePhase=0;
        mCalcTeamAScoreSum=0;
        mCalcTeamBScoreSum=0;
        mCalcA1TD=0;
        mCalcA2TD=0;
        mCalcA3TD=0;
        mCalcB1TD=0;
        mCalcB2TD=0;
        mCalcB3TD=0;
    }
}
