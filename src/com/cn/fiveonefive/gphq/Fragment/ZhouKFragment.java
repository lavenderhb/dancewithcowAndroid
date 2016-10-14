package com.cn.fiveonefive.gphq.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.KBean;
import com.cn.fiveonefive.gphq.dto.RKBean;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.cn.fiveonefive.gphq.view.KChartsView;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by hb on 2016/3/25.
 */
public class ZhouKFragment extends Fragment {
    private String code;

    private KChartsView mMyChartsView;
    private List<KBean> listKBean;

    private GetDataTask getDataTask;
    public boolean isDo=false;
    public Timer timer;

    public ZhouKFragment(String code){
        this.code=code;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataTask=new GetDataTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.k_view,null);
        mMyChartsView = (KChartsView)view.findViewById(R.id.k_view);
        listKBean =new ArrayList<KBean>();

        return view;
    }
    @Override
    public void onResume() {
        isDo=true;
//        GlobStr.ZhouKState=true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isDo=false;
//        GlobStr.ZhouKState=false;
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
        timer.schedule(new RefreshTask(),0,1000* GlobStr.ZhouKPeriod);
    }
    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (!GlobStr.ZhouKState)
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

    public static List<KBean> changeStrToKBean(String str){
        List<KBean> kBeanList=new ArrayList<KBean>();
        Gson gson=new Gson();
        RKBean rk=gson.fromJson(str, RKBean.class);
        List rkDatas = rk.getData();
        ArrayList<String[]> arrayList = new ArrayList<String[]>();
        for(int i=0;i<rkDatas.size();i++ ){
            int indexBegin = rkDatas.get(i).toString().indexOf("[");
            int indexEnd = rkDatas.get(i).toString().indexOf("]");
            String rkData=rkDatas.get(i).toString().substring(indexBegin+1, indexEnd);
            String[] arr = rkData.split(",");
            arrayList.add(arr);
        }
        for(int i=0;i<arrayList.size();i++){
            KBean kBean=new KBean();
            kBean.setDate(arrayList.get(i)[0]);
            kBean.setOpen(Double.valueOf(arrayList.get(i)[1]));
            kBean.setClose(Double.valueOf(arrayList.get(i)[2]));
            kBean.setHigh(Double.valueOf(arrayList.get(i)[3]));
            kBean.setLow(Double.valueOf(arrayList.get(i)[4]));
            kBeanList.add(kBean);
        }
        return kBeanList;
    }
    private class GetDataTask extends AsyncTask<String, Integer, Result> {

        @Override
        protected Result doInBackground(String... params) {
            if (code.equals("")){
                return null;
            }
            for (int i= GlobStr.K_zhou_start;i<= GlobStr.K_zhou_end;i++){
                GetHttpResult getHttpResult=new GetHttpResult(GlobStr.ZhouKUrl+i+"/"+code+".json");
                Result result=getHttpResult.getResult();
                if(result.getResultCode()==0){
                    listKBean.addAll(changeStrToKBean(result.getResultData()));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Result result) {
            Collections.reverse(listKBean);
            mMyChartsView.setKData(listKBean);
            mMyChartsView.setLowerChartTabTitles(new String[]{"MACD", "KDJ"});
            mMyChartsView.postInvalidate();
            super.onPostExecute(result);
        }
    }
}
