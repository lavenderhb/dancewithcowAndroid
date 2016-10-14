package com.cn.fiveonefive.gphq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.glob.GlobMethod;

import java.util.List;

/**
 * Created by hb on 2016/4/21.
 */
public class BDListAdapter extends BaseAdapter{
    private List<GuPiaoMainItem> listItem;
    private LayoutInflater inflater;
    private Context context;

    public BDListAdapter(List<GuPiaoMainItem> listItem,Context context){
        this.listItem=listItem;
        this.context=context;
        this.inflater= LayoutInflater.from(context);

    }
    public final class ViewHolder{
        TextView num;
        TextView name;
        TextView symbol;
        TextView price;
        TextView percent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();

            convertView=inflater.inflate(R.layout.bd_item,null);
            viewHolder.num= (TextView) convertView.findViewById(R.id.num);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name);
            viewHolder.symbol= (TextView) convertView.findViewById(R.id.symbol);
            viewHolder.price= (TextView) convertView.findViewById(R.id.price);
            viewHolder.percent= (TextView) convertView.findViewById(R.id.percent);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.num.setText(position+1+"");
        viewHolder.name.setText(listItem.get(position).getName());
        viewHolder.symbol.setText(listItem.get(position).getSymbol());
        viewHolder.price.setText(GlobMethod.cgPriceToDF(listItem.get(position).getNewPrice()));

        float s=0;
        if(listItem.get(position).getZdf()!=null){
            viewHolder.percent.setText(listItem.get(position).getZdf());
            s= Float.valueOf(listItem.get(position).getZdf().substring(0,listItem.get(position).getZdf().indexOf("%")));
        }else{
            viewHolder.price.setText("--");
            viewHolder.percent.setText("--");
        }
        if(s<0){
            viewHolder.price.setTextColor(context.getResources().getColor(R.color.color_green));
            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        else if(s>0){
            viewHolder.price.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.red));
        }else if(s==0){
            viewHolder.price.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }
    private void setColor(String string){
//        float s=Float.valueOf(string);
//        if(s<0){
//            viewHolder.price.setTextColor(context.getResources().getColor(R.color.color_green));
//            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.color_green));
//        }
//        else {
//            viewHolder.name.setTextColor(context.getResources().getColor(R.color.red));
//            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.red));
//        }

    }
    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
