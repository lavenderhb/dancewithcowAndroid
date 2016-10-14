package com.cn.fiveonefive.gphq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.Alone;
import com.cn.fiveonefive.gphq.glob.GlobMethod;

import java.util.List;

/**
 * 上证，深指，300
 * Created by hb on 2016/3/24.
 */
public class AloneItemAdapter extends BaseAdapter {
    private List<Alone> aloneList;
    private LayoutInflater mInflater;
    private ViewHoler viewHoler;
    private Context context;
    public AloneItemAdapter(List<Alone> aloneList,Context context){
        this.aloneList=aloneList;
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }
public final class ViewHoler{
    public TextView price;
    public TextView name;
    public TextView upPrice;
    public TextView percent;
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHoler=null;
        if (convertView==null){
            viewHoler=new ViewHoler();
            convertView=mInflater.inflate(R.layout.itemalone,null);
            viewHoler.price= (TextView) convertView.findViewById(R.id.price);
            viewHoler.name= (TextView) convertView.findViewById(R.id.name);
            viewHoler.upPrice= (TextView) convertView.findViewById(R.id.upprice);
            viewHoler.percent= (TextView) convertView.findViewById(R.id.percent);
            convertView.setTag(viewHoler);
        }
        else {
            viewHoler=(ViewHoler)convertView.getTag();
        }
        viewHoler.price.setText(GlobMethod.cgPriceToDF(aloneList.get(position).getPrice()));
        viewHoler.name.setText(aloneList.get(position).getName());
        viewHoler.upPrice.setText(aloneList.get(position).getUpPrice());
        float s=0;
        if(aloneList.get(position).getPercent()!=null){
            s= Float.valueOf(aloneList.get(position).getPercent());
            viewHoler.percent.setText(GlobMethod.cgPercentToDF(s));
        }else{
            viewHoler.percent.setText("--");
            viewHoler.upPrice.setText("--");
        }
        if(s<0){
            viewHoler.upPrice.setTextColor(context.getResources().getColor(R.color.color_green));
            viewHoler.percent.setTextColor(context.getResources().getColor(R.color.color_green));
            viewHoler.price.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        else if(s>0){
            viewHoler.upPrice.setTextColor(context.getResources().getColor(R.color.red));
            viewHoler.percent.setTextColor(context.getResources().getColor(R.color.red));
            viewHoler.price.setTextColor(context.getResources().getColor(R.color.red));
        }else if(s==0){

        }
        return convertView;
    }
    @Override
    public int getCount() {
        return aloneList.size();
    }

    @Override
    public Object getItem(int position) {
        return aloneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
