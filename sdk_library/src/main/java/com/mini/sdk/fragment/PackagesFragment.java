package com.mini.sdk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.mini.sdk.PluginApi;
import com.mini.sdk.activity.FloatWebActivity;
import com.mini.sdk.adapter.PackagesAdapter;
import com.mini.sdk.bean.UserReLogin;
import com.mini.sdk.bean.UserReLogin.ReLoginCallback;
import com.mini.sdk.entity.GamePackInfo;
import com.mini.sdk.entity.PacksInfo;
import com.mini.sdk.http.process.GamePacksListProcess;
import com.mini.sdk.utils.BitmapHelp;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.view.xlist.XListView;
import com.mini.sdk.view.xlist.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

public class PackagesFragment extends Fragment implements IXListViewListener {

    public FloatWebActivity activity;
    public View contentView;
    public static Handler handler;

    public XListView xListView;// 显示数据
    public PackagesAdapter adapter;// 适配器

    /**
     * 图片加载
     */
    BitmapUtils bitmapUtils;
    /**
     * 礼包列表
     */
    private List<GamePackInfo> packList = new ArrayList<GamePackInfo>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.GET_PACKS_LIST_SUCCESS:

                    PacksInfo packsInfo = (PacksInfo) msg.obj;
                    if (null != packsInfo.getPackInfoList() && packsInfo.getPackInfoList().size() > 0) {
                        packList.addAll(packsInfo.getPackInfoList());
                        adapter.notifyDataSetChanged();
                        xListView.invalidate();
                        xListView.setSelection(adapter.getCount() - 1);
                    } else {
                        // 没有其它礼包了/
                    }
                    break;
                case Constant.GET_PACKS_LIST_FAIL:
                    Logs.w("error:" + (String) msg.obj);
                    Toast.makeText(activity, "没有其它礼包了", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (FloatWebActivity) getActivity();
        handler = activity.handler;
        contentView = inflater.inflate(Resources.getLayoutId(activity, "activity_packages"), container, false);

        initUI(contentView);
        initData();
        return contentView;
    }

    private void initUI(View contentView) {
        bitmapUtils = BitmapHelp.getBitmapUtils(activity.getApplicationContext());

        TextView title = (TextView) contentView.findViewById(Resources.getId(activity, "title"));
        title.setText("礼包中心");

        TextView back = (TextView) contentView.findViewById(Resources.getId(activity, "back"));
        back.setOnClickListener(backClick);

        xListView = (XListView) contentView.findViewById(Resources.getId(activity, "xListView"));

        adapter = new PackagesAdapter(activity, activity.getApplicationContext(), packList, bitmapUtils, mHandler);
        xListView.setAdapter(adapter);

        xListView.setPullLoadEnable(false);
        xListView.setPullRefreshEnable(false);
        xListView.setXListViewListener(this);
        xListView.pullRefreshing();
        xListView.setDividerHeight(20);
    }

    private void initData() {

        if (TextUtils.isEmpty(PluginApi.userLogin.getAccountId())) {
            UserReLogin reLogin = new UserReLogin(activity);
            reLogin.userToLogin(new ReLoginCallback() {

                @Override
                public void reLoginResult(boolean res) {
                    if (res) {
                        queryPacksList();
                    } else {
                        Toast.makeText(activity, "获取礼包失败", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return;
        }
        queryPacksList();
    }

    private void queryPacksList() {
        new GamePacksListProcess().post(mHandler);
    }


    OnClickListener backClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            handler.sendEmptyMessage(1);
        }
    };

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                xListView.stopRefresh();
            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {

    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handler.sendEmptyMessage(1);
        }
        return false;
    }

}
