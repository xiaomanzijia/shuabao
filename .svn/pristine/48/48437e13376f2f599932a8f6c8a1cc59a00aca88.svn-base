package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CommentBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by licheng on 4/2/16.
 */
public class CommentBaseAdapter extends BaseAdapter {

    private List<CommentBean> commentBeanList;

    private Context mContext;


    public CommentBaseAdapter(List<CommentBean> commentBeanList, Context mContext) {
        this.commentBeanList = commentBeanList;
        Log.i("CommentBaseAdapter",commentBeanList.size()+" ");
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return commentBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
            ButterKnife.bind(convertView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imgUserHead.setImageURI(Uri.parse(Constants.OSS_IMAGE_URL + commentBeanList.get(position).getHeadimgurl()+ Constants.OSS_IMAGE_SIZE50));
        holder.textDescribe.setText(commentBeanList.get(position).getContent());
        holder.textThumbNums.setText(commentBeanList.get(position).getLikecount());
        holder.textTime.setText(commentBeanList.get(position).getStrdate());
        holder.textUserName.setText(commentBeanList.get(position).getUsername());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.imgUserHead)
        SimpleDraweeView imgUserHead;
        @Bind(R.id.textUserName)
        TextView textUserName;
        @Bind(R.id.textDescribe)
        TextView textDescribe;
        @Bind(R.id.textTime)
        TextView textTime;
        @Bind(R.id.textThumbNums)
        TextView textThumbNums;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
