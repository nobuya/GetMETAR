package com.example.nobuya.getmetar;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/08/29.
 */
public abstract class GetMetarActivity extends Activity {
    protected EditText editTextCCCC;
//    protected String resultMessageBuffer;
//    protected static int MESSAGE_HISTORY_MAX = 5;
//    protected int numMessages = 0;
//    protected String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
//    protected int head = 0;
    protected String resultMessage = "(message)";

    private String prevDateAndTime = "000000Z"; // dummy
    private String prevCCCC = "____"; // dummy
    private boolean needUpdate = false;
    public boolean isNeedUpdate() {
        return needUpdate;
    }

    //abstract void setResultMessage(String msg);
    public void setResultMessage(String msg) {
        METARMessage newMsg = new METARMessage(msg);
        MessageHistory.init(this);
        MessageHistory.add(newMsg);
        String dateAndTime = newMsg.getDateAndTime();
        String cccc = newMsg.getICAOCode();
        if (prevDateAndTime.equals(dateAndTime) && prevCCCC.equals(cccc)) {
            Toast.makeText(GetMetarActivity.this, "No update message", Toast.LENGTH_LONG).show();
            needUpdate = false;
        } else {
            needUpdate = true;
            setResultMessage1(msg);
        }
        prevDateAndTime = dateAndTime;
        prevCCCC = cccc;
    }

    private void setResultMessage1(String msg) {
        resultMessage = msg + "\n---\n";
        updateResultMessage();
    }

    private void updateResultMessage() {
        TextView textView = (TextView) this.findViewById(R.id.result_message);
        textView.setText(resultMessage);
    }

    private int windVelocity;
    public int getWindVelocity() {
        return windVelocity;
    }
    public void setWindVelocity(int wv) {
        windVelocity = wv;
    }

    private int windDirection;
    public int getWindDirection() {
        return windDirection;
    }
    public void setWindDirection(int wd) {
        windDirection = wd;
    }

    private int windDirectionV1;
    public int getWindDirectionV1() {
        return windDirectionV1;
    }
    public void setWindDirectionV1(int wd) {
        windDirectionV1 = wd;
    }

    private int windDirectionV2;
    public int getWindDirectionV2() {
        return windDirectionV2;
    }
    public void setWindDirectionV2(int wd) {
        windDirectionV2 = wd;
    }

    private int temperature;
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temp) {
        temperature = temp;
    }

    private int dewpoint;
    public int getDewpoint() {
        return dewpoint;
    }
    public void setDewpoint(int dp) {
        dewpoint = dp;
    }

    private int altimeterSetting = 1013; // QNH
    public int getQNH() {
        return altimeterSetting;
    }
    public void setQNH(int qnh) {
        altimeterSetting = qnh;
    }


//    abstract int getWindVelocity();
//    abstract int getWindDirection();
//    abstract int getWindDirectionV1();
//    abstract int getWindDirectionV2();
    boolean isVariableWind() {
//        return getWindDirectionV1() != getWindDirectionV2();
        return windDirectionV1 != windDirectionV2;
    }
}
