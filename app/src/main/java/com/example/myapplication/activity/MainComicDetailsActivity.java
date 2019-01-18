package com.example.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.tool.ComicData;
import com.example.myapplication.adapter.MyChapterListAdapter;
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainComicDetailsActivity extends BaseActivity {
    private ComicData comicData = new ComicData();//漫画信息数据
    private List<ComicData> chapterInfoList = new ArrayList<>();//漫画数据列表
    private Button comicComment;//评论按钮
    private RecyclerView chapterListView;
    private MyChapterListAdapter mChapterListAdapter;

    private static final Integer IS_CONNECT = 0x9999;
    private static final Integer NO_CONNECT = 0x9998;
    private static final Integer ERROR_CONNECT = 0x9997;
    private static final Integer DELETE_CONNECT = 0x9996;
    private static final Integer SUCCESS_CONNECT = 0x9995;
    private static final Integer REPATE_CONNECT = 0x9994;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==SystemParameter.ERROR){
                Toast.makeText(MainComicDetailsActivity.this, "加载失败 检查网络", Toast.LENGTH_SHORT).show();
                mChapterListAdapter.getFooterHolder().setData(3);
            }else if (msg.what == SystemParameter.SUCCESS ) {
                mChapterListAdapter.notifyDataSetChanged();
                mChapterListAdapter.getFooterHolder().setData(2);
            }else if (msg.what == IS_CONNECT){
                isConnect();
            }else if (msg.what == NO_CONNECT){
                noConnect();
            }else if (msg.what == ERROR_CONNECT){
                Toast.makeText(MainComicDetailsActivity.this, "收藏状态加载失败", Toast.LENGTH_SHORT).show();
                confirmConnect();
            }else if (msg.what == DELETE_CONNECT){
                Toast.makeText(MainComicDetailsActivity.this, "漫画取消成功", Toast.LENGTH_SHORT).show();
                noConnect();
            }else if (msg.what == SUCCESS_CONNECT){
                Toast.makeText(MainComicDetailsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                isConnect();
            }else if (msg.what == REPATE_CONNECT){
                Toast.makeText(MainComicDetailsActivity.this, "你已经收藏了该漫画", Toast.LENGTH_SHORT).show();
                isConnect();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_details);
        initToolbar(R.id.main_comic_details_toolbar);
        initData();
        initView();
    }

    private void initView(){
        //设置toolbar
        setMainTitle("漫画详情");
        setRightTitleText("请稍等...");
        setLeftTitleText("返回");
        setLeftTitleDrawable(R.mipmap.ic_back);
        setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        comicComment = findViewById(R.id.main_comic_details_comment);
        chapterListView = findViewById(R.id.main_comic_details_chapterlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainComicDetailsActivity.this);
        //设置为水平布局
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        chapterListView.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        chapterListView.setItemAnimator( new DefaultItemAnimator());
        //设置Adapter
        mChapterListAdapter = new MyChapterListAdapter(chapterInfoList,MainComicDetailsActivity.this);
        chapterListView.setAdapter(mChapterListAdapter);
    }

    private void initData(){
        //接收item传来的对象json字符串
        String c = getIntent().getBundleExtra("data").getString("comic");
        comicData = JSON.parseObject(c,ComicData.class);
        //判断收藏状态
        confirmConnect();
        //绑定控件
        TextView comicname = findViewById(R.id.main_comic_details_title);
        ImageView comicimg = findViewById(R.id.main_comic_details_comicimg);
        TextView comictime = findViewById(R.id.main_comic_details_update);
        TextView comicauthor = findViewById(R.id.main_comic_details_author);
        TextView comiclv = findViewById(R.id.main_comic_details_lv);
        TextView comicpage = findViewById(R.id.main_comic_details_page);
        //设置属性
        comicname.setText(comicData.getName());
        String url = SystemParameter.PATHURL+"/resource/"+comicData.getPath()+"/0001.jpg?v="+SystemParameter.VERSION;
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading_list);
        Glide.with(MainComicDetailsActivity.this).load(url).apply(options).into(comicimg);
        comictime.setText("上传时间："+comicData.getTime());
        comicauthor.setText("作者："+comicData.getAuthor());
        comiclv.setText("限制等级："+(comicData.getLimitLevel()==1?"注册用户":"会员用户"));
        comicpage.setText("章节数："+comicData.getPageNum());
        //获得漫画列表
        MyOkhttp myOkhttp = new MyOkhttp();
        myOkhttp.setUrl("/manhua/getCartoonDetail");
        myOkhttp.addParameter(new String[]{"manId","token"}, new String[]{comicData.getId().toString(), SystemParameter.TOKEN});
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
                if(info.equals("success")){
                    JSONObject data = jsonObject.getJSONObject("data");
                    //漫画列表
                    JSONArray comicjsonArray = data.getJSONArray("chapterInfoList");
                    for(int i = 0; i<comicjsonArray.size();i++){
                        JSONObject obj = comicjsonArray.getJSONObject(i);
                        Integer id = Integer.valueOf(obj.getString("id"));
                        String name = obj.getString("name");
                        String path = obj.getString("path");
                        Integer pageNum = Integer.valueOf(obj.getString("pageNum"));
                        Integer shows = Integer.valueOf(obj.getString("shows"));
                        Integer sortValue = Integer.valueOf(obj.getString("sortValue"))-1;
                        if(shows==1){
                            ComicData comicData = new ComicData();
                            comicData.setId(id);
                            comicData.setName(name);
                            comicData.setPath(path);
                            comicData.setPageNum(pageNum);
                            chapterInfoList.add(sortValue,comicData);
                        }
                    }
                    handler.sendEmptyMessage(SystemParameter.SUCCESS);
                }else{
                    handler.sendEmptyMessage(SystemParameter.ERROR);
                }
            }
        });
    }
    private void confirmConnect(){
        MyOkhttp myOkhttp = new MyOkhttp();
        myOkhttp.setUrl("/collection_info/isConnect");
        myOkhttp.addParameter(new String[]{"token","manId"}, new String[]{SystemParameter.TOKEN, comicData.getId().toString()});
        myOkhttp.myGetOkhttp();
        myOkhttp.request(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(ERROR_CONNECT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String info = jsonObject.getString("info");
                if ("yes".equals(info)){
                    handler.sendEmptyMessage(IS_CONNECT);
                }else {
                    handler.sendEmptyMessage(NO_CONNECT);
                }
            }
        });
    }

    private void isConnect(){
        setRightTitleText("取消收藏");
        setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightTitleText("请稍等...");
                MyOkhttp myOkhttp = new MyOkhttp();
                myOkhttp.setUrl("/collection_info/cancelCollect");
                myOkhttp.addParameter(new String[]{"token","manId"}, new String[]{SystemParameter.TOKEN, comicData.getId().toString()});
                myOkhttp.myGetOkhttp();
                myOkhttp.request(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(ERROR_CONNECT);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        handler.sendEmptyMessage(DELETE_CONNECT);
                    }
                });
            }
        });
    }
    private void noConnect(){
        setRightTitleText("点击收藏");
        setRightTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightTitleText("请稍等...");
                MyOkhttp myOkhttp = new MyOkhttp();
                myOkhttp.setUrl("/collection_info/collect");
                myOkhttp.addParameter(new String[]{"token","manId","action"}, new String[]{SystemParameter.TOKEN, comicData.getId().toString(),"1"});
                myOkhttp.myGetOkhttp();
                myOkhttp.request(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(ERROR_CONNECT);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSON.parseObject(response.body().string());
                        String info = jsonObject.getString("info");
                        if ("success".equals(info)){
                            handler.sendEmptyMessage(SUCCESS_CONNECT);
                        }else if ("repate".equals(info)){
                            handler.sendEmptyMessage(REPATE_CONNECT);
                        }else{
                            handler.sendEmptyMessage(ERROR_CONNECT);
                        }
                    }
                });
            }
        });
    }

    //结束资源清理 防止内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
