package com.mini.sdk.view;

import java.util.List;

import com.mini.sdk.callback.PopWindowClearCallback;
import com.mini.sdk.entity.User;
import com.mini.sdk.utils.Resources;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 描述：自定义PopupWindow 主要用来显示ListView
 */
public class SpinerPopWindow extends PopupWindow {
	private LayoutInflater inflater;
	private ListView mListView;
	private List<User> list;
	private MyAdapter mAdapter;
	private Context context;

	public SpinerPopWindow(Context context, List<User> list, AdapterView.OnItemClickListener clickListener,
			PopWindowClearCallback popWindowClearCallback) {
		super(context);
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		init(clickListener, popWindowClearCallback);
	}

	private void init(AdapterView.OnItemClickListener clickListener, PopWindowClearCallback popWindowClearCallback) {
		View view = inflater.inflate(Resources.getLayoutId(context, "sdk_spiner_window"), null);
		setContentView(view);

		setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
		mListView = (ListView) view.findViewById(Resources.getId(context, "sdk_spiner_window"));
		mListView.setAdapter(mAdapter = new MyAdapter(popWindowClearCallback));
		mListView.setOnItemClickListener(clickListener);
	}

	private class MyAdapter extends BaseAdapter {
		private PopWindowClearCallback popWindowClearCallback;

		public MyAdapter(PopWindowClearCallback popWindowClearCallback) {
			this.popWindowClearCallback = popWindowClearCallback;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position).getAccount();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(Resources.getLayoutId(context, "sdk_spiner_window_item"), null);
				holder.tvName = (TextView) convertView.findViewById(Resources.getId(context, "tv_mch_spiner_window"));
				holder.rlClear = (RelativeLayout) convertView.findViewById(Resources.getId(context, "rl_mch_spiner_window_clear"));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvName.setText(getItem(position).toString());
			holder.rlClear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (popWindowClearCallback != null) {
						popWindowClearCallback.reslut(position);
					}
				}
			});
			return convertView;
		}
	}

	private class ViewHolder {
		private TextView tvName;
		private RelativeLayout rlClear;
	}

}
