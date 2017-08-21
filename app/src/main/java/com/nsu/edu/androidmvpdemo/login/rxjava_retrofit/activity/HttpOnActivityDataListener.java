package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.activity;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;

/**
 * Created by lhy on 2017/8/21.
 */

public interface HttpOnActivityDataListener {

    /**
     * 成功后回调方法
     *
     * @param resulte
     * @param endUrl
     */
    void onNext(String resulte, String endUrl);

    /**
     * 失败
     * 失败或者错误方法
     * 自定义异常处理
     *
     * @param e
     */
    void onError(ApiException e);
}
