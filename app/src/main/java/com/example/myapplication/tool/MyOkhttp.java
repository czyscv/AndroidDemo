package com.example.myapplication.tool;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyOkhttp {
    private String path;
    private String[] keys;
    private String[] values;
    private Call call;

    /**
     * 设置请求的路径
     * @param path 路径
     */
    public void setUrl(String path){
        this.path = path;
    }

    /**
     * 增加请求参数 要求一一对应
     * @param keys
     * @param values
     */
    public void addParameter(String[] keys,String[] values){
        this.keys = keys;
        this.values = values;
    }
    /**
     * 设置为get请求
     */
    public void myGetOkhttp(){
        String url = SystemParameter.PATHURL+path+"?";
        for(int i = 0;i<keys.length;i++){
            url = url+keys[i]+"="+values[i];
            if(i!=keys.length-1){
                url=url+"&";
            }
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        call = okHttpClient.newCall(request);
    }

    /**
     * 设置为post请求
     */
    public void myPostOkhttp(){
        String url = SystemParameter.PATHURL+path;
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for(int i = 0;i<keys.length;i++){
            formBodyBuilder.add(keys[i], values[i]);
        }
        FormBody formBody = formBodyBuilder.build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(formBody).build();
        call = okHttpClient.newCall(request);
    }

    /**
     * 定义回调方法
     * @param callback
     */
    public void request(Callback callback) {
        call.enqueue(callback);
    }
}
