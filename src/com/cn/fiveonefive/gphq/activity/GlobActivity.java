package com.cn.fiveonefive.gphq.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.adapter.SettingAdapter;
import com.cn.fiveonefive.gphq.dto.GlobConfig;
import com.cn.fiveonefive.gphq.glob.GlobStr;
import com.cn.fiveonefive.gphq.glob.MyApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hb on 2016/4/13.
 */
public class GlobActivity extends AbActivity{

    private AbTitleBar mAbTitleBar;

    private SettingAdapter settingAdapter;
    private List<GlobConfig> configList;

    private ListView settinglist;
    InputMethodManager im ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.setting_main);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_column_line);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        setAbContentView(R.layout.setting_main);
        settinglist= (ListView) findViewById(R.id.settinglist);
        configList=new ArrayList<GlobConfig>();

        init();
        settingAdapter=new SettingAdapter(configList,this);
        settinglist.setAdapter(settingAdapter);

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.hideSoftInputFromWindow(GlobActivity.this.getCurrentFocus().getWindowToken(), 0);
        MyApplication.addActivity(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }

    private void init(){
        configList.add(getBean("K_ri_start", GlobStr.K_ri_start));
        configList.add(getBean("K_ri_end",GlobStr.K_ri_end));

        configList.add(getBean("K_zhou_start",GlobStr.K_zhou_start));
        configList.add(getBean("K_zhou_end",GlobStr.K_zhou_end));

        configList.add(getBean("K_yue_start",GlobStr.K_yue_start));
        configList.add(getBean("K_yue_end",GlobStr.K_yue_end));

        configList.add(getBean("MyGPPeriod",GlobStr.MyGPPeriod));
        configList.add(getBean("HQPeriod",GlobStr.HQPeriod));
        configList.add(getBean("DetailPeriod",GlobStr.DetailPeriod));
        configList.add(getBean("FenShiPeriod",GlobStr.FenShiPeriod));
        configList.add(getBean("RiKPeriod",GlobStr.RiKPeriod));
        configList.add(getBean("ZhouKPeriod",GlobStr.ZhouKPeriod));
        configList.add(getBean("YueKPeriod",GlobStr.YueKPeriod));

        configList.add(getBean("MyState",GlobStr.MyGPPeriod));
        configList.add(getBean("HQState",GlobStr.HQState?0:1));
        configList.add(getBean("TabState",GlobStr.TabState?0:1));
        configList.add(getBean("FenShiState",GlobStr.FenShiState?0:1));
        configList.add(getBean("RiKState",GlobStr.RiKState?0:1));
        configList.add(getBean("ZhouKState",GlobStr.ZhouKState?0:1));
        configList.add(getBean("YueKState",GlobStr.YueKState?0:1));

    }
    private GlobConfig getBean(String title,int value){
        GlobConfig globConfig=new GlobConfig();
        globConfig.setTitle(title);
        globConfig.setValue(value);
        return globConfig;
    }
}
