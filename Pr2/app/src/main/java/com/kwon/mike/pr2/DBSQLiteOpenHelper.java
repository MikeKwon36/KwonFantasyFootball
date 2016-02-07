package com.kwon.mike.pr2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Todo on 2/4/2016.
 */
public class DBSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PLAYER_DB";

    public static final String TABLE_NAME = "PLAYERS";

    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_POSITION = "POSITION";
    public static final String COL_TEAM = "TEAM";
    public static final String COL_BIO = "BIO";
    public static final String COL_IMAGE = "IMAGE_REF";

    public static final String[] TABLE_COLUMNS = {COL_ID,COL_NAME,COL_POSITION,COL_TEAM,COL_BIO,COL_IMAGE};

    private static final String CREATE_SHOPPING_LIST_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT, " +
                    COL_POSITION + " TEXT, " +
                    COL_TEAM + " TEXT, " +
                    COL_BIO + " TEXT, " +
                    COL_IMAGE + " INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    //Singleton database instance
    private static DBSQLiteOpenHelper mInstance;

    //Singleton database constructor set to private + code to populate database
    private DBSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        addPlayer("Tom Brady", "Quarterback", "New England Patriots",context.getResources().getString(R.string.TomBradyBio),R.drawable.tombradypng);
        addPlayer("Cam Newton", "Quarterback", "Carolina Panthers",context.getResources().getString(R.string.CamBio),R.drawable.camnewton);
        addPlayer("Peyton Manning", "Quarterback", "Denver Broncos",context.getResources().getString(R.string.PeytonBio),R.drawable.peyton);
        addPlayer("Mike Kwon", "Wide Receiver", "Rochester Yellowjackets",context.getResources().getString(R.string.KwonBio),R.drawable.kwon);
        addPlayer("Brandon Marshall", "Wide Receiver", "New York Jets",context.getResources().getString(R.string.BrandonBio),R.drawable.marshall);
        addPlayer("Antonio Brown", "Wide Receiver", "Pittsburgh Steelers",context.getResources().getString(R.string.AntonioBio),R.drawable.antonio_brown);
        addPlayer("Adrian Peterson", "Running Back", "Minnesota Vikings",context.getResources().getString(R.string.APBio),R.drawable.ap);
        addPlayer("Doug Martin", "Running Back", "Tampa Bay Buccaneers",context.getResources().getString(R.string.DougBio),R.drawable.dougmartin);
        addPlayer("Todd Gurley", "Running Back", "Los Angeles Rams",context.getResources().getString(R.string.GurleyBio),R.drawable.gurley);
    }

    //Method to retrieve Singleton database uses application context in constructor (since individual activity contexts are not applicable)
    public static DBSQLiteOpenHelper getInstance (Context context){
        if(mInstance == null){
            mInstance = new DBSQLiteOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    //Method to return a cursor populated with the full list of players
    public Cursor getPlayerList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,TABLE_COLUMNS,null,null,null,null,null,null);
        return cursor;
    }

    //Method to add a new player to the database
    public long addPlayer(String pName, String pPosition, String pTeam, String pBio, int pImageRef){
        String name = pName.toUpperCase();
        String position = pPosition.toUpperCase();
        String team = pTeam.toUpperCase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_POSITION, position);
        values.put(COL_TEAM, team);
        values.put(COL_BIO, pBio);
        values.put(COL_IMAGE, pImageRef);

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    //Delete a player from the database
    public int deletePlayer(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }
    //Search for a player by database id
    public Cursor searchPlayerByid(int query){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, COL_ID + " = " + query,
                null, null, null, null, null);
        return cursor;
    }
    //Search for a player by name
    public Cursor searchPlayerByName(String query){
        String name = query.toUpperCase();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, COL_NAME + " LIKE ?",
                new String[]{"%" + name + "%"}, null, null, null, null);
        return cursor;
    }

    //Search for a player by Name OR Team OR Position
    public Cursor searchPlayerByNameTeamPosition(String query){
        String search = query.toUpperCase();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,TABLE_COLUMNS,COL_NAME + " LIKE "+ "'%" + search + "%'"
                        +" OR " + COL_TEAM + " LIKE "+ "'%" + search + "%'"
                        +" OR " + COL_POSITION + " LIKE "+ "'%" + search + "%'",
                null,null,null,null,null);
        return cursor;
    }
}
