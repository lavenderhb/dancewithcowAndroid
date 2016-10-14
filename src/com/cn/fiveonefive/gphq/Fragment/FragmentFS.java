package com.cn.fiveonefive.gphq.Fragment;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.*;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.view.TimesView;
import com.google.gson.Gson;

import java.text.DecimalFormat;

/**
 * Created by hb on 2016/9/4.
 */
public class FragmentFS extends Fragment implements TimesView.ITimeChange{
    String code;
    String symbol;
    public FragmentFS (String code, String symbol,IFsChange iFsChange){
        this.code=code;
        this.iFsChange=iFsChange;
        this.symbol=symbol;
    }

    private TextView s1,s2,s3,s4,s5,s10,s20,s30,s40,s50,b1,b2,b3,b4,b5,b10,b20,b30,b40,b50;
    private TimesView timesView;
    private LinearLayout sellorbuy;
    private ImageView viewOrGone;

    private TextView time,price,avg,upDownPrice,upDownPercent;

    private TimeDataBeanChild timeDataBeanChild;
    private GPBean2 gp;
    private GetDataTask getDataTask;
    public boolean isDo=false;

    ImageView left,right,up,down;
    IFsChange iFsChange;

    int green,red,white,graytu;
    Drawable open,close;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        green=getResources().getColor(R.color.greed1);
        red=getResources().getColor(R.color.red1);
        white=getResources().getColor(R.color.white);
        graytu=getResources().getColor(R.color.grayfortu);
        open=getResources().getDrawable(R.drawable.to_open);
        close=getResources().getDrawable(R.drawable.to_close);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fenshi_freg,null);
        findView(view);
        if (code==null||code.equals("")){return view;}
        if (code.equals("0000001")){
            sellorbuy.setVisibility(View.GONE);
            viewOrGone.setVisibility(View.GONE);
        }
        if (code.equals("1399001")){
            sellorbuy.setVisibility(View.GONE);
            viewOrGone.setVisibility(View.GONE);
        }
        if (code.equals("1399300")){
            sellorbuy.setVisibility(View.GONE);
            viewOrGone.setVisibility(View.GONE);
        }
        viewOrGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sellorbuy.getVisibility()==View.GONE){
                    sellorbuy.setVisibility(View.VISIBLE);
                    viewOrGone.setImageDrawable(close);
                }else {
                    sellorbuy.setVisibility(View.GONE);
                    viewOrGone.setImageDrawable(open);
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        getDataTask=new GetDataTask();
        getDataTask.execute("");
        super.onResume();
    }

    public void start(){
        getDataTask=new GetDataTask();
        MyApplication.taskMap.put("fs",getDataTask);
    }
    public void stop(){
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("fs");
        }
    }


    @Override
    public void onStop() {
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("fs");
        }
        super.onStop();
    }



    class GetDataTask extends AsyncTask<Object ,String ,TimeDataBeanChild> {

        @Override
        protected TimeDataBeanChild doInBackground(Object[] objects) {
            if (code.equals("")){
                return null;
            }
            GetHttpResult getHttpResult =new GetHttpResult(GlobStr.FenShiUrl+code+".json");
            Result result=getHttpResult.getResult();
            if (result.getResultCode()!=0)
                return null;
            String str=result.getResultData();
            Gson gson=new Gson();
            TimeDataBean timeDataBean=gson.fromJson(str, TimeDataBean.class);

            GetHttpResult getHttpResult1=new GetHttpResult(GlobStr.GPURL2+GlobMethod.change126to(code,symbol));
            Result result1=getHttpResult1.getResult();
            if (result1.getResultCode()==0){
                gp=GlobMethod.changeStrToGPBean2(result1.getResultData());
            }else {
                gp=new GPBean2();
            }

            timeDataBeanChild=new TimeDataBeanChild();
            timeDataBeanChild.setLow(gp.get_32bottom());
            timeDataBeanChild.setHigh(gp.get_30top());
            timeDataBeanChild.setCount(timeDataBean.getCount());
            timeDataBeanChild.setData(timeDataBean.getData());
            timeDataBeanChild.setDate(timeDataBean.getDate());
            timeDataBeanChild.setLastVolume(timeDataBean.getLastVolume());
            timeDataBeanChild.setName(timeDataBean.getName());
            timeDataBeanChild.setSymbol(timeDataBean.getSymbol());
            timeDataBeanChild.setYestclose(timeDataBean.getYestclose());

            Message msg=new Message();
            msg.what=0;
            handler.sendMessage(msg);
            if(isCancelled()) return null;

            return timeDataBeanChild;
        }

        @Override
        protected void onPostExecute(TimeDataBeanChild timeDataBeanChild) {
            if(timeDataBeanChild==null)
                return;
            timesView.setLongiLatitudeColor(graytu);
            timesView.resetData();
            timesView.setTimeDataBean(timeDataBeanChild);
//            timesView.postInvalidate();
            if (isDo){
                getDataTask=new GetDataTask();
                MyApplication.taskMap.put("fs",getDataTask);
            }
            super.onPostExecute(timeDataBeanChild);
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    s5.setText(gp.get_12sell5());
                    s4.setText(gp.get_11sell4());
                    s3.setText(gp.get_10sell3());
                    s2.setText(gp.get_9sell2());
                    s1.setText(gp.get_8sell1());

                    b1.setText(gp.get_3buy1());
                    b2.setText(gp.get_4buy2());
                    b3.setText(gp.get_5buy3());
                    b4.setText(gp.get_6buy4());
                    b5.setText(gp.get_7buy5());

                    s10.setText((gp.get_18sell1num()));
                    s20.setText((gp.get_19sell2num()));
                    s30.setText((gp.get_20sell3num()));
                    s40.setText((gp.get_21sell4num()));
                    s50.setText((gp.get_22sell5num()));

                    b10.setText((gp.get_13buy1num()));
                    b20.setText((gp.get_14buy2num()));
                    b30.setText((gp.get_15buy3num()));
                    b40.setText((gp.get_16buy4num()));
                    b50.setText((gp.get_17buy5num()));
                    colorSet();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void colorSet(){
        setColor(new TextView[]{s1,s2,s3,s4,s5,b1,b2,b3,b4,b5});
    }
    private void setColor(TextView[] textView){
        for (int i=0;i<textView.length;i++){
            if(Float.parseFloat(textView[i].getText().toString())>Float.parseFloat(timeDataBeanChild.getYestclose())){
                textView[i].setTextColor(red);
            }else if (Float.parseFloat(textView[i].getText().toString())<Float.parseFloat(timeDataBeanChild.getYestclose())){
                textView[i].setTextColor(green);
            }if (textView[i].getText().toString().equals("0")){
                textView[i].setTextColor(white);
                textView[i].setText("-");
            }
        }
    }

    private void findView(View view){
        sellorbuy= (LinearLayout) view.findViewById(R.id.sellorbuy);
        s1= (TextView) view.findViewById(R.id.s1);
        s2= (TextView) view.findViewById(R.id.s2);
        s3= (TextView) view.findViewById(R.id.s3);
        s4= (TextView) view.findViewById(R.id.s4);
        s5= (TextView) view.findViewById(R.id.s5);
        s10= (TextView) view.findViewById(R.id.s10);
        s20= (TextView) view.findViewById(R.id.s20);
        s30= (TextView) view.findViewById(R.id.s30);
        s40= (TextView) view.findViewById(R.id.s40);
        s50= (TextView) view.findViewById(R.id.s50);
        b1= (TextView) view.findViewById(R.id.b1);
        b2= (TextView) view.findViewById(R.id.b2);
        b3= (TextView) view.findViewById(R.id.b3);
        b4= (TextView) view.findViewById(R.id.b4);
        b5= (TextView) view.findViewById(R.id.b5);
        b10= (TextView) view.findViewById(R.id.b10);
        b20= (TextView) view.findViewById(R.id.b20);
        b30= (TextView) view.findViewById(R.id.b30);
        b40= (TextView) view.findViewById(R.id.b40);
        b50= (TextView) view.findViewById(R.id.b50);

        time= (TextView) view.findViewById(R.id.time);
        price= (TextView) view.findViewById(R.id.price);
        avg= (TextView) view.findViewById(R.id.avg);
        upDownPrice= (TextView) view.findViewById(R.id.upDownPrice);
        upDownPercent= (TextView) view.findViewById(R.id.upDownPercent);

        timesView= (TimesView) view.findViewById(R.id.time_view);
        timesView.setInter(this);
        viewOrGone= (ImageView) view.findViewById(R.id.vieworgone);

    }
    @Override
    public void change(TimeViewDto timeViewDto,TimeDataBeanChild timeDataBeanChild) {
        float yest0=Float.parseFloat(timeDataBeanChild.getYestclose());
        float price0=Float.parseFloat(timeViewDto.getPrice());
        float aPrice0=Float.parseFloat(timeViewDto.getaPrice());
        float updown0=Float.parseFloat(timeViewDto.getPrice()) - Float.parseFloat(timeDataBeanChild.getYestclose());
        if (price0<yest0){
            price.setTextColor(green);
        }else if (price0>yest0){
            price.setTextColor(red);
        }else if (price0==yest0){
            price.setTextColor(white);
        }
        if (aPrice0<yest0){
            avg.setTextColor(green);
        }else if (aPrice0>yest0){
            avg.setTextColor(red);
        }else if (aPrice0==yest0){
            avg.setTextColor(white);
        }
        if (updown0<0){
            upDownPrice.setTextColor(green);
            upDownPercent.setTextColor(green);
        }else if (updown0>0){
            upDownPrice.setTextColor(red);
            upDownPercent.setTextColor(green);
        }else if (updown0==0){
            upDownPrice.setTextColor(white);
            upDownPercent.setTextColor(green);
        }

        time.setText("时间:"+GlobMethod.changeTimeToTime(timeViewDto.getTime()));
        price.setText("价格:"+timeViewDto.getPrice());
        avg.setText("均价:"+timeViewDto.getaPrice());
        upDownPrice.setText("涨跌:"+new DecimalFormat("#.##").format(updown0));
        upDownPercent.setText("涨跌幅:"+new DecimalFormat("#.##%").format(updown0/Float.parseFloat(timeDataBeanChild.getYestclose())));
    }

    @Override
    public void changePage(int i) {
        iFsChange.changePageFS(i);
    }
    public interface IFsChange{
        void changePageFS(int i);
    }
}
