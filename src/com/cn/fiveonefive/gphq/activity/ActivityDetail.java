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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ab.util.AbToastUtil;
import com.ab.view.sample.AbViewPager;
import com.cn.fiveonefive.gphq.Fragment.FragmentFS;
import com.cn.fiveonefive.gphq.Fragment.FragmentK;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.BaseBean;
import com.cn.fiveonefive.gphq.dto.GPBean;
import com.cn.fiveonefive.gphq.dto.GPBean2;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.util.SystemBarTintManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2016/9/4.
 */
public class ActivityDetail extends FragmentActivity implements
        FragmentFS.IFsChange,
        FragmentK.IKChange{

    private TextView backBtn,codeName,codeSymbol,searchBtn,price,
            upDownPrice,updownPre,addOrDeletePic,addorDeleteText,todayOpen,
            top,dealNum,low,dealMoney,changeHand;
    AbViewPager viewPage;
    RelativeLayout colorView;



    String name,symbol,code;
    boolean state;
    FragmentFS fragmentFS;
    FragmentK fragmentk;
    List<Fragment> fragmentList;
    MyViewPageAdapter myViewPageAdapter;

    ImageView tabLeft,tabRight;

    int green,red,white;
    private GPBean gp;
    GetDataTask getDataTask;

    List<BaseBean> gpBeanList;
    Gson gson;
    int index=-1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.red1);//通知栏所需颜色
        }

        setContentView(R.layout.detail_main);
        gson=new Gson();
        green=getResources().getColor(R.color.greed1);
        red=getResources().getColor(R.color.red1);
        white=getResources().getColor(R.color.white);
        findView();

//        WindowManager wm = this.getWindowManager();
//        int width = wm.getDefaultDisplay().getWidth();
//        ViewGroup.LayoutParams lp=colorView.getLayoutParams();
//        lp.width=width/2;
//        lp.height=30;
//        colorView.setLayoutParams(lp);

        name=getIntent().getStringExtra("name");
        symbol=getIntent().getStringExtra("symbol");
        code=getIntent().getStringExtra("code");
        state=getIntent().getBooleanExtra("state",false);

        if ((code==null)||(code.equals(""))){
            return;
        }
        if (state){
            tabLeft.setVisibility(View.VISIBLE);
            tabRight.setVisibility(View.VISIBLE);

            gpBeanList=gson.fromJson(getIntent().getStringExtra("beanList"),new TypeToken<List<BaseBean>>(){}.getType());
            for (int i=0;i<gpBeanList.size();i++){
                if (code.equals(gpBeanList.get(i).getCode())){
                    index=i;
                    break;
                }
            }
        }

        init();
        setListener();
        gp=new GPBean();
        fragmentk.isDo=false;
    }

    @Override
    protected void onResume() {
        getDataTask=new GetDataTask();
        getDataTask.execute("");
        int i=viewPage.getCurrentItem();
        if (i==0){
            fragmentFS.isDo=true;
            fragmentk.isDo=false;
        }else if (i==1){
            fragmentk.isDo=true;
            fragmentFS.isDo=false;
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("detail");
        }
        fragmentk.isDo=false;
        fragmentFS.isDo=false;
        super.onStop();
    }



    @Override
    public void changePageFS(int i) {
        viewPage.setCurrentItem(i);
    }

    @Override
    public void changePageK(int i) {
        viewPage.setCurrentItem(i);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    GPBean2 gpBean2= (GPBean2) msg.obj;
                    changeHand.setText(gpBean2.get_37changehandpercent()+"%");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    class GetDataTask extends AsyncTask<Object ,String ,String> {
        public GetDataTask(){
            tabLeft.setEnabled(true);
            tabRight.setEnabled(true);
        }

        @Override
        protected String doInBackground(Object[] objects) {
            if (code.equals("")){
                return null;
            }

            GetHttpResult getHttpResult=new GetHttpResult(GlobStr.GPUrl+code);
            Result result=getHttpResult.getResult();

            GetHttpResult getHttpResult1=new GetHttpResult(GlobStr.GPURL2+GlobMethod.change126to(code,symbol));
            Result result1=getHttpResult1.getResult();
            if (result1.getResultCode()==0){
                Message msg=new Message();
                msg.what=0;
                msg.obj=GlobMethod.changeStrToGPBean2(result1.getResultData());
                handler.sendMessage(msg);
            }

            if(isCancelled()) return null;
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

            price.setText(GlobMethod.cgPriceToDF(gp.getPrice()));
            upDownPrice.setText(gp.getUpdown());
            float s=0;
            if(gp.getPercent()!=null) {
                s = Float.valueOf(gp.getPercent());
            }
            updownPre.setText(GlobMethod.cgPercentToDF(s));
            if(s<0){
                price.setTextColor(green);
//                upDownPrice.setTextColor(green);
//                updownPre.setTextColor(green);

                colorView.setBackground(getResources().getDrawable(R.drawable.round_view_green));
            }
            else if(s>0){
                price.setTextColor(red);
                upDownPrice.setText("+"+gp.getUpdown());
                updownPre.setText("+"+GlobMethod.cgPercentToDF(s));
//                upDownPrice.setTextColor(red);
//                updownPre.setTextColor(red);
                colorView.setBackground(getResources().getDrawable(R.drawable.round_view_red));
            }else if(s==0){
                price.setTextColor(white);
//                upDownPrice.setTextColor(white);
//                updownPre.setTextColor(white);
                colorView.setBackground(getResources().getDrawable(R.drawable.round_view_gray));
            }
            float yesDay=Float.parseFloat(gp.getYestclose());
            float today=Float.parseFloat(gp.getOpen());
            float high=Float.parseFloat(gp.getHigh());
            float min=Float.parseFloat(gp.getLow());
            if (today>yesDay){
                todayOpen.setTextColor(red);
            }else if (today<yesDay){
                todayOpen.setTextColor(green);
            }
            if (high>yesDay){
                top.setTextColor(red);
            }else if (high<yesDay){
                top.setTextColor(green);
            }
            if (min>yesDay){
                low.setTextColor(red);
            }else if (min<yesDay){
                low.setTextColor(green);
            }
            top.setText(GlobMethod.cgPriceToDF(gp.getHigh()));
            low.setText(GlobMethod.cgPriceToDF(gp.getLow()));
            todayOpen.setText(GlobMethod.cgPriceToDF(gp.getOpen()));

            dealNum.setText(GlobMethod.changeCJLToNOShou(gp.getVolume()));
            dealMoney.setText(GlobMethod.changeCJEToNoYuanStr(gp.getTurnover()));

            getDataTask=new GetDataTask();
            MyApplication.taskMap.put("detail",getDataTask);
            tabLeft.setEnabled(true);
            tabRight.setEnabled(true);
            super.onPostExecute(value);
        }


    }



    private void init(){
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("detail");
        }
        getDataTask=new GetDataTask();
        getDataTask.execute("");

        fragmentFS=new FragmentFS(code,symbol,this);
        fragmentk=new FragmentK(code,this);
        fragmentList=new ArrayList<>();
        fragmentList.add(fragmentFS);
        fragmentList.add(fragmentk);
        myViewPageAdapter=new MyViewPageAdapter(getSupportFragmentManager());
        viewPage.setAdapter(myViewPageAdapter);
        codeName.setText(name);
        codeSymbol.setText(symbol);
        if (GlobMethod.getIsAdd(code)){
            addorDeleteText.setText("删自选");
            addOrDeletePic.setBackground(getResources().getDrawable(R.drawable.sub_net));
        }else {
            addorDeleteText.setText("加自选");
            addOrDeletePic.setBackground(getResources().getDrawable(R.drawable.add_net));
        }

    }

    private void setListener() {
        tabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLeft.setEnabled(false);
                tabRight.setEnabled(false);
                if (index==-1)
                    return;
                if (index>0){
                    index--;
                }else {
                    index=gpBeanList.size()-1;
                }
                name=gpBeanList.get(index).getName();
                symbol=gpBeanList.get(index).getSymbol();
                code=gpBeanList.get(index).getCode();
                init();
            }
        });
        tabRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabRight.setEnabled(false);
                tabLeft.setEnabled(false);
                if (index==-1)
                    return;
                if (index==gpBeanList.size()-1){
                    index=0;
                }else {
                    index++;
                }
                name=gpBeanList.get(index).getName();
                symbol=gpBeanList.get(index).getSymbol();
                code=gpBeanList.get(index).getCode();
                init();
            }
        });
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==0){
                    fragmentk.stop();
                    fragmentk.isDo=false;
                    fragmentFS.start();
                    fragmentFS.isDo=true;
                }else if (i==1){
                    fragmentk.start();
                    fragmentk.isDo=true;
                    fragmentFS.stop();
                    fragmentFS.isDo=false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityDetail.this,ActivitySearch.class);
                startActivity(intent);
            }
        });
        addOrDeletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addorDeleteText.getText().toString().equals("加自选")){
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
                            ActivityDetail.this.sendBroadcast(intent);
                            addorDeleteText.setText("删自选");
                            addOrDeletePic.setBackground(getResources().getDrawable(R.drawable.sub_net));
                        }catch (Exception e){
                            e.printStackTrace();
                            AbToastUtil.showToast(ActivityDetail.this, "添加失败");
                        }
                    }
                }else if(addorDeleteText.getText().toString().equals("删自选")){
                    if(GlobMethod.getIsAdd(code)){
                        Intent intent = new Intent("cn.fiveonefive.removeMyList");
                        intent.putExtra("code",code);
                        try {
                            MyApplication.db.delete(MyGuPiao.class, WhereBuilder.b("code", "=", code));
                            ActivityDetail.this.sendBroadcast(intent);
                            addorDeleteText.setText("加自选");
                            addOrDeletePic.setBackground(getResources().getDrawable(R.drawable.add_net));
                        }catch (Exception e){
                            e.printStackTrace();
                            AbToastUtil.showToast(ActivityDetail.this, "删除失败");
                        }
                    }
                }
            }
        });
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

        }
    }


    private void findView(){
        backBtn= (TextView) findViewById(R.id.leftBtn);
        codeName= (TextView) findViewById(R.id.codeName);
        codeSymbol= (TextView) findViewById(R.id.codeSymbol);
        searchBtn= (TextView) findViewById(R.id.rightBtn);
        price= (TextView) findViewById(R.id.price);
        upDownPrice= (TextView) findViewById(R.id.updownprice);
        updownPre= (TextView) findViewById(R.id.updownpre);
        addorDeleteText= (TextView) findViewById(R.id.addordelete2);
        addOrDeletePic= (TextView) findViewById(R.id.addordelete1);
        todayOpen= (TextView) findViewById(R.id.todayopen);
        top= (TextView) findViewById(R.id.top);
        dealNum= (TextView) findViewById(R.id.dealnum);
        changeHand= (TextView) findViewById(R.id.change_hand);
        low= (TextView) findViewById(R.id.low);
        dealMoney= (TextView) findViewById(R.id.dealmoney);
        viewPage= (AbViewPager) findViewById(R.id.viewPage);
        colorView= (RelativeLayout) findViewById(R.id.colorview);
        tabLeft= (ImageView) findViewById(R.id.left);
        tabRight= (ImageView) findViewById(R.id.right);

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
