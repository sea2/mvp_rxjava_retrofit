package com.nsu.edu.androidmvpdemo.login.mvp;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by lhy on 2017/12/13.
 */

public abstract class BaseActivity<V, P extends BasePresenter<V>>
        extends Activity {

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attach((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach();
    }

    public abstract P createPresenter();

}