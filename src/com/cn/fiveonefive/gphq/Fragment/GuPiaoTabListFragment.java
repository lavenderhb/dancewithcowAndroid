package com.cn.fiveonefive.gphq.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ab.view.sliding.AbSlidingTabView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.activity.SearchListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by hb on 2016/3/26.
 */
public class GuPiaoTabListFragment extends Fragment {


    private EditText etSearch;
    private AbSlidingTabView mAbSlidingTabView;

    private Button btnBack;
    private TextView tvTitle;

    MyGupiaoFragment myGupiaoFragment;
    HQFragment hqFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_top,null);

        btnBack= (Button) view.findViewById(R.id.btnBack);
        tvTitle= (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.sliding_menu_pager_name);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        etSearch= (EditText) view.findViewById(R.id.etSearch);
        etSearch.setOnClickListener(etSearchClickListener);

        AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)view.findViewById(R.id.mAbSlidingTabView);
        mAbSlidingTabView.getViewPager().setOffscreenPageLimit(5);

        myGupiaoFragment=new MyGupiaoFragment();
        hqFragment=new HQFragment();

        final List<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(myGupiaoFragment);
        fragmentList.add(hqFragment);

        List<String> tabNameList=new ArrayList<String>();
        tabNameList.add("我的自选");
        tabNameList.add("行情");

        mAbSlidingTabView.setTabTextColor(Color.BLACK);
        mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
        mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
        mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
        mAbSlidingTabView.addItemViews(tabNameList, fragmentList);
        mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
        mAbSlidingTabView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                switch (i){
                    case 0:
                        myGupiaoFragment.isDo=true;
                        hqFragment.isDo=false;
                        if (myGupiaoFragment.timer==null){
                            myGupiaoFragment.timer=new Timer();
                            myGupiaoFragment.doRefresh();
                        }
                        break;
                    case 1:
                        hqFragment.isDo=true;
                        myGupiaoFragment.isDo=false;
                        if (hqFragment.timer==null){
                            hqFragment.timer=new Timer();
                            hqFragment.doRefresh();
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int i) {
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        return view;
    }
    View.OnClickListener etSearchClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getActivity(), SearchListActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onPause() {
        hqFragment.isDo=false;
        myGupiaoFragment.isDo=false;
        super.onPause();
    }

}
