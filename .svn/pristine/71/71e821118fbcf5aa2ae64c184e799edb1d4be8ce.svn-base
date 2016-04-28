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
import com.jhlc.km.sb.model.CommentThumbBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by licheng on 4/2/16.
 */
public class TabCommentThumbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentThumbBean> commentThumbBeanList;

    private Context mContext;

    private String fragmentType;

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

    public TabCommentThumbAdapter(List<CommentThumbBean> commentThumbBeanList, Context mContext,String fragmentType) {
        this.commentThumbBeanList = commentThumbBeanList;
        this.mContext = mContext;
        this.fragmentType = fragmentType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.fragment_comment_thumb_item, parent, false);
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
            String headimgurl = commentThumbBeanList.get(position).getHeadimgurl();
            String indeximgurl = commentThumbBeanList.get(position).getIndeximage();
            switch (fragmentType) {
                case Constants.INTENT_FRAGMENT_TYPE_COMMENT:
                    ((ItemViewHolder) holder).textThumbComment.setText(commentThumbBeanList.get(position).getStrdate()+" 评论了");
                    ((ItemViewHolder) holder).textTime.setText(commentThumbBeanList.get(position).getContent());
                    break;
                case Constants.INTENT_FRAGMENT_TYPE_THUMB:
                    ((ItemViewHolder) holder).textThumbComment.setText(" 赞了");
                    ((ItemViewHolder) holder).textTime.setText(commentThumbBeanList.get(position).getStrdate());
                    break;
                default:
                    break;
            }
            ((ItemViewHolder) holder).imgUserHead.setImageURI(Uri.parse(headimgurl+Constants.OSS_IMAGE_SIZE50));
            ((ItemViewHolder) holder).imgIndex.setImageURI(Uri.parse(indeximgurl+Constants.OSS_IMAGE_SIZE50));
            ((ItemViewHolder) holder).textUserName.setText(commentThumbBeanList.get(position).getUsername());
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
        return commentThumbBeanList.size();
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
        @Bind(R.id.textThumbComment)
        TextView textThumbComment;
        @Bind(R.id.textTime)
        TextView textTime;
        @Bind(R.id.imgIndex)
        SimpleDraweeView imgIndex;

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
