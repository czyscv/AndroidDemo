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
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activity.MainComicDetailsActivity;

import java.util.ArrayList;
import java.util.List;

//漫画列表适配器
public class MyComicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ComicData> mDatas;//数据
    private Context context;
    private FooterHolder footerHolder;

    public MyComicListAdapter(List<ComicData> data, Context context) {
        if (mDatas == null) {
            Log.e("Warning","ComicListAdapter mData must not be null");
            mDatas = new ArrayList<>();
        }
        this.context = context;
        this.mDatas = data;
    }

    //该方法把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例，当然这个ViewHolder需要我们自己去编写。
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_dashboard_list, parent, false);
            return new VH(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_dashboard_list_footer, parent, false);
            footerHolder = new FooterHolder(view);
            return footerHolder;
        }
    }

    //onBindViewHolder方法为item的UI绑定展示数据
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof VH){
            VH viewHolder = (VH)holder;
            ComicData comicData = mDatas.get(position);
            String name = comicData.getName();
            int l = name.length();
            if(name.getBytes().length>=160){
                name = name.substring(0,(l<150?l:150))+"...";
            }
            viewHolder.comictitle.setText(name);
            viewHolder.comictime.setText("上传时间："+comicData.getTime());
            //加载图片
            String url = SystemParameter.PATHURL+"/resource/"+comicData.getPath()+"/0001.jpg?v="+SystemParameter.VERSION;
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading);
            Glide.with(context).load(url).apply(options).into(viewHolder.comicimg);
            //定义点击和长按事件
            //单击
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MainComicDetailsActivity.class);
                    Bundle data = new Bundle();
                    data.putString("comic", JSON.toJSONString(mDatas.get(position)));
                    intent.putExtra("data",data);
                    context.startActivity(intent);
                }
            });
            //长按
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return  true;//表示此事件已经消费，不会触发单击事件
                }
            });
        }
    }

    //这个方法就类似于BaseAdapter的getCount方法了，即总共有多少个条目。
    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
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

    //对外方法 提供上划
    public FooterHolder getFooterHolder(){
        return footerHolder;
    }

    //展示漫画的ViewHolder
    private static class VH extends RecyclerView.ViewHolder{
        public final ImageView comicimg;
        public final TextView comictitle;
        public final TextView comictime;
        public VH(View v) {
            super(v);
            comicimg = v.findViewById(R.id.dashboard_comicimg);
            comictitle = v.findViewById(R.id.dashboard_comictitle);
            comictime = v.findViewById(R.id.dashboard_comictime);
        }
    }
}