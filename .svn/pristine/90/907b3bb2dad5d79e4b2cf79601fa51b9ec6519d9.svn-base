package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
public class TabCommentHotNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentBean> commentBeanList;

    private Context mContext;

    private String fragmentType;

    private final int TYPE_FOOTER = 1;
    private final int TYPE_NORMAL = 0;

    private onThumbClickClistener listener;

    public void setListener(onThumbClickClistener listener) {
        this.listener = listener;
    }

    public interface onThumbClickClistener{
        void onThumbCick(int postion,String commentid);
    }


    //    private Boolean isShowFooter = true;
//
//    public Boolean getShowFooter() {
//        return isShowFooter;
//    }
//
//    public void setShowFooter(Boolean showFooter) {
//        isShowFooter = showFooter;
//    }

    public TabCommentHotNewAdapter(List<CommentBean> commentBeanList, Context mContext, String fragmentType) {
        this.commentBeanList = commentBeanList;
        this.mContext = mContext;
        this.fragmentType = fragmentType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.comment_item, parent, false);
            ButterKnife.bind(view);
            ItemViewHolder holder = new ItemViewHolder(view);
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
            String headimgurl = commentBeanList.get(position).getHeadimgurl();
            ((ItemViewHolder) holder).imgUserHead.setImageURI(Uri.parse(headimgurl + Constants.OSS_IMAGE_SIZE50));
            ((ItemViewHolder) holder).textUserName.setText(commentBeanList.get(position).getUsername());
            ((ItemViewHolder) holder).textDescribe.setText(commentBeanList.get(position).getContent());
            ((ItemViewHolder) holder).textThumbNums.setText(commentBeanList.get(position).getLikecount());
            ((ItemViewHolder) holder).textTime.setText(commentBeanList.get(position).getStrdate());

            if(listener != null){
                ((ItemViewHolder) holder).ibtnThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        listener.onThumbCick(pos,commentBeanList.get(pos).getId());
                    }
                });
            }

        }
//        else if(holder instanceof FooterViewHolder){
//            ((FooterViewHolder) holder).textFooter.setText("加载中...");
//        }
    }





    @Override
    public int getItemCount() {
//        int begin = isShowFooter?1:0;
//        if(antiqueBeanList == null) {
//            return begin;
//        }
//        return antiqueBeanList.size() + begin;
        return commentBeanList.size();
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

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgUserHead)
        SimpleDraweeView imgUserHead;
        @Bind(R.id.textUserName)
        TextView textUserName;
        @Bind(R.id.textDescribe)
        TextView textDescribe;
        @Bind(R.id.textTime)
        TextView textTime;
        @Bind(R.id.ibtnThumb)
        ImageButton ibtnThumb;
        @Bind(R.id.textThumbNums)
        TextView textThumbNums;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
