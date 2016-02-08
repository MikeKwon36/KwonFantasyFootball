package com.kwon.mike.pr2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView mTitle, mRosterTitle, mSearchTitle;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;
    ListView mRosterListView, mSearchListView;
    ArrayAdapter<Player> mFFBRosterArrayAdapter;
    CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView)findViewById(R.id.xmlTitle);
        mRosterTitle = (TextView)findViewById(R.id.xmlRosterTitle);
        mSearchTitle = (TextView)findViewById(R.id.xmlSearchTitle);
        mRosterListView = (ListView)findViewById(R.id.xmlFantasyRosterListView);
        mSearchListView = (ListView)findViewById(R.id.xmlSearchListView);

        //Singleton ArrayList used to store all players selected by the user for their fantasy roster
        mFFBRosterArrayAdapter = new CustomArrayAdapter(MainActivity.this,FantasyFootballRosterA.getInstance().getFullRosterA());
        mRosterListView.setAdapter(mFFBRosterArrayAdapter);

        //Instantiate a singleton database with a getInstance method, instead of a normal constructor
        mHelper = DBSQLiteOpenHelper.getInstance(MainActivity.this);
        mCursor = mHelper.getPlayerList();

        //Using the helper's database connection, the cursor adapter shows the database search results
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

        //OnItemClick listener aligns Search result list clicks with database items by moving
        // cursor accordingly, and passing the selected player's id to the Details Activity
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                mCursor.moveToPosition(position);
                intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(mHelper.COL_ID)));
                startActivityForResult(intent, 1);
            }
        });

        //OnItemClick listener aligns player object selected in RosterArray with Database file via
        // name (instead of id), and uses startActivity instead of startActivityForResult to
        // prevent re-adding a current player again
        mRosterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                intent.putExtra("Name", FantasyFootballRosterA.getInstance().getPlayerA(position).getmName());
                startActivity(intent);
            }
        });

        //Long clicking a player on the fantasy roster removes them from the arrayList
        mRosterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FantasyFootballRosterA.getInstance().removePlayerA(position);
                mFFBRosterArrayAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Player removed!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //CatchAll handleIntent statement
        handleIntent(getIntent());
    }

    // Creates a SearchView interface and associates searchable configuration with it
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
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
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

    // If user adds player to roster in the details activity, onActivityResult checks if player is
    // already accounted for and whether the position is already taken on the roster.  If neither
    // check returns true, a new player object is created and added to the roster. The method
    // concludes by resetting the search results.
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

                //roster tested to see if player OR position is already accounted for
                boolean dupTest = false;
                for (Player test : FantasyFootballRosterA.getInstance().getFullRosterA()){
                    if(test.getmName().equals(newPlayer.getmName())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this, "Player already on roster!", Toast.LENGTH_SHORT).show();
                    } else if (test.getmPosition().equals(newPlayer.getmPosition())){
                        dupTest = true;
                        Toast.makeText(MainActivity.this, "Position already taken!", Toast.LENGTH_SHORT).show();
                    }
                }
                if(!dupTest){
                    FantasyFootballRosterA.getInstance().addPlayerA(newPlayer);
                    mFFBRosterArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,"Long-click to remove player",Toast.LENGTH_SHORT).show();
                }

                //Search results are reset
                mCursor = mHelper.getPlayerList();
                mCursorAdapter.swapCursor(mCursor);
            }
        }
    }
}
