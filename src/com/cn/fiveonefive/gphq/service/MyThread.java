package com.cn.fiveonefive.gphq.service;

import android.os.AsyncTask;
import android.util.Log;
import com.cn.fiveonefive.gphq.glob.MyApplication;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hb on 2016/9/8.
 */
public class MyThread extends Thread{

    @Override
    public void run() {
        while (true){
            if (!MyApplication.isDo){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (MyApplication.taskMap.size()>0){
                Iterator iter = MyApplication.taskMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    AsyncTask task = (AsyncTask)entry.getValue();
                    Log.d("thread:",entry.toString());
                    if (task!=null){
                        if (task.getStatus().equals(AsyncTask.Status.PENDING)){
                            task.execute("");
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
