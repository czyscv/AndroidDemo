package com.example.myapplication.tool;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.myapplication.R;

//列表最下方的下拉加载
public class FooterHolder extends RecyclerView.ViewHolder {
    View mLoadingViewstubstub;
    View mEndViewstub;
    View mNetworkErrorViewstub;

    public FooterHolder(View view) {
        super(view);
        mLoadingViewstubstub = view.findViewById(R.id.loading_viewstub);
        mEndViewstub = view.findViewById(R.id.end_viewstub);
        mNetworkErrorViewstub = view.findViewById(R.id.network_error_viewstub);
        setData(1);
    }

    //根据传过来的status控制哪个状态可见
    public void setData(int status) {
        switch (status) {
            case 0:
                setAllGone();
                break;
            case 1:
                setAllGone();
                mLoadingViewstubstub.setVisibility(View.VISIBLE);
                break;
            case 2:
                setAllGone();
                mEndViewstub.setVisibility(View.VISIBLE);
                break;
            case 3:
                setAllGone();
                mNetworkErrorViewstub.setVisibility(View.VISIBLE);
                break;
            default:
                setAllGone();
                mEndViewstub.setVisibility(View.VISIBLE);
                break;
        }

    }

    //全部不可见
    void setAllGone() {
        if (mLoadingViewstubstub != null) {
            mLoadingViewstubstub.setVisibility(View.GONE);
        }
        if (mEndViewstub != null) {
            mEndViewstub.setVisibility(View.GONE);
        }
        if (mNetworkErrorViewstub != null) {
            mNetworkErrorViewstub.setVisibility(View.GONE);
        }
    }
}