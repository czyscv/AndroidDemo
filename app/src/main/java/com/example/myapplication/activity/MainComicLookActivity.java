package com.example.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;

import com.bm.library.PhotoView;
import com.example.myapplication.R;
import com.example.myapplication.tool.BaseActivity;

public class MainComicLookActivity extends BaseActivity {
    private PhotoView photoView;
    private SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_look);
        initToolbar(R.id.main_comic_look_toolbar);
        initView();
    }

    private void initView(){
        photoView = findViewById(R.id.main_comic_look_img);
        seekBar = findViewById(R.id.main_comic_look_seekBar);
        setLeftTitleText("返回");
        setLeftTitleDrawable(R.mipmap.ic_back);
        setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
