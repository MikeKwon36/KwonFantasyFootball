package com.kwon.mike.pr2;

import java.util.ArrayList;

/**
 * Created by Todo on 2/10/2016.
 */
public class StadiumWeatherVariables {
    private static StadiumWeatherVariables mInstance;
    private static ArrayList<String> mStadiumWeatherVariablesArray;

    //Singleton ArrayList of Strings instantiated in private constructor
    private StadiumWeatherVariables(){
        mStadiumWeatherVariablesArray = new ArrayList<String>();
    }

    //getInstance() method call instantiates and returns an instance of the StadiumWeatherVariables class
    public static StadiumWeatherVariables getmInstance (){
        if(mInstance==null){
            mInstance = new StadiumWeatherVariables();
        }
        return mInstance;
    }

    public static void add(String name){
        mStadiumWeatherVariablesArray.add(name);
    }

    public static String getVariable(String name){
        int x = mStadiumWeatherVariablesArray.indexOf(name);
        return mStadiumWeatherVariablesArray.get(x);
    }

    public static String getVariable(int index){
        return mStadiumWeatherVariablesArray.get(index);
    }

    public static int getVariableIndex(String name){
        return mStadiumWeatherVariablesArray.indexOf(name);
    }

    public static ArrayList<String> getAllVariables(){
        return mStadiumWeatherVariablesArray;
    }
}
