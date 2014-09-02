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
    protected String resultMessageBuffer;
    protected static int MESSAGE_HISTORY_MAX = 5;
    protected int numMessages = 0;
    protected String resultMessage[] = new String[MESSAGE_HISTORY_MAX];
    protected int head = 0;

    private String prevDateAndTime = "000000Z"; // dummy
    private String prevCCCC = "____"; // dummy
    private boolean needUpdate = false;
    public boolean isNeedUpdate() {
        return needUpdate;
    }

    //abstract void setResultMessage(String msg);
    public void setResultMessage(String msg) {
        String dateAndTime = GetMetar.getDateAndTime(msg);
        String cccc = GetMetar.getICAOCode(msg);
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
//        String windStr = GetMetar.getWind(msg);
//        if (!windStr.equals("???")) {
//            updateGraphics(windStr);
//        }
    }

    private void updateResultMessage() {
        TextView textView = (TextView) this.findViewById(R.id.result_message);
        textView.setText(resultMessageBuffer);
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
