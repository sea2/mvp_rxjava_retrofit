package com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api;

/**
 * Created by Administrator on 2018/3/24.
 */

public class BaseResponse<T> {
    private int code;
    private String msg;
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }

    public boolean isSuccess() {
        return  code == 200;
    }

}
