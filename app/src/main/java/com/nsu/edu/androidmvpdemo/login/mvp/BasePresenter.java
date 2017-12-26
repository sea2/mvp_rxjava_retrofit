package com.nsu.edu.androidmvpdemo.login.mvp;

import java.lang.ref.WeakReference;

/**
 * Created by lhy on 2017/12/13.
 */

public class BasePresenter<V> {
    private WeakReference<V> weakRefView;

    public void attach(V view) {
        weakRefView = new WeakReference<V>(view);
    }

    public void detach() {
        if (isAttach() && weakRefView != null) {
            weakRefView.clear();
            weakRefView = null;
        }
    }

    public V obtainView() {
        return isAttach() ? weakRefView.get() : null;
    }

    protected boolean isAttach() {
        return weakRefView != null && weakRefView.get() != null;
    }
}


