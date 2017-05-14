package com.example.yls.note.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.yls.note.R;
import com.example.yls.note.adapter.ContentAdapter;
import com.example.yls.note.bean.NoteBean;
import com.example.yls.note.database.DBManager;
import com.example.yls.note.listener.ContentRecyclerItemClickListener;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements ContentRecyclerItemClickListener ,View.OnClickListener{

    private SwipeMenuRecyclerView mainRecyclerContent;
    private FloatingActionButton fabAdd;
    private DBManager dbManager = new DBManager(this);
    private ContentAdapter adapter;

    private List<NoteBean> resource = new ArrayList<>();

    @Override
    public int getViewRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mainRecyclerContent = (SwipeMenuRecyclerView) findViewById(R.id.main_recycler_content);
        fabAdd = (FloatingActionButton) findViewById(R.id.main_fab_add);
        fabAdd.setOnClickListener(this);

        resource = initData();
        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mainRecyclerContent.setLayoutManager(manager);

        adapter = new ContentAdapter(resource);
        mainRecyclerContent.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        mainRecyclerContent.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                Drawable d = getResources().getDrawable(R.drawable.delete);

                SwipeMenuItem deleteItem = new SwipeMenuItem(MainActivity.this)
                        .setBackgroundColor(Color.RED)
                        .setImage(d)
                        .setWidth(d.getIntrinsicWidth()*2)
                        .setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        });

        mainRecyclerContent.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();

                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION && menuPosition == 0){
                    dbManager.delete(resource.get(adapterPosition));
                    resource = dbManager.query();
                    adapter = new ContentAdapter(resource);
                    mainRecyclerContent.setAdapter(adapter);

                    adapter.setOnItemClickListener(MainActivity.this);
                }
            }
        });
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.MANAGE_DOCUMENTS},1);
        }
    }


    @Override
    public void OnItemClick(View v, int adapterPosition) {
        Intent intent = new Intent(this, EditerActivity.class);
        intent.putExtra("data",resource.get(adapterPosition));
        intent.putExtra("imageUrls",resource.get(adapterPosition).getImageUrls());
        startActivity(intent);
    }

    public List<NoteBean> initData() {
        List<NoteBean> data = dbManager.query();
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_fab_add:
                Intent intent = new Intent();
                intent.setClass(this,EditerActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    public void refreshData(){
        resource = dbManager.query();
        Log.i("MainActivity",resource.size()+"");
        adapter.notifyDataSetChanged();
    }
}
