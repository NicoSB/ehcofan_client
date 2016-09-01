package com.nicosb.apps.ehcofan;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Nico on 28.07.2016.
 */
public class CacheDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "EHCOFan.db";


    public CacheDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableColumns.PLAYERS_SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(TableColumns.MATCHES_SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TableColumns.PLAYERS_SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(TableColumns.MATCHES_SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public static abstract class TableColumns implements BaseColumns {
        
        // Table players
        public static final String PLAYERS_TABLE_NAME = "players";
        public static final String PLAYERS_COLUMN_NAME_ID = "id";
        public static final String PLAYERS_COLUMN_NAME_NAME = "name";
        public static final String PLAYERS_COLUMN_NAME_SURNAME = "surname";
        public static final String PLAYERS_COLUMN_NAME_POSITION = "position";
        public static final String PLAYERS_COLUMN_NAME_CONTRACT = "contract";
        public static final String PLAYERS_COLUMN_NAME_NATIONALITY = "nationality";
        public static final String PLAYERS_COLUMN_NAME_NUMBER = "number";
        public static final String PLAYERS_COLUMN_NAME_WEIGHT = "weight";
        public static final String PLAYERS_COLUMN_NAME_HEIGHT = "height";
        public static final String PLAYERS_COLUMN_NAME_EP_ID = "ep_id";
        public static final String PLAYERS_COLUMN_NAME_GAMES = "games";
        public static final String PLAYERS_COLUMN_NAME_GOALS = "goals";
        public static final String PLAYERS_COLUMN_NAME_ASSISTS = "assists";
        public static final String PLAYERS_COLUMN_NAME_PIM = "pim";
        public static final String PLAYERS_COLUMN_NAME_BIRTHDATE = "birthday";
        
        // Table matches
        public static final String MATCHES_TABLE_NAME = "matches";
        public static final String MATCHES_COLUMN_NAME_ID = "id";
        public static final String MATCHES_COLUMN_NAME_HOME_TEAM = "home_team";
        public static final String MATCHES_COLUMN_NAME_AWAY_TEAM = "away_team";
        public static final String MATCHES_COLUMN_NAME_COMPETITION = "competition";
        public static final String MATCHES_COLUMN_NAME_DATETIME = "match_datetime";
        public static final String MATCHES_COLUMN_NAME_SCORES_HOME = "scores_away";
        public static final String MATCHES_COLUMN_NAME_SCORES_AWAY = "scores_home";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        

        // Create table players
        private static final String PLAYERS_SQL_CREATE_ENTRIES =
                "CREATE TABLE " + PLAYERS_TABLE_NAME + " (" +
                        PLAYERS_COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        PLAYERS_COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_SURNAME + TEXT_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_POSITION + TEXT_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_CONTRACT + TEXT_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_NATIONALITY + TEXT_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_EP_ID + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_GAMES + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_GOALS + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_ASSISTS + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_PIM + INTEGER_TYPE + COMMA_SEP +
                        PLAYERS_COLUMN_NAME_BIRTHDATE + TEXT_TYPE +
                        " )";
        
        // Create table matches
        private static final String MATCHES_SQL_CREATE_ENTRIES =
                "CREATE TABLE " + MATCHES_TABLE_NAME + " (" +
                        MATCHES_COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        MATCHES_COLUMN_NAME_HOME_TEAM + TEXT_TYPE + COMMA_SEP +
                        MATCHES_COLUMN_NAME_AWAY_TEAM + TEXT_TYPE + COMMA_SEP +
                        MATCHES_COLUMN_NAME_COMPETITION + TEXT_TYPE + COMMA_SEP +
                        MATCHES_COLUMN_NAME_DATETIME + TEXT_TYPE + COMMA_SEP +
                        MATCHES_COLUMN_NAME_SCORES_HOME + TEXT_TYPE + COMMA_SEP +
                        MATCHES_COLUMN_NAME_SCORES_AWAY + TEXT_TYPE +
                        " )";


        private static final String PLAYERS_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + PLAYERS_TABLE_NAME;
        private static final String MATCHES_SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + MATCHES_TABLE_NAME;
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
