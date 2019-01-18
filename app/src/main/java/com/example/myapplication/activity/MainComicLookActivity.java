package com.example.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.R;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.tool.ComicData;
import com.example.myapplication.adapter.MyPhotoViewAdapter;
import com.example.myapplication.tool.SystemParameter;

import java.util.ArrayList;
import java.util.List;

public class MainComicLookActivity extends BaseActivity {
    private ComicData comicData;
    private List<String> comicImgUrl = new ArrayList<>();
    private Integer nowPage;
    private Integer maxPage;
    private ViewPager viewPager;
    private MyPhotoViewAdapter mPhotoViewAdapter;
    private SeekBar seekBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_look);
        initToolbar(R.id.main_comic_look_toolbar);
        viewPager = findViewById(R.id.main_comic_look_viewpage);
        seekBar = findViewById(R.id.main_comic_look_seekBar);
        InitData();
        initView();
    }

    private void initView(){
        setMainTitle("1/"+comicData.getPageNum());
        setLeftTitleText("返回");
        setLeftTitleDrawable(R.mipmap.ic_back);
        setLeftTitleClickListener((view)->{
            finish();
        });
        //view视图
        mPhotoViewAdapter = new MyPhotoViewAdapter(comicImgUrl,MainComicLookActivity.this);
        viewPager.setAdapter(mPhotoViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                nowPage = position+1;
                setMainTitle(nowPage+"/"+maxPage);
                seekBar.setProgress(nowPage);
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        //进度条
        seekBar.setMax(maxPage);
        seekBar.setProgress(1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<1){
                    i = 1;
                    seekBar.setProgress(i);
                }
                nowPage = i;
                setMainTitle(nowPage+"/"+maxPage);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                viewPager.setCurrentItem(nowPage-1,true);
            }
        });
    }

    private void InitData(){
        String c = getIntent().getBundleExtra("data").getString("comic");
        comicData = JSON.parseObject(c,ComicData.class);
        String path = comicData.getPath();
        maxPage = comicData.getPageNum();
        for(int i =1 ; i<=maxPage ; i++){
            String url = SystemParameter.PATHURL+"/resource/"+comicData.getPath()+"/";
            if (i<10){
                url = url + "000";
            }else if(i<100){
                url = url + "00";
            }else if(i<1000){
                url = url + "0" ;
            }
            url = url + i + ".jpg?v="+SystemParameter.VERSION+"&token="+SystemParameter.TOKEN;
            comicImgUrl.add(url);
        }
    }
}
