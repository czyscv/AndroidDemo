package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.tool.BaseFragment;
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;
import com.example.myapplication.tool.UserData;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends BaseFragment {
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
        view = inflater.inflate(R.layout.fragment_login_login,null);
        initView();
        return view;
    }

    private void initView() {
        Button login = view.findViewById(R.id.user_login);
        login.setOnClickListener((View)->{
            TextView userCodeView = view.findViewById(R.id.login_usercode);
            TextView userPasswordView = view.findViewById(R.id.login_passworld);
            String userCode = userCodeView.getText().toString();
            String password = userPasswordView.getText().toString();
            MyOkhttp myOkhttp = new MyOkhttp();
            myOkhttp.setUrl("/user_info/login");
            myOkhttp.addParameter(new String[]{"account","password"},new String[]{userCode,password});
            myOkhttp.myGetOkhttp();
            myOkhttp.request(new Callback() {
                //请求失败执行的方法
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = Message.obtain();
                    message.what = 0x1111;
                    message.obj = "error";
                    handler.sendMessage(message);
                }
                //请求成功执行的方法
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    JSONObject jsonObject = JSON.parseObject(response.body().string());
                    String info = jsonObject.getString("info");
                    if(info.equals("login_success")){
                        String token = jsonObject.getJSONObject("data").getString("token");
                        SharedPreferences sf = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString("token",token);
                        editor.apply();
                        SystemParameter.TOKEN = token;
                        SystemParameter.USERINFO = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), UserData.class);
                    }
                    Message message = Message.obtain();
                    message.what = 0x1111;
                    message.obj = info;
                    handler.sendMessage(message);
                }
            });
        });
    }
}
