package com.wzgiceman.rxretrofitlibrary.retrofit_rx.http;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.FactoryException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.interceptor.CommonParamsInterceptor;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.interceptor.HttpHeaderInterceptor;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.subscribers.ProgressObserver;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.RxUtils;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    /*软引用對象*/
    private SoftReference<HttpOnNextListener> onNextListener;
    private static HttpManager instance;

    /*超时时间-默认10秒*/
    private final int connectionTime = 10;
    private Retrofit retrofit;
    private CompositeDisposable compositeDisposable;


    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    public HttpManager() {
        init();
    }

    private void init() {

        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(connectionTime, TimeUnit.SECONDS);
        /*失败后的retry配置*/
        builder.retryOnConnectionFailure(false);
        addInterceptor(builder);

        /*创建retrofit对象*/
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                /*http请求线程*/
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(RxRetrofitApp.getBaseUrl())
                .build();
    }


    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseApi basePar, HttpOnNextListener mHttpOnNextListener) {
        this.onNextListener = new SoftReference<>(mHttpOnNextListener);
        addSubscribe(basePar.getObservable(retrofit)
                .compose(RxUtils.handleAllString())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar)
                .subscribeWith(new ProgressObserver(basePar, onNextListener)));
    }


    /**
     * 添加各种拦截器
     *
     * @param builder
     */
    private void addInterceptor(OkHttpClient.Builder builder) {
        //公共Header参数插值器
        builder.addInterceptor(new HttpHeaderInterceptor.Builder().build());
        //公共参数插值器
        builder.addInterceptor(new CommonParamsInterceptor());

        //缓存使用拦截器
      /*  builder.addInterceptor(sRewriteCacheControlInterceptor);
        builder.addNetworkInterceptor(sRewriteCacheControlInterceptor);
        builder.cache(new Cache(new File(Utils.getContext().getExternalCacheDir() + "/okHttp_cache"), 1024 * 1024 * 100));*/


        // 添加日志拦截器，非debug模式不打印任何日志
        if (RxRetrofitApp.isDebug()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

    }


    /**
     * 封装到base里面效果最佳
     *
     * @param disposable disposable
     */
    private void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }


    /**
     * 取消所有订阅
     */
    public void doCancel() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
        }
    }

    /**
     * 异常处理
     */
    Function funcException = new Function<Throwable, Observable>() {
        @Override
        public Observable apply(Throwable throwable) {
            return Observable.error(FactoryException.analysisExcetpion(throwable));
        }
    };


}
