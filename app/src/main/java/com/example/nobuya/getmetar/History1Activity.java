package com.example.nobuya.getmetar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/09/05.
 */
public class History1Activity extends Activity {
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history1);

        int maxHistorySize = 20;
        MessageHistory.init(this);
        String[] members = MessageHistory.getAirportHistoryArray
                (maxHistorySize);

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
                String cccc = MessageHistory.getCCCC(position);
                Toast.makeText(getApplicationContext(), cccc + " clicked",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(History1Activity.this,
                        METARActivity1.class);
                intent.putExtra("cccc", cccc);
                startActivity(intent);
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
