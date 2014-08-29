package com.example.nobuya.getmetar;

import android.app.Activity;

/**
 * Created by nobuya on 2014/08/29.
 */
public abstract class GetMetarActivity extends Activity {
    abstract void setResultMessage(String msg);
    abstract int getWindVelocity();
    abstract int getWindDirection();
}
