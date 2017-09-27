package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.nsu.edu.androidmvpdemo.R;
import com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.UploadApi;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.upload.ProgressRequestBody;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.upload.UploadProgressListener;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 */
public class Main2Activity extends BaseActivity {

    private android.widget.TextView tvmain2;
    private android.widget.Button btnrequest;
    private Button btnrequest2;
    private Button btnrequest3;
    private com.daimajia.numberprogressbar.NumberProgressBar progressBar;
    private TextView tvMsg;
    private android.widget.ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.img = (ImageView) findViewById(R.id.img);
        this.tvMsg = (TextView) findViewById(R.id.tv_msg);
        this.progressBar = (NumberProgressBar) findViewById(R.id.number_progress_bar);
        this.btnrequest3 = (Button) findViewById(R.id.btn_request3);
        this.btnrequest2 = (Button) findViewById(R.id.btn_request2);
        this.btnrequest = (Button) findViewById(R.id.btn_request);
        this.tvmain2 = (TextView) findViewById(R.id.tv_main2);


        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeakHashMap<String, Object> parametersMap = new WeakHashMap<>();
                parametersMap.put("username", "刘海洋");
                getPostData(parametersMap, "app/system/loadConfigItem");
            }
        });
        btnrequest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, DownLaodActivity.class));
            }
        });
        btnrequest3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Map<String, Object> parametersMap = new HashMap<>();
                parametersMap.put("username", "刘海洋");*/
              /*  SubjectPostApi postEntity = new SubjectPostApi("app", null);
                HttpManager httpManager = new HttpManager(Main2Activity.this);
                httpManager.doHttpDeal(postEntity);*/
            }
        });


    }


    @Override
    public void onSuccess(String resulte, String mothead) {
        tvmain2.setText(mothead + "--" + "---" + resulte);
    }


    //不需要处理失败返回时，可不要
    @Override
    public void onErrorException(ApiException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }


    /*********************************************文件上传***************************************************/

    private void uploadeDo() {
        File file = new File("/storage/emulated/0/Download/11.jpg");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file_name", file.getName(), new ProgressRequestBody(requestBody,
                new UploadProgressListener() {
                    @Override
                    public void onProgress(final long currentBytesCount, final long totalBytesCount) {

                /*回到主线程中，可通过timer等延迟或者循环避免快速刷新数据*/
                        Observable.just(currentBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {

                            @Override
                            public void call(Long aLong) {
                                tvMsg.setText("提示:上传中");
                                progressBar.setMax((int) totalBytesCount);
                                progressBar.setProgress((int) currentBytesCount);
                            }
                        });
                    }
                }));
        UploadApi uplaodApi = new UploadApi();
        uplaodApi.setPart(part);
        HttpManager httpManager = new HttpManager(new HttpOnNextListener() {
            @Override
            public void onNext(String resulte, String endUrl, boolean isCache) {
                onSuccess(resulte, endUrl);
                dismissProgressDialog();
            }

            @Override
            public void onError(ApiException e) {
                onErrorException(e);
                dismissProgressDialog();

            }
        });
        httpManager.doHttpDeal(uplaodApi);
    }


    /**
     * 上传回调
     */
    HttpOnNextListener httpOnNextListener = new HttpOnNextListener() {

        @Override
        public void onNext(String resulte, String endUrl, boolean isCache) {

        }

        @Override
        public void onError(ApiException e) {

        }
    };

}
