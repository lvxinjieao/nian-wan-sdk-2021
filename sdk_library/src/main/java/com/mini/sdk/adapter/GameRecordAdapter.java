package com.mini.sdk.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.mini.sdk.entity.GameRecordEntity;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 游戏充值记录
 */
public class GameRecordAdapter extends BaseAdapter {

	private static final String TAG = "MCGameRecordAdapter";

	Context ctx;
	/**
	 * 充值记录列表
	 */
	private List<GameRecordEntity> gameRecordEntities = new ArrayList<GameRecordEntity>();

	private LayoutInflater mInflater;

	public GameRecordAdapter(Context ctx, List<GameRecordEntity> gameRecodeList) {
		this.ctx = ctx;
		this.gameRecordEntities = gameRecodeList;
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return gameRecordEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return gameRecordEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @param position
	 * @param convertView 布局文件
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// item_mch_addptb_record
		GameRecordEntity gameRecordEntity = gameRecordEntities.get(position);
		Logs.e("fun#getGameRecodeList  gameRecordEntity = " + gameRecordEntity.toString());

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(Resources.getLayoutId(ctx, "item_mch_game_record"), null);
			viewHolder.txtRecodetime = (TextView) convertView.findViewById(Resources.getId(ctx, "txt_mch_rocord_time"));
			viewHolder.txtRecodeName = (TextView) convertView
					.findViewById(Resources.getId(ctx, "txt_mch_rocord_money"));
			viewHolder.txtRecodeMoney = (TextView) convertView
					.findViewById(Resources.getId(ctx, "txt_mch_rocord_name"));
			viewHolder.txtRecodeType = (TextView) convertView.findViewById(Resources.getId(ctx, "txt_mch_rocord_type"));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String time = gameRecordEntity.getPayTime();
		String money = gameRecordEntity.getPayMoney();
		String name = gameRecordEntity.getPayName();
		String type = gameRecordEntity.getPayType();
		viewHolder.txtRecodetime.setText(formatTime(time));
		viewHolder.txtRecodeMoney.setText(money);
		viewHolder.txtRecodeName.setText(name);
		viewHolder.txtRecodeType.setText(ref(type));
		return convertView;
	}

	/**
	 * 格式化时间戳
	 */
	@SuppressLint("SimpleDateFormat")
	private static String formatTime(String timeStamp) {
		final String format1 = "yy-MM-dd HH:mm:ss";
		@SuppressWarnings("unused")
		final String format2 = "yyyy-MM-dd HH:mm:ss";
		if (null == timeStamp || "".equals(timeStamp)) {
			return "";
		}
		/** 10位数时间戳 */
		if (timeStamp.length() == 10) {
			timeStamp += "000";
		}
		DateFormat df = new SimpleDateFormat(format1);
		long ltime = Long.parseLong(timeStamp);
		return df.format(ltime);
	}

	/**
	 * 根据状态码返回支付方式
	 */
	public static String ref(String returnCode) {
		switch (Integer.parseInt(returnCode)) {
		case 0:
			returnCode = "平台币";
			break;
		case 1:
			returnCode = "支付宝";
			break;
		case 2:
			returnCode = "微信";
			break;
		case 3:
			returnCode = "";
			break;
		case 4:
			returnCode = "聚宝云";
			break;
		default:
			break;
		}
		return returnCode;
	}

	static class ViewHolder {
		TextView txtRecodetime;
		TextView txtRecodeName;
		TextView txtRecodeMoney;
		TextView txtRecodeType;
	}
}
