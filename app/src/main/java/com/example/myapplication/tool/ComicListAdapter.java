package com.example.myapplication.tool;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.VH>{
    private List<ComicData> mDatas = new ArrayList<>();
    private View view;
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final ImageView comicimg;
        public final TextView comictitle;
        public final TextView comic;
        public VH(View v) {
            super(v);
            comicimg = v.findViewById(R.id.dashboard_comicimg);
            comictitle = v.findViewById(R.id.dashboard_comictitle);
            comic = v.findViewById(R.id.dashboard_comic);
        }
    }

    public ComicListAdapter(List<ComicData> data,View view) {
        if (mDatas == null) {
            throw new IllegalArgumentException("mData must not be null");
        }
        this.view = view;
        this.mDatas = data;
    }

    //该方法把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例，当然这个ViewHolder需要我们自己去编写。
    @NonNull
    @Override
    public ComicListAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_dashboard_list, parent, false);
        return new VH(v);
    }

    //onBindViewHolder方法为item的UI绑定展示数据
    @Override
    public void onBindViewHolder(@NonNull ComicListAdapter.VH holder, int position) {
        ComicData comicData = mDatas.get(position);
        String name = comicData.getName();
        int l = name.length();
        if(name.getBytes().length>=160){
            name = name.substring(0,(l<150?l:150))+"...";
        }
        holder.comictitle.setText(name);
        holder.comic.setText("上传时间："+comicData.getTime());
        //加载图片
        String url = "http://www.skythinking.cn:7777/resource/"+comicData.getPath()+"/0001.jpg?v=3";
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading);
        Glide.with(view).load(url).apply(options).into(holder.comicimg);
    }

    //这个方法就类似于BaseAdapter的getCount方法了，即总共有多少个条目。
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
