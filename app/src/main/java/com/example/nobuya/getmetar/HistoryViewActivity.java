package com.example.nobuya.getmetar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/09/02.
 */
public class HistoryViewActivity extends Activity {
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        int maxHistorySize = 20;
        MessageHistory.init(this);
        String[] members = MessageHistory.getHistoryArray(maxHistorySize);

        listView = (ListView) findViewById(R.id.history_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, members);
        listView.setAdapter(adapter);

        // when selected item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                String item = (String)lv.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item + " clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                break;
        }
    }
}
