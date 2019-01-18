package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.fragment.LoginFragment;
import com.example.myapplication.R;
import com.example.myapplication.fragment.RegisterFragment;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.adapter.MyFragmentAdapter;
import com.example.myapplication.tool.SystemParameter;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private ViewPager viewPager;
    private MyFragmentAdapter mAdapter;//适配器
    private TabLayout tabLayout;//头部选项卡
    private List<Fragment> fragmentlist = new ArrayList<>();
    private List<String > titlelist = new ArrayList<>();

    public static final Integer LOGIN_SUCCESS = 0x9999;
    public static final Integer LOGIN_ACCOUNT_NOT_EXIST = 0X9998;
    public static final Integer SEND_SUCCESS = 0X9997;
    public static final Integer TEL_IS_ALREADY_TO_USED = 0X9996;
    public static final Integer TEL_NUMBER_ERROR = 0X9995;
    public static final Integer REGISTER_SUCCESS = 0X9994;
    public static final Integer CODE_ERROR = 0X9993;
    public static final Integer CODE_OVERDUE = 0X9992;
    public static final Integer UPDATEUI = 0X9991;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SystemParameter.ERROR){
                Toast.makeText(LoginActivity.this, "网络错误 请检查网络", Toast.LENGTH_SHORT).show();
            }else if (msg.what == LOGIN_SUCCESS){
                Toast.makeText(LoginActivity.this, "欢迎回来", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else if (msg.what == LOGIN_ACCOUNT_NOT_EXIST){
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == SEND_SUCCESS){
                Toast.makeText(LoginActivity.this, "验证码已经发送 请查收", Toast.LENGTH_SHORT).show();
            }else if (msg.what == TEL_IS_ALREADY_TO_USED){
                Toast.makeText(LoginActivity.this, "该手机已经被注册了", Toast.LENGTH_SHORT).show();
            }else if (msg.what == TEL_NUMBER_ERROR){
                Toast.makeText(LoginActivity.this, "手机号错误 请检查后重试", Toast.LENGTH_SHORT).show();
            }else if (msg.what == REGISTER_SUCCESS){
                Toast.makeText(LoginActivity.this, "注册成功 ", Toast.LENGTH_SHORT).show();
            }else if (msg.what == CODE_ERROR){
                Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == CODE_OVERDUE){
                Toast.makeText(LoginActivity.this, "验证码过期", Toast.LENGTH_SHORT).show();
            }else if (msg.what == UPDATEUI) {
                Button button = findViewById(R.id.get_code_button);
                if (button != null){
                    Integer time = Integer.valueOf(msg.obj.toString());
                    if (time == 0) {
                        button.setText("获取验证码");
                        button.setClickable(true);
                    } else{
                        if (time>=10){
                            button.setText(time + "秒后重发");
                        }else{
                            button.setText("0"+time + "秒后重发");
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar(R.id.login_toolbar);
        setMainTitle("欢迎加入我们");
        initView();
    }

    //初始化控件
    private void initView() {
        //获得viwepager并向里加入注册和登录的两个子页面
        viewPager = findViewById(R.id.login_viewpage);
        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        fragmentlist.add(loginFragment);
        fragmentlist.add(registerFragment);
        //设置tab
        tabLayout = findViewById(R.id.login_tabLayout);
        titlelist.add("登录");
        titlelist.add("注册");
        tabLayout.addTab(tabLayout.newTab().setText(titlelist.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titlelist.get(1)));
        //适配器
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(),fragmentlist,titlelist);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //让下层fragment得到handler
    public Handler getActivityHandler() {
        return handler;
    }

    //结束资源清理 防止内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}
