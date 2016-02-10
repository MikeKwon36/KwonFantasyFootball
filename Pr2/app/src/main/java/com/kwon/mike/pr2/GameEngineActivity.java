package com.kwon.mike.pr2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class GameEngineActivity extends AppCompatActivity {

    TextView mTeamAScore,mTeamBScore,mQuarter,mStadium,mWeather;
    Random mGameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_engine);

        mTeamAScore = (TextView) findViewById(R.id.xmlScoreBoardTeamAScore);
        mTeamBScore = (TextView) findViewById(R.id.xmlScoreBoardTeamBScore);
        mQuarter = (TextView) findViewById(R.id.xmlScoreBoardQuarter);
        mStadium = (TextView) findViewById(R.id.xmlStadium);
        mWeather = (TextView) findViewById(R.id.xmlWeather);
        mGameEngine = new Random();

        mStadium.setText(StadiumWeatherVariables.getVariable(mGameEngine.nextInt(9)));
        mWeather.setText(StadiumWeatherVariables.getVariable(9 + mGameEngine.nextInt(3)));


    }
}
