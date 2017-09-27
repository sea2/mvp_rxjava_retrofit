package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lhy on 2017/8/15.
 */

public class RetrofitEntity1 {


    /**
     * code : 200
     * result : {"10004":"http://192.168.3.240:9050/detail.html","10005":"http://192.168.3.240:9050/confirm_order.html"}
     */

    private String code;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "_$10004='" + _$10004 + '\'' +
                    ", _$10005='" + _$10005 + '\'' +
                    '}';
        }

        /**
         * 10004 : http://192.168.3.240:9050/detail.html
         * 10005 : http://192.168.3.240:9050/confirm_order.html
         */

        @SerializedName("10004")
        private String _$10004;
        @SerializedName("10005")
        private String _$10005;

        public String get_$10004() {
            return _$10004;
        }

        public void set_$10004(String _$10004) {
            this._$10004 = _$10004;
        }

        public String get_$10005() {
            return _$10005;
        }

        public void set_$10005(String _$10005) {
            this._$10005 = _$10005;
        }
    }
}
