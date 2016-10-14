package com.cn.fiveonefive.gphq.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.view.sample.AbViewPager;
import com.cn.fiveonefive.gphq.Fragment.FragmentBD;
import com.cn.fiveonefive.gphq.Fragment.FragmentMy;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GPBean;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.util.SystemBarTintManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hb on 2016/9/3.
 */
public class ActivityTab extends FragmentActivity {




    TextView backBtn;
    RadioGroup tabRdGroup;
    RadioButton tabHQRdoBtn;
    RadioButton tabZXRdoBtn;
    TextView price1;
    TextView price2;
    TextView price3;
    TextView updownprice1;
    TextView updownprice2;
    TextView updownprice3;
    TextView updownpre1;
    TextView updownpre2;
    TextView updownpre3;
    AbViewPager viewPage;
    TextView searchBtn;

    RelativeLayout shangzheng,shenzheng,hushen;


    FragmentBD fragmentBD;
    FragmentMy fragmentMy;
    List<Fragment> fragmentList;
    MyViewPageAdapter myViewPageAdapter;

    int currenttab=-1;
    int green,red,white;
    GetDataTask getDataTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red1);//通知栏所需颜色
        }
        green=getResources().getColor(R.color.greed1);
        red=getResources().getColor(R.color.red1);
        white=getResources().getColor(R.color.white);

        setContentView(R.layout.gp_main);
        findViewById();
        init();
        setListener();
        fragmentMy.isDo=false;
    }

    @Override
    protected void onResume() {
        getDataTask=new GetDataTask();
        getDataTask.execute("");
        int i=viewPage.getCurrentItem();
        if (i==0){
            fragmentBD.isDo=true;
            fragmentMy.isDo=false;
        }else if (i==1){
            fragmentMy.isDo=true;
            fragmentBD.isDo=false;
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("tab");
        }
        super.onStop();
    }

    private class GetDataTask extends AsyncTask<Object, Map<String,String>, String> {

        @Override
        protected String doInBackground(Object[] objects) {
            //alone1
            GetHttpResult getHttpResultAlone1=new GetHttpResult(GlobStr.GPUrl+"0000001");
            Result resultAlone1=getHttpResultAlone1.getResult();
            if (resultAlone1.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone1",resultAlone1.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            //alone2
            GetHttpResult getHttpResultAlone2=new GetHttpResult(GlobStr.GPUrl+"1399001");
            Result resultAlone2=getHttpResultAlone2.getResult();
            if (resultAlone2.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone2",resultAlone2.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            //alone3
            GetHttpResult getHttpResultAlone3=new GetHttpResult(GlobStr.GPUrl+"1399300");
            Result resultAlone3=getHttpResultAlone3.getResult();
            if (resultAlone3.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone3",resultAlone3.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Map<String, String>... values) {
            Map<String,String> map=values[0];
            if(map.get("alone1")!=null){
                Message msg=new Message();
                msg.what=1;
                msg.obj=map;
                handler.sendMessage(msg);


            }else if(map.get("alone2")!=null){
                Message msg=new Message();
                msg.what=2;
                msg.obj=map;
                handler.sendMessage(msg);

            }else if(map.get("alone3")!=null){
                Message msg=new Message();
                msg.what=3;
                msg.obj=map;
                handler.sendMessage(msg);

            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            getDataTask=new GetDataTask();
            MyApplication.taskMap.put("tab",getDataTask);
            super.onPostExecute(s);
        }

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Map<String,String> map= (Map<String, String>) msg.obj;
            Gson gson=new Gson();
            switch (msg.what){
                case 1:
                    String str=map.get("alone1");
                    int indexBegin = str.indexOf("code");
                    int indexEnd = str.indexOf(";");
                    String result=str.substring(indexBegin - 2, indexEnd - 3);
                    GPBean gp= gson.fromJson(result,GPBean.class);
                    price1.setText(GlobMethod.cgPriceToDF(gp.getPrice()));
                    float s1=0;
                    float p1=0;
                    if (gp.getPercent()!=null){
                        s1= Float.valueOf(gp.getPercent());
                        p1= Float.valueOf(gp.getUpdown());
                    }

                    float now=Float.parseFloat(gp.getPrice());
                    float yest=Float.parseFloat(gp.getYestclose());
                    if (now<yest){
                        price1.setTextColor(green);
                        updownpre1.setTextColor(green);
                        updownpre1.setText(GlobMethod.cgPercentToDF(s1));
                        updownprice1.setTextColor(green);
                        updownprice1.setText(GlobMethod.cgPrice(p1));
                    }else if(now>yest){
                        price1.setTextColor(red);
                        updownpre1.setTextColor(red);
                        updownpre1.setText("+"+GlobMethod.cgPercentToDF(s1));
                        updownprice1.setTextColor(red);
                        updownprice1.setText("+"+GlobMethod.cgPrice(p1));

                    }
                    break;
                case 2:
                    String str2=map.get("alone2");
                    int indexBegin2 = str2.indexOf("code");
                    int indexEnd2 = str2.indexOf(";");
                    String result2=str2.substring(indexBegin2 - 2, indexEnd2 - 3);
                    GPBean gp2= gson.fromJson(result2,GPBean.class);
                    price2.setText(GlobMethod.cgPriceToDF(gp2.getPrice()));

                    float s2=0;
                    float p2=0;
                    if (gp2.getPercent()!=null){
                        s2= Float.valueOf(gp2.getPercent());
                        p2= Float.valueOf(gp2.getUpdown());
                    }
                    float now2=Float.parseFloat(gp2.getPrice());
                    float yest2=Float.parseFloat(gp2.getYestclose());
                    if (now2<yest2){
                        price2.setTextColor(green);
                        updownpre2.setText(GlobMethod.cgPercentToDF(s2));
                        updownpre2.setTextColor(green);
                        updownprice2.setTextColor(green);
                        updownprice2.setText(GlobMethod.cgPrice(p2));
                    }else if(now2>yest2){
                        price2.setTextColor(red);
                        updownpre2.setTextColor(red);
                        updownpre2.setText("+"+GlobMethod.cgPercentToDF(s2));
                        updownprice2.setTextColor(red);
                        updownprice2.setText("+"+GlobMethod.cgPrice(p2));
                    }
                    break;
                case 3:
                    String str3=map.get("alone3");
                    int indexBegin3 = str3.indexOf("code");
                    int indexEnd3 = str3.indexOf(";");
                    String result3=str3.substring(indexBegin3 - 2, indexEnd3 - 3);
                    GPBean gp3= gson.fromJson(result3,GPBean.class);
                    price3.setText(GlobMethod.cgPriceToDF(gp3.getPrice()));

                    float s3=0;
                    float p3=0;
                    if (gp3.getPercent()!=null){
                        s3= Float.valueOf(gp3.getPercent());
                        p3= Float.valueOf(gp3.getUpdown());
                    }
                    float now3=Float.parseFloat(gp3.getPrice());
                    float yest3=Float.parseFloat(gp3.getYestclose());
                    if (now3<yest3){
                        price3.setTextColor(green);
                        updownpre3.setTextColor(green);
                        updownpre3.setText(GlobMethod.cgPercentToDF(s3));
                        updownprice3.setTextColor(green);
                        updownprice3.setText(GlobMethod.cgPrice(p3));
                    }else if(now3>yest3){
                        price3.setTextColor(red);
                        updownpre3.setTextColor(red);
                        updownpre3.setText("+"+GlobMethod.cgPercentToDF(s3));
                        updownprice3.setTextColor(red);
                        updownprice3.setText("+"+GlobMethod.cgPrice(p3));
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
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


    private void init() {
        viewPage.setPagingEnabled(false);
        fragmentBD=new FragmentBD();
        fragmentMy=new FragmentMy();
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(fragmentBD);
        fragmentList.add(fragmentMy);
        myViewPageAdapter=new MyViewPageAdapter(getSupportFragmentManager());
        viewPage.setAdapter(myViewPageAdapter);


    }
    private void setListener() {
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==0){
                    fragmentMy.stop();
                    fragmentMy.isDo=false;
                    fragmentBD.start();
                    fragmentBD.isDo=true;
                }else if (i==1){
                    fragmentMy.start();
                    fragmentMy.isDo=true;
                    fragmentBD.stop();
                    fragmentBD.isDo=false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearch();
            }
        });
        tabRdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (tabHQRdoBtn.getId()==checkedId){
                    viewPage.setCurrentItem(0);
                }else if (tabZXRdoBtn.getId()==checkedId){
                    viewPage.setCurrentItem(1);
                }
            }
        });
        shangzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityTab.this, ActivityDetail.class);
                intent.putExtra("code", "0000001");
                intent.putExtra("name","上证指数");
                intent.putExtra("symbol","000001");
                startActivity(intent);
            }
        });
        shenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityTab.this, ActivityDetail.class);
                intent.putExtra("code", "1399001");
                intent.putExtra("name","深证成指");
                intent.putExtra("symbol","399001");
                startActivity(intent);
            }
        });
        hushen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityTab.this, ActivityDetail.class);
                intent.putExtra("code", "1399300");
                intent.putExtra("name","沪深300");
                intent.putExtra("symbol","399300");
                startActivity(intent);
            }
        });
    }

    private void changeRadioButton(int i){
        if (i==0){
            tabHQRdoBtn.setChecked(true);
            tabZXRdoBtn.setChecked(false);
        }else if (i==1){
            tabHQRdoBtn.setChecked(false);
            tabZXRdoBtn.setChecked(true);
        }

//        if (tabHQRdoBtn.isChecked()){
//            tabHQRdoBtn.setChecked(false);
//            tabZXRdoBtn.setChecked(true);
//        }else{
//            tabHQRdoBtn.setChecked(true);
//            tabZXRdoBtn.setChecked(false);
//        }
//        if (tabZXRdoBtn.isChecked()){
//            tabHQRdoBtn.setChecked(true);
//            tabZXRdoBtn.setChecked(false);
//        }else{
//            tabHQRdoBtn.setChecked(false);
//            tabZXRdoBtn.setChecked(true);
//        }
    }

    class MyViewPageAdapter extends FragmentStatePagerAdapter {

        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int currentItem=viewPage.getCurrentItem();
            if (currentItem==currenttab)
            {
                return ;
            }
            currenttab=viewPage.getCurrentItem();
            changeRadioButton(viewPage.getCurrentItem());
        }
    }


    public void finishActivity(){
        this.finish();
    }
    public void moveToSearch(){
        Intent intent=new Intent(ActivityTab.this,ActivitySearch.class);
        startActivity(intent);
    }
    private void findViewById(){
        backBtn= (TextView) findViewById(R.id.leftBtn);
        tabRdGroup= (RadioGroup) findViewById(R.id.tab);
        tabHQRdoBtn= (RadioButton) findViewById(R.id.tabLeft);
        tabZXRdoBtn= (RadioButton) findViewById(R.id.tabRight);
        price1= (TextView) findViewById(R.id.price1);
        price2= (TextView) findViewById(R.id.price2);
        price3= (TextView) findViewById(R.id.price3);
        updownprice1= (TextView) findViewById(R.id.updownprice1);
        updownprice2= (TextView) findViewById(R.id.updownprice2);
        updownprice3= (TextView) findViewById(R.id.updownprice3);
        updownpre1= (TextView) findViewById(R.id.updownpre1);
        updownpre2= (TextView) findViewById(R.id.updownpre2);
        updownpre3= (TextView) findViewById(R.id.updownpre3);
        viewPage= (AbViewPager) findViewById(R.id.viewPage);
        searchBtn= (TextView) findViewById(R.id.rightBtn);
        shangzheng= (RelativeLayout) findViewById(R.id.shangzheng);
        shenzheng= (RelativeLayout) findViewById(R.id.shenzheng);
        hushen= (RelativeLayout) findViewById(R.id.hushen);

    }

}
