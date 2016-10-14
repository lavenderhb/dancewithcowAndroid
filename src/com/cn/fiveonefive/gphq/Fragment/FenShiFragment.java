package com.cn.fiveonefive.gphq.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GPBean;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.dto.TimeDataBean;
import com.cn.fiveonefive.gphq.dto.TimeDataBeanChild;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.view.TimesView;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hb on 2016/3/25.
 */
public class FenShiFragment extends Fragment {
    private String code;

    private TextView s1,s2,s3,s4,s5,s10,s20,s30,s40,s50,b1,b2,b3,b4,b5,b10,b20,b30,b40,b50;
    private TimesView timesView;
    private LinearLayout sellorbuy;
    private TimeDataBeanChild timeDataBeanChild;

    private GPBean gp;
    private GetDataTask getDataTask;
    public boolean isDo=false;
    public Timer timer;

    private int green;
    private int red;
    private int black;


    public FenShiFragment(String code){
        this.code=code;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    s5.setText(gp.getAsk5());
                    s4.setText(gp.getAsk4());
                    s3.setText(gp.getAsk3());
                    s2.setText(gp.getAsk2());
                    s1.setText(gp.getAsk1());

                    b1.setText(gp.getBid1());
                    b2.setText(gp.getBid2());
                    b3.setText(gp.getBid3());
                    b4.setText(gp.getBid4());
                    b5.setText(gp.getBid5());

                    s10.setText(GlobMethod.cgGuToShouDF(gp.getAskvol1()));
                    s20.setText(GlobMethod.cgGuToShouDF(gp.getAskvol2()));
                    s30.setText(GlobMethod.cgGuToShouDF(gp.getAskvol3()));
                    s40.setText(GlobMethod.cgGuToShouDF(gp.getAskvol4()));
                    s50.setText(GlobMethod.cgGuToShouDF(gp.getAskvol5()));

                    b10.setText(GlobMethod.cgGuToShouDF(gp.getBidvol1()));
                    b20.setText(GlobMethod.cgGuToShouDF(gp.getBidvol2()));
                    b30.setText(GlobMethod.cgGuToShouDF(gp.getBidvol3()));
                    b40.setText(GlobMethod.cgGuToShouDF(gp.getBidvol4()));
                    b50.setText(GlobMethod.cgGuToShouDF(gp.getAskvol5()));
                    colorSet();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataTask=new GetDataTask();
    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fenshi_view,null);
        findView(view);
        if (code.equals("0000001"))
            sellorbuy.setVisibility(View.GONE);
        if (code.equals("1399001"))
            sellorbuy.setVisibility(View.GONE);
        if (code.equals("1399300"))
            sellorbuy.setVisibility(View.GONE);
        return view;
    }


    @Override
    public void onResume() {
        isDo=true;
//        GlobStr.FenShiState=true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isDo=false;
//        GlobStr.FenShiState=false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        try{
            timer.cancel();
            timer=null;
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }
    public void doRefresh(){
        timer.schedule(new RefreshTask(),0,1000* GlobStr.FenShiPeriod);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (!GlobStr.FenShiState)
                return;
            if (!isDo)
                return;
            if(getDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                getDataTask.cancel(true);
                getDataTask=new GetDataTask();
                getDataTask.execute();
            }
            else if(getDataTask.getStatus().equals(AsyncTask.Status.PENDING)){
                getDataTask.execute();
            }
        }
    }

    class GetDataTask extends AsyncTask<String ,String ,TimeDataBeanChild> {

        @Override
        protected TimeDataBeanChild doInBackground(String... params) {
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
            GetHttpResult getHttpResult1=new GetHttpResult(GlobStr.GPUrl+code);
            Result result1=getHttpResult1.getResult();
            if (result.getResultCode()!=0)
                return null;
            String str1=result1.getResultData();
            int indexBegin = str1.indexOf("code");
            int indexEnd = str1.indexOf(";");
            String aaa=str1.substring(indexBegin - 2, indexEnd - 3);
            gp= gson.fromJson(aaa,GPBean.class);

            timeDataBeanChild=new TimeDataBeanChild();
            timeDataBeanChild.setLow(gp.getLow());
            timeDataBeanChild.setHigh(gp.getHigh());
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

            return timeDataBeanChild;
        }

        @Override
        protected void onPostExecute(TimeDataBeanChild timeDataBeanChild) {
            if(timeDataBeanChild==null)
                return;
            timesView.resetData();
            timesView.setTimeDataBean(timeDataBeanChild);
            timesView.postInvalidate();
            super.onPostExecute(timeDataBeanChild);
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
        timesView= (TimesView) view.findViewById(R.id.time_view);
    }
    private void colorSet(){
        setColor(new TextView[]{s1,s2,s3,s4,s5,b1,b2,b3,b4,b5});
    }
    private void setColor(TextView[] textView){
        for (int i=0;i<textView.length;i++){
            if(Float.parseFloat(textView[i].getText().toString())>Float.parseFloat(timeDataBeanChild.getYestclose())){
                textView[i].setTextColor(red);
            }else if (Float.parseFloat(textView[i].getText().toString())<Float.parseFloat(timeDataBeanChild.getYestclose())){
                textView[i].setTextColor(green);
            }
        }


    }
}
