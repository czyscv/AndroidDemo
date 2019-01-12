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

import java.security.MessageDigest;

import jp.wasabeef.glide.transformations.internal.FastBlur;
import jp.wasabeef.glide.transformations.internal.RSBlur;

public class MainPersonFragment extends BaseFragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_main_person,null);
        //用户背景
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideBlurTransformer(getContext(),25,1));
        Glide.with(getContext())
                .load(R.mipmap.user_head)
                .apply(options)
                .into((ImageView) view.findViewById(R.id.h_back));
        //用户头像
        options = new RequestOptions()
                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .circleCrop();
        Glide.with(getContext())
                .load(R.mipmap.user_head)
                .apply(options)
                .into((ImageView) view.findViewById(R.id.h_head));
        return view;
    }

    //内部类实现高斯模糊和图片放大
    public class GlideBlurTransformer extends BitmapTransformation {
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