package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nsu.edu.androidmvpdemo.R;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class Main2Activity extends BaseActivity implements HttpOnActivityDataListener {

    private android.widget.TextView tvmain2;
    private android.widget.Button btnrequest;
    private Button btnrequest2;
    private Button btnrequest3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.btnrequest3 = (Button) findViewById(R.id.btn_request3);
        this.btnrequest2 = (Button) findViewById(R.id.btn_request2);
        this.btnrequest = (Button) findViewById(R.id.btn_request);
        this.tvmain2 = (TextView) findViewById(R.id.tv_main2);


        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> parametersMap = new HashMap<>();
                parametersMap.put("username", "刘海洋");
                getPostData(parametersMap, "app/system/loadConfigItem", Main2Activity.this);
            }
        });
        btnrequest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Map<String, Object> parametersMap = new HashMap<>();
                parametersMap.put("username", "刘海洋");
                SubjectPostApi postEntity = new SubjectPostApi("app/product/appointmentInvestProList", parametersMap);
                postEntity.setCache(false);
                HttpManager httpManager = new HttpManager(Main2Activity.this);
                httpManager.doHttpDeal(postEntity);*/
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
    public void onNext(String resulte, String mothead) {
        tvmain2.setText(mothead + "--" + "---" + resulte);
    }


    @Override
    public void onError(ApiException e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
