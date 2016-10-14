package com.cn.fiveonefive.gphq.Fragment;


import android.app.AlertDialog;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.activity.ActivityDetail;
import com.cn.fiveonefive.gphq.adapter.MainListAdapter;
import com.cn.fiveonefive.gphq.dto.BaseBean;
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

/**
 * Created by hb on 2016/9/3.
 */
public class FragmentMy extends Fragment {


    RefreshListRecevice refreshListRecevice;

    private ListView lvMy;

    private List<GuPiaoMainItem> myList;
    private MainListAdapter mainListAdapter;

    private String[] codes;
    private int itemPosition;

    private GetDataTask getDataTask;

    public boolean isDo=false;
    Gson gson;


    class RefreshListRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("cn.fiveonefive.addMyList")){
                GuPiaoMainItem guPiao=new GuPiaoMainItem();
                guPiao.setCode(intent.getStringExtra("code"));
                guPiao.setName(intent.getStringExtra("name"));
                guPiao.setSymbol(intent.getStringExtra("symbol"));
                myList.add(guPiao);
                mainListAdapter.notifyDataSetChanged();
            }else if (intent.getAction().equals("cn.fiveonefive.removeMyList")){
                String code=intent.getStringExtra("code");
                for (int i=0;i<myList.size();i++){
                    if (myList.get(i).getCode().equals(code)){
                        myList.remove(i);
                    }
                }
                mainListAdapter.notifyDataSetChanged();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.my_gp_fre,null );

        if(refreshListRecevice==null){
            refreshListRecevice=new RefreshListRecevice();
            IntentFilter filter=new IntentFilter();
            filter.addAction("cn.fiveonefive.addMyList");
            filter.addAction("cn.fiveonefive.removeMyList");
            getActivity().registerReceiver(refreshListRecevice,filter);
        }

        lvMy= (ListView) view.findViewById(R.id.mListView);


        myList=new ArrayList<GuPiaoMainItem>();
        mainListAdapter=new MainListAdapter(myList,getActivity());
        lvMy.setAdapter(mainListAdapter);
        lvMy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ActivityDetail.class);
                intent.putExtra("name",myList.get(position).getName());
                intent.putExtra("symbol",myList.get(position).getSymbol());
                intent.putExtra("code",myList.get(position).getCode());
                intent.putExtra("state",true);
                List<BaseBean> baseBeanList=new ArrayList<BaseBean>();
                for (GuPiaoMainItem gp:myList){
                    BaseBean baseBean=new BaseBean();
                    baseBean.setName(gp.getName());
                    baseBean.setSymbol(gp.getSymbol());
                    baseBean.setCode(gp.getCode());
                    baseBeanList.add(baseBean);
                }
                intent.putExtra("beanList",gson.toJson(baseBeanList));
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
        getDataFromBase();
        return view;
    }

    @Override
    public void onResume() {
        getDataTask=new GetDataTask();
        getDataTask.execute();
        super.onResume();
    }

    @Override
    public void onStop() {
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("my");
        }
        super.onStop();
    }
    public void start(){
        getDataTask=new GetDataTask();
        MyApplication.taskMap.put("my",getDataTask);
    }
    public void stop(){
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("my");
        }
    }

    @Override
    public void onDestroy() {
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

    private void getDataFromBase(){
        List<MyGuPiao> tempList=null;
        try{
            tempList= MyApplication.db.findAll(MyGuPiao.class);
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
    class GetDataTask extends AsyncTask<Object,String ,String> {

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
        protected String doInBackground(Object[] objects) {
            if(codes==null)
                return null;
            for (String code:codes){
                Result result=getDataFromOL(code);
                if(result.getResultCode()==0){
                    publishProgress(new String[]{result.getResultData(),code});
                }
                if(isCancelled()) return null;
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
                    myList.get(i).setZde(gp.getUpdown());
                }
            }
            Message msg=new Message();
            msg.what=0;
            handler.sendMessage(msg);
            super.onProgressUpdate(value);
        }

        @Override
        protected void onPostExecute(String s) {
            if (isDo){
                getDataTask=new GetDataTask();
                MyApplication.taskMap.put("my",getDataTask);
            }
            super.onPostExecute(s);
        }
    }
    private Result getDataFromOL(String code){
        GetHttpResult getHttpResult=new GetHttpResult(GlobStr.GPUrl+code);
        Result result=getHttpResult.getResult();
        return result;
    }

}
