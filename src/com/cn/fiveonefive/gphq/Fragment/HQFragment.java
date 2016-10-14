package com.cn.fiveonefive.gphq.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.activity.DetailTabActivity;
import com.cn.fiveonefive.gphq.adapter.AloneItemAdapter;
import com.cn.fiveonefive.gphq.adapter.ExpanListAdapter;
import com.cn.fiveonefive.gphq.dto.*;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.google.gson.Gson;

import java.util.*;

/**
 * Created by hb on 2016/3/22.
 */
public class HQFragment extends Fragment {

    //sanban
    private GridView gvAlone;
    private AloneItemAdapter aloneItemAdapter;
    private List<Alone> aloneList;
    //bangdan
    private ArrayList<GuPiaoMainItem> listZF;//zhangfu
    private ArrayList<GuPiaoMainItem> listDF;//diefu
    private ArrayList<GuPiaoMainItem> listHSL;//huanshoulv
    private ArrayList<GuPiaoMainItem> listZHF;//zhenfu
    private ArrayList<ArrayList<GuPiaoMainItem>> listGroupBD;
    private ExpanListAdapter bdExpanListAdapter;
    private ExpandableListView exlvBD;
    private String[] arrayTitle;

    private GetDataTask getDataTask;
    public boolean isDo=false;
    public Timer timer;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    aloneItemAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    bdExpanListAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataTask=new GetDataTask();
//        timer=new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mainlist,null);

        gvAlone= (GridView) view.findViewById(R.id.alonetiem);
        exlvBD= (ExpandableListView) view.findViewById(R.id.bangdan);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        exlvBD.setIndicatorBounds(width-40, width-25);
        exlvBD.setChildIndicatorBounds(20, 53);
        init();
        return view;
    }

    @Override
    public void onResume() {
        isDo=true;
//        GlobStr.HQState=true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isDo=false;
//        GlobStr.HQState=false;
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
        timer.schedule(new RefreshTask(),0,1000* GlobStr.HQPeriod);
    }
    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            if (!GlobStr.HQState)
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
    private void init(){
        aloneList=new ArrayList<Alone>();
        initAloneList();
        aloneItemAdapter=new AloneItemAdapter(aloneList,getActivity());
        gvAlone.setAdapter(aloneItemAdapter);

        listZF=new ArrayList<GuPiaoMainItem>();
        listDF=new ArrayList<GuPiaoMainItem>();
        listHSL=new ArrayList<GuPiaoMainItem>();
        listZHF=new ArrayList<GuPiaoMainItem>();
        listGroupBD=new ArrayList<ArrayList<GuPiaoMainItem>>();
        listGroupBD.add(listZF);
        listGroupBD.add(listDF);
        listGroupBD.add(listHSL);
        listGroupBD.add(listZHF);
        arrayTitle=new String[]{"   涨幅","   跌幅","   换手率","   振幅榜"};
        bdExpanListAdapter=new ExpanListAdapter(getActivity(),listGroupBD,arrayTitle);
        exlvBD.setAdapter(bdExpanListAdapter);

        gvAlone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailTabActivity.class);
                intent.putExtra("code",aloneList.get(position).getCode());
                intent.putExtra("name",aloneList.get(position).getName());
                intent.putExtra("symbol",aloneList.get(position).getSymbol());
                startActivity(intent);
            }
        });
        exlvBD.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getActivity(), DetailTabActivity.class);
                intent.putExtra("code", listGroupBD.get(groupPosition).get(childPosition).getCode());
                intent.putExtra("name",listGroupBD.get(groupPosition).get(childPosition).getName());
                intent.putExtra("symbol",listGroupBD.get(groupPosition).get(childPosition).getSymbol());
                startActivity(intent);
                return true;
            }
        });

    }
    private void initAloneList(){
        Alone alone=new Alone();alone.setName("上证指数");alone.setPrice("00.00");alone.setUpPrice("00.00");alone.setPercent("00.00");aloneList.add(alone);
        alone=new Alone();alone.setName("深证成指");alone.setPrice("00.00");alone.setUpPrice("00.00");alone.setPercent("00.00");aloneList.add(alone);
        alone=new Alone();alone.setName("沪深300");alone.setPrice("00.00");alone.setUpPrice("00.00");alone.setPercent("00.00");aloneList.add(alone);
    }
    private void refreshList(){
        aloneList.clear();
        listZF.clear();
        listDF.clear();
        listHSL.clear();
        listZHF.clear();
    }
    private void setAloneData(String str){
        int indexBegin = str.indexOf("code");
        int indexEnd = str.indexOf(";");
        String result=str.substring(indexBegin - 2, indexEnd - 3);
        Gson gson=new Gson();
        GPBean gp= gson.fromJson(result,GPBean.class);
        if (gp.getCode().equals("0000001")){
            aloneList.get(0).setPercent(gp.getPercent());
            aloneList.get(0).setPrice(gp.getPrice());
            aloneList.get(0).setUpPrice(gp.getUpdown());
            aloneList.get(0).setSymbol(gp.getSymbol());
            aloneList.get(0).setCode(gp.getCode());
        }else if (gp.getCode().equals("1399001")){
            aloneList.get(1).setPercent(gp.getPercent());
            aloneList.get(1).setPrice(gp.getPrice());
            aloneList.get(1).setUpPrice(gp.getUpdown());
            aloneList.get(1).setSymbol(gp.getSymbol());
            aloneList.get(1).setCode(gp.getCode());
        }else if (gp.getCode().equals("1399300")){
            aloneList.get(2).setPercent(gp.getPercent());
            aloneList.get(2).setPrice(gp.getPrice());
            aloneList.get(2).setUpPrice(gp.getUpdown());
            aloneList.get(2).setSymbol(gp.getSymbol());
            aloneList.get(2).setCode(gp.getCode());
        }
    }
    private void setZFData(String str){
        List<String[]> listZF0=changeStrToBD(str);
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
            guPiaoMainItem.setZdf(zf[11]);
            list.add(guPiaoMainItem);
        }
        this.listZF.clear();
        this.listZF.addAll(list);
    }
    private void setDFData(String str){
        List<String[]> listZF0=changeStrToBD(str);
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
            guPiaoMainItem.setZdf(zf[11]);
            list.add(guPiaoMainItem);
        }
        this.listDF.clear();
        this.listDF.addAll(list);
    }
    private void setHSLData(String str){
        List<String[]> listZF0=changeStrToBD(str);
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
            guPiaoMainItem.setZdf(zf[23]);
            list.add(guPiaoMainItem);
        }
        this.listHSL.clear();
        this.listHSL.addAll(list);
    }
    private void setZFBData(String str){
        List<String[]> listZF0=changeStrToBD(str);
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
            guPiaoMainItem.setZdf(zf[13]);
            list.add(guPiaoMainItem);
        }
        this.listZHF.clear();
        this.listZHF.addAll(list);
    }
    public static ArrayList<String[]> changeStrToBD(String str){
        int indexBegin = str.indexOf("{");
//        int indexEnd = str.indexOf("}");
        String aaa=str.substring(indexBegin);
        Gson gson=new Gson();
        BDBeanTmp bdBeanTmp=gson.fromJson(aaa,BDBeanTmp.class);
        ArrayList<String[]> listStr = new ArrayList<String[]>();
        for(String rank:bdBeanTmp.getRank()){
            String[] params = rank.split(",");
            listStr.add(params);
        }
        return listStr;
    }

    private class GetDataTask extends AsyncTask<String, Map<String,String>, String> {

        @Override
        protected String doInBackground(String... params) {
            //alone1
            GetHttpResult getHttpResultAlone1=new GetHttpResult(GlobStr.GPUrl+"0000001");
            Result resultAlone1=getHttpResultAlone1.getResult();
            if (resultAlone1.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone1",resultAlone1.getResultData());
                onProgressUpdate(map);
            }
            //alone2
            GetHttpResult getHttpResultAlone2=new GetHttpResult(GlobStr.GPUrl+"1399001");
            Result resultAlone2=getHttpResultAlone2.getResult();
            if (resultAlone2.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone2",resultAlone2.getResultData());
                onProgressUpdate(map);
            }
            //alone3
            GetHttpResult getHttpResultAlone3=new GetHttpResult(GlobStr.GPUrl+"1399300");
            Result resultAlone3=getHttpResultAlone3.getResult();
            if (resultAlone3.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("alone3",resultAlone3.getResultData());
                onProgressUpdate(map);
            }
            //涨幅
            GetHttpResult getHttpResultZF=new GetHttpResult(GlobStr.ZFUrl+GlobStr.BDPageSize);
            Result resultZF=getHttpResultZF.getResult();
            if (resultZF.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("zf",resultZF.getResultData());
                onProgressUpdate(map);
            }
            //跌幅
            GetHttpResult getHttpResultDF=new GetHttpResult(GlobStr.DFUrl+GlobStr.BDPageSize);
            Result resultDF=getHttpResultDF.getResult();
            if (resultDF.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("df",resultDF.getResultData());
                onProgressUpdate(map);
            }
            //换手率
            GetHttpResult getHttpResultHSL=new GetHttpResult(GlobStr.HSLUrl+GlobStr.BDPageSize);
            Result resultHSL=getHttpResultHSL.getResult();
            if (resultHSL.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("hsl",resultHSL.getResultData());
                onProgressUpdate(map);
            }
            //振幅榜
            GetHttpResult getHttpResultZFB=new GetHttpResult(GlobStr.ZFBUrl+GlobStr.BDPageSize);
            Result resultZFB=getHttpResultZFB.getResult();
            if (resultZFB.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("zfb",resultZFB.getResultData());
                onProgressUpdate(map);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Map<String, String>... values) {
            Map<String,String> value=values[0];
            Message msg=new Message();
            if(value.get("alone1")!=null){
                setAloneData(value.get("alone1"));
                msg.what=0;
                handler.sendMessage(msg);
            }else if(value.get("alone2")!=null){
                setAloneData(value.get("alone2"));
                msg.what=0;
                handler.sendMessage(msg);
            }else if(value.get("alone3")!=null){
                setAloneData(value.get("alone3"));
                msg.what=0;
                handler.sendMessage(msg);
            }else if(value.get("zf")!=null){
                setZFData(value.get("zf"));
                msg.what=1;
                handler.sendMessage(msg);
            }else if (value.get("df")!=null){
                setDFData(value.get("df"));
                msg.what=1;
                handler.sendMessage(msg);
            }else if (value.get("hsl")!=null){
                setHSLData(value.get("hsl"));
                msg.what=1;
                handler.sendMessage(msg);
            }else if (value.get("zfb")!=null){
                setZFBData(value.get("zfb"));
                msg.what=1;
                handler.sendMessage(msg);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
