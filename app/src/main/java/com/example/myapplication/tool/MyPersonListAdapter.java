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

import java.util.ArrayList;
import java.util.List;

public class MyPersonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Integer[] icon;
    private List<String> mData;//数据
    private String[] values;
    private Context context;

    public MyPersonListAdapter(Context context, Integer[] icon, List<String> data, String... values) {
        if (mData == null) {
            Log.e("Warning","MyPersonListAdapter mData must not be null");
            mData = new ArrayList<>();
        }
        this.context = context;
        this.icon = icon;
        this.mData = data;
        this.values = values;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_person_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyPersonListAdapter.VH) {
            MyPersonListAdapter.VH viewHolder = (MyPersonListAdapter.VH) holder;
            RequestOptions options = new RequestOptions().error(R.mipmap.ic_warn);
            Glide.with(context).load(icon[position]).apply(options).into(viewHolder.icon);
            viewHolder.title.setText(mData.get(position));
            if (position<values.length && values[position] != null && !values[position].equals("")){
                viewHolder.value.setText(values[position]);
            }
            //单击
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, MainComicLookActivity.class);
//                    Bundle data = new Bundle();
//                    data.putString("comic", JSON.toJSONString(mDatas.get(position)));
//                    intent.putExtra("data",data);
//                    context.startActivity(intent);
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
        return mData.size();
    }

    //展示章节的ViewHolder
    private static class VH extends RecyclerView.ViewHolder{
        public final ImageView icon;
        public final TextView title;
        public final TextView value;
        public VH(View v) {
            super(v);
            icon = v.findViewById(R.id.main_person_list_icon);
            title = v.findViewById(R.id.main_person_list_name);
            value = v.findViewById(R.id.main_person_list_value);
        }
    }
}
