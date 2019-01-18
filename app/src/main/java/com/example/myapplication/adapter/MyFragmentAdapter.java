package com.example.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private List<String> titlelist;                              //tab名的列表

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            // TODO Auto-generated constructor stub
            this.fragmentList = fragmentList;
            this.titlelist = null;
        }
        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titlelist) {
            super(fm);
            // TODO Auto-generated constructor stub
            this.fragmentList = fragmentList;
            this.titlelist = titlelist;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentList.size();
        }
        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return titlelist.get(position % titlelist.size());
        }
    }

