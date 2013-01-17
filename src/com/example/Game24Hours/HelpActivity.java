package com.example.Game24Hours;

import android.os.Bundle;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: krld
 * Date: 24.12.12
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class HelpActivity extends QuizActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        InputStream iFile = getResources().openRawResource(R.raw.game24);
        TextView helpText = (TextView) findViewById(R.id.helpText);
        String strFile = inputStreamToString(iFile);
        helpText.setText(strFile);
    }

    private String inputStreamToString(InputStream iFile) {
        Scanner scanner = new Scanner(iFile).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}