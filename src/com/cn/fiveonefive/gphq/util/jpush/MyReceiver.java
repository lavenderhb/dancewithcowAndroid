package com.cn.fiveonefive.gphq.util.jpush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.cn.fiveonefive.gphq.activity.WebPageModule;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String Push_ID = "cn.jpush.android.PUSH_ID";
	public static final String ALERT = "cn.jpush.android.ALERT";
	public static final String EXTRA = "cn.jpush.android.EXTRA";
	public static final String NOTIFICATION_ID= "cn.jpush.android.NOTIFICATION_ID";
	public static final String NOTIFICATION_CONTENT_TITLE ="cn.jpush.android.NOTIFICATION_CONTENT_TITLE";
	public static final String MSG_ID = "cn.jpush.android.MSG_ID";


	@Override
	public void onReceive(final Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			if (MyApplication.activityMain==null){
				Intent intent1=new Intent(context, WebPageModule.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent1.putExtra("state","fromJPUSH");
				intent1.putExtras(bundle);
				context.startActivity(intent1);
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						Intent intent0 = new Intent("cn.fiveonefive.sendH5Msg");
//						intent0.putExtras(bundle);
//						context.sendBroadcast(intent0);
//					}
//				},1200);


			}else if(isTopActivy("ComponentInfo{com.cn.fiveonefive.gphq/com.cn.fiveonefive.gphq.activity.WebPageModule}",context)){
				Intent intent0 = new Intent("cn.fiveonefive.sendH5Msg");
				intent0.putExtras(bundle);
				context.sendBroadcast(intent0);
			}else {
				Intent intent1=new Intent(context.getApplicationContext(),WebPageModule.class);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent1);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent0 = new Intent("cn.fiveonefive.sendH5Msg");
						intent0.putExtras(bundle);
						context.sendBroadcast(intent0);
					}
				},500);
			}






//			String alert=bundle.getString(ALERT);
//			String extra=bundle.getString(EXTRA);
//			int noticationId=bundle.getInt(NOTIFICATION_ID);
//			String noticationContentTitle=bundle.getString(NOTIFICATION_CONTENT_TITLE);
//			String messageId=bundle.getString(MSG_ID);
//			JSONObject jsonObject=new JSONObject();
//
//			try {
//				jsonObject.put(ALERT,alert);
//				jsonObject.put(EXTRA,extra);
//				jsonObject.put(NOTIFICATION_ID,noticationId);
//				jsonObject.put(NOTIFICATION_CONTENT_TITLE,noticationContentTitle);
//				jsonObject.put(MSG_ID,messageId);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}

        	//打开自定义的Activity
//        	Intent i = new Intent(context, TestActivity.class);
//        	i.putExtras(bundle);
//        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	context.startActivity(i);
        	
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}
	public static boolean isTopActivy(String cmdName, Context context)
	{
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
		String cmpNameTemp = null;
		if (null != runningTaskInfos)
		{
			cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
		}
		if (null == cmpNameTemp)
		{
			return false;
		}
		return cmpNameTemp.equals(cmdName);

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
}
