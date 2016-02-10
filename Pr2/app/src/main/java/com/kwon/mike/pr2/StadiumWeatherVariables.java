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
        mStadiumWeatherVariablesArray.add(0,"MetLife Stadium, NY/NJ");
        mStadiumWeatherVariablesArray.add(1,"MileHigh Stadium, CO");
        mStadiumWeatherVariablesArray.add(2,"Bank of America Stadium, NC");
        mStadiumWeatherVariablesArray.add(3,"Gillette Stadium, MA");
        mStadiumWeatherVariablesArray.add(4,"Heinz Field, PA");
        mStadiumWeatherVariablesArray.add(5,"Raymond James Stadium, FL");
        mStadiumWeatherVariablesArray.add(6,"U.S. Bank Stadium, MN");
        mStadiumWeatherVariablesArray.add(7,"Fauver Stadium, ROC");
        mStadiumWeatherVariablesArray.add(8,"Los Angeles Entertainment Center, CA");
        mStadiumWeatherVariablesArray.add(9,"Blizzard");
        mStadiumWeatherVariablesArray.add(10,"Rainy");
        mStadiumWeatherVariablesArray.add(11,"Sunny");
    }

    //getInstance() method call instantiates and returns an instance of the StadiumWeatherVariables class
    public static StadiumWeatherVariables getmInstance (){
        if(mInstance==null){
            mInstance = new StadiumWeatherVariables();
        }
        return mInstance;
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
