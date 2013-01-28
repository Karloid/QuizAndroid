package com.example.Game24Hours;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.*;

import java.net.URL;

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
        final ImageSwitcher questionImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher_QuestionImage);
        questionImageSwitcher.setFactory(new MyImageSwitcherFactory());
        questionImageSwitcher.setImageDrawable(getResources().getDrawable(R.drawable.cat_bw));
        final TextSwitcher questionTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher_QuestionText);
        questionTextSwitcher.setFactory(new MyTextSwitcherFactory());
        questionTextSwitcher.setCurrentText("First Text Switcher String");
        Button yesButton = (Button) findViewById(R.id.button_Yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextSwitcher.setText("Clicked on Yes Button");
                new GetImageFromNetwork() {
                    @Override
                    public void onPostExecute(Object result) {
                        questionImageSwitcher.setImageDrawable((Drawable) result);
                    }
                }.execute();
               // questionImageSwitcher.setImageDrawable(getQuestionImageDrawable(0));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gameoptions, menu);
        menu.findItem(R.id.help_menu_item).setIntent(new Intent(this, HelpActivity.class));
        menu.findItem(R.id.settings_menu_item).setIntent(new Intent(this, SettingsActivity.class));
        return true;
    }

    private Drawable getQuestionImageDrawable(int questionNumber) {
        Drawable image;
        URL imageUrl;
        try {
            imageUrl = new URL(getQuestionImageUrl(questionNumber));
            Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
            image = new BitmapDrawable(bitmap);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Decoding bitmap stream failed");
            e.printStackTrace();
            image = getResources().getDrawable(R.drawable.noquestion);
        }
        return image;
    }

    private String getQuestionImageUrl(int questionNumber) {
        return "http://www.perlgurl.org/Android/BeenThereDoneThat/Questions/q3.png";
    }

    /*    @Override
        public boolean onOptionsSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
            startActivity(item.getIntent());
            return true;
        }*/
    public class MyImageSwitcherFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            ImageView imageView = new ImageView(GameActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
            return imageView;
        }
    }

    public class MyTextSwitcherFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            TextView textView = new TextView(GameActivity.this);
            textView.setGravity(Gravity.CENTER);
            Resources res = getResources();
            float dimension = res.getDimension(R.dimen.game_question_size);
            int titleColor = res.getColor(R.color.title_color);
            int shadowColor = res.getColor(R.color.title_glow);
            textView.setTextSize(dimension);
            textView.setTextColor((int) titleColor);
            textView.setShadowLayer(10, 5, 5, shadowColor);
            return textView;
        }
    }

    private class GetImageFromNetwork extends AsyncTask {
        @Override
        protected Object doInBackground(Object... objects) {
            Drawable image;
            URL imageUrl;
            try {
                int questionNumber = 1;
                imageUrl = new URL(getQuestionImageUrl(questionNumber));
                Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
                image = new BitmapDrawable(bitmap);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Decoding bitmap stream failed");
                e.printStackTrace();
                image = getResources().getDrawable(R.drawable.noquestion);
            }
            return image;
        }
    }
}