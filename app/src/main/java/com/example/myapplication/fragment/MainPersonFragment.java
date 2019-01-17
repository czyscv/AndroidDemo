package com.example.myapplication.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.RSRuntimeException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.tool.BaseFragment;
import com.example.myapplication.tool.MyPersonListAdapter;
import com.example.myapplication.tool.SystemParameter;
import com.example.myapplication.tool.UserData;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.internal.FastBlur;
import jp.wasabeef.glide.transformations.internal.RSBlur;

public class MainPersonFragment extends BaseFragment {
    private UserData userData;
    private Integer[] iconlist;
    private List<String> titlelist = new ArrayList<>(Arrays.asList(new String[]{"我的收藏","购买会员","帮助信息"}));
    private View view;
    private RecyclerView recyclerView;
    private MyPersonListAdapter myPersonListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main_person,null);
        userData = SystemParameter.USERINFO;
        titlelist.add(0, userData.getUsername());
        iconlist = new Integer[]{R.mipmap.ic_warn,R.mipmap.ic_favor,R.mipmap.ic_vip,R.mipmap.ic_question};
        initView();
        return view;
    }

    private void initView(){
        String headurl = SystemParameter.PATHURL+"/resource/userHead/"+userData.getHeadUrl();
        //用户背景
        RequestOptions options = new RequestOptions().error(R.mipmap.user_head).priority(Priority.HIGH)
                .transform(new GlideBlurTransformer(getContext(),25,4));
        Glide.with(getContext())
                .load(headurl)
                .apply(options)
                .into((ImageView) view.findViewById(R.id.main_person_userhead_back));
        //用户头像
        options = new RequestOptions().error(R.mipmap.user_head).priority(Priority.HIGH).circleCrop();
        Glide.with(getContext())
                .load(headurl)
                .apply(options)
                .into((ImageView) view.findViewById(R.id.main_person_userhead));
        recyclerView = view.findViewById(R.id.main_person_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        //设置Adapter
        myPersonListAdapter = new MyPersonListAdapter(getContext(),iconlist,titlelist,new String[]{userData.getTelephone()});
        recyclerView.setAdapter(myPersonListAdapter);
    }


    /**
     * 私有内部类实现高斯模糊和图片放大
     */
    private class GlideBlurTransformer extends BitmapTransformation {
        private int radius;
        private int sampling;
        private Context context;

        public GlideBlurTransformer(Context context,int radius, int sampling) {
            this.context = context;
            this.radius = radius;
            this.sampling = sampling;
        }
        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            return blurCrop(pool,bitmap);
        }
        private Bitmap blurCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;
            int width = source.getWidth();
            int height = source.getHeight();
            int scaledWidth = width / sampling;
            int scaledHeight = height / sampling;
            Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(1 / (float) sampling, 1 / (float) sampling);
            Paint paint = new Paint();
            paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.drawBitmap(source, 0, 0, paint);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                try {
                    bitmap = RSBlur.blur(context, bitmap, radius);
                } catch (RSRuntimeException e) {
                    bitmap = FastBlur.blur(bitmap, radius, true);
                }
            } else {
                bitmap = FastBlur.blur(bitmap, radius, true);
            }
            return bitmap;
        }
        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
        }
    }
}