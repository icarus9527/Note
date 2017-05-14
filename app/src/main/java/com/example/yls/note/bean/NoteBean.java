package com.example.yls.note.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yls on 2017/5/8.
 */

public class NoteBean implements Parcelable{
    private int id;
    private String type;
    private String text;
    private String[] imageUrls;
    private String videoUrl;
    private String soundUrl;
    private String date;
    private String title;

    public NoteBean(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(text) ;
        if (imageUrls == null){
            dest.writeInt(0);
        }else{
            dest.writeInt(imageUrls.length);
        }
        if (imageUrls != null){
            dest.writeStringArray(imageUrls);
        }
        dest.writeString(videoUrl);
        dest.writeString(soundUrl);
        dest.writeString(date);
        dest.writeString(title);

    }

    public static final Parcelable.Creator<NoteBean> CREATOR = new Parcelable.Creator<NoteBean>() {
        @Override
        public NoteBean createFromParcel(Parcel source) {
            return new NoteBean(source);
        }

        @Override
        public NoteBean[] newArray(int size) {
            return new NoteBean[size];
        }
    };

    private NoteBean(Parcel source){
        this.id = source.readInt();
        this.type = source.readString();
        this.text = source.readString();
        int length = source.readInt();
        String[] imageUrls = null;
        if (length>0){
            imageUrls = new String[length];
            source.readStringArray(imageUrls);
        }
        this.videoUrl = source.readString();
        this.soundUrl = source.readString();
        this.date = source.readString();
        this.title = source.readString();

    }
}
