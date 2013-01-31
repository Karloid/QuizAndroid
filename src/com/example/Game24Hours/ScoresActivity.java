package com.example.Game24Hours;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: krld
 * Date: 24.12.12
 * Time: 22:21
 * To change this template use File | Settings | File Templates.
 */
public class ScoresActivity extends QuizActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();
        TabHost.TabSpec allScoresTab = host.newTabSpec("AllTab");
        allScoresTab.setIndicator(getResources().getString(R.string.all_scores),
                getResources().getDrawable(android.R.drawable.star_on));
        allScoresTab.setContent(R.id.tab1);
        host.addTab(allScoresTab);
        TabHost.TabSpec friendScore = host.newTabSpec("FriendTab");
        friendScore.setIndicator(getResources().getString(R.string.friend_scores),
                getResources().getDrawable(android.R.drawable.star_on));
        friendScore.setContent(R.id.tab2);
        host.addTab(friendScore);
        host.setCurrentTabByTag("AllTab");

        try {
            XmlResourceParser scores = getResources().getXml(R.xml.allscores);
            TableLayout scoreTable = (TableLayout)findViewById(R.id.tableLayout_allScores);
            insertScoreRow(scoreTable, "Score", "Rank", "Username");
            parseScores(scores, scoreTable);
            scores = getResources().getXml(R.xml.friendscores);
            scoreTable = (TableLayout)findViewById(R.id.tableLayout_friendScores);
            insertScoreRow(scoreTable, "Score", "Rank", "Username");
            parseScores(scores, scoreTable);
        } catch (Exception e) {
            Log.i("game24:", "Exception! scores");
            e.printStackTrace();
        }
    }

    private void parseScores(XmlResourceParser scores, TableLayout scoreTable) throws IOException, XmlPullParserException {
        int eventType = -1;
        boolean bFoundScores = false;
        while (eventType != XmlResourceParser.END_DOCUMENT){
            if (eventType == XmlResourceParser.START_TAG){
                String strName = scores.getName();
                if (strName.equals("score")) {
                    bFoundScores = true;
                    String scoreValue = scores.getAttributeValue(null, "score");
                    String scoreRank = scores.getAttributeValue(null, "rank");
                    String scoreUserName = scores.getAttributeValue(null, "username");
                    insertScoreRow(scoreTable, scoreValue, scoreRank, scoreUserName);

                }
            }    eventType = scores.next();
        }
    }

    private void insertScoreRow(TableLayout scoreTable, String scoreValue, String scoreRank, String scoreUserName) {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TextView userName = new TextView(this);
        userName.setText(scoreUserName);

        TextView value = new TextView(this);
        value.setText(scoreValue);

        TextView rank = new TextView(this);
        rank.setText(scoreRank);

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(tableParams);
        tableRow.addView(userName);
        tableRow.addView(value);
        tableRow.addView(rank);
        scoreTable.addView(tableRow);
      /* */
    }
}