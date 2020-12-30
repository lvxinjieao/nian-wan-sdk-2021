package com.mini.sdk.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mini.sdk.entity.AddPtbEntity;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.utils.TimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoneyRecordAdapter extends BaseAdapter {

	private static final String TAG = "MCMoneyRecordAdapter";

	Context context;
	/**
	 * 充值记录列表
	 */
	private List<AddPtbEntity> packList = new ArrayList<AddPtbEntity>();

	private LayoutInflater mInflater;

	public MoneyRecordAdapter(Context context, List<AddPtbEntity> packList) {
		this.context = context;
		this.packList = packList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return packList.size();
	}

	@Override
	public Object getItem(int position) {
		return packList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AddPtbEntity addPtbInfo = packList.get(position);

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(Resources.getLayoutId(context, "item_mch_addptb_record"), null);
			viewHolder.txtRecodetime = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_rocord_time"));
			viewHolder.txtRecodePtb = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_rocord_ptb"));
			viewHolder.txtRecodeType = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_rocord_type"));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String time = TimeUtil.timet(addPtbInfo.getAddPtbTime());
		viewHolder.txtRecodetime.setText(time);

		StringBuilder ptb = new StringBuilder();
		ptb.append(addPtbInfo.getBlannce());
		viewHolder.txtRecodePtb.setText("￥" + ptb.toString());

		viewHolder.txtRecodeType.setText(addPtbInfo.getBuyPTBType());

		return convertView;
	}

	static class ViewHolder {
		TextView txtRecodetime;
		TextView txtRecodePtb;
		TextView txtRecodeType;
	}
}
