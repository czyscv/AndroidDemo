package com.example.myapplication.fragment;

import android.content.Context;
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
import android.widget.ImageButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.R;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.tool.BaseFragment;
import com.example.myapplication.tool.ComicData;
import com.example.myapplication.tool.MyComicListAdapter;
import com.example.myapplication.tool.EndlessRecyclerOnScrollListener;
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainDashboardFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;//布局
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新
    private MyComicListAdapter myComicListAdapter;//自定义漫画监听器
    private ImageButton totop;//回到顶部按钮
    private ArrayList<ComicData> dataList = new ArrayList<>();//数据列表
    private Integer now = 1;//显示第几页
    private static final Integer EACH = 20;//每一页显示多少
    private static final Integer MAX_EACH = 1000;//最多显示多少&datalist最多包含多少数据
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
        recyclerView = view.findViewById(R.id.mian_dashboard_comiclist);
        swipeRefreshLayout = view.findViewById(R.id.main_dashboard_update);
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
        myComicListAdapter = new MyComicListAdapter(dataList,getContext());
        recyclerView.setAdapter(myComicListAdapter);
        //下拉刷新
        swipeRefreshLayout.setColorSchemeColors(0X008577);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //清除数据列表 重置页数 重新获取数据 完成后取消刷新动画 然后发送完成的消息
                dataList.clear();
                updateUI();
                now = 1;
                getdata();
                swipeRefreshLayout.setRefreshing(false);
                Message message = Message.obtain();
                message.what = 0x1111;
                message.obj = "updateOK";
                handler.sendMessage(message);
            }
        });
        //上划加载
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                myComicListAdapter.getFooterHolder().setData(1);
                if (dataList.size() < MAX_EACH) {
                    getdata();
                } else {
                    // 显示加载到底的提示
                    myComicListAdapter.getFooterHolder().setData(2);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int position  = linearManager.findFirstVisibleItemPosition();
                    if(position>5){
                        totop.setVisibility(View.VISIBLE);
                    }else{
                        totop.setVisibility(View.GONE);
                    }
                }
            }
        });
        //点击和长按事件在这里实现 这里交给适配器来实现
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                return false;
//            }
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
//        });
        //回到顶部
        totop = view.findViewById(R.id.main_dashboard_top);
        totop.setVisibility(View.GONE);
        totop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int position  = linearManager.findFirstVisibleItemPosition();
                    if(position>100){
                        //立刻回到顶部
                        recyclerView.scrollToPosition(0);
                    }else{
                        //平滑回到顶部
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        });
    }

    //对外的更新UI的方法
    public void updateUI(){
        myComicListAdapter.notifyDataSetChanged();
    }

    //获得数据
    void getdata(){
        MyOkhttp myOkhttp = new MyOkhttp();
        myOkhttp.setUrl("/manhua/get_home_manHua");
        myOkhttp.addParameter(new String[]{"token","now","each"}, new String[]{SystemParameter.TOKEN, (now++).toString(),EACH.toString()});
        myOkhttp.myGetOkhttp();
        myOkhttp.request(new Callback() {
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
                        ComicData comicData = JSON.parseObject(job.toJSONString(),ComicData.class);
                        dataList.add(comicData);
//                        Integer id = job.getInteger("id");//ID
//                        String name = job.getString("name");//名字
//                        String author = job.getString("author");//作者
//                        String path = job.getString("path");//相对路径
//                        String time = job.getString("time");//上传时间
//                        Integer limitLevel = job.getInteger("limitLevel");//限制等级
//                        Integer toShow = job.getInteger("toShow");//是否显示 0隐藏
//                        Integer pageNum = job.getInteger("pageNum");//页数
//                        if(toShow==1){
//                            ComicData comic = new ComicData();
//                            comic.setId(id);
//                            comic.setName(name);
//                            comic.setAuthor(author);
//                            comic.setPath(path);
//                            comic.setTime(time);
//                            comic.setLimitLevel(limitLevel);
//                            comic.setPageNum(1);
//                            dataList.add(comic);
//                        }
                    }
                    if(data.size()==0){
                        myComicListAdapter.getFooterHolder().setData(2);
                    }else{
                        Message message = Message.obtain();
                        message.what = 0x1111;
                        message.obj = "updateUI";
                        handler.sendMessage(message);
                    }
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