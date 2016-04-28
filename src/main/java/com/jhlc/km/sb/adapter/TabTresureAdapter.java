package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.AntiqueBean;
import com.jhlc.km.sb.model.Beauty;

import java.util.List;

/**
 * Created by licheng on 4/2/16.
 */
public class TabTresureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AntiqueBean> antiqueBeanList;

    private Context mContext;

    private final int TYPE_FOOTER = 1;
    private final int TYPE_NORMAL = 0;

//    private Boolean isShowFooter = true;
//
//    public Boolean getShowFooter() {
//        return isShowFooter;
//    }
//
//    public void setShowFooter(Boolean showFooter) {
//        isShowFooter = showFooter;
//    }

    public TabTresureAdapter(List<AntiqueBean> antiqueBeanList, Context mContext) {
        this.antiqueBeanList = antiqueBeanList;
        this.mContext = mContext;
    }

    private onItenClickListener listener;

    public void setListener(onItenClickListener listener) {
        this.listener = listener;
    }

    //实现点击
    public interface onItenClickListener{
        void onItemClick(View view, int position,String id,String name);
        void onItemLongClick(View view, int position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL) {
            ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.cardview_layout, parent, false));
            return holder;
        }
//        else if(viewType == TYPE_FOOTER){
//            View footerview = LayoutInflater.from(mContext).inflate(R.layout.footerview,parent,false);
//            return new FooterViewHolder(footerview);
//        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            String headimgurl = antiqueBeanList.get(position).getHeadimgurl();
            String indeximgurl = antiqueBeanList.get(position).getIndeximage();
            Uri headuri = Uri.parse(headimgurl+Constants.OSS_IMAGE_SIZE100);
            Uri indexuri = Uri.parse(indeximgurl+Constants.OSS_IMAGE_SIZE200);
            ((ItemViewHolder) holder).imgUserHead.setImageURI(headuri);
            ((ItemViewHolder) holder).imgTreasure.setImageURI(indexuri);
            ((ItemViewHolder) holder).tvUserName.setText(antiqueBeanList.get(position).getUserName());
            ((ItemViewHolder) holder).tvDiscribe.setText(antiqueBeanList.get(position).getDescribe());
            ((ItemViewHolder) holder).tvPrice.setText(antiqueBeanList.get(position).getPrice()+"元");
            ((ItemViewHolder) holder).textCommentNums.setText(antiqueBeanList.get(position).getCommentcount());
            ((ItemViewHolder) holder).textThumbNums.setText(antiqueBeanList.get(position).getLikecount());

        }
//        else if(holder instanceof FooterViewHolder){
//            ((FooterViewHolder) holder).textFooter.setText("加载中...");
//        }

        //点击事件回调
        if(listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemClick(holder.itemView,pos,antiqueBeanList.get(pos).getId(),antiqueBeanList.get(pos).getName());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        int begin = isShowFooter?1:0;
//        if(antiqueBeanList == null) {
//            return begin;
//        }
//        return antiqueBeanList.size() + begin;
        return antiqueBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_NORMAL;

//        if(!isShowFooter){
//            return TYPE_NORMAL;
//        }
//
//        if(position + 1 == getItemCount()){
//            return TYPE_FOOTER;
//        }
//        else {
//            return TYPE_NORMAL;
//        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvDiscribe;
        TextView tvPrice;
        TextView textCommentNums;
        TextView textThumbNums;
        SimpleDraweeView imgUserHead;
        SimpleDraweeView imgTreasure;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvDiscribe = (TextView) itemView.findViewById(R.id.tvDiscribe);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            textCommentNums = (TextView) itemView.findViewById(R.id.textCommentNums);
            textThumbNums = (TextView) itemView.findViewById(R.id.textThumbNums);
            imgUserHead = (SimpleDraweeView) itemView.findViewById(R.id.imgUserHead);
            imgTreasure = (SimpleDraweeView) itemView.findViewById(R.id.imgTreasure);
        }
    }

//    public class FooterViewHolder extends RecyclerView.ViewHolder{
//        TextView textFooter;
//        public FooterViewHolder(View itemView) {
//            super(itemView);
//            textFooter = (TextView) itemView.findViewById(R.id.textFooter);
//        }
//    }


}
