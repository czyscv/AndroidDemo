package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TabHost;

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
        viewPager = findViewById(R.id.login_viewpage);
        LoginFragment loginFragment = new LoginFragment();
        RegisterFragment registerFragment = new RegisterFragment();
        fragmentlist.add(loginFragment);
        fragmentlist.add(registerFragment);

        tabLayout = findViewById(R.id.login_tabLayout);
        titlelist.add("登录");
        titlelist.add("注册");
        tabLayout.addTab(tabLayout.newTab().setText(titlelist.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titlelist.get(1)));
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(),fragmentlist,titlelist);
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }
}
