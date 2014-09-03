package com.example.nobuya.getmetar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/08/29.
 */
public class SecondActivity extends GetMetarActivity {
/*
    private EditText editTextCCCC;
    private String resultMessageBuffer;
    private static int MESSAGE_HISTORY_MAX = 5;
    private int numMessages = 0;
    private String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
    private int head = 0;
*/
    private String resultMessage2;

    @Override
    public void setResultMessage(String msg) {
        String prevMessage = resultMessage;
        super.setResultMessage(msg);
        if (isNeedUpdate()) {
            resultMessage2 = prevMessage;
            updateResultMessage2();
            String windStr = GetMetar.getWind(msg);
            String tempStr = GetMetar.getTemperatureAndDewpoint(msg);
            String qnhStr = GetMetar.getQNH(msg);
            Toast.makeText(SecondActivity.this, "temp: " + tempStr,
                    Toast.LENGTH_SHORT).show();
            if (!windStr.equals("???") && !tempStr.equals("??/??")) {
                updateGraphics(windStr, tempStr, qnhStr);
            }
        }
    }

    private void updateResultMessage2() {
        TextView textView = (TextView) this.findViewById(R.id.result_message2);
        textView.setText(resultMessage2);
    }

    // 07011KT, 21/18, Q1020
    private void updateGraphics(String windStr, String tempStr, String qnhStr) {
        // 01234567   01234567
        // 07011KT    VRB03KT
        int windVelocity = ((windStr.charAt(3) == '0') ?
                (windStr.charAt(4) - '0') :
                (windStr.charAt(3) - '0') * 10 + (windStr.charAt(4) - '0'));
        setWindVelocity(windVelocity);
        int windDirection = ((windStr.charAt(0) == 'V') ? 999 :
                (windStr.charAt(0) - '0') * 100 +
                        (windStr.charAt(1) - '0') * 10 +
                        (windStr.charAt(2) - '0'));
        setWindDirection(windDirection);

        // 0123456789012345
        // 08011KT 110V140
        int windDirectionV1;
        int windDirectionV2;
        if (windStr.length() > 14 && windStr.charAt(11) == 'V') {
            // Variable wind direction
            windDirectionV1 = (windStr.charAt(8) - '0') * 100 +
                    (windStr.charAt(9) - '0') * 10 +
                    (windStr.charAt(10) - '0');
            windDirectionV2 = (windStr.charAt(12) - '0') * 100 +
                    (windStr.charAt(13) - '0') * 10 +
                    (windStr.charAt(14) - '0');
        } else {
            windDirectionV1 = 0;
            windDirectionV2 = 0;
        }
        setWindDirectionV1(windDirectionV1);
        setWindDirectionV2(windDirectionV2);

        // temperature and dewpoint
        if (tempStr.length() >= 5) {
            int temperature = 0;
            int dewpoint = 0;
            int p = 0;
            // temperature
            if (tempStr.charAt(0) == 'M' && tempStr.charAt(3) == '/') { // M02/M02
                temperature = -((tempStr.charAt(1) - '0') * 10 + (tempStr.charAt(2) - '0'));
                p = 3;
            } else if (tempStr.charAt(2) == '/') {
                temperature = ((tempStr.charAt(0) - '0') * 10 + (tempStr.charAt(1) - '0'));
                p = 2;
            } else {
                temperature = 0;
            }
            // dewpoint
            if (tempStr.charAt(p + 1) == 'M') { // M02/M02
                dewpoint = -((tempStr.charAt(p + 2) - '0') * 10 + (tempStr.charAt(p + 3) - '0'));
            } else if (tempStr.charAt(p) == '/') {
                dewpoint = ((tempStr.charAt(p + 1) - '0') * 10 + (tempStr.charAt(p + 2) - '0'));
            } else {
                dewpoint = 0;
            }
            setTemperature(temperature);
            setDewpoint(dewpoint);
        }

        // altimeter setting (QNH)
        if (qnhStr.length() >= 5) { // Q1020
            int qnh = 1000;
            if (qnhStr.charAt(0) == 'Q') {
                qnh = (((qnhStr.charAt(1) - '0') * 1000) +
                        ((qnhStr.charAt(2) - '0') * 100) +
                        ((qnhStr.charAt(3) - '0') * 10) +
                        (qnhStr.charAt(4) - '0'));
            }
            setQNH(qnh);
        }

        findViewById(R.id.graphics_view).invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // enable scrolling result message area
        TextView textView = (TextView)findViewById(R.id.result_message);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView textView2 = (TextView)findViewById(R.id.result_message2);
        textView2.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextCCCC = (EditText)findViewById(R.id.editTextCCCC);
//        resultMessageBuffer = "(message)";
        resultMessage = "(message)";
        resultMessage2 = "(previous message)";
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
        String airportText = AirportDB.getAirportText(cccc);
        Intent intent = getIntent();
        TextView textView = (TextView)findViewById(R.id.airport_text);
        textView.setText(airportText);
        //
        AsyncGetMetar asyncGetMetar = new AsyncGetMetar(this);
        asyncGetMetar.execute(cccc);
        String msg = "Getting " + cccc + "...";
        Toast.makeText(SecondActivity.this,
                msg,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
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
        } else if (id == R.id.action_about) {
            Toast.makeText(SecondActivity.this, "About...",
                    Toast.LENGTH_SHORT).show();
            handleAboutMenu();
            return true;
        } else if (id == R.id.action_history) {
            Toast.makeText(SecondActivity.this, "History view ...",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SecondActivity.this, HistoryViewActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_develop) {
            Toast.makeText(SecondActivity.this, "Exit develop mode...",
                    Toast.LENGTH_SHORT).show();
            handleDevelopMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private PopupWindow popupWindow;

    private void handleAboutMenu() {
        popupWindow = new PopupWindow(SecondActivity.this);
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
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
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
