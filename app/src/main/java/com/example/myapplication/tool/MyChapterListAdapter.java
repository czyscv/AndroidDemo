package com.example.myapplication.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MainComicLookActivity;

import java.util.ArrayList;
import java.util.List;

//章节适配器
public class MyChapterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ComicData> mDatas;//数据
    private Context context;
    private FooterHolder footerHolder;

    public MyChapterListAdapter(List<ComicData> data, Context context) {
        if (mDatas == null) {
            Log.e("Warning","ChapterListAdapter mData must not be null");
            mDatas = new ArrayList<>();
        }
        this.context = context;
        this.mDatas = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_comic_details_list, parent, false);
            return new VH(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_dashboard_list_footer, parent, false);
            footerHolder = new FooterHolder(view);
            return footerHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VH) {
            VH viewHolder = (VH) holder;
            ComicData comicData = mDatas.get(position);
            String name = comicData.getName();
            viewHolder.chaptertitle.setText(name);
            //单击
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(context,MainComicLookActivity.class);
                Bundle data = new Bundle();
                data.putString("comic", mDatas.get(position).getPath());
                intent.putExtra("data",data);
                context.startActivity(intent);
                }
            });
            //长按
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;//表示此事件已经消费，不会触发单击事件
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+1;
    }

    //用于判断当前的ltem是不是最后一个
    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()){
            return 1;
        }else{
            return 0;
        }
    }

    //对外方法
    public FooterHolder getFooterHolder(){
        return footerHolder;
    }

    //展示章节的ViewHolder
    private static class VH extends RecyclerView.ViewHolder{
        public final TextView chaptertitle;
        public VH(View v) {
            super(v);
            chaptertitle = v.findViewById(R.id.details_list_name);
        }
    }
}
