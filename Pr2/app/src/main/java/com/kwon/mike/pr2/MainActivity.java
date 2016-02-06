package com.kwon.mike.pr2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mTitle, mRosterTitle, mSearchTitle;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;
    ListView mRosterListView, mSearchListView;
    ArrayList<Player> mFFBRosterArrayList;
    ArrayAdapter<Player> mFFBRosterArrayAdapter;
    CursorAdapter mCursorAdapter;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView)findViewById(R.id.xmlTitle);
        mRosterTitle = (TextView)findViewById(R.id.xmlRosterTitle);
        mSearchTitle = (TextView)findViewById(R.id.xmlSearchTitle);
        mRosterListView = (ListView)findViewById(R.id.xmlFantasyRosterListView);
        mSearchListView = (ListView)findViewById(R.id.xmlSearchListView);

        //Create an ArrayList showing all the players selected by the user for their fantasy roster
        mFFBRosterArrayList = new ArrayList<>();
        mFFBRosterArrayAdapter = new CustomArrayAdapter(MainActivity.this,mFFBRosterArrayList);
        mRosterListView.setAdapter(mFFBRosterArrayAdapter);

        //Shared Preferences instantiated to save fantasy football roster
        mPrefs = getSharedPreferences("prefs",MODE_PRIVATE);

        //Instantiate the singleton database helper with a getInstance method instead of the constructor
        mHelper = DBSQLiteOpenHelper.getInstance(MainActivity.this);

        //Using the database connection from the helper, setup a cursor adapter to show search results
        mCursor = mHelper.getPlayerList();
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

        //OnItemClick listener aligns Roster clicks with database items by moving cursor accordingly, and passing the selected player's id
        mRosterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                mCursor.moveToPosition(position);
                intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(mHelper.COL_ID)));
                startActivity(intent);
            }
        });

        //OnItemClick listener aligns SearchList clicks with database items by moving cursor accordingly, and passing the selected player's id
        mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                mCursor.moveToPosition(position);
                intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(mHelper.COL_ID)));
                startActivityForResult(intent, 1);
            }
        });

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    //onNewIntent method overridden to execute database search - if the launched intent has an "Action_Search" filter
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cursor = mHelper.searchPlayerByName(query);
            mCursorAdapter.swapCursor(cursor);
        } else {
            mCursor = mHelper.getPlayerList();
            mCursorAdapter.swapCursor(mCursor);
        }
    }

    //If the user clicks adds the player to their roster in the details activity, a Player object is added to their roster and the search results are reset
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int id = data.getIntExtra("id",-1);
                Cursor cursor = mHelper.searchPlayerByid(id);
                cursor.moveToFirst();
                Player newPlayer = new Player();
                newPlayer.setmName(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
                newPlayer.setmTeam(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
                newPlayer.setmPosition(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
                newPlayer.setmBio(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
                newPlayer.setmImage(cursor.getInt(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));
                mFFBRosterArrayList.add(newPlayer);
                mFFBRosterArrayAdapter.notifyDataSetChanged();
                mCursor = mHelper.getPlayerList();
                mCursorAdapter.swapCursor(mCursor);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPrefs.edit();

        //Need code to save fantasyRoster after a search is conducted

    }
}
