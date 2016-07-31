package com.nicosb.apps.ehcofan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nicosb.apps.ehcofan.models.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nico on 28.07.2016.
 */
public class Cacher {

    public static boolean hasImage(Context context, Player player){
        FileInputStream fis = null;
        try{
            String fileName = "player_" + player.getId();
            fis = context.openFileInput(fileName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static Bitmap getPlayerImage(Context context, Player player) {
        FileInputStream fis = null;
        try{
            String fileName = "player_" + player.getId();
            fis = context.openFileInput(fileName);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_white_48dp);
        }
    }

    public static Bitmap storePlayerImage(Context context, Player player, Bitmap bitmap){
        FileOutputStream fos = null;
            try {
            String fileName = "player_" + player.getId();
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    public static boolean cachePlayer(Context context, Player player){
        PlayerCacheHelper helper = new PlayerCacheHelper(context);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        long newRowId;
        newRowId = sqLiteDatabase.insertWithOnConflict(PlayerCacheHelper.PlayerCache.TABLE_NAME, PlayerCacheHelper.PlayerCache.COLUMN_NAME_WEIGHT, player.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE);

        return newRowId > 0;
    }
}
