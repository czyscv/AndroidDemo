package com.example.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.tool.BaseFragment;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegisterFragment extends BaseFragment {
    private int time = 60;
    private boolean flag = false;
    private View view;
    private Handler handler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LoginActivity){
            handler =  ((LoginActivity)context).getActivityHandler();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_register, null);
        initView();
        //新建一个守护计时器 每秒钟传一次消息
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                if (flag){
                    Message message = Message.obtain();
                    message.what = 0x1234;
                    message.obj = time;
                    handler.sendMessage(message);
                    if(time<=0){
                        time = 60;
                        flag = false;
                    }
                    time--;
                }
            }
        }, 0,1000);
        return view;
    }

    private void initView() {
        Button getCodeButton = view.findViewById(R.id.get_code_button);
        getCodeButton.setOnClickListener((View) -> {
            TextView mobilephoneview = view.findViewById(R.id.register_Mobilephone);
            String mobilephone = mobilephoneview.getText().toString();
            if (mobilephone.length() != 11) {
                Toast.makeText(getContext(), "请输入正确的11位手机号", Toast.LENGTH_SHORT).show();
            } else {
                flag = true;
                getCodeButton.setClickable(false);
                //1.创建OkHttpClient对象
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://www.skythinking.cn:7777/user_info/send_message_to_telephone?telephone="+mobilephone).get().build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    //请求失败执行的方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message = Message.obtain();
                        message.what = 0x2222;
                        message.obj = "error";
                        handler.sendMessage(message);
                    }
                    //请求成功执行的方法
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        Message message = Message.obtain();
                        message.what = 0x2222;
                        message.obj = info;
                        handler.sendMessage(message);
                    }
                });
            }
        });
        TextView passwordview2 = view.findViewById(R.id.register_passworld2);
        passwordview2.setOnFocusChangeListener((View,hasFocus)->{
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
            } else {
                TextView passwordview = view.findViewById(R.id.register_passworld);
                String password = passwordview.getText().toString();
                String password2 = passwordview2.getText().toString();
                if(!password.equals(password2)){
                    Toast.makeText(getContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button registerButton = view.findViewById(R.id.user_register);
        registerButton.setOnClickListener((View)->{
            TextView phoneview = view.findViewById(R.id.register_Mobilephone);
            TextView passwordview = view.findViewById(R.id.register_passworld);
            TextView codeview = view.findViewById(R.id.register_code);
            String phone = phoneview.getText().toString();
            String password = passwordview.getText().toString();
            String password2 = passwordview2.getText().toString();
            String code = codeview.getText().toString();
            if(!password.equals(password2)){
                Toast.makeText(getContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            }else{
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody formBody = new FormBody.Builder()
                        .add("telephone", phone)
                        .add("password",password)
                        .add("code",code).build();
                Request request = new Request.Builder().url("http://www.skythinking.cn:7777/user_info/register").post(formBody).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    //请求失败执行的方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message = Message.obtain();
                        message.what = 0x3333;
                        message.obj = "error";
                        handler.sendMessage(message);
                    }
                    //请求成功执行的方法
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        if ("send success".equals(info)){
                            Message message = Message.obtain();
                            message.what = 0x3333;
                            message.obj = info;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
    }
}
