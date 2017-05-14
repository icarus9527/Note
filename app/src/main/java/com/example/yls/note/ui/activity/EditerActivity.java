package com.example.yls.note.ui.activity;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.yls.note.R;
import com.example.yls.note.bean.NoteBean;
import com.example.yls.note.database.DBManager;
import com.example.yls.note.view.EditerView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by icarus9527 on 2017/5/8.
 */

public class EditerActivity extends BaseActivity implements View.OnClickListener{

    private static final int CAPTURE_PICTURE_REQUEST_CODE = 1001;


    private LinearLayout edtContent;
    private RecyclerView imageList;
    private EditerView edtText;
    private EditText edtTitle;
    private Toolbar edtToolBar;
    private ImageButton ibtnPic;

    private NoteBean note;
    private DBManager manager;
    private String[] imgUrls;
    private StringBuffer imgUrlsBuffer = new StringBuffer();
    private String videoUrl = null;
    private String soundUrl = null;
    private String type = null;

    private boolean isExist = false;

    @Override
    public int getViewRes() {
        return R.layout.editer_activity;
    }

    @Override
    protected void init() {
        initView();
        initToolBar();
    }

    private void initView() {
        edtContent = (LinearLayout) findViewById(R.id.editer_linearlayout_Content);
        imageList = (RecyclerView) findViewById(R.id.editer_imageList);
        edtTitle = (EditText) findViewById(R.id.editer_edt_title);
        edtTitle.clearFocus();
        edtText = (EditerView) findViewById(R.id.editer_edt_text);
        edtText.clearFocus();
        edtToolBar = (Toolbar) findViewById(R.id.editer_toolbar);
        ibtnPic = (ImageButton) findViewById(R.id.editer_imgbtn_picture);
        ibtnPic.setOnClickListener(this);

        Intent intent = getIntent();
        note = intent.getParcelableExtra("data");
        if (note != null){
            isExist = true;

            edtTitle.setText(note.getTitle());
            String[] imageUrls = intent.getStringArrayExtra("imageUrls");
            edtText.recoverContent(note.getText(),imageUrls);
            for (int i=0;i<imageUrls.length; i++){
                imgUrlsBuffer.append(imageUrls[i]+"!!");
            }
        }
    }

    private void initToolBar() {
        edtToolBar.setTitle("编辑中...");
        setSupportActionBar(edtToolBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_editer_save:
                    if (isExist){
                        update();
                    }else{
                        save();
                    }
                break;
        }
        return true;
    }

    private void save(){
        note = new NoteBean();
        note.setType(type);
        note.setText(edtText.getText().toString());
        note.setTitle(edtTitle.getText().toString());
        Date date = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        note.setDate(new SimpleDateFormat(dateFormat).format(date));
        imgUrls = imgUrlsBuffer.toString().split("!!");
        note.setImageUrls(imgUrls);
        note.setVideoUrl(videoUrl);
        note.setSoundUrl(soundUrl);
        manager = new DBManager(this);
        manager.insert(note);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void update(){
        note.setType(type);
        note.setText(edtText.getText().toString());
        note.setTitle(edtTitle.getText().toString());
        Date date = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        note.setDate(new SimpleDateFormat(dateFormat).format(date));
        imgUrls = imgUrlsBuffer.toString().split("!!");
        note.setImageUrls(imgUrls);
        note.setVideoUrl(videoUrl);
        note.setSoundUrl(soundUrl);
        manager = new DBManager(this);
        manager.update(note);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.editer_imgbtn_picture:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,CAPTURE_PICTURE_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_PICTURE_REQUEST_CODE && data != null){
            Uri imageUri = data.getData();
            edtText.insertPicture(imageUri.toString());
            imgUrlsBuffer.append(imageUri.toString()+"!!");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    //获取图片真实地址
    public String getPathFromFile(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        String[] pojo = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null,null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(pojo[0]));
        return path;
    }
}
