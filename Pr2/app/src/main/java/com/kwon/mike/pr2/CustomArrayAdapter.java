package com.kwon.mike.pr2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Todo on 2/4/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<Player> {

    ArrayList<Player> mListOfPlayers;

    public CustomArrayAdapter(Context context, ArrayList<Player> array) {
        super(context, -1);
        mListOfPlayers = array;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Player currentPlayer = mListOfPlayers.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.roster_entry,parent,false);

        TextView name = (TextView)view.findViewById(R.id.xmlPlayerName);
        TextView pos = (TextView)view.findViewById(R.id.xmlPlayerPos);
        TextView team = (TextView)view.findViewById(R.id.xmlPlayerTeam);
        name.setText(currentPlayer.getmName());
        pos.setText(currentPlayer.getmPosition());
        team.setText(currentPlayer.getmTeam());

        return view;
    }

    @Override
    public int getCount() {
        return mListOfPlayers.size();
    }
}
