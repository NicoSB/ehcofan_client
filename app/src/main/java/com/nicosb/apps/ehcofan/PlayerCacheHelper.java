package com.nicosb.apps.ehcofan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Nico on 28.07.2016.
 */
public class PlayerCacheHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "EHCOFan.db";


    public PlayerCacheHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PlayerCache.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(PlayerCache.SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public static abstract class PlayerCache implements BaseColumns {
        public static final String TABLE_NAME = "players";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SURNAME = "surname";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_CONTRACT = "contract";
        public static final String COLUMN_NAME_NATIONALITY = "nationality";
        public static final String COLUMN_NAME_NUMBER = "number";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_HEIGHT = "height";
        public static final String COLUMN_NAME_EP_ID = "ep_id";
        public static final String COLUMN_NAME_BIRTHDATE = "birthday";

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SURNAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_POSITION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CONTRACT + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NATIONALITY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NUMBER + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EP_ID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BIRTHDATE + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
