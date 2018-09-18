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
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.StringUtils;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
    String text = "";

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
                HashMap<String, String> parametersMap = new HashMap<>();
                parametersMap.put("username", "刘海洋");
                getPostData(parametersMap, "app/account/databytype", true);
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
    public void onComplete(String result, String endUrl, boolean isCache, ApiException e) {
        if (StringUtils.isNotEmpty(result)) {
            text = result + "\n" + text;
            tvmain2.setText("\n----" + endUrl + "--" + "---" + text);
        } else {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        super.onComplete(result, endUrl, isCache, e);
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
                        Disposable disposable = Observable.just(currentBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                tvMsg.setText("提示:上传中");
                                progressBar.setMax((int) totalBytesCount);
                                progressBar.setProgress((int) currentBytesCount);
                            }
                        });
                    }
                }));
        UploadApi uplaodApi = new UploadApi();
        uplaodApi.setPart(part);
        HttpManager httpManager = new HttpManager();
        httpManager.doHttpDeal(uplaodApi, new HttpOnNextListener() {
            @Override
            public void onComplete(String result, String endUrl, boolean isCache, ApiException e) {

            }
        });
    }


}
