package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.model.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 22/3/16.
 */
public class PopWindowApater extends BaseAdapter {

    private List<CategoryBean> menulist;
    private Context context;

    public PopWindowApater(List<CategoryBean> menulist, Context context) {
        this.menulist = menulist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menulist.size();
    }

    @Override
    public Object getItem(int position) {
        return menulist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.tab_popwindow_item,null);
            convertView.setTag(viewHolder);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textSearchName);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(menulist.get(position).getName());
        return convertView;
    }

    private static class ViewHolder{
        TextView textView;
    }
}
