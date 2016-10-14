package com.cn.fiveonefive.gphq.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by hb on 2016/4/20.
 */
public class FirstActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setDb();

        try{
            initConfig();
        }catch (Exception e){
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(FirstActivity.this,WebPageModule.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        },800);

        MyApplication.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }

    private void setDb(){
        SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        Boolean is_first=preferences.getBoolean("FIRST",true);
        if(is_first){ //第一次
            editor.putBoolean("FIRST", false).commit();
            if(!GlobMethod.getIsAdd("0000001")){
                MyGuPiao myGuPiao=new MyGuPiao();
                myGuPiao.setCode("0000001");
                myGuPiao.setName("上证指数");
                myGuPiao.setSymbol("000001");
                try {
                    MyApplication.db.saveBindingId(myGuPiao);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (!GlobMethod.getIsAdd("1399001")){
                MyGuPiao myGuPiao=new MyGuPiao();
                myGuPiao.setCode("1399001");
                myGuPiao.setName("深证成指");
                myGuPiao.setSymbol("399001");
                try {
                    MyApplication.db.saveBindingId(myGuPiao);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
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
        GlobStr.listBD.add(GlobStr.ZFUrl);
        GlobStr.listBD.add(GlobStr.DFUrl);
        GlobStr.listBD.add(GlobStr.HSLUrl);
        GlobStr.listBD.add(GlobStr.ZFBUrl);

    }
}
