package com.kwon.mike.pr2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView mTitle, mRosterTitle,mRoster2Title, mSearchTitle, mGameEngineTitle;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;
    Spinner mRosterASpinner,mRosterBSpinner;
    ListView mSearchListView;
    ArrayAdapter<Player> mFFBRosterArrayAdapterA, mFFBRosterArrayAdapterB;
    CursorAdapter mCursorAdapter;
    Random mGameEngine;
    SharedPreferences mPrefs;
    int mRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView)findViewById(R.id.xmlTitle);
        mGameEngineTitle = (TextView)findViewById(R.id.xmlGameEngineTitle);
        mRosterTitle = (TextView)findViewById(R.id.xmlRosterTitle);
        mRoster2Title = (TextView)findViewById(R.id.xmlRoster2Title);
        mRosterASpinner = (Spinner)findViewById(R.id.xmlFantasyRoster1Spinner);
        mRosterBSpinner = (Spinner)findViewById(R.id.xmlFantasyRoster2Spinner);
        mSearchTitle = (TextView)findViewById(R.id.xmlSearchTitle);
        mSearchListView = (ListView)findViewById(R.id.xmlSearchListView);
        mPrefs = getSharedPreferences("prefs",MODE_PRIVATE);
        mGameEngine = new Random();
        mRequestCode = 0;

        //GameEngine TextView cycles through player turns and initiates GameEngineActivity
        // when roster requirements are fulfilled
        mGameEngineTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameEngineFacilitator();
            }
        });

        //Singleton ArrayLists used to store all players drafted for both fantasy rosters and
        // displayed in spinners (artificial player used in index 0 to prevent Spinner from
        // automatically initiating OnItemSelectListener on first player drafted)
        mFFBRosterArrayAdapterA = new ArrayAdapter<Player>(MainActivity.this,android.R.layout.simple_spinner_item,FantasyFootballRosterA.getInstance().getFullRosterA());
        mFFBRosterArrayAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRosterASpinner.setAdapter(mFFBRosterArrayAdapterA);
        mFFBRosterArrayAdapterB = new ArrayAdapter<Player>(MainActivity.this,android.R.layout.simple_spinner_item,FantasyFootballRosterB.getInstance().getFullRosterB());
        mFFBRosterArrayAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRosterBSpinner.setAdapter(mFFBRosterArrayAdapterB);

        //Instantiate a singleton database with a getInstance method, instead of a normal constructor
        mHelper = DBSQLiteOpenHelper.getInstance(MainActivity.this);
        mCursor = mHelper.getPlayerList();

        //Using the DBhelper's open database connection, cursor adapter displays database search results
        mCursorAdapter = new CursorAdapter(MainActivity.this,mCursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.roster_entry,parent,false);
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView name = (TextView)view.findViewById(R.id.xmlPlayerName);
                TextView pos = (TextView)view.findViewById(R.id.xmlPlayerPos);
                TextView team = (TextView)view.findViewById(R.id.xmlPlayerTeam);
                name.setText(cursor.getString(cursor.getColumnIndex(mHelper.COL_NAME)));
                pos.setText(cursor.getString(cursor.getColumnIndex(mHelper.COL_POSITION)));
                team.setText(cursor.getString(cursor.getColumnIndex(mHelper.COL_TEAM)));
            }
        };
        mSearchListView.setAdapter(mCursorAdapter);

        //OnItemClick listener aligns Search result list-clicks with database items by moving
        // cursor accordingly, and passing the selected player's id to the Details Activity
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRequestCode<1){
                    Toast.makeText(MainActivity.this, "Flip coin to start draft order!", Toast.LENGTH_SHORT).show();
                } else if (mRequestCode>2){
                    Toast.makeText(MainActivity.this, "Rosters set! Time to play.", Toast.LENGTH_SHORT).show();
                } else {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                mCursor.moveToPosition(position);
                intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(mHelper.COL_ID)));
                startActivityForResult(intent, mRequestCode);
                }
            }
        });

        //OnItemSelectedListener aligns player objects selected in Spinners with Database via
        // name (instead of id), and uses startActivity instead of startActivityForResult to
        // prevent re-adding a drafted player again
        mRosterASpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){return;}
                else {
                    Intent intent = new Intent(MainActivity.this, ResultDetailActivity.class);
                    intent.putExtra("Name", FantasyFootballRosterA.getInstance().getPlayerA(position).getmName());
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mRosterBSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){return;}
                else {
                    Intent intent = new Intent(MainActivity.this, ResultDetailActivity.class);
                    intent.putExtra("Name", FantasyFootballRosterB.getInstance().getPlayerB(position).getmName());
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Clicking the search results title TextView will reset the list of searchable players
        mSearchTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCursor = mHelper.getPlayerList();
                mCursorAdapter.swapCursor(mCursor);
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toastResetSearch), Toast.LENGTH_SHORT).show();
            }
        });

        //CatchAll handleIntent statement
        handleIntent(getIntent());
    }

    //Creates a SearchView interface and associates searchable configuration with it
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    //onNewIntent method overridden to execute database search which allows user to search database
    // by three different criteria (Name, Team, OR position).  If the received intent has an
    // "Action_Search" filter, the searchView cursor is updated to reflect the search results.
    @Override
    protected void onNewIntent(Intent intent) {handleIntent(intent);}
    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mCursor = mHelper.searchPlayerByNameTeamPosition(query);
            mCursorAdapter.swapCursor(mCursor);
        } else {
            mCursor = mHelper.getPlayerList();
            mCursorAdapter.swapCursor(mCursor);
        }
    }

    // If user adds player to roster via ResultDetailActivity, onActivityResult checks if player has
    // already been drafted and if the position is already taken on their roster.  If neither
    // check returns true, a new player object is created & added to the roster. The method
    // concludes by updating the player turn via RequestCode and resetting the search results.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int id = data.getIntExtra("id", -1);
                Cursor cursor = mHelper.searchPlayerByid(id);
                cursor.moveToFirst();
                Player newPlayer = new Player();
                newPlayer.setmName(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
                newPlayer.setmTeam(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
                newPlayer.setmPosition(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
                newPlayer.setmBio(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
                newPlayer.setmImage(cursor.getInt(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));

                //roster tested to see if player is already taken in either roster OR position is already accounted for
                boolean dupTest = false;
                for (Player test : FantasyFootballRosterA.getInstance().getFullRosterA()){
                    if(test.getmName().equals(newPlayer.getmName())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPlayerDuplicate),Toast.LENGTH_SHORT).show();
                    } else if (test.getmPosition().equals(newPlayer.getmPosition())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPositionTaken),Toast.LENGTH_SHORT).show();
                    }
                }
                for (Player test : FantasyFootballRosterB.getInstance().getFullRosterB()){
                    if(test.getmName().equals(newPlayer.getmName())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPlayerDuplicate),Toast.LENGTH_SHORT).show();
                    }}
                if(!dupTest){
                    FantasyFootballRosterA.getInstance().addPlayerA(newPlayer);
                    mFFBRosterArrayAdapterA.notifyDataSetChanged();
                    mRequestCode=2;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Once rosters are complete, option to begin game is presented
                if(FantasyFootballRosterA.getInstance().getFullRosterA().size()==FantasyFootballRosterB.getInstance().getFullRosterB().size()
                        && FantasyFootballRosterA.getInstance().getFullRosterA().size()==4) {
                    mGameEngineTitle.setText("<-Click to start game!->");
                    mRequestCode = 3;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Search results are reset
                mCursor = mHelper.getPlayerList();
                mCursorAdapter.swapCursor(mCursor);
            }
        }
        if (requestCode==2){
            if(resultCode == RESULT_OK){
                int id = data.getIntExtra("id", -1);
                Cursor cursor = mHelper.searchPlayerByid(id);
                cursor.moveToFirst();
                Player newPlayer = new Player();
                newPlayer.setmName(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
                newPlayer.setmTeam(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
                newPlayer.setmPosition(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
                newPlayer.setmBio(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
                newPlayer.setmImage(cursor.getInt(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));

                //roster tested to see if player is already taken in either roster OR position is already accounted for
                boolean dupTest = false;
                for (Player test : FantasyFootballRosterB.getInstance().getFullRosterB()){
                    if(test.getmName().equals(newPlayer.getmName())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPlayerDuplicate),Toast.LENGTH_SHORT).show();
                    } else if (test.getmPosition().equals(newPlayer.getmPosition())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPositionTaken),Toast.LENGTH_SHORT).show();
                    }
                }
                for (Player test : FantasyFootballRosterA.getInstance().getFullRosterA()){
                    if(test.getmName().equals(newPlayer.getmName())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this,getResources().getString(R.string.toastPlayerDuplicate),Toast.LENGTH_SHORT).show();
                    }
                }
                if(!dupTest){
                    FantasyFootballRosterB.getInstance().addPlayerB(newPlayer);
                    mFFBRosterArrayAdapterB.notifyDataSetChanged();
                    mRequestCode=1;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Once rosters are complete, option to begin game is presented
                if(FantasyFootballRosterA.getInstance().getFullRosterA().size()==FantasyFootballRosterB.getInstance().getFullRosterB().size()
                        && FantasyFootballRosterA.getInstance().getFullRosterA().size()==4){
                    mGameEngineTitle.setText("<-Rosters ready, click to Kick-off!->");
                    mRequestCode = 3;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Search results are reset
                mCursor = mHelper.getPlayerList();
                mCursorAdapter.swapCursor(mCursor);
            }
        }
    }

    //onResume overridden to retrieve the RequestCode in sharedPreferences to restore the player turn
    @Override
    protected void onResume() {
        super.onResume();
        mPrefs = getSharedPreferences("prefs",MODE_PRIVATE);
        mRequestCode = mPrefs.getInt("prefs",0);
        switch (mRequestCode){
            case 0:
                mGameEngineTitle.setText("<Click here to flip coin>");
                break;
            case 1:
                mGameEngineTitle.setText("Player 1 draft");
                break;
            case 2:
                mGameEngineTitle.setText("Player 2 draft");
                break;
            case 3:
                mGameEngineTitle.setText("<Click to start game!>");
                break;
        }
    }

    //GameEngineMethod called when GameEngineTextView clicked to start draft & launch GameActivity
    public void gameEngineFacilitator(){
        if(mRequestCode==0){
            int coinFlip = mGameEngine.nextInt(2);
            if (coinFlip==0){
                mGameEngineTitle.setText("Player 1 draft");
                mRequestCode = 1;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("prefs",mRequestCode);
                editor.commit();
            } else {
                mGameEngineTitle.setText("Player 2 draft");
                mRequestCode = 2;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("prefs",mRequestCode);
                editor.commit();
            }
        }
        if(mRequestCode==3){
            Intent intent = new Intent(MainActivity.this,GameEngineActivity.class);
            startActivity(intent);
        }
    }
}
