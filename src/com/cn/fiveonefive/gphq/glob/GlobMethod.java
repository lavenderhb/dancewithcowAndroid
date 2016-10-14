package com.cn.fiveonefive.gphq.glob;

import android.content.Context;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.dto.GPBean2;
import com.cn.fiveonefive.gphq.dto.GPBean2Temp;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

/**
 * Created by hb on 2016/4/1.
 */
public class GlobMethod {
    public static GPBean2 changeStrToGPBean2(String str){
        String a=str.replace("callback(","").replace(")","");
        Gson gson=new Gson();
        GPBean2 gpBean=new GPBean2();
        try {
            GPBean2Temp gpBean2Temp = gson.fromJson(a, new TypeToken<GPBean2Temp>() {
            }.getType());
            List jsonResult = gpBean2Temp.getValue();

            gpBean.set_0citycode(jsonResult.get(0).toString());
            gpBean.set_1symbol(jsonResult.get(1).toString());
            if (gpBean.get_0citycode().equals("1")) {
                gpBean.setCode("0" + gpBean.get_1symbol());
            } else if (gpBean.get_0citycode().equals("2")) {
                gpBean.setCode("1" + gpBean.get_1symbol());
            }
            gpBean.set_2name(jsonResult.get(2).toString());
            gpBean.set_3buy1(jsonResult.get(3).toString());
            gpBean.set_4buy2(jsonResult.get(4).toString());
            gpBean.set_5buy3(jsonResult.get(5).toString());
            gpBean.set_6buy4(jsonResult.get(6).toString());
            gpBean.set_7buy5(jsonResult.get(7).toString());
            gpBean.set_8sell1(jsonResult.get(8).toString());
            gpBean.set_9sell2(jsonResult.get(9).toString());
            gpBean.set_10sell3(jsonResult.get(10).toString());
            gpBean.set_11sell4(jsonResult.get(11).toString());
            gpBean.set_12sell5(jsonResult.get(12).toString());
            gpBean.set_13buy1num(jsonResult.get(13).toString());
            gpBean.set_14buy2num(jsonResult.get(14).toString());
            gpBean.set_15buy3num(jsonResult.get(15).toString());
            gpBean.set_16buy4num(jsonResult.get(16).toString());
            gpBean.set_17buy5num(jsonResult.get(17).toString());
            gpBean.set_18sell1num(jsonResult.get(18).toString());
            gpBean.set_19sell2num(jsonResult.get(19).toString());
            gpBean.set_20sell3num(jsonResult.get(20).toString());
            gpBean.set_21sell4num(jsonResult.get(21).toString());
            gpBean.set_22sell5num(jsonResult.get(22).toString());
            gpBean.set_23zhangting(jsonResult.get(23).toString());
            gpBean.set_24dieting(jsonResult.get(24).toString());
            gpBean.set_25price(jsonResult.get(25).toString());
            gpBean.set_26averageprice(jsonResult.get(26).toString());
            gpBean.set_27updown(jsonResult.get(27).toString());
            gpBean.set_28todayopen(jsonResult.get(28).toString());
            gpBean.set_29updownpercent(jsonResult.get(29).toString());
            gpBean.set_30top(jsonResult.get(30).toString());
            gpBean.set_31sumhand(jsonResult.get(31).toString());
            gpBean.set_32bottom(jsonResult.get(32).toString());
            gpBean.set_34yestclose(jsonResult.get(34).toString());
            gpBean.set_35dealmoney(jsonResult.get(35).toString());
            gpBean.set_36liangbi(jsonResult.get(36).toString());
            gpBean.set_37changehandpercent(jsonResult.get(37).toString());
            gpBean.set_39waipan(jsonResult.get(39).toString());
            gpBean.set_40neipan(jsonResult.get(40).toString());
            gpBean.set_41weibi(jsonResult.get(41).toString());
            gpBean.set_42weicha(jsonResult.get(42).toString());
            gpBean.set_45liutongshizhi(jsonResult.get(45).toString());
            gpBean.set_46zongshizhi(jsonResult.get(46).toString());
            gpBean.set_49time(jsonResult.get(49).toString());
            gpBean.set_50zhenfu(jsonResult.get(50).toString());
        }catch (Exception e){
            return gpBean;
        }
        return gpBean;
    }
    public static String change126to(String code ,String symbol){
        String a=code.substring(0,1);
        if (a .equals("0") ){
            return symbol+"1";
        }else if (a.equals("1")){
            return symbol+"2";
        }else{
            return "";
        }
    }
    /**
     * cong 20160908->2016/09/08
     * @param data
     * @return
     */
    public static String changeDataToData(String data){
        if ((data!=null)||(!data.equals(""))){
            String begin=data.substring(0,4);
            String middle=data.substring(4,6);
            String end=data.substring(6,8);
            return begin+"/"+middle+"/"+end;
        }else {
            return "";
        }
    }
    public static String changeTimeToTime(String time){
        if ((time!=null)||(!time.equals(""))){
            String begin=time.substring(0,2);
            String end=time.substring(2,4);
            return begin+":"+end;
        }else {
            return "";
        }
    }

    public static String changeCJLToShou(String numStr) {
        if (numStr.equals(""))
            return "--";
        float num = Float.valueOf(numStr);

        if (num > 0 && num <= 100000) {
            return Math.round(num / 100) + "手";
        } else if (num > 100000 && num <= 1000000000) {
            return Math.round(num / 10000) / 100.0 + "万手";
        } else if (num > 100000000) {
            return Math.round(num / 100000000) / 100.0 + "亿手";
        } else {
            return "--";
        }
    }
    public static String changeCJLToNOShou(String numStr) {
        if (numStr.equals(""))
            return "--";
        float num = Float.valueOf(numStr);

        if (num > 0 && num <= 100000) {
            return Math.round(num / 100) + "";
        } else if (num > 100000 && num <= 1000000000) {
            return Math.round(num / 10000) / 100.0 + "万";
        } else if (num > 100000000) {
            return Math.round(num / 100000000) / 100.0 + "亿";
        } else {
            return "--";
        }
    }

//    public static String changeCJLToShou(double num) {
//
//        if (num > 0 && num <= 100000) {
//            return Math.round(num / 100) + "手";
//        } else if (num > 100000 && num <= 1000000000) {
//            return Math.round(num / 10000) / 100.0 + "万手";
//        } else if (num > 100000000) {
//            return Math.round(num / 100000000) / 100.0 + "亿手";
//        } else {
//            return "--";
//        }
//    }


    public static void setTextView(TextView mTextView,String str){


// 计算TextView宽度：xml中定义的宽度300dp，转换成px
        float textViewWidth = mTextView.getWidth();
        float dotWidth = getCharWidth(mTextView, '.');

        int sumWidth = 0;
        for (int index=0; index<str.length(); index++) {
            // 计算每一个字符的宽度
            char c = str.charAt(index);
            float charWidth = getCharWidth(mTextView, c);
            sumWidth += charWidth;

            if (sumWidth + dotWidth*3 >= textViewWidth) {
                break;
            }
        }

    }
    private static float getCharWidth(TextView textView, char c) {
        textView.setText(String.valueOf(c));
        textView.measure(0, 0);
        return textView.getMeasuredWidth();
    }
    public static String changeXXEXXToNormal(String str){
        BigDecimal num = new BigDecimal(str);
        DecimalFormat df = new DecimalFormat("#.###");
        String res = df.format(num);
        return res;
    }

    public static String changeCJEToStr(String cje) {
        if (cje.equals(""))
            return "--";
        Double numDou = Double.valueOf(cje);

        if (numDou > 0 && numDou <= 10000) {
            return Math.round(numDou) + "元";
        } else if (numDou > 10000 && numDou <= 100000000) {
            return Math.round(numDou / 100) / 100.0 + "万元";
        } else if (numDou > 100000000) {
            return Math.round(numDou / 1000000) / 100.0 + "亿元";
        } else {
            return "--";
        }
    }
    public static String changeCJEToNoYuanStr(String cje) {
        if (cje.equals(""))
            return "--";
        Double numDou = Double.valueOf(cje);

        if (numDou > 0 && numDou <= 10000) {
            return Math.round(numDou) + "";
        } else if (numDou > 10000 && numDou <= 100000000) {
            return Math.round(numDou / 100) / 100.0 + "万";
        } else if (numDou > 100000000) {
            return Math.round(numDou / 1000000) / 100.0 + "亿";
        } else {
            return "--";
        }
    }

    public static boolean getIsAdd(String code) {
        List<MyGuPiao> tempList = null;
        try {
            tempList = MyApplication.db.findAll(Selector.from(MyGuPiao.class).where("code", "=", code));
//            tempList= DCApplication.db.findAll(MyGuPiao.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (tempList == null || tempList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String cgPriceToDF(String price) {
        if (price == null) {
            return "--";
        }
        if (price.equals("")) {
            return "--";
        }
        return new DecimalFormat("#.##").format(Float.parseFloat(price));
    }

    public static String cgPercentToDF(String percent) {
        return "";
    }

    public static String cgPercentToDF(Float percent) {
        return new DecimalFormat("#.##%").format(percent);
    }
    public static String cgPrice(Float price){
        return new DecimalFormat("#.##").format(price);
    }

    public static String cgGuToShouDF(String gu) {
        if (gu == null) {
            return "--";
        }
        if (gu.equals("")) {
            return "";
        }
        return new DecimalFormat("###").format(Float.parseFloat(gu) / 100);
    }

    public static Properties getConfig(Context context) {
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(GlobStr.configLocalPath);
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public static void putConfig(String key, String value) {
        Properties p = new Properties();
        try {
            InputStream in = new FileInputStream(GlobStr.configLocalPath);
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setProperty(key, value);
        OutputStream fos;
        try {
            fos = new FileOutputStream(GlobStr.configLocalPath);
            p.store(fos, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
