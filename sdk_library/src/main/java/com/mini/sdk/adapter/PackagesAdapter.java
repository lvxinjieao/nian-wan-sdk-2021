package com.mini.sdk.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.dialog.ReceivePackDialog;
import com.mini.sdk.entity.GamePackInfo;
import com.mini.sdk.entity.PackCodeEntity;
import com.mini.sdk.http.process.PacksCodeProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Resources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PackagesAdapter extends BaseAdapter {
	Context context;
	Activity activity;
	Handler handler;
	/**
	 * 礼包列表
	 */
	private List<GamePackInfo> packList = new ArrayList<GamePackInfo>();
	private List<ViewHolder> holderList = new LinkedList<ViewHolder>();

	/**
	 * 图片加载
	 */
	BitmapUtils bitmapUtils;

	private LayoutInflater mInflater;

	public PackagesAdapter(Activity activity, Context context, List<GamePackInfo> packList, BitmapUtils bitmapUtils,
			Handler handler) {
		this.activity = activity;
		this.context = context;
		this.packList = packList;
		this.bitmapUtils = bitmapUtils;
		this.handler = handler;
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
		final GamePackInfo packInfo = packList.get(position);

		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(Resources.getLayoutId(context, "item_mch_packs"), null);
			viewHolder.ivPackImage = (ImageView) convertView.findViewById(Resources.getId(context, "iv_mch_pack_image"));
			viewHolder.tvPackName = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_pack_name"));
			viewHolder.tvPackEffective = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_pack_effective"));
			viewHolder.tvPackDesc = (TextView) convertView.findViewById(Resources.getId(context, "txt_mch_pack_desc"));
			viewHolder.btnReceivePack = (Button) convertView.findViewById(Resources.getId(context, "btn_mch_receive_pack"));

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		holderList.add(viewHolder);
		bitmapUtils.display(viewHolder.ivPackImage,packInfo.getPackImageUrl(), new DefaultBitmapLoadCallBack<ImageView>());

		viewHolder.tvPackName.setText(ChannelAndGame.getInstance().getGameName() + ":" + packInfo.getPackName());
		
		if (packInfo.getEndTime().equals("0")) {
			viewHolder.tvPackEffective.setText("有效期：长期有效");
		} else {
			viewHolder.tvPackEffective.setText("有效期：" + packInfo.getStartTime() + "至" + packInfo.getEndTime());
		}
		if (packInfo.getPackStatus().equals("1"))// 礼包领取状态 0未领取，1已领取
		{
			viewHolder.btnReceivePack.setEnabled(false);
			viewHolder.btnReceivePack.setText("已领取");
			viewHolder.btnReceivePack
					.setBackgroundResource(Resources.getDrawableId(activity, "mch_cricle_5dp_bg_gray"));
		}
		viewHolder.tvPackDesc.setText(packInfo.getPackDesc());

		viewHolder.btnReceivePack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewHolder.setPackInfo(packInfo);
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView ivPackImage;
		TextView tvPackName;
		TextView tvPackEffective;
		TextView tvPackDesc;
		Button btnReceivePack;

		@SuppressLint("NewApi")
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case Constant.PACKS_CODE_FAIL:
					String codeerror = (String) msg.obj;
					Toast.makeText(activity, codeerror,Toast.LENGTH_LONG).show();
					break;
				case Constant.PACKS_CODE_SUCCESS:
					PackCodeEntity packCode = (PackCodeEntity) msg.obj;
					new ReceivePackDialog.Builder().setPackName(packCode.getPackName())
							.setPackCode(packCode.getNovice()).setPackStatus(packCode.getReceiveStatus())
							.show(activity, activity.getFragmentManager());

					btnReceivePack.setEnabled(false);
					btnReceivePack.setText("已领取");
					btnReceivePack.setBackgroundResource(Resources.getDrawableId(activity, "sdk_cricle5_bg_gray"));
					break;
				}
			}
		};

		public void setPackInfo(GamePackInfo packInfo) {
			PacksCodeProcess packCode = new PacksCodeProcess();
			packCode.setGiftId(packInfo.getId());
			packCode.setPackName(packInfo.getPackName());
			packCode.post(handler);
		}
	}

}
