package com.example.Game24Hours;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class GameActivity extends QuizActivity {
    SharedPreferences prefs;
    HashMap<Integer, Question> questions;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        prefs = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
        loadQuestions();

        int currentQuestion = prefs.getInt(GAME_PREFERENCES_CURRENT_QUESTION, 1);

        final ImageSwitcher questionImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher_QuestionImage);
        questionImageSwitcher.setFactory(new MyImageSwitcherFactory());
        final TextSwitcher questionTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher_QuestionText);
        questionTextSwitcher.setFactory(new MyTextSwitcherFactory());

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        questionImageSwitcher.setInAnimation(in);
        questionImageSwitcher.setOutAnimation(out);
        questionTextSwitcher.setInAnimation(in);
        questionTextSwitcher.setOutAnimation(out);
        Button yesButton = (Button) findViewById(R.id.button_Yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAnswerAndShowNextQuestion(true);
            }
        });
        Button noButton = (Button) findViewById(R.id.button_No);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAnswerAndShowNextQuestion(false);
            }
        });
        if (questions.containsKey(currentQuestion)) {
            new GetImageFromNetwork().execute(currentQuestion);
            questionTextSwitcher.setCurrentText(questions.get(currentQuestion).text);
        }  else {
            handleNoQuestions();
        }
    }

    private void handleAnswerAndShowNextQuestion(boolean answer) {
        int curScore = prefs.getInt(GAME_PREFERENCES_SCORE, 0);
        int nextQuestionNumber = prefs.getInt(GAME_PREFERENCES_CURRENT_QUESTION, 1) + 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(GAME_PREFERENCES_CURRENT_QUESTION, nextQuestionNumber);
        if (answer) {
            editor.putInt(GAME_PREFERENCES_SCORE, curScore + 1);
        }
        editor.commit();
        if (!questions.containsKey(nextQuestionNumber)) {
            loadQuestions();
        }
        ;
        if (questions.containsKey(nextQuestionNumber)) {
            TextSwitcher questionTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher_QuestionText);
            questionTextSwitcher.setText(questions.get(nextQuestionNumber).text);
            new GetImageFromNetwork().execute(nextQuestionNumber);
        } else {
            handleNoQuestions();
        }
    }

    private void handleNoQuestions() {
        TextSwitcher questionTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher_QuestionText);
        questionTextSwitcher.setText(getResources().getText(R.string.no_questions));
        ImageSwitcher questionImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher_QuestionImage);
        questionImageSwitcher.setImageDrawable(getResources().getDrawable(R.drawable.noquestion));
        Button yesButton = (Button) findViewById(R.id.button_Yes);
        yesButton.setEnabled(false);
        Button noButton = (Button) findViewById(R.id.button_No);
        noButton.setEnabled(false);
    }


    private void loadQuestions() {
        questions = new HashMap<Integer, Question>(QUESTION_BATCH_SIZE);
        XmlResourceParser questionBatch = getResources().getXml(R.xml.samplequestions);

        int eventType = -1;
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String strName = questionBatch.getName();
                if (strName.equals("question")) {
                    Integer questionNumber = new Integer(questionBatch.getAttributeValue(null, XML_TAG_ATTRIBUTE_NUMBER));
                    String questionText = questionBatch.getAttributeValue(null, XML_TAG_ATTRIBUTE_TEXT);
                    String questionImageUrl = questionBatch.getAttributeValue(null, XML_TAG_ATTRIBUTE_IMAGEURL);
                    questions.put(questionNumber, new Question(questionNumber, questionText, questionImageUrl));
                }
            }
            try {
                eventType = questionBatch.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gameoptions, menu);
        menu.findItem(R.id.help_menu_item).setIntent(new Intent(this, HelpActivity.class));
        menu.findItem(R.id.settings_menu_item).setIntent(new Intent(this, SettingsActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_menu_item) {
            prefs = getSharedPreferences(GAME_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(GAME_PREFERENCES_CURRENT_QUESTION);
            editor.remove(GAME_PREFERENCES_SCORE);
            editor.commit();
            ImageSwitcher questionImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher_QuestionImage);
            new GetImageFromNetwork().execute(prefs.getInt(GAME_PREFERENCES_CURRENT_QUESTION, 1));
            final TextSwitcher questionTextSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher_QuestionText);
            questionTextSwitcher.setCurrentText(questions.get(prefs.getInt(GAME_PREFERENCES_CURRENT_QUESTION, 1)).text);
            Button yesButton = (Button) findViewById(R.id.button_Yes);
            Button noButton = (Button) findViewById(R.id.button_No);
            yesButton.setEnabled(true);
            noButton.setEnabled(true);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getQuestionImageUrl(int questionNumber) {
        return "http://www.perlgurl.org/Android/BeenThereDoneThat/Questions/q3.png";
    }

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
            //  textView.setShadowLayer(10, 5, 5, shadowColor); бажно отображается
            return textView;
        }
    }

    private class GetImageFromNetwork extends AsyncTask {
        @Override
        protected Object doInBackground(Object... objects) {
            Drawable image;
            URL imageUrl;
            //Log.i(DEBUG_TAG, "objects : " + (Integer)objects[0]);
            try {
                int questionNumber = 1;
                imageUrl = new URL(questions.get(objects[0]).imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
                image = new BitmapDrawable(bitmap);
            } catch (Exception e) {
                Log.e(DEBUG_TAG, "Decoding bitmap stream failed");
                e.printStackTrace();
                image = getResources().getDrawable(R.drawable.noquestion);
            }
            return image;
        }

        @Override
        public void onPostExecute(Object result) {
            ImageSwitcher questionImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher_QuestionImage);
            questionImageSwitcher.setImageDrawable((Drawable) result);
        }
    }

    private class Question {
        int number;
        String text;
        String imageUrl;

        public Question(int questionNumb, String questionText, String questionImageUrl) {
            number = questionNumb;
            text = questionText;
            imageUrl = questionImageUrl;
        }
    }
}