package com.cn.fiveonefive.gphq.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;

import java.util.ArrayList;

/**
 *
 * Created by hb on 2016/3/24.
 */
public class ExpanListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ArrayList<GuPiaoMainItem>> listGroupBD;
    private String[] arrayTitle;
    private LayoutInflater inflater;
    ViewHolder viewHolder;
    GroupHolder groupHolder;
    Drawable right,down;


    public ExpanListAdapter(Context context,ArrayList<ArrayList<GuPiaoMainItem>> listGroupBD,String[] arrayTitle){
        this.listGroupBD=listGroupBD;
        this.arrayTitle=arrayTitle;
        this.inflater= LayoutInflater.from(context);
        this.context=context;
        right=context.getResources().getDrawable(R.drawable.bd_to_right);
        down=context.getResources().getDrawable(R.drawable.bd_to_down);
    }

    public final class ViewHolder{
        TextView name;
        TextView symbol;
        TextView price;
        TextView percent;
    }
    public final class GroupHolder{
        TextView image;
        TextView title;
        TextView toMore;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.main_bd_item,null);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name);
            viewHolder.symbol= (TextView) convertView.findViewById(R.id.symbol);
            viewHolder.price= (TextView) convertView.findViewById(R.id.price);
            viewHolder.percent= (TextView) convertView.findViewById(R.id.percent);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(listGroupBD.get(groupPosition).get(childPosition).getName());
        viewHolder.symbol.setText(listGroupBD.get(groupPosition).get(childPosition).getSymbol());
        viewHolder.price.setText(listGroupBD.get(groupPosition).get(childPosition).getNewPrice());
        viewHolder.percent.setText(listGroupBD.get(groupPosition).get(childPosition).getZdf());
        switch(groupPosition){
            case 0:
                viewHolder.percent.setTextColor(context.getResources().getColor(R.color.red1));
                viewHolder.price.setTextColor(context.getResources().getColor(R.color.red1));
                break;
            case 1:
                viewHolder.percent.setTextColor(context.getResources().getColor(R.color.greed1));
                viewHolder.price.setTextColor(context.getResources().getColor(R.color.greed1));
                break;
            default:
                viewHolder.percent.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.price.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listGroupBD.get(groupPosition).get(childPosition);
    }
    @Override
    public int getGroupCount() {
        return listGroupBD.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return listGroupBD.get(groupPosition).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return listGroupBD.get(groupPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        groupHolder=null;
        if(convertView == null){
            groupHolder=new GroupHolder();
            convertView=inflater.inflate(R.layout.parent_item,null);

            groupHolder.title= (TextView) convertView.findViewById(R.id.title_text);
            groupHolder.toMore= (TextView) convertView.findViewById(R.id.to_all_list);
            groupHolder.image=(TextView) convertView.findViewById(R.id.image);
            convertView.setTag(groupHolder);
        }else{
         groupHolder= (GroupHolder) convertView.getTag();
        }
        if(isExpanded){
            groupHolder.image.setBackground(down);
        }else {
            groupHolder.image.setBackground(right);
        }
        groupHolder.title.setText(arrayTitle[groupPosition]);
        groupHolder.title.setTextColor(context.getResources().getColor(R.color.white));
        groupHolder.toMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, MoreListActivity.class);
//                intent.putExtra("type",groupPosition);
//                intent.putExtra("typeName",arrayTitle[groupPosition]);
//                context.startActivity(intent);
            }
        });
        return convertView;
    }


}
