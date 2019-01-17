package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.fragment.MainDashboardFragment;
import com.example.myapplication.fragment.MainHomeFragment;
import com.example.myapplication.fragment.MainPersonFragment;
import com.example.myapplication.fragment.MainSearchFragment;
import com.example.myapplication.tool.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.tool.BottomNavigationViewHelper;
import com.example.myapplication.tool.MyFragmentAdapter;
import com.example.myapplication.tool.MyOkhttp;
import com.example.myapplication.tool.SystemParameter;
import com.example.myapplication.tool.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;//底部按钮视图
    private ViewPager viewPager;
    private MyFragmentAdapter mAdapter;//适配器
    private List<Fragment> list = new ArrayList<>();
    private MenuItem menuItem;
    private Boolean isExit = false;//两次退出的指针
    private static final int COMIC_DASHBOARD = 0x1111;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0x0000){
                switch (message.obj.toString()){
                    case "noExit":
                        //取消退出
                        isExit = false;
                        break;
                    case "error":
                        Toast.makeText(MainActivity.this, "数据错误",Toast.LENGTH_SHORT).show();
                        goLogin();
                        break;
                    case "not_login":
                        Toast.makeText(MainActivity.this, "您还没有登录",Toast.LENGTH_SHORT).show();
                        goLogin();
                        break;
                }
            }else if (message.what == COMIC_DASHBOARD ) {
                //加载漫画列表
                switch (message.obj.toString()){
                    case "error":
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        break;
                    case "updateUI":
                        MainDashboardFragment mainDashboardFragment = (MainDashboardFragment)list.get(2);
                        mainDashboardFragment.updateUI();
                        break;
                    case "updateOK":
                        Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();
                        break;
                    case "not_login":
                        Toast.makeText(MainActivity.this, "您还没有登录",Toast.LENGTH_SHORT).show();
                        goLogin();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "网络错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar(R.id.main_toolbar);
        setMainTitle("首页");
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.main_viewpage);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);//按钮平均分
        MainHomeFragment mainHomeFragment = new MainHomeFragment();
        MainSearchFragment mainSearchFragment = new MainSearchFragment();
        MainDashboardFragment mainDashboardFragment = new MainDashboardFragment();
        MainPersonFragment mainPersonFragment = new MainPersonFragment();
        list.add(mainHomeFragment);
        list.add(mainSearchFragment);
        list.add(mainDashboardFragment);
        list.add(mainPersonFragment);
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(mAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener((item)->{
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_search:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                setMainTitle(bottomNavigationView.getMenu().getItem(position)+"");
                if(position==3){
                    setRightTitleText("设置");
                }else{
                    setRightTitleText("");
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        setRightTitleClickListener((view)->{
            //TODO
            Toast.makeText(MainActivity.this,"这里是设置",Toast.LENGTH_SHORT).show();
        });
    }
//    private void initUser(){
//        MyOkhttp myOkhttp = new MyOkhttp();
//        myOkhttp.setUrl("/user_info/get_user_info");
//        myOkhttp.addParameter(new String[]{"token"}, new String[]{SystemParameter.TOKEN});
//        myOkhttp.myGetOkhttp();
//        myOkhttp.request(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Message message = Message.obtain();
//                message.what = 0x0000;
//                message.obj = "error";
//                mainHandler.sendMessage(message);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                JSONObject jsonObject = JSON.parseObject(response.body().string());
//                String info = jsonObject.getString("info");
//                if(info.equals("success")){
//                    JSONObject data = jsonObject.getJSONObject("data");
//                    SystemParameter.USERINFO = JSON.parseObject(data.toJSONString() , UserData.class);
//                }
//                Message message = Message.obtain();
//                message.what = 0x0000;
//                message.obj = info;
//                mainHandler.sendMessage(message);
//            }
//        });
//    }

    private void goLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isExit){
                isExit=true;
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                Message message = Message.obtain();
                message.what = 0x0000;
                message.obj = "noExit";
                mainHandler.sendMessageAtTime(message,1500);
            }else{
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //让下层fragment得到handler
    public Handler getActivityHandler() {
        return mainHandler;
    }

    //结束资源清理 防止内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
