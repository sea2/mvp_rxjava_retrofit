package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;


import com.google.gson.Gson;
import com.nsu.edu.androidmvpdemo.login.config.MyConstants;
import com.nsu.edu.androidmvpdemo.login.util.LogManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPostApi extends BaseApi {


    /**
     * 默认初始化需要给定回调和rx周期类
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     * 设置可查看BaseApi
     */
    public SubjectPostApi(String endUrl, Map<String, Object> map) {
        setBaseUrl(MyConstants.getHost());
        setShowProgress(true);
        setCancel(true);
        setCache(true);
        setEndUrl(endUrl);
        setParametersMap(map);
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {
        Gson gson = new Gson();
        String strEntity = gson.toJson(getParametersMap());
        LogManager.i("http", "----------------------------------------------------------------------------------------------------------------");
        LogManager.i("http", "* " + getUrl() + "\n* 参数：" + strEntity);
        LogManager.i("http", "----------------------------------------------------------------------------------------------------------------");
        HttpPostService httpService = retrofit.create(HttpPostService.class);
        return httpService.getPostResult(getEndUrl(), RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity));
    }


}
