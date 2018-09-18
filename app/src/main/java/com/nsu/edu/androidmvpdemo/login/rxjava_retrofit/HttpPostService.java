package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 测试接口service-post相关
 * Created by WZG on 2016/12/19.
 * 那么最好传递参数时使用@Field、@FieldMap和@FormUrlEncoded,因为@Query和或QueryMap都是将参数拼接在url后面的，而@Field或@FieldMap传递的参数时放在请求体的。
 *
 */

public interface HttpPostService {


    @FormUrlEncoded
    @POST("/newfind/index_ask")
    Observable<String> getDaJia(@Field("page") int page,
                                @Field("pageSize") int size,
                                @Field("tokenMark") long tokenMark,
                                @Field("token") String token);

    @FormUrlEncoded
    @POST("FundPaperTrade/AppUserLogin")
    Observable<String> getTransData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST()
    Observable<String> getPostResult(@Url() String endUrl, @FieldMap Map<String, String> params);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST()
    Observable<String> getPostResult(@Url() String endUrl, @Body RequestBody mRequestBody);
}
