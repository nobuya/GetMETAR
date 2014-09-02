package com.example.nobuya.getmetar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by nobuya on 2014/09/02.
 */
public class HistoryViewActivity extends Activity {
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // String[]  members = {"AAA", "BBBB", "CCCC" };
        int maxHistorySize = 10;
        String []  members = MessageHistory.getHistoryArray(maxHistorySize);

        listView = (ListView) findViewById(R.id.history_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, members);
        listView.setAdapter(adapter);
    }
}
