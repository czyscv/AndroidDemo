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
import com.example.myapplication.tool.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private ViewPager viewPager;
    private MyFragmentAdapter mAdapter;//适配器
    private TabLayout tabLayout;//头部选项卡
    private List<Fragment> fragmentlist = new ArrayList<>();
    private List<String > titlelist = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0x1111 ) {
                //登录用的
               switch (message.obj.toString()){
                   case "error":
                       Toast.makeText(LoginActivity.this, "登陆失败 请检查网络后重试", Toast.LENGTH_SHORT).show();
                       break;
                   case "account_not_exist":
                       Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                       break;
                   case "login_success":
                       Toast.makeText(LoginActivity.this, "欢迎回来", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                       startActivity(intent);
                       finish();
                       break;
                   default:
                       Toast.makeText(LoginActivity.this, "登录失败",Toast.LENGTH_SHORT).show();
               }
            }else if(message.what == 0x2222){
                //发送验证码
                switch (message.obj.toString()){
                    case "send success":
                        Toast.makeText(LoginActivity.this, "验证码已经发送 请查收", Toast.LENGTH_SHORT).show();
                        break;
                    case "telephone is already to used":
                        Toast.makeText(LoginActivity.this, "该手机已经被注册了", Toast.LENGTH_SHORT).show();
                        break;
                    case "mobile_number_error":
                        Toast.makeText(LoginActivity.this, "手机号不存在 请检查后重试", Toast.LENGTH_SHORT).show();
                        break;
                    case "error":
                        Toast.makeText(LoginActivity.this, "验证码请求失败 请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }else if(message.what == 0x3333){
                //注册
                switch (message.obj.toString()){
                    case "send success":
                        Toast.makeText(LoginActivity.this, "注册成功 ", Toast.LENGTH_SHORT).show();
                        break;
                    case "telephone_repeat":
                        Toast.makeText(LoginActivity.this, "注册失败 该号码已经注册过了", Toast.LENGTH_SHORT).show();
                        break;
                    case "code_error":
                        Toast.makeText(LoginActivity.this, "注册失败 验证码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "code_overdue":
                        Toast.makeText(LoginActivity.this, "注册失败 验证码过期", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }else if (message.what == 0x1234) {
                Button button = findViewById(R.id.get_code_button);
                Integer time = Integer.valueOf(message.obj.toString());
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
