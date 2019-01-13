package com.example.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.example.myapplication.tool.SystemParameter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainComicDetailsActivity extends BaseActivity {
    private ComicData comicData = new ComicData();
    private JSONArray chapterInfoList = new JSONArray();
    private Button comicLook;//观看漫画按钮
    private Button comicCollection;//收藏漫画按钮
    private Button comicComment;//评论按钮
    private RecyclerView comicView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1111 ) {
                //加载漫画列表
                switch (msg.obj.toString()){
                    case "success":
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
                        RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading);
                        Glide.with(MainComicDetailsActivity.this).load(url).apply(options).into(comicimg);
                        comictime.setText("上传时间："+comicData.getTime());
                        comicauthor.setText("作者："+comicData.getAuthor());
                        comiclv.setText("限制等级："+comicData.getLimitLevel());
                        comicpage.setText("页数："+comicData.getPageNum());
                        break;
                    default:
                        Toast.makeText(MainComicDetailsActivity.this, "加载失败 数据错误", Toast.LENGTH_SHORT).show();
                        finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_comic_details);
        initToolbar(R.id.main_comic_details_toolbar);
        initView();
    }

    private void initView(){
        //设置toolbar
        setMainTitle("漫画详情");
        setLeftTitleText("返回");
        setLeftTitleDrawable(R.mipmap.ic_back);
        setLeftTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        comicLook = findViewById(R.id.main_comic_details_look);
        comicCollection = findViewById(R.id.main_comic_details_collection);
        comicComment = findViewById(R.id.main_comic_details_comment);
        getdata();
    }

    private void getdata(){
        String id = getIntent().getBundleExtra("data").getString("comic");
        String url = SystemParameter.PATHURL+"/manhua/getCartoonDetail?manId="+id+"&token="+SystemParameter.TOKEN;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = 0x1111;
                message.obj = "error";
                handler.sendMessage(message);
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.body().string());
                String info = jsonObject.getString("info");
                if(info.equals("success")){
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject comicinfo = data.getJSONObject("manInfo");
                    Integer id = comicinfo.getInteger("id");//ID
                    String name = comicinfo.getString("name");//名字
                    String author = comicinfo.getString("author");//作者
                    String path = comicinfo.getString("path");//相对路径
                    String time = comicinfo.getString("time");//上传时间
                    Integer limitLevel = comicinfo.getInteger("limitLevel");//限制等级
                    Integer pageNum = comicinfo.getInteger("pageNum");//页数
                    comicData.setId(id);
                    comicData.setName(name);
                    comicData.setAuthor(author);
                    comicData.setPath(path);
                    comicData.setTime(time);
                    comicData.setLimitLevel(limitLevel);
                    comicData.setPageNum(pageNum);
                    //漫画列表
                    chapterInfoList = data.getJSONArray("chapterInfoList");
                    Message message = Message.obtain();
                    message.what = 0x1111;
                    message.obj = "success";
                    handler.sendMessage(message);
                }else{
                    Message message = Message.obtain();
                    message.what = 0x1111;
                    message.obj = "error";
                    handler.sendMessage(message);
                }
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
