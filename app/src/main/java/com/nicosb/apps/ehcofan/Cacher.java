package com.nicosb.apps.ehcofan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.StandingsTeam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nico on 28.07.2016.
 */
public class Cacher {

    public static boolean hasImage(Context context, Player player) {
        FileInputStream fis = null;
        try {
            String fileName = "player_" + player.getId();
            fis = context.openFileInput(fileName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static Bitmap getPlayerImage(Context context, Player player) {
        FileInputStream fis = null;
        try {
            String fileName = "player_" + player.getId();
            fis = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_white_48dp);
        }
    }

    public static void storePlayerImage(Context context, Player player, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            String fileName = "player_" + player.getId();
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void cachePlayer(Context context, Player player) {
        SQLiteDatabase sqLiteDatabase = CacheDBHelper.getReadableDB(context);
        sqLiteDatabase.insertWithOnConflict(CacheDBHelper.TableColumns.PLAYERS_TABLE_NAME, CacheDBHelper.TableColumns.PLAYERS_COLUMN_NAME_WEIGHT, player.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void cacheMatch(Context context, Match match) {
        SQLiteDatabase sqLiteDatabase = CacheDBHelper.getReadableDB(context);
        sqLiteDatabase.insertWithOnConflict(CacheDBHelper.TableColumns.MATCHES_TABLE_NAME, CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION, match.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void cacheTeam(Context context, StandingsTeam team) {
        SQLiteDatabase sqLiteDatabase = CacheDBHelper.getWritableDB(context);
        sqLiteDatabase.insertWithOnConflict(CacheDBHelper.TableColumns.STANDINGSTEAMS_TABLE_NAME, CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GROUP, team.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void delete(Context context, String table, long id){
        SQLiteDatabase sqLiteDatabase = CacheDBHelper.getReadableDB(context);
        sqLiteDatabase.delete(table, "id="+id, null);
    }
}
