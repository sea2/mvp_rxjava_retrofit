package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by lhy on 2017/8/18.
 */

public interface UploadService {

    @Multipart
    @POST("")
    Observable<String> upload(@Url() String endUrl, @Part("description") RequestBody description,
                              @Part MultipartBody.Part part);


    @Multipart
    @POST()
    Observable<ResponseBody> uploads(
            @Url String url,
            @Part() MultipartBody.Part file);


    @Multipart
    @POST
    Observable<ResponseBody> uploadFileWithPartMap(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Part("file") MultipartBody.Part file);


    @POST()
    Observable<ResponseBody> upLoad(
            @Url() String url,
            @Body RequestBody Body);


    @Multipart
    @POST
    Observable<ResponseBody> uploadFileWithPartMap2(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Part  MultipartBody.Part file);



}
