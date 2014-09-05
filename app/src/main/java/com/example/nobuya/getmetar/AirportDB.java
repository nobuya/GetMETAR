package com.example.nobuya.getmetar;

/**
 * Created by nobuya on 2014/09/01.
 */
public class AirportDB {
    public enum AirportInfo {
        RJTT("rjtt", 30, 35.553333, 139.781111, "Tokyo Int'l (Haneda)"),
        RJFM("rjfm", 60, 31.877222, 131.448611, "Miyazaki"),
        RJFK("rjfk", 60, 31.8,      130.721667, "Kagoshima"),
        RJOA("rjoa", 60, 34.436111, 132.919444, "Hiroshima"),
        RJOB("rjob", 60, 34.756944, 133.855278, "Okayama"),
        RJOT("rjot", 60, 34.214167, 134.015556, "Takamatsu"),
        RJFT("rjft", 60, 32.837222, 130.855,    "Kumamoto"),
        RJBE("rjbe", 60, 34.633333, 135.226389, "Kobe"),
        RJBB("rjbb", 30, 34.427222, 135.243889, "Kansai Int'l (Osaka)"),
        RJFF("rjff", 30, 33.585942, 130.450686, "Fukuoka"),
        RJAA("rjaa", 30, 35.763889, 140.391667, "New Tokyo Int'l (Narita)"),
        ROAH("roah", 30, 26.195833, 127.645833, "Naha"),
        RJGG("rjgg", 30, 34.858333, 136.805278, "Chubu Centrair Int'l"),
        RJCC("rjcc", 30, 42.775,    141.692222, "New Chitose"),
        RJFU("rjfu", 60, 32.916944, 129.913611, "Nagasaki"),
        RJOO("rjoo", 60, 34.785278, 135.438056, "Osaka Int'l (Itami)"),
        RJOM("rjom", 60, 33.827222, 132.699722, "Matsuyama"),
        RJSS("rjss", 60, 38.136944, 140.9225,   "Sendai"),
        RJSC("rjsc", 60, 38.411667, 140.371111, "Yamagata"),
        RJSN("rjsn", 60, 37.955833, 139.120556, "Niigata"),
        RJNT("rjnt", 60, 36.648333, 137.1875,   "Toyama"),
        RJNK("rjnk", 60, 36.393889, 136.4075,   "Komatsu"),
        RJTI("rjti", 60, 35.636111, 139.839444, "Tokyo Heliport"),
        RJCH("rjch", 60, 41.77,     140.821944, "Hakodate");
        //
        //
        String name;
        long updateInterval; // in msec
        // GeoLocation
        double latitude;
        double longitude;
        String text;

        AirportInfo(String name, int updateInterval, double latitude, double longitude,
                    String text) {
            this.name = name;
            this.updateInterval = updateInterval;
            this.latitude = latitude;
            this.longitude = longitude;
            this.text = text;
        }
    }

    public static String getAirportText(String cccc) {
        String result = "???";
        String cccc2 = cccc.toLowerCase();
        for (AirportInfo info: AirportInfo.values()) {
            if (cccc2.equals(info.name)) {
                return info.text;
            }
        }
        return result;
    }
}
