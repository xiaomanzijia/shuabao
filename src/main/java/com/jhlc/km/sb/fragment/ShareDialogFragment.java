package com.jhlc.km.sb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 24/3/16.
 */
public class ShareDialogFragment extends DialogFragment {

    @Bind(R.id.llShareSina)
    LinearLayout llShareSina;
    @Bind(R.id.llShareWeChat)
    LinearLayout llShareWeChat;
    @Bind(R.id.llShareFriendCicle)
    LinearLayout llShareFriendCicle;
    @Bind(R.id.llShareQQ)
    LinearLayout llShareQQ;
    @Bind(R.id.llShareMessage)
    LinearLayout llShareMessage;
    @Bind(R.id.btnCancle)
    TextView btnCancle;

    private ShareClickListener listener;

    public interface ShareClickListener{
        void onClick(View view);
    }

    public void setListener(ShareClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置宽度为屏宽、靠近屏幕底部
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        View view = inflater.inflate(R.layout.dialogfragment_share_layout, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.llShareSina, R.id.llShareWeChat, R.id.llShareFriendCicle, R.id.llShareQQ, R.id.llShareMessage, R.id.btnCancle})
    public void onClick(View view) {
        listener.onClick(view);
    }
}
