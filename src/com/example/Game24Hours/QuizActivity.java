package com.example.Game24Hours;
     /*Git test idea */
import android.app.Activity;

public class QuizActivity extends Activity {
    public static final String GAME_PREFERENCES = "GamePrefs";
    public static final String GAME_PREFERENCES_NICKNAME = "Nickname";
    public static final String GAME_PREFERENCES_EMAIL = "Email";
    public static final String GAME_PREFERENCES_PASSWORD = "Password";
    public static final String GAME_PREFERENCES_DOB = "DOB";
    public static final String GAME_PREFERENCES_GENDER = "Gender";
    public static final String GAME_PREFERENCES_SCORE = "Score";
    public static final String GAME_PREFERENCES_CURRENT_QUESTION = "CurQuestion";
    public static final String DEBUG_TAG = "GAME24DEBUG";

    public static final String XML_TAG_QUESTION_BLOCK = "questions";
    public static final String XML_TAG_QUESTION = "question";
    public static final String XML_TAG_ATTRIBUTE_NUMBER = "number";
    public static final String XML_TAG_ATTRIBUTE_TEXT = "text";
    public static final String XML_TAG_ATTRIBUTE_IMAGEURL = "imageUrl";
    public static final int QUESTION_BATCH_SIZE = 15;
}
