package com.wzgiceman.rxretrofitlibrary.retrofit_rx.subscribers;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.CodeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.FactoryException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.HttpTimeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.cookie.CookieResulte;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.AppUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.CookieDbUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.LogManager;

import java.lang.ref.SoftReference;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.CodeException.NETWORD_ERROR;

/**
 * 统一处理缓存-数据持久化
 * 异常回调
 * Created by WZG on 2016/7/16.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements Subscription {
    //    回调接口
    private SoftReference<HttpOnNextListener> mSubscriberOnNextListener;
    /*请求数据*/
    private BaseApi api;


    /**
     * 构造
     *
     * @param api
     */
    public ProgressSubscriber(BaseApi api, SoftReference<HttpOnNextListener> listenerSoftReference) {
        this.api = api;
        this.mSubscriberOnNextListener = listenerSoftReference;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        /*缓存并且网络不可以*/
        if (api.isCache() && (!AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication()))) {
            if (mSubscriberOnNextListener.get() != null)
                mSubscriberOnNextListener.get().onError(new ApiException(null, NETWORD_ERROR, "当前网络不可用，请检查您的网络设置"));
            //*获取缓存数据*//*
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrlAndParams());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNoNetWorkTime()) {
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getEndUrl(), true);
                    }
                    onCompleted();
                    unsubscribe();
                }
            }
        }
    }


    @Override
    public void onCompleted() {

    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        /*需要緩存并且本地有缓存才返回*/
        if (api.isCache()) {
            getCache();
        }
        errorDo(e);
    }

    /**
     * 获取cache数据
     */
    private void getCache() {
        Observable.just(api.getUrlAndParams()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                errorLog(e);
            }

            @Override
            public void onNext(String s) {
                  /*获取缓存数据*/
                CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(s);
                if (cookieResulte == null) {
                    throw new HttpTimeException(HttpTimeException.NO_CHACHE_ERROR);
                }
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNoNetWorkTime()) {
                    if (mSubscriberOnNextListener.get() != null) {
                        mSubscriberOnNextListener.get().onNext(cookieResulte.getResulte(), api.getEndUrl(), true);
                    }
                } else {
                    CookieDbUtil.getInstance().deleteCookie(cookieResulte);
                    throw new HttpTimeException(HttpTimeException.CHACHE_TIMEOUT_ERROR);
                }
            }
        });
    }


    /**
     * 错误统一处理
     *
     * @param e
     */
    private void errorDo(Throwable e) {
        HttpOnNextListener httpOnNextListener = mSubscriberOnNextListener.get();
        if (httpOnNextListener == null) return;
        if (!AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())) {
            httpOnNextListener.onError(new ApiException(e, NETWORD_ERROR, "当前网络不可用，请检查您的网络设置"));
        } else {
            ApiException mApiException = FactoryException.analysisExcetpion(e);
            httpOnNextListener.onError(new ApiException(e, mApiException.getCode(), mApiException.getDisplayMessage()));
        }
    }

    private void errorLog(Throwable e) {
        if (!AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication())) {
            LogManager.i("ProgressSubscriber", CodeException.NETWORD_ERROR + "--当前网络不可用，请检查您的网络设置");
        } else {
            ApiException mApiException = FactoryException.analysisExcetpion(e);
            LogManager.i("ProgressSubscriber", mApiException.getCode() + "--" + mApiException.getDisplayMessage());
        }
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(final T t) {

        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext((String) t, api.getEndUrl(), false);
        }


         /*缓存处理*/
        if (api.isCache()) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        CookieResulte resulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrlAndParams());
                        long time = System.currentTimeMillis();
                       /*保存和更新本地数据*/
                        if (resulte == null) {
                            resulte = new CookieResulte(null, api.getUrlAndParams(), t.toString(), time);
                            CookieDbUtil.getInstance().saveCookie(resulte);
                            subscriber.onNext(api.getUrlAndParams() + "插入缓存数据");
                            subscriber.onCompleted();
                        } else {
                            resulte.setResulte(t.toString());
                            resulte.setTime(time);
                            CookieDbUtil.getInstance().updateCookie(resulte);
                            subscriber.onNext(api.getUrlAndParams() + "更新缓存数据");
                            subscriber.onCompleted();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onNext("缓存失败");
                        subscriber.onCompleted();
                    }

                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( //此行为订阅,只有真正的被订阅,整个流程才算生效
                    new Observer<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(String s) {
                            LogManager.i("ProgressSubscriber_greenDao", s);
                        }
                    });


        }
    }


}