package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by lhy on 2017/8/18.
 */

public interface HttpGetService {

    @GET("blog/{id}")
    Observable<String> getBlog(@Path("id") int id);


    @GET("telematics/v3/movie/")
    Observable<String> getMovie(@QueryMap Map<String, String> map);


    @GET("users/yuhengye")
    Observable<String> getUserInfo(@HeaderMap Map<String, String> headers);

 /*   Map<String,String> headers = new HashMap<>();
headers.put("Accept", "text/plain");
headers.put("Accept-Charset", "utf-8");*/
}
