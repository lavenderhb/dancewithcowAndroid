package com.cn.fiveonefive.gphq.glob;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by hb on 2016/3/31.
 */
public class GlobStr {
    public static String localPath= Environment
            .getExternalStorageDirectory()
            + File.separator
            + "dancecow"+ File.separator;

    public static String dbLocalPath=localPath +"db"
            + File.separator;
    public static String configFileName="config.properties";
    public static String configLocalPath=localPath+configFileName;

    //接口列表


    public static final String GPUrl="http://api.money.126.net/data/feed/";
    public static final String GPURL2 = "http://nuff.eastmoney.com/EM_Finance2015TradeInterface/JS.ashx?id=";
    public static final String SearchUrl="http://quotes.money.163.com/stocksearch/json.do?type=&count=";
    public static final int SearchCountOfOnePage=20;
    public static final String ZFUrl="http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&page=1&jsName=quote_123&style=33&pageSize=";
    public static final String DFUrl="http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=1&page=1&jsName=quote_123&style=33&pageSize=";
    public static final String HSLUrl="http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=J&sortRule=-1&page=1&jsName=quote_123&style=33&pageSize=";
    public static final String ZFBUrl="http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=K&sortRule=-1&page=1&jsName=quote_123&style=33&pageSize=";
    public static int BDPageSize=10;
    public static List<String> listBD=new ArrayList<>();

    public static final String FenShiUrl="http://img1.money.126.net/data/hs/time/today/";
    public static final String RiKUrl="http://img1.money.126.net/data/hs/kline/day/history/";
    public static final String ZhouKUrl="http://img1.money.126.net/data/hs/kline/week/history/";
    public static final String YueKUrl="http://img1.money.126.net/data/hs/kline/month/history/";

    //开始结束时间
    public static int year=new GregorianCalendar().get(Calendar.YEAR);
    public static int K_ri_start=2015;
    public static int K_ri_end=year;
    public static int K_zhou_start=2010;
    public static int K_zhou_end=year;
    public static int K_yue_start=2005;
    public static int K_yue_end=year;

    //循环周期
    public static int MyGPPeriod=7;
    public static int HQPeriod=7;
    public static int DetailPeriod=7;
    public static int FenShiPeriod=7;
    public static int RiKPeriod=20;
    public static int ZhouKPeriod=60;
    public static int YueKPeriod=60;

    //是否循环
    public static boolean MyState=false;
    public static boolean HQState=false;
    public static boolean TabState=false;
    public static boolean FenShiState=false;
    public static boolean RiKState=false;
    public static boolean ZhouKState=false;
    public static boolean YueKState=false;

}
