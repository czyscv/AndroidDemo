package com.example.myapplication.tool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
    private View view;
    private BaseToolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initToolbar(View view,int res){
        this.view = view;
        this.toolbar = view.findViewById(res);
    }
    //设置中间title的内容
    public void setMainTitle(String text) {
        this.toolbar.setMainTitle(text);
    }
    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color) {
        this.toolbar.setMainTitleColor(color);
    }

    //设置title左边文字和颜色
    public void setLeftTitleText(String text) {
        this.toolbar.setLeftTitleText(text);
    }
    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        this.toolbar.setLeftTitleColor(color);
    }
    //设置title左边图标
    public void setLeftTitleDrawable(int res) {
        this.toolbar.setLeftTitleDrawable(res);
    }
    //设置title左边点击事件
    public void setLeftTitleClickListener(View.OnClickListener onClickListener){
        this.toolbar.setLeftTitleClickListener(onClickListener);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        this.toolbar.setRightTitleText(text);
    }
    //设置title右边文字颜色
    public void setRightTitleColor(int color) {
        this.toolbar.setRightTitleColor(color);
    }
    //设置title右边图标
    public void setRightTitleDrawable(int res) {
        this.toolbar.setRightTitleDrawable(res);
    }
    //设置title右边点击事件
    public void setRightTitleClickListener(View.OnClickListener onClickListener){
        this.toolbar.setRightTitleClickListener(onClickListener);
    }

}
