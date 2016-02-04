package com.kwon.mike.pr2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView mTitle;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;
    ListView mRosterListView;
    ArrayList<Player> mFFBRosterArrayList;
    ArrayAdapter<Player> mFFBRosterArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView)findViewById(R.id.xmlTitle);
        mRosterListView = (ListView)findViewById(R.id.xmlFantasyRosterListView);
        mFFBRosterArrayList = new ArrayList<>();
        mFFBRosterArrayAdapter = new CustomArrayAdapter(MainActivity.this,mFFBRosterArrayList);
        mRosterListView.setAdapter(mFFBRosterArrayAdapter);

        handleIntent(getIntent());

        //Instantiate the singleton database helper with a getInstance method instead of the constructor
        mHelper = DBSQLiteOpenHelper.getInstance(MainActivity.this);

        //OnItemClick listener aligns listview clicks with database items by moving cursor accordingly, and passing the selected player's id
        mRosterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ResultDetailActivity.class);
                mCursor.moveToPosition(position);
                intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(mHelper.COL_ID)));
                startActivity(intent);
            }
        });
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
            cursor.moveToFirst();
            Player newPlayer = new Player();
            newPlayer.setmName(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
            newPlayer.setmTeam(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
            newPlayer.setmPosition(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
            newPlayer.setmBio(cursor.getString(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
            newPlayer.setmImage(cursor.getInt(cursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));
            mFFBRosterArrayList.add(newPlayer);
            mFFBRosterArrayAdapter.notifyDataSetChanged();
        }
    }
}
