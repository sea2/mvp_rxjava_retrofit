package com.nsu.edu.androidmvpdemo.login.mvp;

/**
 * Created by Anthony on 2016/2/15.
 * Class Note:
 * 1 完成presenter的实现。这里面主要是Model层和View层的交互和操作。
 * 2  presenter里面还有个OnLoginFinishedListener，
 * 其在Presenter层实现，给Model层回调，更改View层的状态，
 * 确保 Model层不直接操作View层。如果没有这一接口在LoginPresenterImpl实现的话，
 * LoginPresenterImpl只 有View和Model的引用那么Model怎么把结果告诉View呢？
 */
public class LoginPresenter extends  BasePresenter<LoginView> implements ILoginPresenter, OnLoginFinishedListener {
    private LoginView loginView;
    private ILoginModel loginModel;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }



    @Override
    public void validateCredentials(String username, String password) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginModel.login(username, password, this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
        if(loginModel != null) {
            loginModel.cancleTasks();
            loginModel = null;
        }
    }

    @Override
    public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}
