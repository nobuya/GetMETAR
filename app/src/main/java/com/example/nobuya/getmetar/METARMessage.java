package com.example.nobuya.getmetar;

/**
 * Created by nobuya on 2014/09/02.
 */
public class METARMessage {
    private int number = 0;
    private String message = null;
    private String dateAndTime = null;
    private String ICAOCode = null;

    public METARMessage(String msg) {
        message = msg;
        dateAndTime = GetMetar.getDateAndTime(msg);
        ICAOCode = GetMetar.getICAOCode(msg);
    }

    public void setNumber(int num) {
        number = num;
    }

    public int getNumber() {
        return number;
    }
    public String getDateAndTime() {
        return dateAndTime;
    }
    public String getICAOCode() {
        return ICAOCode;
    }
}
