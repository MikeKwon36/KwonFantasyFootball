package com.kwon.mike.pr2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDetailActivity extends AppCompatActivity {

    TextView mName, mPosition, mTeam, mBio;
    ImageView mImage;
    DBSQLiteOpenHelper mHelper;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        mName = (TextView) findViewById(R.id.xmlDetailName);
        mPosition = (TextView) findViewById(R.id.xmlDetailPosition);
        mTeam = (TextView) findViewById(R.id.xmlDetailTeam);
        mBio = (TextView) findViewById(R.id.xmlDetailBio);
        mImage = (ImageView) findViewById(R.id.xmlDetailImage);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mHelper = DBSQLiteOpenHelper.getInstance(ResultDetailActivity.this);

        final int databaseID = getIntent().getIntExtra("id", -1);

        if(databaseID>=0){
            mCursor = mHelper.searchPlayerByid(databaseID);
            mCursor.moveToFirst();
            mName.setText("Name: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_NAME)));
            mPosition.setText("Position: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_POSITION)));
            mTeam.setText("Team: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_TEAM)));
            mBio.setText("Bio: " + mCursor.getString(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_BIO)));
            mImage.setImageResource(mCursor.getInt(mCursor.getColumnIndex(DBSQLiteOpenHelper.COL_IMAGE)));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultDetailActivity.this,MainActivity.class);
                intent.putExtra("id", databaseID);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

}
