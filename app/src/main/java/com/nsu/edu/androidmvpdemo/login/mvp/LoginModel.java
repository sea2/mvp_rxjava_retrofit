package com.nsu.edu.androidmvpdemo.login.mvp;

import android.os.Handler;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:延时模拟登陆（2s），如果名字或者密码为空则登陆失败，否则登陆成功
 */
public class LoginModel implements ILoginModel {

    private Handler myHandler = null;
    private Runnable mRunnable = null;

    @Override
    public void login(final String username, final String password, final LoginModelCallListener listener) {




    }

    @Override
    public void cancleTasks() {

    }


    interface LoginModelCallListener{
        void onComplete(boolean isSuccess, String str, Throwable e);
    }


}
