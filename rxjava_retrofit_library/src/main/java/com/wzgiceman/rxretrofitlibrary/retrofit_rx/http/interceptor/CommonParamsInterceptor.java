package com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.interceptor;

/**
 * Created by Administrator on 2018/3/24.
 */

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 网络请求公共参数插入器
 * <p>
 *
 * @author Administrator
 */
public class CommonParamsInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request.method().equals("GET")) {
            HttpUrl httpUrl = request.url().newBuilder()
                    .addQueryParameter("version", "xxx")
                    .addQueryParameter("device", "Android")
//                    .addQueryParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();
            request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();
                if (formBody != null)
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                formBody = bodyBuilder
                        .addEncoded("version", "xxx")
                        .addEncoded("device", "Android")
//                       .addEncoded("timestamp", String.valueOf(System.currentTimeMillis()))
                        .build();
                request = request.newBuilder().post(formBody).build();
            } else {
                //buffer流
                Buffer buffer = new Buffer();
                if (request.body() != null) {
                    request.body().writeTo(buffer);
                }
                String oldParamsJson = buffer.readUtf8();
                Gson mGson = new Gson();
                HashMap rootMap = mGson.fromJson(oldParamsJson, HashMap.class);  //原始参数
                if (rootMap != null) {
                    rootMap.put("version", "2.0.2");  //增加参数
                }
                String newJsonParams = mGson.toJson(rootMap);  //装换成json字符串
                request = request.newBuilder().post(RequestBody.create(request.body().contentType(), newJsonParams)).build();
            }
        }

        return chain.proceed(request);
    }


    // func to inject params into url
    private void injectParamsIntoUrl(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }

        requestBuilder.url(httpUrlBuilder.build());
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
