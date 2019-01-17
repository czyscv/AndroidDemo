package com.example.myapplication.tool;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;

import java.util.List;

public class MyPhotoViewAdapter extends PagerAdapter {
    private List<String> mData;
    private Context context;

    public MyPhotoViewAdapter(List<String> mData,Context context){
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String url = mData.get(position);
        PhotoView photoView = new PhotoView(context);
        // 启用图片缩放功能
        photoView.enable();
        // 获取/设置 最大缩放倍数
        photoView.setMaxScale(2);
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().placeholder(R.mipmap.loading))
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
