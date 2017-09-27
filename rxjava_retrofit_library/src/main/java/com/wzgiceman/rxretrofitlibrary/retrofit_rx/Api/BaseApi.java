package com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.HttpTimeException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.LogManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseApi<T> implements Func1<T, String> {
    /*是否能取消加载框*/
    private boolean cancel = false;
    /*是否显示加载框*/
    private boolean showProgress = true;
    /*是否需要缓存处理*/
    private boolean cache = false;
    /*基础url*/
    private String baseUrl = "";
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String endUrl = "";
    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /*是否是原装Json*/
    private boolean isOriginal = true;
    public Map<String, Object> parametersMap;

    /**
     * 设置参数
     *
     * @param retrofit
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }


    public Map<String, Object> getParametersMap() {
        if (parametersMap == null) parametersMap = new HashMap<>();
        parametersMap.put("app_version", "");
        Map<String, Object> params = new HashMap<>();
        if (parametersMap.size() > 0) {
            for (Map.Entry<String, Object> entry : parametersMap.entrySet()) {
                if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null) {
                    if (entry.getValue() instanceof String) params.put(entry.getKey().trim(), entry.getValue().toString().trim());
                    else params.put(entry.getKey().trim(), entry.getValue());
                }
            }
        }
        return params;
    }

    public void setParametersMap(Map<String, Object> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return baseUrl + endUrl;
    }


    public String getUrlAndParams() {
        return getUrlAndParams(getUrl());
    }

    /**
     * Get地址拼接
     */
    public String getUrlAndParams(String uri) {
        StringBuilder strBuffer = new StringBuilder();
        strBuffer.append(uri);
        Map<String, Object> map = getParametersMap();
        if (map != null && map.size() > 0) {
            strBuffer.append("?");
            Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                if (iter.hasNext()) {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue() + "&"));
                } else {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue()));
                }
            }
        }
        return StringUtils.toURLEncoded(strBuffer.toString());
    }

    public String getEndUrl() {
        return endUrl;
    }

    public void setEndUrl(String endUrl) {
        this.endUrl = endUrl;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public String call(T httpResult) {
        LogManager.i("http——BaseApi", "----------------------------------------------------------------------------------------------------------------");
        LogManager.i("http——BaseApi", "* " + getUrl() + "\n* 参数：" + getParametersMap().toString() + "\n* 返回结果：" + httpResult.toString());
        LogManager.i("http——BaseApi", "----------------------------------------------------------------------------------------------------------------");


        if (isOriginal()) {//返回原装json
            return httpResult.toString();
        } else {
            Gson gson = new Gson();
            BaseResultEntity baseResulte = gson.fromJson(httpResult.toString(), new TypeToken<BaseResultEntity>() {
            }.getType());
            if (baseResulte.getRet() == 0) {
                throw new HttpTimeException(baseResulte.getMsg());
            }
            return baseResulte.getData();
        }
    }
}
