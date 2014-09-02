package com.example.nobuya.getmetar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by nobuya on 2014/09/02.
 */
public class MessageHistory {
    private static LinkedList<METARMessage> messageList = new LinkedList<METARMessage>();
    private static HashMap<String, LinkedList<METARMessage>> mapDateAndTime
            = new HashMap<String, LinkedList<METARMessage>>();
    private static HashMap<String, LinkedList<METARMessage>> mapCCCC
            = new HashMap<String, LinkedList<METARMessage>>();
    private static int numMessages = 0;

    public static void add(METARMessage msg) {
        String dateAndTime = msg.getDateAndTime();
        String cccc = msg.getICAOCode();
        msg.setNumber(++numMessages);
        messageList.addFirst(msg);
        //
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

    public static String[] getHistoryArray(int numHistory) {
        int size = (messageList.size() > numHistory) ? numHistory : messageList.size();
        String [] strArray = new String[size];
        int j = 0;
        Iterator<METARMessage> i = messageList.iterator();
        while (i.hasNext()) {
            METARMessage msg = i.next();
            strArray[j] = "(" + msg.getNumber() + ") " +
                    msg.getDateAndTime() + " " + msg.getICAOCode();
            j++;
        }
        return strArray;
    }
}
