package com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.nsu.edu.androidmvpdemo.R;
import com.nsu.edu.androidmvpdemo.login.rxjava_retrofit.adapter.DownAdapter;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.downlaod.DownInfo;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.downlaod.DownState;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.DbDwonUtil;

import java.io.File;
import java.util.List;
/**
 * 多任務下載
 */
public class DownLaodActivity extends BaseActivity{
    List<DownInfo> listData;
    DbDwonUtil dbUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_laod);
        initResource();
        initWidget();
    }

    /*数据*/
    private void initResource(){
        dbUtil= DbDwonUtil.getInstance();
        listData=dbUtil.queryDownAll();
        /*第一次模拟服务器返回数据掺入到数据库中*/
        if(listData.isEmpty()){
            String[] downUrl=new String[]{"http://pic33.nipic.com/20130907/12906030_161342990000_2.png",
                    "http://img3.redocn.com/tupian/20150430/mantenghuawenmodianshiliangbeijing_3924704.jpg"};
            for (int i = 0; i < downUrl.length; i++) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "test"+i + ".apk");
                DownInfo apkApi=new DownInfo(downUrl[i]);
                apkApi.setId(i);
                apkApi.setState(DownState.START);
                apkApi.setSavePath(outputFile.getAbsolutePath());
                dbUtil.save(apkApi);
            }
            listData=dbUtil.queryDownAll();
        }
    }

    /*加载控件*/
    private void initWidget(){
        EasyRecyclerView recyclerView=(EasyRecyclerView)findViewById(R.id.rv);
        DownAdapter adapter=new DownAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*记录退出时下载任务的状态-复原用*/
        for (DownInfo downInfo : listData) {
            dbUtil.update(downInfo);
        }
    }
}
