package com.cn.fiveonefive.gphq.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.activity.ActivityDetail;
import com.cn.fiveonefive.gphq.adapter.ExpanListAdapter;
import com.cn.fiveonefive.gphq.dto.BDBeanTmp;
import com.cn.fiveonefive.gphq.dto.BaseBean;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.dto.Result;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.cn.fiveonefive.gphq.util.GetHttpResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hb on 2016/9/3.
 */
public class FragmentBD extends Fragment {
    private ArrayList<GuPiaoMainItem> listZF;//zhangfu
    private ArrayList<GuPiaoMainItem> listDF;//diefu
    private ArrayList<GuPiaoMainItem> listHSL;//huanshoulv
    private ArrayList<GuPiaoMainItem> listZHF;//zhenfu
    private ArrayList<ArrayList<GuPiaoMainItem>> listGroupBD;
    private ExpanListAdapter bdExpanListAdapter;

    private ExpandableListView exlvBD;
    private String[] arrayTitle;
    public boolean isDo=false;
    Gson gson;

    private GetDataTask getDataTask;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    bdExpanListAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bd_freg,null);

        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        exlvBD= (ExpandableListView) view.findViewById(R.id.bangdan);
//        exlvBD.setIndicatorBounds(width-40, width-25);
//        exlvBD.setChildIndicatorBounds(20, 53);
        listZF=new ArrayList<GuPiaoMainItem>();
        listDF=new ArrayList<GuPiaoMainItem>();
        listHSL=new ArrayList<GuPiaoMainItem>();
        listZHF=new ArrayList<GuPiaoMainItem>();
        listGroupBD=new ArrayList<ArrayList<GuPiaoMainItem>>();
        listGroupBD.add(listZF);
        listGroupBD.add(listDF);
        listGroupBD.add(listHSL);
        listGroupBD.add(listZHF);
        arrayTitle=new String[]{"涨幅","跌幅","换手率","振幅榜"};
        bdExpanListAdapter=new ExpanListAdapter(getActivity(),listGroupBD,arrayTitle);
        //设置下拉箭头
        exlvBD.setGroupIndicator(null);
        exlvBD.setAdapter(bdExpanListAdapter);
        exlvBD.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getActivity(), ActivityDetail.class);
                intent.putExtra("code", listGroupBD.get(groupPosition).get(childPosition).getCode());
                intent.putExtra("name",listGroupBD.get(groupPosition).get(childPosition).getName());
                intent.putExtra("symbol",listGroupBD.get(groupPosition).get(childPosition).getSymbol());
                intent.putExtra("state",true);
                List<BaseBean> baseBeanList=new ArrayList<BaseBean>();
                for (GuPiaoMainItem gp:listGroupBD.get(groupPosition)){
                    BaseBean baseBean=new BaseBean();
                    baseBean.setName(gp.getName());
                    baseBean.setSymbol(gp.getSymbol());
                    baseBean.setCode(gp.getCode());
                    baseBeanList.add(baseBean);
                }
                intent.putExtra("beanList",gson.toJson(baseBeanList));

                startActivity(intent);
                return true;
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        getDataTask=new GetDataTask();
        getDataTask.execute();
        super.onResume();
    }

    @Override
    public void onStop() {
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("bd");
        }
        super.onStop();
    }
    public void start(){
        getDataTask=new GetDataTask();
        MyApplication.taskMap.put("bd",getDataTask);
    }
    public void stop(){
        if (getDataTask!=null){
            getDataTask.cancel(true);
            MyApplication.taskMap.remove("bd");
        }
    }

    private class GetDataTask extends AsyncTask<Object, Map<String,String>, String> {

        @Override
        protected String doInBackground(Object[] objects) {
            //alone1
            //涨幅
            GetHttpResult getHttpResultZF=new GetHttpResult(GlobStr.ZFUrl+GlobStr.BDPageSize);
            Result resultZF=getHttpResultZF.getResult();
            if (resultZF.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("zf",resultZF.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            //跌幅
            GetHttpResult getHttpResultDF=new GetHttpResult(GlobStr.DFUrl+GlobStr.BDPageSize);
            Result resultDF=getHttpResultDF.getResult();
            if (resultDF.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("df",resultDF.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            //换手率
            GetHttpResult getHttpResultHSL=new GetHttpResult(GlobStr.HSLUrl+GlobStr.BDPageSize);
            Result resultHSL=getHttpResultHSL.getResult();
            if (resultHSL.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("hsl",resultHSL.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            //振幅榜
            GetHttpResult getHttpResultZFB=new GetHttpResult(GlobStr.ZFBUrl+GlobStr.BDPageSize);
            Result resultZFB=getHttpResultZFB.getResult();
            if (resultZFB.getResultCode()==0){
                Map<String,String> map= new HashMap<String,String>();
                map.put("zfb",resultZFB.getResultData());
                if(isCancelled()) return null;
                publishProgress(map);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Map<String, String>... values) {
            Map<String,String> value=values[0];
            Message msg=new Message();
            if(value.get("zf")!=null){
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
            if (isDo){
                getDataTask=new GetDataTask();
                MyApplication.taskMap.put("bd",getDataTask);
            }

            super.onPostExecute(s);
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
}
