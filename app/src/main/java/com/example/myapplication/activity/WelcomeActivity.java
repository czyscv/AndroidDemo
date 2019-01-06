package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.R;

public class WelcomeActivity extends BaseActivity {
    private static final int TIME = 3000;
    private static final int GO_MAIN = 100;
    private static final int GO_GUIDE = 101;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_MAIN:
                    goMain();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init() {
        SharedPreferences sf = getSharedPreferences("data", MODE_PRIVATE);//判断是否是第一次进入
        boolean isFirstIn = sf.getBoolean("isFirstIn", true);
        SharedPreferences.Editor editor = sf.edit();
        if (isFirstIn) {     //若为true，则是第一次进入
            editor.putBoolean("isFirstIn", false);
            mhandler.sendEmptyMessageDelayed(GO_GUIDE, TIME);//将欢迎页停留5秒，并且将message设置为跳转到引导页SplashActivity，跳转在goGuide中实现
        } else {
            mhandler.sendEmptyMessageDelayed(GO_MAIN, TIME);//将欢迎页停留5秒，并且将message设置文跳转到 MainActivity，跳转功能在goMain中实现
        }
        editor.commit();

    }

    private void goMain() {
        //检测是否有登录
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(
                WelcomeActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
