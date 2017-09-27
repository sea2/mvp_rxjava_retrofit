package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.model.BodyInfo1;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by lhy on 2017/8/15.
 */

public interface HttpService {

    @POST("v1.0/app/system/loadConfigItem")
    Observable<RetrofitEntity1> getAllVedioBy(@Body BodyInfo1 keywords);


    @GET("blog/{id}")
    Observable<RetrofitEntity1> getBlog(@Path("id") int id);

    /**
     * method 表示请求的方法，区分大小写
     * path表示路径
     * hasBody表示是否有请求体
     */
    @GET("/course_api/wares/hot")
    Observable<RetrofitEntity1> getShop(@Query("pageSize") int pageSize, @Query("curPage") int curPage);


    @GET("applist/apps/detail")
    Observable<RetrofitEntity1> getDetail(@QueryMap Map<String, String> param);

    /**
     * 当然，还可以支持固定参数与动态参数的混用
     */
    @GET("applist/apps/detail?type=detail")
    Observable<RetrofitEntity1> getDetail1(@Query("appid") String appid);

    @Headers("Cache-Control: max-age=640000")
    @GET("/tasks")
    Observable<RetrofitEntity1> getDataList();

    @GET("applist/apps/detail?type=detail")
    Observable<RetrofitEntity1> getDetail3(@Header("Accept-Encoding") String appid);


    @Headers({
            "X-Foo: Bar",
            "X-Ping: Pong"
    })
    @GET("applist/apps/detail?type=detail")
    Observable<RetrofitEntity1> getDetail4(@Header("Accept-Encoding") String appid);


    @Multipart
    @POST
    Observable<ResponseBody> uploadFileWithPartMap(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Query("key") String key,
            @Part("file") MultipartBody.Part file);


}
