package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.tool.BaseFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginFragment extends BaseFragment {
    private View view;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0x3333 ) {
               //TODO
                Toast.makeText(getContext(),"api出错了",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_login,null);
        initView();
        return view;
    }

    private void initView() {
        Button login = view.findViewById(R.id.user_login);
        login.setOnClickListener((View)->{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
            //TODO
            TextView userCodeView = view.findViewById(R.id.login_usercode);
            TextView userPasswordView = view.findViewById(R.id.login_passworld);
            String userCode = userCodeView.getText().toString();
            String password = userPasswordView.getText().toString();
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()
                    .add("account", userCode)
                    .add("password",password).build();
            Request request = new Request.Builder().url("http://www.skythinking.cn:8437/user_info/login").post(formBody).build();
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
        });
    }
}
