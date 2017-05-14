package com.example.yls.note.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yls.note.R;
import com.example.yls.note.listener.ContentRecyclerItemClickListener;

/**
 * Created by icarus9527 on 2017/5/8.
 */

public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView image;
    TextView text,title,date;


    private ContentRecyclerItemClickListener listener;

    public ContentViewHolder(View itemView,ContentRecyclerItemClickListener listener) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.main_item_img);
        text = (TextView) itemView.findViewById(R.id.main_item_text);
        title = (TextView) itemView.findViewById(R.id.main_item_title);
        date = (TextView) itemView.findViewById(R.id.main_item_date);

        this.listener = listener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.OnItemClick(v,getAdapterPosition());
        }
    }
}
