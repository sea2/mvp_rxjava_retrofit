package com.nsu.edu.androidmvpdemo.login.mvp;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:延时模拟登陆（2s），如果名字或者密码为空则登陆失败，否则登陆成功
 */
public class LoginModel implements ILoginModel {

    private Handler myHandler = null;
    private Runnable mRunnable = null;

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        myHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(username)) {
                    listener.onUsernameError();//model层里面回调listener
                    error = true;
                }
                if (TextUtils.isEmpty(password)) {
                    listener.onPasswordError();
                    error = true;
                }
                if (!error) {
                    listener.onSuccess();
                }
            }
        };
        myHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    public void cancleTasks() {
        if (myHandler != null) {
            if (mRunnable != null) {
                myHandler.removeCallbacks(mRunnable);
                mRunnable = null;
            }
            myHandler = null;
        }
    }


}
