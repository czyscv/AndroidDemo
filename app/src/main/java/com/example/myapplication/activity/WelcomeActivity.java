package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.tool.SystemParameter;
import com.example.myapplication.tool.UserData;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends BaseActivity {
    private static final int TIME = 3000;
    private static final int GO_GUIDE = 100;
    private static final int GO_LOGIN= 101;
    private static final int GO_MAIN = 102;
    private static final int GO_MAIN_ERROR= 103;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    goGuide();
                    break;
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_MAIN:
                    goMain();
                    break;
                case GO_MAIN_ERROR:
                    Toast.makeText(WelcomeActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    goMain();
                    break;
                default:
                    Toast.makeText(WelcomeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    goLogin();
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
            handler.sendEmptyMessageDelayed(GO_GUIDE, TIME);//将欢迎页停留，并且将message设置为跳转到引导页SplashActivity，跳转在goGuide中实现
        } else {
            String token = sf.getString("token",null);
            if(token==null){
                handler.sendEmptyMessageDelayed(GO_MAIN, TIME);//将欢迎页停留，并且将message设置文跳转到 LoginActivity，跳转功能在goMain中实现
            }else{
                //自动登录
                SystemParameter.TOKEN = token;
                String url = SystemParameter.PATHURL+"/user_info/autoLogin?token="+token;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    //请求失败执行的方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(GO_MAIN_ERROR);
                    }
                    //请求成功执行的方法
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        if(info.equals("success")){
                            handler.sendEmptyMessageDelayed(GO_MAIN, TIME);
                            SystemParameter.USERINFO = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserData.class);
                        }else{
                            handler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
                        }
                    }
                });
            }
        }
        editor.apply();

    }

    private void goLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void goMain() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //结束资源清理 防止内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
