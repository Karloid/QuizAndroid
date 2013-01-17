package com.example.Game24Hours;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: krld
 * Date: 24.12.12
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class MenuActivity extends QuizActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ListView listView = (ListView) findViewById(R.id.listView_menu);
        String[] menu_items = {
                getResources().getString(R.string.menu_play),
                getResources().getString(R.string.menu_scores),
                getResources().getString(R.string.menu_settings),
                getResources().getString(R.string.menu_help),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menu_item, menu_items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View child, int position, long id) {
                // Toast.makeText(SuggestionActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                Log.i("game24: ", "On click listener menu");

                TextView textView = (TextView) child;
                String strText = textView.getText().toString();
                if (strText.equals(getResources().getString(R.string.menu_play))) {
                    startActivity(new Intent(MenuActivity.this, GameActivity.class));
                } else if ((strText.equals(getResources().getString(R.string.menu_scores)))) {
                    startActivity(new Intent(MenuActivity.this, ScoresActivity.class));
                } else if ((strText.equals(getResources().getString(R.string.menu_settings)))) {
                    startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
                }  else if ((strText.equals(getResources().getString(R.string.menu_help)))) {
                    startActivity(new Intent(MenuActivity.this, HelpActivity.class));
                }
            }
        });
/*
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView parentView, View childView, int position, long id)
            {
                TextView textView = (TextView) childView;
                String strText = textView.getText().toString();
                if (strText.equals(getResources().getString(R.string.menu_play))) {
                    startActivity(new Intent(MenuActivity.this, GameActivity.class));
                }
            }
            public void onNothingSelected(AdapterView parentView)
            {
            }
        });*/
       /* listView.setOnClickListener(new AdapterView.OnClickListener() {
      *//*      public void onClick(AdapterView<?> parent, View itemClicked, int position, long id)
            {
                TextView textView = (TextView) itemClicked;

            }*//*

            @Override
            public void onClick(View itemClicked) {

            }
        });*/

    }
}