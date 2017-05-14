package com.example.yls.note.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.yls.note.R;
import com.example.yls.note.util.ThreadUtils;

import java.util.concurrent.ExecutionException;

/**
 * Created by icarus9527 on 2017/5/10.
 */

public class EditerView extends EditText{

    public static final String TAG = "|";
    private ThreadUtils threadUtils = new ThreadUtils();

    public EditerView(Context context) {
        super(context);
    }

    public EditerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void insertDrawable(Uri uri){
        final SpannableString ss = new SpannableString(TAG);
        ImageSpan span = new ImageSpan(getContext(),uri,ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span,0,TAG.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }

    public void insertDrawable(final String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        final SpannableString ss = new SpannableString(TAG);
        ImageSpan span = new ImageSpan(getContext(), bmp,ImageSpan.ALIGN_BASELINE);
        ss.setSpan(span,0,TAG.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }

    public void insertDrawable(Bitmap bitmap) {
        final SpannableString ss = new SpannableString(TAG);
        //得到drawable对象，即所要插入的图片
        Drawable d = new BitmapDrawable(null,bitmap);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, TAG.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }



    //获取图片真实地址
    public Bitmap getBitmapFromFile(Uri uri){
        ContentResolver resolver = getContext().getContentResolver();
        String[] pojo = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, pojo, null,null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(pojo[0]));
        Bitmap bmp = BitmapFactory.decodeFile(path);
        return bmp;
    }

    public void insertDrawable(int id) {
        final SpannableString ss = new SpannableString("p");
        //得到drawable对象，即所要插入的图片
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        //用这个drawable对象代替字符串easy
        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, "p".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        append(ss);
    }

    public void insertPicture(String path){
        Glide.with(getContext()).load(path).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                final SpannableString ss = new SpannableString(TAG);
                //得到drawable对象，即所要插入的图片
                Drawable d = new BitmapDrawable(null,resource);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                //用这个drawable对象代替字符串easy
                ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
                ss.setSpan(span, 0, TAG.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                append(ss);
            }
        });
    }

    public void recoverContent(String text, String[] imageUrls) {
        String[] textBuffer = text.split(TAG);
        int i;
        for (i = 0; i<imageUrls.length; i++){
            if (textBuffer[i].length() > 1){
                append(textBuffer[i].substring(0,textBuffer[i].length()-2));
            }
            insertPicture(imageUrls[i]);
        }
        if (i<textBuffer.length-1){
            append(textBuffer[textBuffer.length-1]);
        }

    }
}
