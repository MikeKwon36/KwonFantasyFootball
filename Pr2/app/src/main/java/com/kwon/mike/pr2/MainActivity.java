package com.kwon.mike.pr2;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private TextView mSearchTitle, mGameEngineTitle;
    private DBSQLiteOpenHelper mHelper;
    private Cursor mCursor;
    private Spinner mRosterASpinner,mRosterBSpinner;
    private ListView mSearchListView;
    private ArrayAdapter<Player> mFFBRosterArrayAdapterA, mFFBRosterArrayAdapterB;
    private CursorAdapter mCursorAdapter;
    private Random mGameEngine;
    private android.support.v7.app.ActionBar mToolbar;
    public SharedPreferences mPrefs;
    public int mRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameEngineTitle = (TextView)findViewById(R.id.xmlGameEngineTitle);
        mRosterASpinner = (Spinner)findViewById(R.id.xmlFantasyRoster1Spinner);
        mRosterBSpinner = (Spinner)findViewById(R.id.xmlFantasyRoster2Spinner);
        mSearchTitle = (TextView)findViewById(R.id.xmlSearchTitle);
        mSearchListView = (ListView)findViewById(R.id.xmlSearchListView);
        mToolbar = getSupportActionBar();
        mHelper = DBSQLiteOpenHelper.getInstance(MainActivity.this);

        mToolbar.setTitle("");
        mPrefs = getSharedPreferences("prefs", MODE_PRIVATE);
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

        //AsyncTask used to populate player list from database and setup adapter with it
        StartUpTask startUpTask = new StartUpTask();
        startUpTask.execute();

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
                ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
                resetPlayerListTask.execute();
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
        SearchView searchView = (SearchView) menu.findItem(R.id.xmlActionBarSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Name/Pos/Team");
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        //Code to update search results dynamically as the User types
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
                    resetPlayerListTask.execute();
                }
                else {
                    SearchByNamePositionTeamTask searchByNamePositionTeamTask = new SearchByNamePositionTeamTask();
                    searchByNamePositionTeamTask.execute(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.xmlActionBarShare){Toast.makeText(MainActivity.this, "Draft results posted online!", Toast.LENGTH_SHORT).show();}
        return super.onOptionsItemSelected(item);
    }

    //onNewIntent method overridden to execute database search which allows user to search database
    // by three different criteria (Name, Team, OR position).  If the received intent has an
    // "Action_Search" filter, the searchView cursor is updated to reflect the search results.
    @Override
    protected void onNewIntent(Intent intent) {handleIntent(intent);}
    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchByNamePositionTeamTask searchByNamePositionTeamTask = new SearchByNamePositionTeamTask();
            searchByNamePositionTeamTask.execute(query);
        } else {
            ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
            resetPlayerListTask.execute();
        }
    }

    // If user adds player to roster via ResultDetailActivity, onActivityResult checks if player has
    // already been drafted and if the position is already taken on their roster.  If neither
    // check returns true, a new player object is created & added to the roster. The method
    // concludes by updating the player turn via RequestCode and resetting the search results.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Sequence of actions to execute if Player A drafts a player
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
                    mGameEngineTitle.setText(getResources().getString(R.string.GameKickOff));
                    mRequestCode = 3;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Search results are reset
                ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
                resetPlayerListTask.execute();
            }
        }

        //Sequence of actions to execute if Player B drafts a player
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
                    mGameEngineTitle.setText(getResources().getString(R.string.GameKickOff));
                    mRequestCode = 3;
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putInt("prefs", mRequestCode);
                    editor.commit();
                }

                //Search results are reset
                ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
                resetPlayerListTask.execute();
            }
        }

        //After game is played in the GameEngineActivity, rosters, searchResults, and
        // MainActivity's gameEngine facilitator are reset
        if (requestCode==3){
            if(resultCode == RESULT_OK){
                FantasyFootballRosterA.getInstance().getFullRosterA().clear();
                Player titleA = new Player("--Roster A--","","","",0);
                FantasyFootballRosterA.getInstance().getFullRosterA().add(titleA);
                mRosterASpinner.setSelection(0);

                FantasyFootballRosterB.getInstance().getFullRosterB().clear();
                Player titleB = new Player("--Roster B--","","","",0);
                FantasyFootballRosterB.getInstance().getFullRosterB().add(titleB);
                mRosterBSpinner.setSelection(0);

                ResetPlayerListTask resetPlayerListTask = new ResetPlayerListTask();
                resetPlayerListTask.execute();

                mGameEngineTitle.setText(getResources().getString(R.string.GameFlipCoin));
                mRequestCode = 0;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("prefs", mRequestCode);
                editor.commit();
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
                mGameEngineTitle.setText(getResources().getString(R.string.GameFlipCoin));
                break;
            case 1:
                mGameEngineTitle.setText(getResources().getString(R.string.GamePlayer1Draft));
                break;
            case 2:
                mGameEngineTitle.setText(getResources().getString(R.string.GamePlayer2Draft));
                break;
            case 3:
                mGameEngineTitle.setText(getResources().getString(R.string.GameKickOff));
                break;
        }
    }

    //GameEngineMethod called when GameEngineTextView clicked... Method initiates draft
    // sequence by determining which player drafts first.  Once rosters are filled, method
    // launches GameEngineActivity
    public void gameEngineFacilitator(){
        if(mRequestCode==0){
            int coinFlip = mGameEngine.nextInt(2);
            if (coinFlip==0){
                mGameEngineTitle.setText(getResources().getString(R.string.GamePlayer1Draft));
                mRequestCode = 1;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("prefs",mRequestCode);
                editor.commit();
            } else {
                mGameEngineTitle.setText(getResources().getString(R.string.GamePlayer2Draft));
                mRequestCode = 2;
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("prefs",mRequestCode);
                editor.commit();
            }
        }
        if(mRequestCode==3){
            Intent intent = new Intent(MainActivity.this, GameEngineActivity.class);
            startActivityForResult(intent,mRequestCode);
        }
    }

    //AsyncTasks extended to handle database calls
    public class SearchByNamePositionTeamTask extends AsyncTask<String,Void,Cursor>{
        @Override
        protected Cursor doInBackground(String... params) {
            mCursor = mHelper.searchPlayerByNameTeamPosition(params[0]);
            return mCursor;
        }

        @Override
        protected void onPostExecute(Cursor mCursor) {
            super.onPostExecute(mCursor);
            mCursorAdapter.swapCursor(mCursor);
        }
    }
    public class ResetPlayerListTask extends AsyncTask<Void,Void,Cursor>{
        @Override
        protected Cursor doInBackground(Void... params) {
            //Instantiate a singleton database with a getInstance method, instead of a normal constructor
            mCursor = mHelper.getPlayerList();
            return mCursor;
        }

        @Override
        protected void onPostExecute(Cursor mCursor) {
            super.onPostExecute(mCursor);
            mCursorAdapter.swapCursor(mCursor);
        }
    }
    public class StartUpTask extends AsyncTask<Void,Void,Cursor>{
        @Override
        protected Cursor doInBackground(Void... params) {
            //Instantiate a singleton database with a getInstance method, instead of a normal constructor
            mCursor = mHelper.getPlayerList();
            return mCursor;
        }

        @Override
        protected void onPostExecute(Cursor mCursor) {
            super.onPostExecute(mCursor);
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
            mSearchListView.setTextFilterEnabled(true);
        }
    }
}
