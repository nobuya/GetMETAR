package com.example.nobuya.getmetar;

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

    @Override
    public void setResultMessage(String msg) {
        super.setResultMessage(msg);
        String windStr = GetMetar.getWind(msg);
        if (!windStr.equals("???")) {
            updateGraphics(windStr);
        }
    }

    private void updateGraphics(String windStr) { // 07011KT
        int windVelocity = ((windStr.charAt(3) == '0') ?
                (windStr.charAt(4) - '0') :
                (windStr.charAt(3) - '0') * 10 + (windStr.charAt(4) - '0'));
        setWindVelocity(windVelocity);
        int windDirection =
                (windStr.charAt(0) - '0') * 100 +
                        (windStr.charAt(1) - '0') * 10 +
                        (windStr.charAt(2) - '0');
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
        findViewById(R.id.graphics_view).invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

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
        Toast.makeText(SecondActivity.this,
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
    /*
        } else if (id == R.id.action_about) {
            Toast.makeText(SecondActivity.this, "About...",
                    Toast.LENGTH_LONG).show();
            handleAboutMenu();
            return true;
    */
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

    @Override
    protected void onDestroy() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        super.onDestroy();
    }
}
