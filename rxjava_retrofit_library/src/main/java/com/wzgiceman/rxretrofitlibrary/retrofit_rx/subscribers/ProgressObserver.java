package com.wzgiceman.rxretrofitlibrary.retrofit_rx.subscribers;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.RxRetrofitApp;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.CodeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.FactoryException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.HttpTimeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.greendao.entity.CookieResulte;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.AppUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.CookieDbUtil;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.LogManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.StringUtils;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * 统一处理缓存-数据持久化
 * 异常回调
 * Created by WZG on 2016/7/16.
 */
public class ProgressObserver extends ResourceObserver<String> {
    //    回调接口
    private SoftReference<HttpOnNextListener> mSubscriberOnNextListener;
    /*请求数据*/
    private BaseApi api;


    /**
     * 构造
     *
     * @param api
     */
    public ProgressObserver(BaseApi api, SoftReference<HttpOnNextListener> listenerSoftReference) {
        this.api = api;
        this.mSubscriberOnNextListener = listenerSoftReference;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        super.onStart();
        /*缓存并且网络不可以*/
        if (api.isCache() && (!AppUtil.isNetworkAvailable(RxRetrofitApp.getApplication()))) {
            //*获取缓存数据*//*
            CookieResulte cookieResulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrlAndParams());
            if (cookieResulte != null) {
                long time = (System.currentTimeMillis() - cookieResulte.getTime()) / 1000;
                if (time < api.getCookieNoNetWorkTime()) {
                    if (mSubscriberOnNextListener.get() != null) {
                        if (StringUtils.isNotEmpty(cookieResulte.getResulte())) {
                            mSubscriberOnNextListener.get().onComplete(cookieResulte.getResulte(), api.getEndUrl(), true, null);
                            onComplete();
                            return;
                        }
                    }
                }
            }
            if (mSubscriberOnNextListener.get() != null) {
                mSubscriberOnNextListener.get().onComplete(null, api.getEndUrl(), false, new ApiException(null, CodeException.NETWORD_ERROR, "当前网络不可用，请检查您的网络设置"));
                onComplete();
            }
        }
    }


    @Override
    public void onComplete() {

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
        Observable.just(api.getUrlAndParams()).subscribe(new Observer<String>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {

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
                        mSubscriberOnNextListener.get().onComplete(cookieResulte.getResulte(), api.getEndUrl(), true, null);
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
            mSubscriberOnNextListener.get().onComplete(null, api.getEndUrl(), false, new ApiException(null, CodeException.NETWORD_ERROR, "当前网络不可用，请检查您的网络设置"));
        } else {
            ApiException mApiException = FactoryException.analysisExcetpion(e);
            mSubscriberOnNextListener.get().onComplete(null, api.getEndUrl(), false, new ApiException(null, mApiException.getCode(), mApiException.getDisplayMessage()));
        }
    }


    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(final String t) {

        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onComplete(t, api.getEndUrl(), true, null);
        }


        /*缓存处理*/
        if (api.isCache()) {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> observableEmitter) {
                    try {
                        CookieResulte resulte = CookieDbUtil.getInstance().queryCookieBy(api.getUrlAndParams());
                        long time = System.currentTimeMillis();
                        /*保存和更新本地数据*/
                        if (resulte == null) {
                            resulte = new CookieResulte(null, api.getUrlAndParams(), t, time);
                            CookieDbUtil.getInstance().saveCookie(resulte);
                            observableEmitter.onNext(api.getUrlAndParams() + "插入缓存数据");
                            observableEmitter.onComplete();
                        } else {
                            resulte.setResulte(t);
                            resulte.setTime(time);
                            CookieDbUtil.getInstance().updateCookie(resulte);
                            observableEmitter.onNext(api.getUrlAndParams() + "更新缓存数据");
                            observableEmitter.onComplete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        observableEmitter.onNext("缓存失败");
                        observableEmitter.onComplete();
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( //此行为订阅,只有真正的被订阅,整个流程才算生效
                    new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String s) {
                            LogManager.i("ProgressSubscriber_greenDao", s);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        }
    }


}