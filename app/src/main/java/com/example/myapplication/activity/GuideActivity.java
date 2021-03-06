package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.tool.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;//需要ViewPaeger
    private PagerAdapter mAdapter;//需要PagerAdapter适配器
    private List<View> mViews = new ArrayList<>();//准备数据源
    private Button bt_home;//在ViewPager的最后一个页面设置一个按钮，用于点击跳转到MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setStatusColor();
        setSystemInvadeBlack();
        initView();//初始化view
    }

    @Override
    protected void setStatusColor() {
        StatusBarUtil.setColor(this, Color.parseColor("#00FFFFFF"));
    }

//    @Override
    protected void setSystemInvadeBlack() {
        StatusBarUtil.setTranslucent(this);
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);

        LayoutInflater inflater = LayoutInflater.from(this);//将每个xml文件转化为View
        View guideOne = inflater.inflate(R.layout.fragment_welcome_guidance01, null);//每个xml中就放置一个imageView
        View guideTwo = inflater.inflate(R.layout.fragment_welcome_guidance02, null);
        View guideThree = inflater.inflate(R.layout.fragment_welcome_guidance03, null);

        mViews.add(guideOne);//将view加入到list中
        mViews.add(guideTwo);
        mViews.add(guideThree);

        mAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);//初始化适配器，将view加到container中
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = mViews.get(position);
                container.removeView(view);//将view从container中移除
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;//判断当前的view是我们需要的对象
            }
        };

        viewPager.setAdapter(mAdapter);
        bt_home= guideThree.findViewById(R.id.to_Main);
        bt_home.setOnClickListener((View)-> {
                Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        );

    }
}
