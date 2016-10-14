package com.cn.fiveonefive.gphq.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2016/3/22.
 */
public class SearchListActivity extends AbActivity implements AbPullToRefreshView.OnFooterLoadListener{

    private AbPullToRefreshView abPullToRefreshView;

    private ImageView ivDeleteText;
    private EditText etSearch;
    private Button btnSearch;
    private ListView listResult;

    private TempListAdapter tempListAdapter;
    private List<GuPiaoMainItem> listTemp;
    private GetTempListTask getTempListTask;
    private ProgressDialog progressDialog=null;

    InputMethodManager im ;

    private int page=0;

    private Button btnBack;     
    private TextView tvTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.activity_search);

        btnBack= (Button) findViewById(R.id.btnBack);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(R.string.search_page_name);


        abPullToRefreshView= (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        abPullToRefreshView.setLoadMoreEnable(true);
        abPullToRefreshView.setPullRefreshEnable(false);
        abPullToRefreshView.setOnFooterLoadListener(this);
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch= (Button) findViewById(R.id.btnSearch);
        listResult= (ListView) findViewById(R.id.result);

        ivDeleteText.setOnClickListener(deletListener);
        etSearch.addTextChangedListener(etWatcher);
        btnSearch.setOnClickListener(searchListener);

        listTemp=new ArrayList<GuPiaoMainItem>();
        tempListAdapter=new TempListAdapter(listTemp,this);
        listResult.setAdapter(tempListAdapter);

        getTempListTask=new GetTempListTask();

        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                etSearch.clearFocus();
                im.hideSoftInputFromWindow(SearchListActivity.this.getCurrentFocus().getWindowToken(), 0);
                Intent intent = new Intent(SearchListActivity.this, DetailTabActivity.class);
                intent.putExtra("code", listTemp.get(position).getCode());
                intent.putExtra("name", listTemp.get(position).getName());
                intent.putExtra("symbol", listTemp.get(position).getSymbol());
                startActivity(intent);
            }
        });
        MyApplication.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
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
            } else {
                ivDeleteText.setVisibility(View.VISIBLE);
            }

        }

    };

    @Override
    public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
        getDate();
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
                AbToastUtil.showToast(SearchListActivity.this, "查询结果为空");
                progressDialog.dismiss();
                abPullToRefreshView.onFooterLoadFinish();
            }
            super.onPostExecute(str);
        }

    };
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
            progressDialog= ProgressDialog.show(SearchListActivity.this, "", "正在查询", false, true);
//            etSearch.clearFocus();
            im.hideSoftInputFromWindow(SearchListActivity.this.getCurrentFocus().getWindowToken(), 0);
            page=0;
            listTemp.clear();
            getDate();
        }
    };

}
