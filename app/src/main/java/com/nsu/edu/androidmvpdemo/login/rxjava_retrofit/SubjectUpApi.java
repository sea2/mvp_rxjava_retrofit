package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit;

import com.nsu.edu.androidmvpdemo.login.config.MyConstants;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by lhy on 2017/8/18.
 */

public class SubjectUpApi extends BaseApi {
    //    接口需要传入的参数 可自定义不同类型
    /*任何你先要传递的参数*/
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;


    /**
     * 默认初始化需要给定回调和rx周期类
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     * 设置可查看BaseApi
     */
    public SubjectUpApi(String endUrl) {
        setBaseUrl(MyConstants.getHost());
        setShowProgress(true);
        setCancel(true);
        setCache(true);
        setEndUrl(endUrl);
    }


    @Override
    public Observable getObservable(Retrofit retrofit) {


        Observable mObservable = null;
        switch (getUrl()) {
            case "1"://body体
                File file = new File("");
                //构建body
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("name", "呵呵")
                        .addFormDataPart("psd", "123")
                        .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                        .build();
                UploadService uploadService = retrofit.create(UploadService.class);
                mObservable = uploadService.upLoad(getUrl(), requestBody);
                break;
            case "2"://表单形式
                File fil2e = new File("");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fil2e);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", fil2e.getName(), requestFile);

                String token = "dsdsddadad244";
                RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), token);

                HashMap<String, RequestBody> map = new HashMap<>();
                map.put("token", tokenBody);

                UploadService uploadService2 = retrofit.create(UploadService.class);
                mObservable = uploadService2.uploadFileWithPartMap2(getUrl(), map, filePart);
                break;
            default:
                break;
        }
        return mObservable;
    }

}
