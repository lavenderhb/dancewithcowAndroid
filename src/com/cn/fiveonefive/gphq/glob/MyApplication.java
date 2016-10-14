package com.cn.fiveonefive.gphq.glob;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import cn.jpush.android.api.JPushInterface;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.service.MyThread;
import com.cn.fiveonefive.gphq.util.jpush.ExampleUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.uzmap.pkg.openapi.APICloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by hb on 2016/4/19.
 */
public class MyApplication extends Application implements DbUtils.DbUpgradeListener{
    public static DbUtils db;
    public static List<Activity> activitys = new ArrayList<Activity>();
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private MessageReceiver mMessageReceiver;
    public  static Map<String,AsyncTask> taskMap=new HashMap<String, AsyncTask>();
    public static Boolean isDo=true;
    MyThread myThread;
    @Override
    public void onCreate() {
        super.onCreate();
        APICloud.initialize(this);
        initDb();
        JPushInterface.init(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        myThread=new MyThread();
        myThread.start();
    }

    @Override
    public void onTerminate() {
        JPushInterface.stopPush(this);
        super.onTerminate();
        myThread.destroy();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
//                setCostomMsg(showMsg.toString());
            }
        }
    }
//    private void setCostomMsg(String msg){
//        if (null != msgText) {
//            msgText.setText(msg);
//            msgText.setVisibility(android.view.View.VISIBLE);
//        }
//    }


    public static Activity activityMain=null;
    public static void addMainActivity(Activity activity){
        activityMain=activity;
    }
    public static void removeMainActivity(){
        activityMain=null;
    }

    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }
    public static void addActivity(int position,Activity activity) {
        activitys.add(position,activity);
    }
    public static void removeActivity(Activity activity){
        activitys.remove(activity);
    }
    public static void killAll() {

        for (Activity activity : activitys) {
            activity.finish();
        }
        activitys.clear();
    }
    private void initDb(){
        File file=new File(GlobStr.dbLocalPath);
        if (!file.exists()){
            file.mkdirs();
        }
        db=DbUtils.create(this, GlobStr.dbLocalPath,"GuPiao.db",1,this);
    }
    private void initConfig() throws Exception{
        File file=new File(GlobStr.configLocalPath);
        //复制文件
        if(!file.exists()){
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(GlobStr.configLocalPath);
            myInput = this.getAssets().open(GlobStr.configFileName);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while(length > 0)
            {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        }
        //从文件到Glo
        Properties properties= GlobMethod.getConfig(this);
        GlobStr. K_ri_start=Integer.parseInt(properties.getProperty("K_ri_start"));
        GlobStr. K_ri_end=Integer.parseInt(properties.getProperty("K_ri_end").equals("0")?GlobStr.year+"":properties.getProperty("K_ri_end"));
        GlobStr. K_zhou_start=Integer.parseInt(properties.getProperty("K_zhou_start"));
        GlobStr. K_zhou_end=Integer.parseInt(properties.getProperty("K_zhou_end").equals("0")?GlobStr.year+"":properties.getProperty("K_zhou_end"));
        GlobStr. K_yue_start=Integer.parseInt(properties.getProperty("K_yue_start"));
        GlobStr. K_yue_end=Integer.parseInt(properties.getProperty("K_yue_end").equals("0")?GlobStr.year+"":properties.getProperty("K_yue_end"));

        //循环周期
        GlobStr. MyGPPeriod=Integer.parseInt(properties.getProperty("MyGPPeriod"));
        GlobStr. HQPeriod=Integer.parseInt(properties.getProperty("HQPeriod"));
        GlobStr. DetailPeriod=Integer.parseInt(properties.getProperty("DetailPeriod"));
        GlobStr. FenShiPeriod=Integer.parseInt(properties.getProperty("FenShiPeriod"));
        GlobStr. RiKPeriod=Integer.parseInt(properties.getProperty("RiKPeriod"));
        GlobStr. ZhouKPeriod=Integer.parseInt(properties.getProperty("ZhouKPeriod"));
        GlobStr. YueKPeriod=Integer.parseInt(properties.getProperty("YueKPeriod"));

        //是否循环
        GlobStr. MyState=properties.getProperty("MyState").equals("1")?false:true;
        GlobStr. HQState=properties.getProperty("HQState").equals("1")?false:true;
        GlobStr. TabState=properties.getProperty("TabState").equals("1")?false:true;
        GlobStr. FenShiState=properties.getProperty("FenShiState").equals("1")?false:true;
        GlobStr. RiKState=properties.getProperty("RiKState").equals("1")?false:true;
        GlobStr. ZhouKState=properties.getProperty("ZhouKState").equals("1")?false:true;
        GlobStr. YueKState=properties.getProperty("YueKState").equals("1")?false:true;
    }
    @Override
    public void onUpgrade(DbUtils dbUtils, int i, int i1) {
        try {
            db.dropTable(MyGuPiao.class);
            db.createTableIfNotExist(MyGuPiao.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
