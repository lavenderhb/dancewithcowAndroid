package com.cn.fiveonefive.gphq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ab.view.pullview.AbPullToRefreshView;
import com.cn.fiveonefive.gphq.Fragment.HQFragment;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.adapter.BDListAdapter;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2016/4/20.
 */
public class MoreListActivity extends Activity {

    private Button btnBack;
    private TextView tvTitle;
    private Button btnRefresh;


    private GetDataTask getDataTask;
    private int type;
    private String typeName;
    private String url;

    private ListView lvMore;
    private AbPullToRefreshView mPullRefreshView;
    private List<GuPiaoMainItem> listMore;
    private BDListAdapter moreListAdapter;

    private int pageCount=1;
    private int pageSize=20;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mPullRefreshView.onFooterLoadFinish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_list);
        initView();
        initData();
        setListener();
        getDataTask.execute();
        MyApplication.addActivity(this);
    }

    private void initView(){
        btnBack= (Button) findViewById(R.id.btnBack);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        btnRefresh= (Button) findViewById(R.id.btnRefresh);
        mPullRefreshView= (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        lvMore= (ListView) findViewById(R.id.listBD);

    }
    private void initData(){
        type=getIntent().getIntExtra("type",-1);
        typeName=getIntent().getStringExtra("typeName");
        tvTitle.setText(typeName);
        btnRefresh.setVisibility(View.VISIBLE);
        url = GlobStr.listBD.get(type);
        listMore=new ArrayList<GuPiaoMainItem>();
        moreListAdapter=new BDListAdapter(listMore,this);
        lvMore.setAdapter(moreListAdapter);
        mPullRefreshView.setPullRefreshEnable(false);
        getDataTask=new GetDataTask();

    }
    private void setListener(){
        lvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MoreListActivity.this,DetailTabActivity.class);
                intent.putExtra("code",listMore.get(position).getCode());
                intent.putExtra("name",listMore.get(position).getName());
                intent.putExtra("symbol",listMore.get(position).getSymbol());
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    getDataTask.cancel(true);
                    getDataTask = new GetDataTask();
                    getDataTask.execute();
                } else if (getDataTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                    getDataTask.execute();
                }
            }
        });
        mPullRefreshView.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                if (getDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    getDataTask.cancel(true);
                    getDataTask = new GetDataTask();
                    pageCount++;
                    getDataTask.execute();
                } else if (getDataTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                    pageCount++;
                    getDataTask.execute();
                }else{
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    class GetDataTask extends AsyncTask<String ,String ,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (url==null||url.equals("")){
                return false;
            }
            GetHttpResult getHttpResult=new GetHttpResult(url+pageCount*pageSize);
            Result result=getHttpResult.getResult();
            if (result.getResultCode()==0){
                String str=result.getResultData();
                List<String[]> listZF0= HQFragment.changeStrToBD(str);
                List<GuPiaoMainItem> list=new ArrayList<GuPiaoMainItem>();
                for(String[] zf:listZF0){
                    GuPiaoMainItem guPiaoMainItem=new GuPiaoMainItem();
                    guPiaoMainItem.setName(zf[2]);
                    guPiaoMainItem.setSymbol(zf[1]);
                    if(zf[0].substring(zf[0].length()-1,zf[0].length()).equals("2")){//深圳
                        guPiaoMainItem.setCode("1"+zf[1]);
                    }else if (zf[0].substring(zf[0].length()-1,zf[0].length()).equals("1")){//上海
                        guPiaoMainItem.setCode("0"+zf[1]);
                    }
                    guPiaoMainItem.setNewPrice(zf[5]);
                    switch (type){
                        case 0:
                            guPiaoMainItem.setZdf(zf[11]);
                            break;
                        case 1:
                            guPiaoMainItem.setZdf(zf[11]);
                            break;
                        case 2:
                            guPiaoMainItem.setZdf(zf[23]);
                            break;
                        case 3:
                            guPiaoMainItem.setZdf(zf[13]);
                            break;
                    }
                    list.add(guPiaoMainItem);
                }
                listMore.clear();
                listMore.addAll(list);
                return true;
            }else {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mPullRefreshView.onFooterLoadFinish();
            if (aBoolean){
                moreListAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(aBoolean);
        }
    }

}
