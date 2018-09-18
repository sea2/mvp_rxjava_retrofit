package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.SubjectPostApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;

import java.util.HashMap;

/**
 * Created by lhy on 2017/8/21.
 */

public abstract class BaseActivity extends FragmentActivity implements HttpOnNextListener {


    private ProgressDialog progressDialog;
    protected boolean isRunning = true;// 该activity是在运行（未被销毁）true：在运行 false：已销毁

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    // -------------------------网络请求-------------------------


    protected void getPostData(HashMap<String, String> parametersMap, String url, boolean isShowDialog) {
        showProgressDialog();
        if (isShowDialog) {
            showProgressDialog();
        }
        SubjectPostApi postEntity = new SubjectPostApi(url, parametersMap);
        HttpManager.getInstance().doHttpDeal(postEntity, this);
    }


    /**
     * 圈圈提示框
     *
     * @author windy 2014-8-16 下午2:41:47
     */
    public void showProgressDialog() {
        if (isRunning) {
            if (null == progressDialog) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("加载中...");
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
            } else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            progressDialog.show();
        }
    }


    /**
     * 圈圈提示框消失
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            if (isFinishing()) {
                return;
            }
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        dismissProgressDialog();
        HttpManager.getInstance().doCancel();

    }

    @Override
    public void finish() {
        isRunning = false;
        dismissProgressDialog();
        super.finish();
    }


    @Override
    public void onComplete(String result, String endUrl, boolean isCache, ApiException e) {
        dismissProgressDialog();
    }
}
