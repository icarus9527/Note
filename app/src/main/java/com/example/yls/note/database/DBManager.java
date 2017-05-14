package com.example.yls.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yls.note.bean.NoteBean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by icarus9527 on 2017/5/11.
 */

public class DBManager {
    private NoteSQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;
    private static final String TABLE_NAME = "note";

    public DBManager(Context context){
        this.context = context;
        helper = new NoteSQLiteOpenHelper(context,"note.db",null,1);
    }

    public void insert(NoteBean note){
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type",note.getType());
        values.put("title",note.getTitle());
        values.put("text",note.getText());
        values.put("videoUrl",note.getVideoUrl());
        values.put("soundUrl",note.getSoundUrl());

        StringBuffer strBuffer = new StringBuffer();
        String[] imgUrls = note.getImageUrls();
        for (int i = 0;i < imgUrls.length; i++){
            strBuffer.append(imgUrls[i]+"!!");
        }
        values.put("imageUrl",strBuffer.toString());
        values.put("date",note.getDate());
        db.insert(TABLE_NAME,null,values);
        Log.i("DBManager","insert data successful!!");
    }

    public void update(NoteBean note){
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("type",note.getType());
        values.put("title",note.getTitle());
        values.put("text",note.getText());
        values.put("videoUrl",note.getVideoUrl());
        values.put("soundUrl",note.getSoundUrl());
        StringBuffer strBuffer = new StringBuffer();
        String[] imgUrls = note.getImageUrls();
        for (int i = 0;i < imgUrls.length; i++){
            strBuffer.append(imgUrls[i]+"!!");
        }
        values.put("imageUrl",strBuffer.toString());
        values.put("date",note.getDate());
        db.update(TABLE_NAME,values,"id=?", new String[]{String.valueOf(note.getId())});
    }

    public List<NoteBean> query(){
        db = helper.getWritableDatabase();

        List<NoteBean> dataList = new ArrayList<>();
        Cursor cursor = db.query("note",null,null,null,null,null,null,null);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            NoteBean note = new NoteBean();
            note.setId(cursor.getInt(cursor.getColumnIndex("id")));
            note.setType(cursor.getString(cursor.getColumnIndex("type")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setText(cursor.getString(cursor.getColumnIndex("text")));
            note.setVideoUrl(cursor.getString(cursor.getColumnIndex("videoUrl")));
            note.setSoundUrl(cursor.getString(cursor.getColumnIndex("soundUrl")));
            String imgUrls = cursor.getString(cursor.getColumnIndex("imageUrl"));
            String[] imageUrls = imgUrls.split("!!");
            note.setImageUrls(imageUrls);
            note.setDate(cursor.getString(cursor.getColumnIndex("date")));

            dataList.add(note);
        }
        return dataList;
    }

    public void delete(NoteBean note) {
        db = helper.getWritableDatabase();
        db.delete(TABLE_NAME,"id=?",new String[]{String.valueOf(note.getId())});
    }
}
