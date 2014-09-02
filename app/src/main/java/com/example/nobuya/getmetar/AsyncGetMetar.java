package com.example.nobuya.getmetar;

import android.os.AsyncTask;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/08/28.
 */
public class AsyncGetMetar extends AsyncTask<String, Void, String> {
    private GetMetarActivity activity;

    public AsyncGetMetar(GetMetarActivity _activity) {
        this.activity = _activity;
    }

    @Override
    protected String doInBackground(String... args) {
        String cccc = args[0];
        String metarMsg = GetMetar.getMetarMessage(cccc);
        return metarMsg;
    }

    @Override
    protected void onPostExecute(String result) { // executed by Main Thread
        String msg = "RESULT: " + result;
        Toast.makeText(this.activity, msg, Toast.LENGTH_LONG).show();
//        TextView textView = (TextView)activity.findViewById(R.id
//                .result_message);
//        textView.setText(result);
        activity.setResultMessage(result);
    }

}
