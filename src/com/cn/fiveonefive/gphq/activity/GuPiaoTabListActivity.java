package com.cn.fiveonefive.gphq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.ab.activity.AbActivity;
import com.cn.fiveonefive.gphq.Fragment.GuPiaoTabListFragment;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.glob.MyApplication;

/**
 * Created by hb on 2016/3/22.
 */
public class GuPiaoTabListActivity extends AbActivity {

    private int count=0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.sliding_menu_content);

        GuPiaoTabListFragment guPiaoTabListFragment=new GuPiaoTabListFragment();
        getFragmentManager().beginTransaction().replace(R.id.content_frame,guPiaoTabListFragment).commit();
        MyApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.removeActivity(this);
    }
    View.OnClickListener setListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            count++;
            if(count==6){
                count = 0;
                Intent intent=new Intent(GuPiaoTabListActivity.this,GlobActivity.class);
                startActivity(intent);
            }
        }
    };
}
