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
import com.jhlc.km.sb.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 4/2/16.
 */
public class TabTresureMeFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public TabTresureMeFragmentAdapter(List<AntiqueBean> antiqueBeanList, Context mContext) {
        this.antiqueBeanList = antiqueBeanList;
        this.mContext = mContext;
    }

    private onItenClickListener listener;

    public void setListener(onItenClickListener listener) {
        this.listener = listener;
    }




    //实现点击
    public interface onItenClickListener {
        void onItemClick(View view, int position, String id, String name);
        void onItemLongClick(View view, int position);
        void onDeleteClick(int position, String id);
        void onShareClick(int position, String id, String content,String tresureIndexImage);
        void onEditClick(int position, String id, String content, String price, String picUrl);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.cardview_fragment_me_layout, parent, false);
//            ButterKnife.bind(view);
//            ItemViewHolder holder = new ItemViewHolder(view);
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
            String indeximgurl = antiqueBeanList.get(position).getIndeximage();
            ((ItemViewHolder) holder).imgTreasure.setImageURI(Uri.parse(indeximgurl+ Constants.OSS_IMAGE_SIZE200));
            ((ItemViewHolder) holder).tvDiscribe.setText(antiqueBeanList.get(position).getDescribe());
            ((ItemViewHolder) holder).tvPrice.setText(antiqueBeanList.get(position).getPrice() + "元");
            ((ItemViewHolder) holder).textCommentNums.setText(antiqueBeanList.get(position).getCommentcount());
            ((ItemViewHolder) holder).textThumbNums.setText(antiqueBeanList.get(position).getLikecount());
        }

//        if (holder instanceof ItemViewHolder) {
//            String indeximgurl = antiqueBeanList.get(position).getIndeximage();
//            Uri indexuri = Uri.parse(indeximgurl);
//            ((ItemViewHolder) holder).imgTreasure.setImageURI(indexuri);
//            ((ItemViewHolder) holder).tvUserName.setText(antiqueBeanList.get(position).getUserName());
//            ((ItemViewHolder) holder).tvDiscribe.setText(antiqueBeanList.get(position).getDescribe());
//            ((ItemViewHolder) holder).tvPrice.setText(antiqueBeanList.get(position).getPrice() + "元");
//            ((ItemViewHolder) holder).textCommentNums.setText(antiqueBeanList.get(position).getCommentcount());
//            ((ItemViewHolder) holder).textThumbNums.setText(antiqueBeanList.get(position).getLikecount());
//
//        }

//        else if(holder instanceof FooterViewHolder){
//            ((FooterViewHolder) holder).textFooter.setText("加载中...");
//        }

        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                listener.onDeleteClick(pos,antiqueBeanList.get(pos).getId());
            }
        });

        holder.itemView.findViewById(R.id.btnEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                listener.onEditClick(pos,antiqueBeanList.get(pos).getId(),
                        antiqueBeanList.get(pos).getDescribe(),antiqueBeanList.get(pos).getPrice(),
                        antiqueBeanList.get(pos).getIndeximage() + Constants.OSS_IMAGE_SIZE200);
            }
        });

        holder.itemView.findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                listener.onShareClick(pos,antiqueBeanList.get(pos).getId(),antiqueBeanList.get(pos).getName(),
                        antiqueBeanList.get(pos).getIndeximage()+Constants.OSS_IMAGE_SIZE100);
            }
        });


        //点击事件回调
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemClick(holder.itemView, pos, antiqueBeanList.get(pos).getId(), antiqueBeanList.get(pos).getName());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemLongClick(holder.itemView, pos);
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

//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        TextView tvUserName;
//        TextView tvDiscribe;
//        TextView tvPrice;
//        TextView textCommentNums;
//        TextView textThumbNums;
//        SimpleDraweeView imgTreasure;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
//            tvDiscribe = (TextView) itemView.findViewById(R.id.tvDiscribe);
//            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
//            textCommentNums = (TextView) itemView.findViewById(R.id.textCommentNums);
//            textThumbNums = (TextView) itemView.findViewById(R.id.textThumbNums);
//            imgTreasure = (SimpleDraweeView) itemView.findViewById(R.id.imgTreasure);
//        }
//    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgTreasure)
        SimpleDraweeView imgTreasure;
        @Bind(R.id.tvDiscribe)
        TextView tvDiscribe;
        @Bind(R.id.tvPrice)
        TextView tvPrice;
        @Bind(R.id.textCommentNums)
        TextView textCommentNums;
        @Bind(R.id.textThumbNums)
        TextView textThumbNums;
        @Bind(R.id.btnEdit)
        TextView btnEdit;
        @Bind(R.id.btnShare)
        TextView btnShare;
        @Bind(R.id.btnDelete)
        TextView btnDelete;

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
