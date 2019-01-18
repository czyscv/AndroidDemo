package com.example.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.adapter.MyCollectionListAdapter;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.tool.ComicData;
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainComicCollectionActivity extends BaseActivity {
    private SearchView searchView;//搜索框
    private RecyclerView recyclerView;//列表视图
    private MyCollectionListAdapter myCollectionListAdapter;//适配器
    private List<ComicData> dataList = new ArrayList<>();
    private List<ComicData> dataListCopy = new ArrayList<>();
    private Integer page = 1;//显示的页数
    private static final Integer EACH = 50;//每页显示的数量

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SystemParameter.SUCCESS){
                myCollectionListAdapter.notifyDataSetChanged();
                if (dataList.size()<(page*EACH)){
                    myCollectionListAdapter.getFooterHolder().setData(2);
                }else{
                    myCollectionListAdapter.getFooterHolder().setData(1);
                }
            }else{
                myCollectionListAdapter.getFooterHolder().setData(3);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_collection);
        initToolbar(R.id.main_comic_collection_toolbar);
        searchView = findViewById(R.id.main_comic_collection_searchview);
        recyclerView = findViewById(R.id.main_comic_collection_recyclerview);
        initData();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainComicCollectionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        myCollectionListAdapter = new MyCollectionListAdapter(dataList,MainComicCollectionActivity.this);
        recyclerView.setAdapter(myCollectionListAdapter);
    }

    private void initData(){
        MyOkhttp myOkhttp = new MyOkhttp();
        myOkhttp.setUrl("/collection_info/getConnectionManListByUserId");
        myOkhttp.addParameter(new String[]{"token","first","each"},new String[]{SystemParameter.TOKEN, (page++).toString(), EACH.toString()});
        myOkhttp.myGetOkhttp();
        myOkhttp.request(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(SystemParameter.ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String info = jsonObject.getString("info");
                if ("success".equals(info)){
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0 ;i<data.size();i++){
                        JSONObject obj = data.getJSONObject(i);
                        ComicData comicData = JSON.parseObject(obj.toJSONString(),ComicData.class);
                        dataList.add(comicData);
                        dataListCopy.add(comicData);
                    }
                    handler.sendEmptyMessage(SystemParameter.SUCCESS);
                }else if ("not_login".equals(info)){
                    new MainActivity().getActivityHandler().sendEmptyMessage(SystemParameter.NO_LOGIN);
                }else{
                    handler.sendEmptyMessage(SystemParameter.ERROR);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
