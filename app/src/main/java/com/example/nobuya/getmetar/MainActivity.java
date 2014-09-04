package com.example.nobuya.getmetar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
// import android.os.AsyncTask;

public class MainActivity extends GetMetarActivity {
/*
    private EditText editTextCCCC;
    private String resultMessageBuffer;
    private static int MESSAGE_HISTORY_MAX = 5;
    private int numMessages = 0;
    private String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
    private int head = 0; */

    //protected EditText editTextCCCC;
    private String resultMessageBuffer;
    private final static int MESSAGE_HISTORY_MAX = 5;
    private int numMessages = 0;
    private String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
    private int head = 0;

    private String prevDateAndTime = "000000Z"; // dummy
    private String prevCCCC = "____"; // dummy
    private boolean needUpdate = false;
//    public boolean isNeedUpdate() {
//        return needUpdate;
//    }


    @Override
    public void setResultMessage(String msg) {
//        super.setResultMessage(msg);
        METARMessage newMsg = new METARMessage(msg);
        MessageHistory.init(this);
        MessageHistory.add(newMsg);
        String dateAndTime = newMsg.getDateAndTime();
        String cccc = newMsg.getICAOCode();
        if (prevDateAndTime.equals(dateAndTime) && prevCCCC.equals(cccc)) {
            Toast.makeText(MainActivity.this, "No update message", Toast.LENGTH_LONG).show();
            needUpdate = false;
        } else {
            needUpdate = true;
            setResultMessage1(msg);
        }
        prevDateAndTime = dateAndTime;
        prevCCCC = cccc;
    }

    private void setResultMessage1(String msg) {
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
            for (int i = curr; i >= 0; i--) {
                stringBuffer.append(resultMessage[i]);
                stringBuffer.append("\n---\n");
            }
            for (int i = MESSAGE_HISTORY_MAX - 1; i > curr; i--) {
                stringBuffer.append(resultMessage[i]);
                stringBuffer.append("\n---\n");
            }
            newMessage = stringBuffer.toString();
        }
        resultMessageBuffer = newMessage;
        updateResultMessage();
    }

    private void updateResultMessage() {
        TextView textView = (TextView) this.findViewById(R.id.result_message);
        textView.setText(resultMessageBuffer);
    }

    /*
    public void setResultMessage(String msg) {
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
            for (int i = curr; i >= 0; i--) {
                stringBuffer.append(resultMessage[i]);
                stringBuffer.append("\n---\n");
            }
            for (int i = MESSAGE_HISTORY_MAX - 1; i > curr; i--) {
                stringBuffer.append(resultMessage[i]);
                stringBuffer.append("\n---\n");
            }
            newMessage = stringBuffer.toString();
        }
        resultMessageBuffer = newMessage;
        updateResultMessage();
    }
*/

    /*
    private void updateResultMessage() {
        TextView textView = (TextView) this.findViewById(R.id.result_message);
        textView.setText(resultMessageBuffer);
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Settings.init(this);
        String defaultCCCC = Settings.getDefaultCCCC();
        // enable scrolling result message area
        TextView textView = (TextView)findViewById(R.id.result_message);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextCCCC = (EditText)findViewById(R.id.editTextCCCC);
        editTextCCCC.setHint(defaultCCCC);
        editTextCCCC.setText(defaultCCCC);
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
            Toast.makeText(MainActivity.this, "Settings...",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(MainActivity.this, "About...",
                    Toast.LENGTH_SHORT).show();
            handleAboutMenu();
            return true;
        } else if (id == R.id.action_debug) {
            if (Settings.getDebugMode()) {
                Toast.makeText(MainActivity.this, "Debug mode ...",
                        Toast.LENGTH_SHORT).show();
                //handleDebugMenu();
            }
            return true;
        } else if (id == R.id.action_develop) {
            if (Settings.getDevelopMode()) {
                Toast.makeText(MainActivity.this, "Develop mode ...",
                        Toast.LENGTH_SHORT).show();
                handleDevelopMenu();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private PopupWindow popupWindow;

    private void handleAboutMenu() {
        popupWindow = new PopupWindow(MainActivity.this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing())
                    popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(popupView);
        // background
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // size (width: 200dp)
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        popupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth((int) width);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // centering
        popupWindow.showAtLocation(findViewById(R.id.metar_at),
                Gravity.CENTER, 0, 0);
    }

    private void handleDevelopMenu() {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onDestroy();
    }
}
