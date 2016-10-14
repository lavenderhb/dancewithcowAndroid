package com.cn.fiveonefive.gphq.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ab.util.AbToastUtil;
import com.cn.fiveonefive.gphq.R;
import com.cn.fiveonefive.gphq.dto.GuPiaoMainItem;
import com.cn.fiveonefive.gphq.entity.MyGuPiao;
import com.cn.fiveonefive.gphq.glob.GlobMethod;
import com.cn.fiveonefive.gphq.glob.MyApplication;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import java.util.List;

/**
 * Created by hb on 2016/3/23.
 */
public class TempListAdapter extends BaseAdapter {
    private List<GuPiaoMainItem> listTemp;
    private LayoutInflater inflater;
    private Context context;
    ViewHolder holder=null;

    public TempListAdapter(List<GuPiaoMainItem> listTemp, Context context){
        this.listTemp=listTemp;
        inflater= LayoutInflater.from(context);
        this.context=context;
    }
    public final class ViewHolder{
        public TextView symbol;
        public TextView name;
        public ImageButton isAdd;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.temp_list_item,null);
            holder.symbol= (TextView) convertView.findViewById(R.id.symbol);
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.isAdd= (ImageButton) convertView.findViewById(R.id.addtomy);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.isAdd.setFocusable(false);


        holder.symbol.setText(listTemp.get(position).getSymbol());
        holder.name.setText(listTemp.get(position).getName());
        if(GlobMethod.getIsAdd(listTemp.get(position).getCode())) {
            holder.isAdd.setVisibility(View.VISIBLE);
            holder.isAdd.setImageResource(R.drawable.sub3);
        }else{
            holder.isAdd.setVisibility(View.VISIBLE);
            holder.isAdd.setImageResource(R.drawable.add3);
            holder.isAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GlobMethod.getIsAdd(listTemp.get(position).getCode())){
                        Intent intent = new Intent("cn.fiveonefive.removeMyList");
                        intent.putExtra("code",listTemp.get(position).getCode());
                        try {
                            MyApplication.db.delete(MyGuPiao.class, WhereBuilder.b("code", "=", listTemp.get(position).getCode()));
                            context.sendBroadcast(intent);
                            holder.isAdd.setImageResource(R.drawable.add3);
                            notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                            AbToastUtil.showToast(context, "删除失败");
                        }

                    }else {
                        MyGuPiao myGuPiao=new MyGuPiao();
                        Intent intent = new Intent("cn.fiveonefive.addMyList");
                        myGuPiao.setCode(listTemp.get(position).getCode());
                        intent.putExtra("code",listTemp.get(position).getCode());
                        myGuPiao.setName(listTemp.get(position).getName());
                        intent.putExtra("name",listTemp.get(position).getName());
                        myGuPiao.setSymbol(listTemp.get(position).getSymbol());
                        intent.putExtra("symbol",listTemp.get(position).getSymbol());
                        try{
                            MyApplication.db.saveBindingId(myGuPiao);
                            holder.isAdd.setImageResource(R.drawable.sub3);
                            notifyDataSetChanged();
                            context.sendBroadcast(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            AbToastUtil.showToast(context,"添加失败");
                        }
                    }

                }
            });
        }
        return convertView;
    }
    @Override
    public int getCount() {
        return listTemp.size();
    }

    @Override
    public Object getItem(int position) {
        return listTemp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }





}
