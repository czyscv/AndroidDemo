package com.example.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.tool.MyComicListAdapter;

public class MainComicCollectionActivity extends BaseActivity {
    private SearchView searchView;//搜索框
    private RecyclerView recyclerView;//列表视图
    private MyComicListAdapter myComicListAdapter;//适配器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_collection);
        initToolbar(R.id.main_comic_collection_toolbar);
        searchView = findViewById(R.id.main_comic_collection_searchview);
        recyclerView = findViewById(R.id.main_comic_collection_recyclerview);
        initView();
    }
    private void initView(){
        setMainTitle("我的收藏");
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
