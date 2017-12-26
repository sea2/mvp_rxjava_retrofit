package com.nsu.edu.androidmvpdemo.login.mvp;

/**
 * Created by lhy on 2017/12/13.
 */

public interface ILoginPresenter {


    void validateCredentials(String username, String password);
    void onDestroy();
}
