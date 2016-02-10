package com.kwon.mike.pr2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class GameEngineActivity extends AppCompatActivity {

    private TextView mTeamAScore,mTeamBScore,mQuarter,mStadium,mWeather;
    private Random mGameEngine;

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

        //Method to populate random game conditions for Stadium and Weather
        populateGameConditions();
        mStadium.setText(StadiumWeatherVariables.getVariable(mGameEngine.nextInt(9)));
        mWeather.setText(StadiumWeatherVariables.getVariable(9 + mGameEngine.nextInt(3)));



    }

    private void populateGameConditions(){
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumCA));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumCAR));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumDEN));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumFL));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumMIN));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumNE));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumNYC));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumPIT));
        StadiumWeatherVariables.add(getResources().getString(R.string.stadiumROC));
        StadiumWeatherVariables.add(getResources().getString(R.string.weatherBlizzard));
        StadiumWeatherVariables.add(getResources().getString(R.string.weatherRainy));
        StadiumWeatherVariables.add(getResources().getString(R.string.weatherSunny));
    }
}
