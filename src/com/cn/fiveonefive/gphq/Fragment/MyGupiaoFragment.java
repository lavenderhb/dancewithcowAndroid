package com.cn.fiveonefive.gphq.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ab.view.pullview.AbPullToRefreshView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.activity.DetailTabActivity;
import com.cn.fiveonefive.gphq.adapter.MainListAdapter;
import com.cn.fiveonefive.gphq.dto.GPBean;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hb on 2016/3/22.
 */
public class MyGupiaoFragment extends Fragment {
    RefreshListRecevice refreshListRecevice;

    private AbPullToRefreshView abPullToRefreshView;
    private ListView lvMy;

    private List<GuPiaoMainItem> myList;
    private MainListAdapter mainListAdapter;

    private String[] codes;
    private int itemPosition;

    public boolean isDo=false;
    private GetDataTask getDataTask;
    public Timer timer;



    class RefreshListRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("cn.fiveonefive.addMyList")){
                GuPiaoMainItem guPiao=new GuPiaoMainItem();
                guPiao.setCode(intent.getStringExtra("code"));
                guPiao.setName(intent.getStringExtra("name"));
                guPiao.setSymbol(intent.getStringExtra("symbol"));
                myList.add(guPiao);
                Message msg=new Message();
                msg.what=0;
                handler.sendMessage(msg);
            }else if (intent.getAction().equals("cn.fiveonefive.removeMyList")){
                String code=intent.getStringExtra("code");
                for (int i=0;i<myList.size();i++){
                    if (myList.get(i).getCode().equals(code)){
                        myList.remove(i);
                    }
                }
                Message msg=new Message();
                msg.what=0;
                handler.sendMessage(msg);
            }
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mainListAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataTask=new GetDataTask();
//        timer=new Timer();

        if(refreshListRecevice==null){
            refreshListRecevice=new RefreshListRecevice();
            IntentFilter filter=new IntentFilter();
            filter.addAction("cn.fiveonefive.addMyList");
            filter.addAction("cn.fiveonefive.removeMyList");
            getActivity().registerReceiver(refreshListRecevice,filter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_gupiao,null);
        abPullToRefreshView= (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
        abPullToRefreshView.setLoadMoreEnable(false);
        abPullToRefreshView.setPullRefreshEnable(false);
        lvMy= (ListView) view.findViewById(R.id.mListView);
        myList=new ArrayList<GuPiaoMainItem>();
        mainListAdapter=new MainListAdapter(myList,getActivity());
        lvMy.setAdapter(mainListAdapter);
        getDataFromBase();



        lvMy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent=new Intent(getActivity(),DetailTabActivity.class);
                intent.putExtra("code",myList.get(position).getCode());
                intent.putExtra("name",myList.get(position).getName());
                intent.putExtra("symbol",myList.get(position).getSymbol());
                startActivity(intent);
            }
        });
        lvMy.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog isExit = new AlertDialog.Builder(getActivity()).create();
                // 设置对话框标题
                isExit.setTitle("提示");
                // 设置对话框消息
                isExit.setMessage("确定删除当前自选股吗");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
                // 显示对话框
                isExit.show();
                itemPosition=position;
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        isDo=true;
//        GlobStr.MyState=true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isDo=false;
//        GlobStr.MyState=false;
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        try{
            timer.cancel();
            timer=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        if(refreshListRecevice!=null){
            getActivity().unregisterReceiver(refreshListRecevice);
        }
        super.onDestroy();
    }
    DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case AlertDialog.BUTTON_POSITIVE:
                    String code=myList.get(itemPosition).getCode();
                    try {
                        MyApplication.db.delete(MyGuPiao.class, WhereBuilder.b("code", "=", code));
                        myList.remove(itemPosition);
                        mainListAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
    public void doRefresh(){
        timer.schedule(new RefreshTask(),0,1000* GlobStr.MyGPPeriod);
    }
    public class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if(!GlobStr.MyState)
                return;
            if (!isDo)
                return;
            if (getDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                getDataTask.cancel(true);
                getDataTask = new GetDataTask();
                getDataTask.execute();
            } else if (getDataTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                getDataTask.execute();
            }
        }
    }
    class GetDataTask extends AsyncTask<String ,String ,String> {

        @Override
        protected void onPreExecute() {
//            getDataFromBase();
            if(myList==null||myList.size()==0)
                return;
            codes=new String[myList.size()];
            for (int i=0;i<myList.size();i++){
                codes[i]=myList.get(i).getCode();
            }
        }

        @Override
        protected String doInBackground(String...code0) {
            if(codes==null)
                return null;
            for (String code:codes){
                Result result=getDataFromOL(code);
                if(result.getResultCode()==0){
                    onProgressUpdate(new String[]{result.getResultData(),code});
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... value) {
            //TODO
            int indexBegin = value[0].indexOf("code");
            int indexEnd = value[0].indexOf(";");
            String aaa=value[0].substring(indexBegin - 2, indexEnd - 3);
            Gson gson=new Gson();
            GPBean gp= gson.fromJson(aaa,GPBean.class);
//            JSONObject object = JSONObject.fromObject(aaa);
//            GPBean gp=(GPBean)JSONObject.toBean(object, GPBean.class);
            String code=gp.getCode();

            for (int i=0;i<myList.size();i++){
                if(code.equals(myList.get(i).getCode())){
                    myList.get(i).setNewPrice(gp.getPrice());
                    myList.get(i).setZdf(gp.getPercent());
                }
            }
            Message msg=new Message();
            msg.what=0;
            handler.sendMessage(msg);
            super.onProgressUpdate(value);
        }
    }
    private void getDataFromBase(){
        List<MyGuPiao> tempList=null;
        try{
            tempList=MyApplication.db.findAll(MyGuPiao.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (tempList==null)
            return;
        myList.clear();
        for(int i=0;i<tempList.size();i++){
            GuPiaoMainItem guPiaoMainItem=new GuPiaoMainItem();
            guPiaoMainItem.setSymbol(tempList.get(i).getSymbol());
            guPiaoMainItem.setName(tempList.get(i).getName());
            guPiaoMainItem.setCode(tempList.get(i).getCode());
            myList.add(guPiaoMainItem);
        }
        Message msg=new Message();
        msg.what=0;
        handler.sendMessage(msg);


    }
    private Result getDataFromOL(String code){
        GetHttpResult getHttpResult=new GetHttpResult(GlobStr.GPUrl+code);
        Result result=getHttpResult.getResult();
        return result;
    }


    private void setRefreshListRecevice(){
        if(refreshListRecevice==null){
            refreshListRecevice=new RefreshListRecevice();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        getActivity().registerReceiver(refreshListRecevice, filter);


    }
    private void destoryReFreshListRecevice(){
        if(refreshListRecevice != null) {
            getActivity().unregisterReceiver(refreshListRecevice);
        }
    }
}
