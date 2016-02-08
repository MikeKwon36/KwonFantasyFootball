package com.kwon.mike.pr2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDetailActivity extends AppCompatActivity {

    TextView mName, mPosition, mTeam, mBio;
    ImageView mImage;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;
    Button mCheckRosterButton;
    String[] mCurrentRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        mName = (TextView) findViewById(R.id.xmlDetailName);
        mPosition = (TextView) findViewById(R.id.xmlDetailPosition);
        mTeam = (TextView) findViewById(R.id.xmlDetailTeam);
        mBio = (TextView) findViewById(R.id.xmlDetailBio);
        mImage = (ImageView) findViewById(R.id.xmlDetailImage);
        mCheckRosterButton = (Button) findViewById(R.id.xmlCheckRosterButton);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mHelper = DBSQLiteOpenHelper.getInstance(ResultDetailActivity.this);

        //transitory array to show the current list of players on your fantasy roster
        mCurrentRoster = new String[FantasyFootballRosterA.getInstance().getFullRosterA().size()];
        for (int i = 0; i < FantasyFootballRosterA.getInstance().getFullRosterA().size(); i++) {
            mCurrentRoster[i] = FantasyFootballRosterA.getInstance().getFullRosterA().get(i).getmName();
        }

        //Detail Screen populated based on either player ID or player name received
        final int databaseID = getIntent().getIntExtra("id", -1);
        final String databaseName = getIntent().getStringExtra("Name");
        if(databaseID>=0){
            mCursor = mHelper.searchPlayerByid(databaseID);
            mCursor.moveToFirst();
            mName.setText("Name: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
            mPosition.setText("Position: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
            mTeam.setText("Team: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
            mBio.setText("Bio: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
            mImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));
        } else {
            mCursor = mHelper.searchPlayerByName(databaseName);
            mCursor.moveToFirst();
            mName.setText("Name: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
            mPosition.setText("Position: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
            mTeam.setText("Team: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
            mBio.setText("Bio: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
            mImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));
        }

        //Floating Action Button sends database ID to MainActivity to add player to roster
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultDetailActivity.this,MainActivity.class);
                intent.putExtra("id", databaseID);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        //Button to display current fantasy roster, with an option to get additional detail on the player
        mCheckRosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultDetailActivity.this);
                builder.setTitle(getResources().getString(R.string.alertDialogTitle));
                builder.setItems(mCurrentRoster, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.alertDialogSearchButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String googleSearch = getResources().getString(R.string.googleSearchString);
                        Uri uri = Uri.parse(googleSearch + " " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
                        Intent search = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(search);
                    }
                });
                builder.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}