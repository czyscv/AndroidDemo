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
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RegisterFragment extends BaseFragment {
    private int time = 60;
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
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = LoginActivity.UPDATEUI;
                        message.obj = time;
                        handler.sendMessage(message);
                        if(time>0){
                            time--;
                        }else{
                            time = 60;
                            this.cancel();
                        }
                    }
                }, 0,1000);
                getCodeButton.setClickable(false);
                //1.创建OkHttpClient对象
                MyOkhttp myOkhttp = new MyOkhttp();
                myOkhttp.setUrl("/user_info/send_message_to_telephone");
                myOkhttp.addParameter(new String[]{"telephone"}, new String[]{mobilephone});
                myOkhttp.myGetOkhttp();
                myOkhttp.request(new Callback() {
                    //请求失败执行的方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(SystemParameter.ERROR);
                    }
                    //请求成功执行的方法
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        if ("send success".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.SEND_SUCCESS);
                        }else if ("telephone is already to used".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.TEL_IS_ALREADY_TO_USED);
                        }else if ("mobile_number_error".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.TEL_NUMBER_ERROR);
                        }else{
                            handler.sendEmptyMessage(LoginActivity.TEL_IS_ALREADY_TO_USED);
                        }
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
                MyOkhttp myOkhttp = new MyOkhttp();
                myOkhttp.setUrl("/user_info/register");
                myOkhttp.addParameter(new String[]{"telephone","password","code"}, new String[]{phone,password,code});
                myOkhttp.myPostOkhttp();
                myOkhttp.request(new Callback() {
                    //请求失败执行的方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(SystemParameter.ERROR);
                    }
                    //请求成功执行的方法
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        if ("register_success".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.REGISTER_SUCCESS);
                        }else if ("telephone_repeat".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.TEL_IS_ALREADY_TO_USED);
                        }else if ("code_error".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.CODE_ERROR);
                        }else if ("code_overdue".equals(info)){
                            handler.sendEmptyMessage(LoginActivity.CODE_OVERDUE);
                        }else {
                            handler.sendEmptyMessage(LoginActivity.CODE_ERROR);
                        }
                    }
                });
            }
        });
    }
}
