package com.cn.fiveonefive.gphq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.cn.fiveonefive.gphq.Fragment.DetailTabFragment;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.glob.MyApplication;

/**
 * Created by hb on 2016/3/25.
 */
public class DetailTabActivity extends AbActivity{
    private AbTitleBar mAbTitleBar;
    private String code;
    private String name;
    private String symbol;
    private View viewAdd;
    private View viewOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.sliding_menu_content);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        code=intent.getStringExtra("code");
        symbol=intent.getStringExtra("symbol");

        DetailTabFragment detailTabFragment =new DetailTabFragment(code,symbol,name);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, detailTabFragment).commit();

        MyApplication.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }
}
