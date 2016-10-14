package com.cn.fiveonefive.gphq.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingTabView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GPBean;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
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
 * Created by hb on 2016/3/27.
 */
public class DetailTabFragment extends Fragment {

    FenShiFragment fenShiFragment;
    RiKFragment riKFragment;
    ZhouKFragment zhouKFragment;
    YueKFragment yueKFragment;

    private GPBean gp;
    public DetailTabFragment(String code,String symbol,String name){
        this.code=code;
        this.symbol=symbol;
        this.name=name;
    }

    private TextView tvPrice,tvZDE,tvZDF,tvJinKai,tvZuoShou,tvCJL,tvCJE;
    private AbSlidingTabView mAbSlidingTabView;
    private GetDataTask getDataTask;

    private int green;
    private int red;
    private int black;

    private boolean isDo=false;
    Timer timer;

    private Button btnBack;
    private TextView tvTitle;
    private Button btnAdd;
    private Button btnSub;

    private String code;
    private String name;
    private String symbol;



    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    break;
            }
        }
    };

    @Override
    public void onStart() {
        if(isAdded()){
            green=getResources().getColor(R.color.color_green);
            red=getResources().getColor(R.color.red);
            black=getResources().getColor(R.color.background_dark);
        }
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataTask=new GetDataTask();
        timer=new Timer();
        if (code==null){
            code="";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.detail_tab,null);

        tvPrice= (TextView) view.findViewById(R.id.priceNow);
        tvZDE= (TextView)view. findViewById(R.id.zde);
        tvZDF= (TextView) view.findViewById(R.id.zdf);
        tvJinKai= (TextView) view.findViewById(R.id.jinkai);
        tvZuoShou= (TextView) view.findViewById(R.id.zuoshou);
        tvCJE= (TextView) view.findViewById(R.id.chengjiaoe);
        tvCJL= (TextView) view.findViewById(R.id.chengjiaoliang);
        mAbSlidingTabView= (AbSlidingTabView) view.findViewById(R.id.mAbSlidingTabView);
        btnBack= (Button) view.findViewById(R.id.btnBack);
        tvTitle= (TextView) view.findViewById(R.id.tvTitle);
        btnAdd= (Button) view.findViewById(R.id.btnAdd);
        btnSub= (Button) view.findViewById(R.id.btnSub);

        initData();
        setListener();

        return view;
    }


    @Override
    public void onResume() {
        isDo=true;
//        GlobStr.TabState=true;
        timer.schedule(new RefreshTask(),0,1000* GlobStr.DetailPeriod);
        super.onResume();
    }

    @Override
    public void onPause() {
        fenShiFragment.isDo=false;
        riKFragment.isDo=false;
        zhouKFragment.isDo=false;
        yueKFragment.isDo=false;

        isDo=false;
//        GlobStr.TabState=false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        try{
            timer.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void initData(){
        tvTitle.setText(name+"("+symbol+")");
        fenShiFragment=new FenShiFragment(code);
        riKFragment=new RiKFragment(code);
        zhouKFragment=new ZhouKFragment(code);
        yueKFragment=new YueKFragment(code);

        List<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(fenShiFragment);
        fragmentList.add(riKFragment);
        fragmentList.add(zhouKFragment);
        fragmentList.add(yueKFragment);

        List<String> tabNameList=new ArrayList<String>();
        tabNameList.add("分时");
        tabNameList.add("日K");
        tabNameList.add("周K");
        tabNameList.add("月K");

        mAbSlidingTabView.addItemViews(tabNameList, fragmentList);
        mAbSlidingTabView.setTabTextColor(Color.BLACK);
        mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
        mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
        mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
        mAbSlidingTabView.setTabPadding(20, 8, 20, 8);

        if (GlobMethod.getIsAdd(code)){
            btnSub.setVisibility(View.VISIBLE);
        }else{
            btnAdd.setVisibility(View.VISIBLE);
        }

    }
    private void setListener(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobMethod.getIsAdd(code)){
                    Intent intent = new Intent("cn.fiveonefive.removeMyList");
                    intent.putExtra("code",code);
                    try {
                        MyApplication.db.delete(MyGuPiao.class, WhereBuilder.b("code", "=", code));
                        getActivity().sendBroadcast(intent);
                        btnAdd.setVisibility(View.VISIBLE);
                        btnSub.setVisibility(View.GONE);
                    }catch (Exception e){
                        e.printStackTrace();
                        AbToastUtil.showToast(getActivity(), "删除失败");
                    }
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobMethod.getIsAdd(code)){
                    MyGuPiao myGuPiao=new MyGuPiao();
                    Intent intent = new Intent("cn.fiveonefive.addMyList");
                    myGuPiao.setCode(code);
                    intent.putExtra("code",code);
                    myGuPiao.setName(name);
                    intent.putExtra("name",name);
                    myGuPiao.setSymbol(symbol);
                    intent.putExtra("symbol",symbol);
                    try{
                        MyApplication.db.saveBindingId(myGuPiao);
                        getActivity().sendBroadcast(intent);
                        btnAdd.setVisibility(View.GONE);
                        btnSub.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        e.printStackTrace();
                        AbToastUtil.showToast(getActivity(), "添加失败");
                    }
                }
            }
        });


        mAbSlidingTabView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switch (i){
                    case 0:
                        fenShiFragment.isDo=true;
                        riKFragment.isDo=false;
                        zhouKFragment.isDo=false;
                        yueKFragment.isDo=false;
                        if (fenShiFragment.timer==null){
                            fenShiFragment.timer=new Timer();
                            fenShiFragment.doRefresh();
                        }
                        break;
                    case 1:
                        fenShiFragment.isDo=false;
                        riKFragment.isDo=true;
                        zhouKFragment.isDo=false;
                        yueKFragment.isDo=false;
                        if (riKFragment.timer==null){
                            riKFragment.timer=new Timer();
                            riKFragment.doRefresh();
                        }
                        break;
                    case 2:
                        fenShiFragment.isDo=false;
                        riKFragment.isDo=false;
                        zhouKFragment.isDo=true;
                        yueKFragment.isDo=false;
                        if (zhouKFragment.timer==null){
                            zhouKFragment.timer=new Timer();
                            zhouKFragment.doRefresh();
                        }
                        break;
                    case 3:
                        fenShiFragment.isDo=false;
                        riKFragment.isDo=false;
                        zhouKFragment.isDo=false;
                        yueKFragment.isDo=true;
                        if (yueKFragment.timer==null){
                            yueKFragment.timer=new Timer();
                            yueKFragment.doRefresh();
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (!GlobStr.TabState)
                return;
            if (!isDo)
                return;
            if (getDataTask.getStatus().equals(AsyncTask.Status.FINISHED)){
                getDataTask.cancel(true);
                getDataTask = new GetDataTask();
                getDataTask.execute();
            }
            else if (getDataTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                getDataTask.execute();
            }
        }
    }
    class GetDataTask extends AsyncTask<String ,String ,String> {

        @Override
        protected String doInBackground(String...code0) {
            if (code.equals("")){
                return null;
            }
            GetHttpResult getHttpResult=new GetHttpResult(GlobStr.GPUrl+code);
            Result result=getHttpResult.getResult();
            if(result.getResultCode()==0){
                return result.getResultData();
            }else {
                return null;
            }
        }


        @Override
        protected void onPostExecute(String value) {
            if(value==null)
                return ;
            try {
                int indexBegin = value.indexOf("code");
                int indexEnd = value.indexOf(";");
                String aaa=value.substring(indexBegin - 2, indexEnd - 3);
                Gson gson=new Gson();
                gp= gson.fromJson(aaa,GPBean.class);
            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            tvPrice.setText(GlobMethod.cgPriceToDF(gp.getPrice()));
            tvZDE.setText(gp.getUpdown());
            float s=0;
            if(gp.getPercent()!=null) {
                s = Float.valueOf(gp.getPercent());
            }
            tvZDF.setText(GlobMethod.cgPercentToDF(s));
            if(s<0){
                tvPrice.setTextColor(green);
                tvZDE.setTextColor(green);
                tvZDF.setTextColor(green);
            }
            else if(s>0){
                tvPrice.setTextColor(red);
                tvZDE.setTextColor(red);
                tvZDF.setTextColor(red);
            }else if(s==0){
                tvPrice.setTextColor(black);
                tvZDE.setTextColor(black);
                tvZDF.setTextColor(black);
            }
            tvJinKai.setText(GlobMethod.cgPriceToDF(gp.getOpen()));
            tvZuoShou.setText(GlobMethod.cgPriceToDF(gp.getYestclose()));
            tvCJL.setText(GlobMethod.changeCJLToShou(gp.getVolume()));
            tvCJE.setText(GlobMethod.changeCJEToStr(gp.getTurnover()));

            super.onPostExecute(value);
        }


    }
}
