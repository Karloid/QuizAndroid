package com.example.Game24Hours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created with IntelliJ IDEA.
 * User: krld
 * Date: 24.12.12
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class GameActivity extends QuizActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gameoptions, menu);
        menu.findItem(R.id.help_menu_item).setIntent(
                new Intent(this, HelpActivity.class));
        menu.findItem(R.id.settings_menu_item).setIntent(
                new Intent(this, SettingsActivity.class));
        return true;
    }

/*    @Override
    public boolean onOptionsSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        startActivity(item.getIntent());
        return true;
    }*/

}