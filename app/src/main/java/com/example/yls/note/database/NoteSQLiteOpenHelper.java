package com.example.yls.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by icarus9527 on 2017/5/8.
 */

public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {
    public NoteSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        Log.i("NoteSQLiteOpenHelper","create database successful!!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("NoteSQLiteOpenHelper","create table successful!!");
        String create = "create table if not exists note ("+
                "id integer primary key AUTOINCREMENT," +
                "type char(5),"+
                "title varchar," +
                "text varchar,"+
                "videoUrl varchar," +
                "soundUrl varchar," +
                "imageUrl varchar," +
                "date char(10))";
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
