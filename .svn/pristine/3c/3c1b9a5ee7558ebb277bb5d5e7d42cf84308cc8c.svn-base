package com.jhlc.km.sb.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jhlc.km.sb.R;


@SuppressLint("NewApi")
public class LoadingFragment extends DialogFragment {
	private TextView vLoading_text;
	private String mMsg = "正在加载···";

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_loading, null);
		vLoading_text = (TextView) view.findViewById(R.id.loading_text);
		vLoading_text.setText(mMsg);
		Dialog dialog = new Dialog(getActivity(), R.style.MyLoadDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		System.out.println("======================================================onCreateDialog");
		return dialog;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		System.out.println("======================================================onActivityCreated");
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("======================================================onStart");
	}
	public void setMsg(String msg) {
		if (msg != null) {
			this.mMsg = msg;
		}
		System.out.println("======================================================setMsg");
	}

}
