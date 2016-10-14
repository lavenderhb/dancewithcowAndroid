package com.cn.fiveonefive.gphq.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GlobConfig;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.GlobStr;

import java.util.List;

/**
 * Created by hb on 2016/4/13.
 */
public class SettingAdapter extends BaseAdapter {

    private List<GlobConfig> configList;
    private Context context;
    private LayoutInflater inflater;
    private int position;


    public SettingAdapter(List<GlobConfig> configList,Context context){
        this.configList=configList;
        this.context=context;
        this.inflater= LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position=position;
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.settings_main_listitem,null);
            viewHolder.title= (TextView) convertView.findViewById(R.id.title);
            viewHolder.value= (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(configList.get(position).getTitle());
        viewHolder.value.setText(configList.get(position).getValue()+"");
        viewHolder.value.addTextChangedListener(textWatcher);
        return convertView;
    }


    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            {
                String input=s.toString();
                if (configList.get(position).getValue()== Integer.parseInt(input)){
                    return;
                }
                if (s.length() != 0) {
                    switch (position){
                        case 0://K_ri_start
                            GlobStr.K_ri_start= Integer.parseInt(input);
                            GlobMethod.putConfig("K_ri_start", input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 1://K_ri_end
                            GlobStr.K_ri_end= Integer.parseInt(input);
                            GlobMethod.putConfig("K_ri_end",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 2:
                            GlobStr.K_zhou_start= Integer.parseInt(input);
                            GlobMethod.putConfig("K_zhou_start",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 3:
                            GlobStr.K_zhou_end= Integer.parseInt(input);
                            GlobMethod.putConfig("K_zhou_end",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 4:
                            GlobStr.K_yue_start= Integer.parseInt(input);
                            GlobMethod.putConfig("K_yue_start",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 5:
                            GlobStr.K_yue_end= Integer.parseInt(input);
                            GlobMethod.putConfig("K_yue_end",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 6:
                            GlobStr.MyGPPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("MyGPPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 7:
                            GlobStr.HQPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("HQPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 8:
                            GlobStr.DetailPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("DetailPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 9:
                            GlobStr.FenShiPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("FenShiPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 10:
                            GlobStr.RiKPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("RiKPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 11:
                            GlobStr.ZhouKPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("ZhouKPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 12:
                            GlobStr.YueKPeriod= Integer.parseInt(input);
                            GlobMethod.putConfig("YueKPeriod",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 13:
                            GlobStr.MyState=input.equals("0")?true:false;
                            GlobMethod.putConfig("MyState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 14:
                            GlobStr.HQState=input.equals("0")?true:false;
                            GlobMethod.putConfig("HQState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 15:
                            GlobStr.TabState=input.equals("0")?true:false;
                            GlobMethod.putConfig("TabState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 16:
                            GlobStr.FenShiState=input.equals("0")?true:false;
                            GlobMethod.putConfig("FenShiState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 17:
                            GlobStr.RiKState=input.equals("0")?true:false;
                            GlobMethod.putConfig("RiKState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 18:
                            GlobStr.ZhouKState=input.equals("0")?true:false;
                            GlobMethod.putConfig("ZhouKState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;
                        case 19:
                            GlobStr.YueKState=input.equals("0")?true:false;
                            GlobMethod.putConfig("YueKState",input);
                            configList.get(position).setValue(Integer.parseInt(input));
                            break;

                    }
                }
            }
        }
    };
    @Override
    public int getCount() {
        return configList.size();
    }

    @Override
    public Object getItem(int position) {
        return configList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class ViewHolder{
        TextView title;
        TextView value;
    }


}
