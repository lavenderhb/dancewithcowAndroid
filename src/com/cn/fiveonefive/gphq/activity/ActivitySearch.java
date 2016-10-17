package com.cn.fiveonefive.gphq.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.adapter.TempListAdapter;
import com.cn.fiveonefive.gphq.dto.CXBean;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.util.SystemBarTintManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hb on 2016/3/22.
 */
public class ActivitySearch extends AbActivity implements AbPullToRefreshView.OnFooterLoadListener{

    private AbPullToRefreshView abPullToRefreshView;

    private ImageView ivDeleteText;
    private EditText etSearch;
    private TextView btnSearch;
    private ListView listResult;

    private TempListAdapter tempListAdapter;
    private List<GuPiaoMainItem> listTemp;
    private GetTempListTask getTempListTask;
    private ProgressDialog progressDialog=null;

    InputMethodManager im ;

    private int page=0;

    TextView backBtn,title,rightBtn,title0;

    ExecutorService singleThreadExecutor;
    List<Thread> threadList=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red1);//通知栏所需颜色
        }
        setAbContentView(R.layout.search_act);
        singleThreadExecutor = Executors.newSingleThreadExecutor();

        abPullToRefreshView= (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        abPullToRefreshView.setLoadMoreEnable(true);
        abPullToRefreshView.setPullRefreshEnable(false);
        abPullToRefreshView.setOnFooterLoadListener(this);
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        findview();
        setListener();

        title.setText("搜索");
        rightBtn.setVisibility(View.GONE);
        title0.setVisibility(View.GONE);

        listTemp=new ArrayList<>();
        tempListAdapter=new TempListAdapter(listTemp,this);
        listResult.setAdapter(tempListAdapter);

//        getTempListTask=new GetTempListTask();
        MyApplication.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        im.hideSoftInputFromWindow(ActivitySearch.this.getCurrentFocus().getWindowToken(), 0);
        MyApplication.removeActivity(this);
    }
    private void setListener(){
        ivDeleteText.setOnClickListener(deletListener);
        etSearch.addTextChangedListener(etWatcher);
        btnSearch.setOnClickListener(searchListener);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                etSearch.clearFocus();
                im.hideSoftInputFromWindow(ActivitySearch.this.getCurrentFocus().getWindowToken(), 0);
                Intent intent = new Intent(ActivitySearch.this, ActivityDetail.class);
                intent.putExtra("code", listTemp.get(position).getCode());
                intent.putExtra("name", listTemp.get(position).getName());
                intent.putExtra("symbol", listTemp.get(position).getSymbol());
                startActivity(intent);
            }
        });
    }

    TextWatcher etWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                ivDeleteText.setVisibility(View.GONE);
                listTemp.clear();
                tempListAdapter.notifyDataSetChanged();
            } else {
                ivDeleteText.setVisibility(View.VISIBLE);
                page=1;
//                progressDialog= ProgressDialog.show(ActivitySearch.this, "", "正在查询", false, true);
                listTemp.clear();
                MyThread myThread=new MyThread(s.toString());
                singleThreadExecutor.execute(myThread);
            }

        }

    };

    @Override
    public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
//        getDate();
        page++;
        progressDialog=ProgressDialog.show(ActivitySearch.this, "", "正在查询", false, true);
        MyThread myThread=new MyThread(etSearch.getText().toString());
        myThread.start();
    }

    class GetTempListTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... keyWord) {
            GetHttpResult getHttpResult=new GetHttpResult(GlobStr.SearchUrl+(GlobStr.SearchCountOfOnePage*page)+"&word="+keyWord[0]);
            Result result=getHttpResult.getResult();
            if(result.getResultCode()==0){
                String str=result.getResultData();
                if(!str.equals("")){
                    int indexBegin = str.indexOf("[");
                    int endBegin = str.indexOf("]");
                    String aaa=str.substring(indexBegin+1,endBegin);
                    String[] array;
                    try {
                        array = aaa.split("\\},");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("searchResult", "wrong");
                        return "1";
                    }
                    if (array.length==1&&array[0].equals("")){
                        return "2";
                    }

                    ArrayList<String> newArray = new ArrayList<String>();
                    for(int i=0;i<array.length;i++){
                        String arr = array[i];
                        if(i<array.length-1){
                            arr=arr.concat("}");
                        }
                        newArray.add(arr);
                    }
                    Gson gson=new Gson();
                    ArrayList<CXBean> cxBeanList = new ArrayList<CXBean>();
                    for(Object item:newArray){
                        CXBean cx=gson.fromJson((String) item, CXBean.class);
                        cxBeanList.add(cx);
                    }
                    for(int i=GlobStr.SearchCountOfOnePage*(page-1);i<cxBeanList.size();i++){
                        GuPiaoMainItem guPiaoMainItem=new GuPiaoMainItem();
                        guPiaoMainItem.setName(cxBeanList.get(i).getName());
                        guPiaoMainItem.setSymbol(cxBeanList.get(i).getSymbol());
                        if(cxBeanList.get(i).getType().equals("SH")) {
                            guPiaoMainItem.setCode(0 + cxBeanList.get(i).getSymbol());
                            listTemp.add(guPiaoMainItem);
                        }else if(cxBeanList.get(i).getType().equals("SZ")){
                            guPiaoMainItem.setCode(1+cxBeanList.get(i).getSymbol());
                            listTemp.add(guPiaoMainItem);
                        }else {
                        }
                    }
                }
                return "0";
            }else{
                return "1";
            }
        }

        @Override
        protected void onPostExecute(String str) {
            if(str.equals("1")){
                progressDialog.dismiss();
                abPullToRefreshView.onFooterLoadFinish();
            }
            if (str.equals("0")){
                tempListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                abPullToRefreshView.onFooterLoadFinish();
            }
            if (str.equals("2")){
                AbToastUtil.showToast(ActivitySearch.this, "查询结果为空");
                progressDialog.dismiss();
                abPullToRefreshView.onFooterLoadFinish();
            }
            super.onPostExecute(str);
        }

    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            progressDialog.dismiss();
            abPullToRefreshView.onFooterLoadFinish();
            switch (msg.what){
                case 0:
                    tempListAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    tempListAdapter.notifyDataSetChanged();
//                    AbToastUtil.showToast(ActivitySearch.this, "查询结果为空");
                    break;
            }

        }
    };
    class MyThread extends Thread{
        String keyWord;
        public MyThread (String keyWord){
            this.keyWord=keyWord;
        }
        @Override
        public void run() {
            GetHttpResult getHttpResult=new GetHttpResult(GlobStr.SearchUrl+
                    (GlobStr.SearchCountOfOnePage*page)+"&word="+keyWord);
            Result result=getHttpResult.getResult();
            if(result.getResultCode()==0){
                String str=result.getResultData();
                if(!str.equals("")){
                    int indexBegin = str.indexOf("[");
                    int endBegin = str.indexOf("]");
                    String aaa=str.substring(indexBegin+1,endBegin);
                    String[] array=null;
                    try {
                        array = aaa.split("\\},");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("searchResult", "wrong");
                    }
                    if (array.length==1&&array[0].equals("")){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                        return;
                    }

                    ArrayList<String> newArray = new ArrayList<>();
                    for(int i=0;i<array.length;i++){
                        String arr = array[i];
                        if(i<array.length-1){
                            arr=arr.concat("}");
                        }
                        newArray.add(arr);
                    }
                    Gson gson=new Gson();
                    ArrayList<CXBean> cxBeanList = new ArrayList<>();
                    for(Object item:newArray){
                        CXBean cx=gson.fromJson((String) item, CXBean.class);
                        cxBeanList.add(cx);
                    }
                    for(int i=GlobStr.SearchCountOfOnePage*(page-1);i<cxBeanList.size();i++){
                        GuPiaoMainItem guPiaoMainItem=new GuPiaoMainItem();
                        guPiaoMainItem.setName(cxBeanList.get(i).getName());
                        guPiaoMainItem.setSymbol(cxBeanList.get(i).getSymbol());
                        if(cxBeanList.get(i).getType().equals("SH")) {
                            guPiaoMainItem.setCode(0 + cxBeanList.get(i).getSymbol());
                            listTemp.add(guPiaoMainItem);
                        }else if(cxBeanList.get(i).getType().equals("SZ")){
                            guPiaoMainItem.setCode(1+cxBeanList.get(i).getSymbol());
                            listTemp.add(guPiaoMainItem);
                        }
                    }
                    if (listTemp.size()<=0){
                        Message message=new Message();
                        message.what=2;
                        handler.sendMessage(message);
                    }else {
                        Message message=new Message();
                        message.what=0;
                        handler.sendMessage(message);
                    }
                }

            }

        }
    }


    private void getDate(){
        if(getTempListTask.getStatus().equals(android.os.AsyncTask.Status.FINISHED)){
            getTempListTask.cancel(true);
            getTempListTask=new GetTempListTask();
            page++;
            getTempListTask.execute(etSearch.getText().toString());
        }else if(getTempListTask.getStatus().equals(android.os.AsyncTask.Status.PENDING)){
            page++;
            getTempListTask.execute(etSearch.getText().toString());
        }else{
            progressDialog.dismiss();
            abPullToRefreshView.onFooterLoadFinish();
        }
    }


    View.OnClickListener deletListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etSearch.setText("");
        }
    };
    View.OnClickListener searchListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog= ProgressDialog.show(ActivitySearch.this, "", "正在查询", false, true);
//            etSearch.clearFocus();
            im.hideSoftInputFromWindow(ActivitySearch.this.getCurrentFocus().getWindowToken(), 0);
            page=0;
            listTemp.clear();
            getDate();
        }
    };
    private void findview(){
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch= (TextView) findViewById(R.id.btnSearch);
        listResult= (ListView) findViewById(R.id.result);
        backBtn= (TextView) findViewById(R.id.leftBtn);
        title= (TextView) findViewById(R.id.codeName);
        rightBtn= (TextView) findViewById(R.id.rightBtn);
        title0= (TextView) findViewById(R.id.codeSymbol);
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
