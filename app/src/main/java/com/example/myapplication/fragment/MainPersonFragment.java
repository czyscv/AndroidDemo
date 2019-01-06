package com.example.myapplication.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.myapplication.R;
import com.example.myapplication.tool.BaseFragment;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainPersonFragment extends BaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main_person,null);
        Glide.with(this)
                .load(R.mipmap.user_head)
                .bitmapTransform(new BlurTransformation(view.getContext()),new CenterCrop(view.getContext()))
                .into((ImageView) view.findViewById(R.id.h_back));

        Glide.with(this)
                .load(R.mipmap.user_head)
                .bitmapTransform(new CropCircleTransformation(view.getContext()))
                .into((ImageView) view.findViewById(R.id.h_head));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}