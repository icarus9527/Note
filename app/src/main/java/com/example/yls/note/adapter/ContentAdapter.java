package com.example.yls.note.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.yls.note.R;
import com.example.yls.note.bean.NoteBean;
import com.example.yls.note.listener.ContentRecyclerItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icarus9527 on 2017/5/8.
 */

public class ContentAdapter extends SwipeMenuAdapter<ContentViewHolder> {
    private List<NoteBean> resource;
    private ContentRecyclerItemClickListener listener;
    private Context context;

    public ContentAdapter(List<NoteBean> resource){
        this.resource = resource;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_content_item,parent,false);
        context = parent.getContext();
        return view;
    }

    @Override
    public ContentViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        ContentViewHolder holder = new ContentViewHolder(realContentView,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentViewHolder holder, int position) {
        final NoteBean note = resource.get(position);
        String[] urls = note.getImageUrls();

        if (urls.length > 0){
            Glide.with(context).load(urls[0]).into(holder.image);
        }
        String title = note.getTitle();
        String text = note.getText();
        if (title != null && !title.equals("")){
            if (title.length() <= 8){
                holder.title.setText(title);
            }else{
                String titleBuffer = title.substring(0,7);
                holder.title.setText(titleBuffer+"...");
            }
        }else if(text != null && !text.equals("")){
            if (text.length() <= 20){
                holder.text.setText(text);
            }else{
                String textBuffer = text.substring(0,19);
                holder.text.setText(textBuffer+"...");
            }
        }
        holder.date.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return resource.size();
    }

    public void setOnItemClickListener(ContentRecyclerItemClickListener listener){
        this.listener = listener;
    }
}
