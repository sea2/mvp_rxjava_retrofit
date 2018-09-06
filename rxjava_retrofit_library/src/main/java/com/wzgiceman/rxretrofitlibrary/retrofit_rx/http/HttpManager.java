package com.wzgiceman.rxretrofitlibrary.retrofit_rx.http;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.CodeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.FactoryException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.RetryWhenNetworkException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.subscribers.ProgressSubscriber;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.StringUtils;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
    private ProgressSubscriber subscriber;
    private static HttpManager instance;

    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    Retrofit retrofit;


    public HttpManager(HttpOnNextListener onNextListener) {
        this.onNextListener = new SoftReference<HttpOnNextListener>(onNextListener);
        init();
    }

    private void init() {

        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectionTime, TimeUnit.SECONDS);

        if (StringUtils.isEmpty(RxRetrofitApp.getBaseUrl())) {
            onNextListener.get().onError(new ApiException(null, CodeException.UNKOWNHOST_ERROR, "baseUrl为空"));
            return;
        }
        /*创建retrofit对象*/
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(RxRetrofitApp.getBaseUrl())
                .build();
    }


    public static HttpManager getInstance(HttpOnNextListener onNextListener) {
        if (instance == null) {
            instance = new HttpManager(onNextListener);
        }
        return instance;
    }


    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public ProgressSubscriber doHttpDeal(BaseApi basePar) {

        /*rx处理*/
        subscriber = new ProgressSubscriber(basePar, onNextListener);

        Observable observable = basePar.getObservable(retrofit)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*异常处理*/
                //.onErrorResumeNext(funcException)
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);

        /*数据回调*/
        observable.subscribe(subscriber);
        return subscriber;
    }


    /**
     * 取消订阅
     */
    public void doCancel(ProgressSubscriber subscriber) {
        if (subscriber != null) {
            if (!subscriber.isUnsubscribed())
                subscriber.unsubscribe();
        }



    }

    /**
     * 异常处理
     */
    Func1 funcException = new Func1<Throwable, Observable>() {
        @Override
        public Observable call(Throwable throwable) {
            return Observable.error(FactoryException.analysisExcetpion(throwable));
        }
    };

}
