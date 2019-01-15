package com.example.myapplication.tool;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jaeger.library.StatusBarUtil;


public class BaseActivity extends AppCompatActivity {
    /*工具栏*/
    private BaseToolbar toolBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //基类设置默认值。
        setStatusColor();
//        setSystemInvadeBlack();
    }

    protected void setStatusColor() {
        //设置状态栏的颜色
        StatusBarUtil.setColor(this, Color.parseColor("#008577"));
    }

//    protected void setSystemInvadeBlack() {
//        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
//        StatusUtil.setSystemStatus(this, true, false);
//    }

    /*初始化toolbar*/
    public void initToolbar(int res) {
        toolBar=findViewById(res);
        toolBar.setTitle("");
        toolBar.setTitleTextColor(Color.WHITE);
    }

    //设置中间title的内容
    public void setMainTitle(String text) {
        this.toolBar.setMainTitle(text);
    }

    //设置中间title的内容文字的颜色
    public void setMainTitleColor(int color) {
        this.toolBar.setMainTitleColor(color);
    }

    //设置title左边文字
    public void setLeftTitleText(String text) {
        this.toolBar.setLeftTitleText(text);
    }

    //设置title左边文字颜色
    public void setLeftTitleColor(int color) {
        this.toolBar.setLeftTitleColor(color);
    }

    //设置title左边图标
    public void setLeftTitleDrawable(int res) {
        this.toolBar.setLeftTitleDrawable(res);
    }

    //设置title左边点击事件
    public void setLeftTitleClickListener(View.OnClickListener onClickListener) {
        this.toolBar.setLeftTitleClickListener(onClickListener);
    }

    //设置title右边文字
    public void setRightTitleText(String text) {
        this.toolBar.setRightTitleText(text);
    }

    //设置title右边文字颜色
    public void setRightTitleColor(int color) {
        this.toolBar.setRightTitleColor(color);
    }

    //设置title右边图标
    public void setRightTitleDrawable(int res) {
        this.toolBar.setRightTitleDrawable(res);
    }

    //设置title右边点击事件
    public void setRightTitleClickListener(View.OnClickListener onClickListener) {
        this.toolBar.setRightTitleClickListener(onClickListener);
    }

    //handler内存泄漏的解决办法
//    protected static class MyActivityHandler extends Handler {
//        WeakReference<Activity> mWeakReference;
//        Activity activity;
//        public MyActivityHandler(Activity activity) {
//            mWeakReference=new WeakReference<Activity>(activity);
//            activity = mWeakReference.get();
//        }
//        @Override
//        public void handleMessage(Message message) {
//             if(activity==null){
//                 activity = activityclass.this;
//             }
//        }
//    }
}