package com.example.myapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.tool.BaseFragment;
import com.example.myapplication.tool.ComicData;
import com.example.myapplication.tool.ComicListAdapter;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainDashboardFragment extends BaseFragment {
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;//布局
    private ComicListAdapter comicListAdapter;//自定义监听器
    private ArrayList<ComicData> dataList = new ArrayList<>();//数据列表
    private Integer now = 1;//显示第几页
    private Handler handler;//activity的handler

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            handler =  ((MainActivity)context).getActivityHandler();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main_dashboard,null);
        recyclerView = view.findViewById(R.id.comic_dashboard);
        swipeRefreshLayout = view.findViewById(R.id.update_dashboard);
        initView();
        return view;
    }

    //初始化控件
    void initView(){
        getdata();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        //设置Adapter
        comicListAdapter = new ComicListAdapter(dataList,view);
        recyclerView.setAdapter(comicListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataList.clear();
                now = 1;
                getdata();
                swipeRefreshLayout.setRefreshing(false);
                Message message = Message.obtain();
                message.what = 0x1111;
                message.obj = "updateOK";
                handler.sendMessage(message);
            }
        });
    }

    //对外的更新UI的方法
    public void updateUI(){
        comicListAdapter.notifyDataSetChanged();
    }

    //获得数据
    void getdata(){
        SharedPreferences sf = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String token = sf.getString("token",null);
        String url = "http://www.skythinking.cn:7777/manhua/get_home_manHua?token="+token+"&now="+ (now++)+"&each=20";
        //进行漫画列表的请求
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
                    JSONArray data = jsonObject.getJSONArray("data");
                    for(int i=0;i<data.size();i++){
                        JSONObject job = data.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                        System.out.println(job);
                        Integer id = job.getInteger("id");//ID
                        String name = job.getString("name");//名字
                        String author = job.getString("author");//作者
                        String path = job.getString("path");//相对路径
                        String time = job.getString("time");//上传时间
                        Integer limitLevel = job.getInteger("limitLevel");//限制等级
                        Integer toShow = job.getInteger("toShow");//是否显示 0隐藏
                        Integer pageNum = job.getInteger("pageNum");//页数
                        if(toShow==1){
                            ComicData comic = new ComicData();
                            comic.setId(id);
                            comic.setName(name);
                            comic.setAuthor(author);
                            comic.setPath(path);
                            comic.setTime(time);
                            comic.setLimitLevel(limitLevel);
                            comic.setPageNum(pageNum);
                            dataList.add(comic);
                        }
                    }
                    Message message = Message.obtain();
                    message.what = 0x1111;
                    message.obj = "updateUI";
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

}
