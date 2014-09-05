package com.example.nobuya.getmetar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by nobuya on 2014/09/02.
 */
public class MessageHistory {
    private static LinkedList<METARMessage> messageList = new LinkedList<METARMessage>();
    private static LinkedList<String> ccccList = new LinkedList<String>();
    private static HashMap<String, LinkedList<METARMessage>> mapDateAndTime
            = new HashMap<String, LinkedList<METARMessage>>();
    private static HashMap<String, LinkedList<METARMessage>> mapCCCC
            = new HashMap<String, LinkedList<METARMessage>>();
    private static int numMessages = 0;
    private static boolean isInitialized = false;
    private static METARMessage latestMessage = null;
    private static METARMessage previousMessage = null;

    public static void init(Context context) {
        if (!isInitialized) {
            createDatabase(context);
            readDB();
            if (messageList.size() > 0) {
                latestMessage = messageList.get(0);
                previousMessage = latestMessage;
            }
            isInitialized = true;
        }
    }

    public static void add(METARMessage msg) {
        //String dateAndTime = msg.getDateAndTime();
        //String cccc = msg.getICAOCode();
        msg.setNumber(++numMessages);
        messageList.addFirst(msg);
        previousMessage = latestMessage;
        latestMessage = msg;
        //
        registerDB(msg);
        addList(msg);
    }

    private static void addList(METARMessage msg) {
        String dateAndTime = msg.getDateAndTime();
        String cccc = msg.getICAOCode();
        addCCCCList(cccc);

        LinkedList<METARMessage> list1 = mapDateAndTime.get(dateAndTime);
        if (list1 == null) {
            LinkedList<METARMessage> newList = new LinkedList<METARMessage>();
            newList.addFirst(msg);
            mapDateAndTime.put(dateAndTime, newList);
        } else {
            list1.addFirst(msg);
        }
        //
        LinkedList<METARMessage> list2 = mapCCCC.get(cccc);
        if (list2 == null) {
            LinkedList<METARMessage> newList = new LinkedList<METARMessage>();
            newList.addFirst(msg);
            mapCCCC.put(cccc, newList);
        } else {
            list2.addFirst(msg);
        }
    }

    private static void addCCCCList(String cccc) {
        if (cccc.equals("????")) return;
        if (ccccList.contains(cccc)) {
            ccccList.remove(cccc);
        }
        ccccList.addFirst(cccc);
    }

    public static METARMessage getLatestMessage() {
        return latestMessage;
    }

    public static METARMessage getPreviousMessage() {
        return previousMessage;
    }

    public static String[] getHistoryArray(int numHistory) {
        int size = (messageList.size() > numHistory) ? numHistory : messageList.size();
        String [] strArray = new String[size];
        int j = 0;
        Iterator<METARMessage> i = messageList.iterator();
        while (i.hasNext() && j < size) {
            METARMessage msg = i.next();
            strArray[j] = "(" + msg.getNumber() + ") " +
                    msg.getDateAndTime() + " " + msg.getICAOCode();
            j++;
        }
        return strArray;
    }

    public static String[] getAirportHistoryArray(int num) {
        int size = (ccccList.size() > num) ? num : ccccList.size();
        String [] strArray = new String[size];
        int j = 0;
        Iterator<String> i = ccccList.iterator();
        while (i.hasNext() && j < size) {
            String cccc = i.next();
            strArray[j] = "(" + (j + 1) + ") " + cccc;
            j++;
        }
        return strArray;
    }

    public static String getCCCC(int location) {
        return ccccList.get(location);
    }

    public static METARMessage getAirportHistory(String cccc, int pos) {
        LinkedList<METARMessage> list = mapCCCC.get(cccc);
        return list.get(pos);
    }

    public static int getAirportHistorySize(String cccc) {
        LinkedList list = mapCCCC.get(cccc);
        return list.size();
    }

    private static final String DATABASE_NAME = "msg_history.db";
    private static final String DATABASE_TABLE = "table1";
    private static final int DATABASE_VERSION = 1;

    private final static String COL_ID = "id";
    private final static String COL_NUM = "num";
    private final static String COL_ICAO = "icao";
    private final static String COL_DATETIME = "datetime";
    private final static String COL_MSG = "msg";

    static DatabaseHelper dbHelper = null;
//    static SQLiteDatabase db = null;

    public static void createDatabase(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public static void registerDB(METARMessage msg) {
        String dateAndTime = msg.getDateAndTime();
        String cccc = msg.getICAOCode();
        String msgStr = msg.getMessage();
        int  num = msg.getNumber();
        ContentValues val = new ContentValues();
        val.put(COL_NUM, num);
        val.put(COL_ICAO, cccc);
        val.put(COL_DATETIME, dateAndTime);
        val.put(COL_MSG, msgStr);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            db.insert(DATABASE_TABLE, null, val);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            Log.e("register error", e.toString());
        }
        db.close();
    }

    public static void readDB() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = { COL_ID, COL_NUM, COL_ICAO, COL_DATETIME, COL_MSG };

        try {
            // query(String table, String[] columns, String selection, String[] selectionArgs,
            //       String groupBy, String having, String orderBy)
            Cursor cursor =
                    db.query(DATABASE_TABLE, columns, null, null, null, null, COL_ID);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int num = cursor.getInt(1);
                String cccc = cursor.getString(2);
                String dateAndTime = cursor.getString(3);
                String msgStr = cursor.getString(4);
                METARMessage msg = new METARMessage(msgStr);
                msg.setNumber(num);
                messageList.addFirst(msg);
                addList(msg);
                numMessages = num;
            }
        } catch (Exception e) {
            Log.e("db read failed", e.toString());
        }
        db.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table if not exists " +
                    DATABASE_TABLE + " (" +
                    COL_ID + " integer primary key autoincrement," +
                    COL_NUM + " integer," +
                    COL_ICAO + " text not null," +
                    COL_DATETIME + " text not null," +
                    COL_MSG + " text not null);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // delete table
            db.execSQL("drop table if exists " + DATABASE_TABLE);
            // create table
            onCreate(db);
        }
    }
}
