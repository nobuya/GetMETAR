package com.example.nobuya.getmetar;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.os.AsyncTask;

public class MainActivity extends Activity {

    private EditText editTextCCCC;
    private String resultMessageBuffer;
    private Boolean isFirst = true;
    private static int MESSAGE_HISTORY_MAX = 5;
    private int numMessages = 0;
    private String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
    private int head = 0;
    private int tail = 0;


    public void addResultMessage(String msg) {
        String newMessage;
        int curr = head;
        resultMessage[head++] = msg;
        head %= MESSAGE_HISTORY_MAX;
        numMessages++;
        if (numMessages == 1) {
            newMessage = msg + "\n---\n";
        } else if (numMessages <= MESSAGE_HISTORY_MAX) {
            newMessage = msg + "\n---\n" + resultMessageBuffer;
        } else { // if (numMessages > MESSAGE_HISTORY_MAX)
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = curr; i >= 0; i--)
                stringBuffer.append(resultMessage[i] + "\n---\n");
            for (int i = MESSAGE_HISTORY_MAX - 1; i > curr; i--)
                stringBuffer.append(resultMessage[i] + "\n---\n");
            newMessage = stringBuffer.toString();
        }
        resultMessageBuffer = newMessage;
        updateResultMessage();
    }

    private void updateResultMessage() {
        TextView textView = (TextView) this.findViewById(R.id.result_message);
        textView.setText(resultMessageBuffer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // enable scrolling result message area
        TextView textView = (TextView)findViewById(R.id.result_message);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextCCCC = (EditText)findViewById(R.id.editTextCCCC);
        resultMessageBuffer = "(message)";
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_get: // GET Button
                getMetar();
                break;
        }
    }

    private void getMetar() {
        String cccc = editTextCCCC.getText().toString();
        AsyncGetMetar asyncGetMetar = new AsyncGetMetar(this);
        asyncGetMetar.execute(cccc);
        String msg = "Getting " + cccc + "...";
        Toast.makeText(MainActivity.this,
                       msg,
                       Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
