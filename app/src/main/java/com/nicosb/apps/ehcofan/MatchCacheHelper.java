package com.nicosb.apps.ehcofan;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nico on 28.07.2016.
 */
public class MatchCacheHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "EHCOFan.db";


    public MatchCacheHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MatchCache.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MatchCache.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public static abstract class MatchCache implements BaseColumns {
        public static final String TABLE_NAME = "matches";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_HOME_TEAM = "home_team";
        public static final String COLUMN_NAME_AWAY_TEAM = "away_team";
        public static final String COLUMN_NAME_COMPETITION = "competition";
        public static final String COLUMN_NAME_DATETIME = "match_datetime";
        public static final String COLUMN_NAME_SCORES_HOME = "scores_away";
        public static final String COLUMN_NAME_SCORES_AWAY = "scores_home";

        private static final String TEXT_TYPE = " TEXT ";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_HOME_TEAM + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AWAY_TEAM + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COMPETITION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DATETIME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SCORES_HOME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SCORES_AWAY + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String encodeScores(int[] scores){
        String encoded = String.valueOf(scores[0]);
        for(int i = 1; i < scores.length; i++){
            encoded += ";" + scores[i];
        }

        return encoded;
    }

    public static int[] decodeScores(String scores){
        int counter = 0;
        int[] decoded = new int[4];
        String number = "";
        for(int i = 0; i < scores.length(); i++){
            if(scores.charAt(i) != ';'){
                number = number + scores.charAt(i);
            }
            else{
                decoded[counter] = Integer.valueOf(number);
                number = "";
                counter++;
            }
        }
        decoded[counter] = Integer.valueOf(number);
        return decoded;
    }
}
