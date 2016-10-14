package com.cn.fiveonefive.gphq.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.ab.util.AbToastUtil;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.uzmap.pkg.openapi.ExternalActivity;
import com.uzmap.pkg.openapi.Html5EventListener;
import com.uzmap.pkg.openapi.WebViewProvider;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hb on 2016/4/18.
 */
public class WebPageModule extends ExternalActivity{
    public static final String ALERT = "cn.jpush.android.ALERT";
    public static final String EXTRA = "cn.jpush.android.EXTRA";
    public static final String NOTIFICATION_ID= "cn.jpush.android.NOTIFICATION_ID";
    public static final String NOTIFICATION_CONTENT_TITLE ="cn.jpush.android.NOTIFICATION_CONTENT_TITLE";
    public static final String MSG_ID = "cn.jpush.android.MSG_ID";
    SendH5Msg sendH5Msg;

    String state="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.red1);//通知栏所需颜色
//        }
        //打开股票详情
        this.addHtml5EventListener(new Html5EventListener("start fiveonefive activity") {
            @Override
            public void onReceive(WebViewProvider webViewProvider, Object o) {
                Intent intent =new Intent(WebPageModule.this,ActivityTab.class);
                startActivity(intent);
            }
        });
        if(sendH5Msg==null){
            sendH5Msg=new SendH5Msg();
            IntentFilter filter=new IntentFilter();
            filter.addAction("cn.fiveonefive.sendH5Msg");
            this.registerReceiver(sendH5Msg, filter);
        }
        if (getIntent().getStringExtra("state")!=null){
            state=getIntent().getStringExtra("state");
        }
        if (state.equals("fromJPUSH")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle=getIntent().getExtras();
                    Intent intent0 = new Intent("cn.fiveonefive.sendH5Msg");
                    intent0.putExtras(bundle);
                    WebPageModule.this.sendBroadcast(intent0);
                }
            },1200);
        }

        MyApplication.addMainActivity(this);
        MyApplication.addActivity(this);
    }
    class SendH5Msg extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("cn.fiveonefive.sendH5Msg")){
                Bundle bundle = intent.getExtras();
                String alert=bundle.getString(ALERT);
                String extra=bundle.getString(EXTRA);
                int noticationId=bundle.getInt(NOTIFICATION_ID);
                String noticationContentTitle=bundle.getString(NOTIFICATION_CONTENT_TITLE);
                String messageId=bundle.getString(MSG_ID);
                JSONObject jsonObject=new JSONObject();

                try {
                    jsonObject.put(ALERT,alert);
                    jsonObject.put(EXTRA,extra);
                    jsonObject.put(NOTIFICATION_ID,noticationId);
                    jsonObject.put(NOTIFICATION_CONTENT_TITLE,noticationContentTitle);
                    jsonObject.put(MSG_ID,messageId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WebPageModule.this.sendEventToHtml5("test",jsonObject);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sendH5Msg!=null){
            this.unregisterReceiver(sendH5Msg);
        }
        MyApplication.removeMainActivity();
        MyApplication.removeActivity(this);
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>1000){
                AbToastUtil.showToast(WebPageModule.this,"再按一次返回键退出");
                exitTime=System.currentTimeMillis();
            }else {
                MyApplication.killAll();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}