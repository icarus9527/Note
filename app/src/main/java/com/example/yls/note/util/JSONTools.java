package com.example.yls.note.util;

import com.example.yls.note.bean.NoteBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icarus9527 on 2017/3/8.
 */

public class JSONTools {

    //利用Gson生成Json类型的String
    public String createJsonString(Object values){
        Gson gson = new Gson();
        String str = gson.toJson(values);
        return str;
    }

    public static List<NoteBean> getJsonDataList(String jsonString) {
            List<NoteBean> list;
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, new TypeToken<List<NoteBean>>(){}.getType());
            return list;
        }

}
