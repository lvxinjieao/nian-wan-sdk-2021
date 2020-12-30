package com.mini.sdk.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.sdk.PluginApi;
import com.mini.sdk.bean.ServiceModel;
import com.mini.sdk.bean.UserReLogin;
import com.mini.sdk.bean.UserReLogin.ReLoginCallback;
import com.mini.sdk.entity.UserLogin;
import com.mini.sdk.fragment.CertificateNoFragment;
import com.mini.sdk.fragment.CertificateOKFragment;
import com.mini.sdk.fragment.ChangePasswordFragment;
import com.mini.sdk.fragment.PackagesFragment;
import com.mini.sdk.fragment.SecurityFragment;
import com.mini.sdk.fragment.SettingsFragment;
import com.mini.sdk.http.process.UserInfoProcess;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.Resources;
import com.mini.sdk.view.LoncentPagerAdapter;
import com.mini.sdk.view.LoncentViewPager;

import java.util.ArrayList;

public class FloatWebActivity extends FragmentActivity {

    private TextView title;

    private RelativeLayout user_info;
    private TextView coin_bind_balance;
    private TextView coin_balance;

    private GridView grid_view;
    private GridViewAdapter gridViewAdapter;

    private LoncentViewPager fragment_pages;
    private LoncentPagerAdapter pager_adapter;
    private ArrayList<Fragment> fragments;
    public Integer page_num = 0;

    private int[] imgs;
    private String[] tvs;

    private static final int HIDE_VIEW = 0;
    private static final int SHOW_VIEW = 1;

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case HIDE_VIEW:
                    user_info.setVisibility(View.GONE);
                    grid_view.setVisibility(View.GONE);
                    fragment_pages.setVisibility(View.VISIBLE);
                    fragment_pages.setCurrentItem(page_num);
                    break;

                case SHOW_VIEW:
                    fragment_pages.setVisibility(View.GONE);
                    user_info.setVisibility(View.VISIBLE);
                    grid_view.setVisibility(View.VISIBLE);
                    break;

                /////////////////////////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////
                case Constant.GET_USER_INFO_SUCCESS:// 获取用户信息
                    UserLogin info = (UserLogin) msg.obj;
                    if (null != info) {
                        handlerUser(info);
                    } else {
                        Toast.makeText(FloatWebActivity.this, "请重新登录", Toast.LENGTH_LONG).show();
                        FloatWebActivity.this.finish();
                    }
                    break;
                case Constant.GET_USER_INFO_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(FloatWebActivity.this, message, Toast.LENGTH_LONG).show();
                    FloatWebActivity.this.finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(true);
        setContentView(Resources.getLayoutId(this, "float_webview_layout"));

        tvs = new String[]{"安全设置", "修改密码", "实名认证", "游戏礼包", "关于设置", "联系客服"};
        imgs = new int[]{Resources.getDrawableId(this, "float_icon_safety"),
                Resources.getDrawableId(this, "float_icon_change_password"),
                Resources.getDrawableId(this, "float_icon_realname"),
                Resources.getDrawableId(this, "float_icon_packages"),
                Resources.getDrawableId(this, "float_icon_settings"),
                Resources.getDrawableId(this, "float_icon_service")};

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        int orientation = this.getResources().getConfiguration().orientation;
        WindowManager.LayoutParams p = getWindow().getAttributes();

        // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 1.0); // 高度设置为屏幕的1.0
        if (Configuration.ORIENTATION_PORTRAIT == orientation) {
            p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        } else {
            p.width = (int) (d.getWidth() * 0.45); // 宽度设置为屏幕的0.4
        }

        p.alpha = 0.95f; // 设置本身透明度
        p.dimAmount = 0.95f; // 设置黑暗度
        getWindow().setAttributes(p); // 设置生效
        getWindow().setGravity(Gravity.LEFT);

        fragment_pages = (LoncentViewPager) findViewById(Resources.getId(this, "fragment_pages"));

        initUI();
        initData();
    }

    private void initData() {

        if (TextUtils.isEmpty(PluginApi.userLogin.getAccountId())) {
            UserReLogin reLogin = new UserReLogin(FloatWebActivity.this);

            reLogin.userToLogin(new ReLoginCallback() {

                @Override
                public void reLoginResult(boolean res) {
                    if (res) {
                        quereUser();
                    } else {
                        Toast.makeText(FloatWebActivity.this, "获取个人信息失败", Toast.LENGTH_LONG).show();
                        FloatWebActivity.this.finish();
                    }
                }
            });
            return;
        }
        quereUser();

        fragments = new ArrayList<Fragment>();
        Fragment security = new SecurityFragment();
        Fragment changePassword = new ChangePasswordFragment();
        Fragment certification_ok = new CertificateOKFragment();
        Fragment certification_no = new CertificateNoFragment();
        Fragment packages = new PackagesFragment();
        Fragment settings = new SettingsFragment();

        fragments.add(security);
        fragments.add(changePassword);
        if (type == 2) {
            fragments.add(certification_ok);
        } else {
            fragments.add(certification_no);
        }
        fragments.add(packages);
        fragments.add(settings);

        pager_adapter = new LoncentPagerAdapter(getSupportFragmentManager(), fragments);
        fragment_pages.setCurrentItem(0);
        fragment_pages.setOffscreenPageLimit(5);
        fragment_pages.setAdapter(pager_adapter);
        fragment_pages.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                fragment_pages.setCurrentItem(page_num, true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    private void quereUser() {
        UserInfoProcess userInfoProcess = new UserInfoProcess();
        userInfoProcess.setType("0");
        userInfoProcess.setAccount("");
        userInfoProcess.post(handler);
    }

    public String age_status;
    public int type = 1;// 标识是否实名认证 -1 该用户ID不存在 0该用户身份未审核 1 该用户身份审核未通过 2该用户身份审核通过成年 3该用户身份审核通过未成年

    protected void handlerUser(UserLogin info) {
        PluginApi.userLogin.setNikeName(info.getNikeName());
        PluginApi.userLogin.setPlatformMoney(info.getPlatformMoney());
        PluginApi.userLogin.setPhoneNumber(info.getPhoneNumber());
        PluginApi.userLogin.setBindPtbMoney(info.getBindPtbMoney());
        PluginApi.userLogin.setAge_status(info.getAge_status());
        PluginApi.userLogin.setIdcard(info.getIdcard());
        PluginApi.userLogin.setReal_name(info.getReal_name());
        age_status = info.getAge_status();

        coin_bind_balance.setText(String.format("%.2f", info.getBindPtbMoney()));// min
        coin_balance.setText(String.format("%.2f", info.getPlatformMoney()));// min

        if (age_status.equals("2")) {// 已认证
            type = 2;
        } else if (age_status.equals("0")) {// 未认证
            type = 0;
        } else if (age_status.equals("1")) {// 未通过
            type = 1;
        } else if (age_status.equals("3")) {// 已认证未满18周岁
            type = 3;//
        } else if (age_status.equals("4")) {// 审核中
            type = 4;
        }
    }

    private void initUI() {
        title = (TextView) findViewById(Resources.getId(this, "title"));
        title.setText(PluginApi.userLogin.getAccount());

        user_info = (RelativeLayout) findViewById(Resources.getId(this, "user_info"));
        coin_bind_balance = (TextView) findViewById(Resources.getId(this, "coin_bind_balance"));
        coin_balance = (TextView) findViewById(Resources.getId(this, "coin_balance"));

        grid_view = (GridView) findViewById(Resources.getId(this, "grid_view"));
        grid_view.setSelector(new ColorDrawable(Color.TRANSPARENT));

        gridViewAdapter = new GridViewAdapter(this, imgs, tvs);
        grid_view.setAdapter(gridViewAdapter);

        grid_view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                page_num = position;
                switch (position) {
                    case 0:
                        handler.sendEmptyMessage(HIDE_VIEW);
                        fragment_pages.setCurrentItem(0, true);
                        break;
                    case 1:
                        handler.sendEmptyMessage(HIDE_VIEW);
                        fragment_pages.setCurrentItem(1, true);
                        break;
                    case 2:
                        handler.sendEmptyMessage(HIDE_VIEW);
                        fragment_pages.setCurrentItem(2, true);
                        break;

                    case 3:
                        handler.sendEmptyMessage(HIDE_VIEW);
                        fragment_pages.setCurrentItem(3, true);
                        break;

                    case 4:
                        handler.sendEmptyMessage(HIDE_VIEW);
                        fragment_pages.setCurrentItem(4, true);
                        break;

                    case 5:
                        ServiceModel.getInstance().contactService(FloatWebActivity.this, true);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        pressKey(keyCode, event);
        return true;
    }

    private void pressKey(int keyCode, KeyEvent event) {
        switch (page_num) {
            case 0:
                SecurityFragment.onKeyDown(keyCode, event);
                break;
            case 1:
                ChangePasswordFragment.onKeyDown(keyCode, event);
                break;
            case 2:
                if (type == 2)
                    CertificateOKFragment.onKeyDown(keyCode, event);
                else
                    CertificateNoFragment.onKeyDown(keyCode, event);
                break;

            case 3:
                PackagesFragment.onKeyDown(keyCode, event);
                break;

            case 4:
                SettingsFragment.onKeyDown(keyCode, event);
                break;
        }
    }

    class GridViewAdapter extends BaseAdapter {

        private Context context;
        private int[] imgs;
        private String[] tvs;

        GridViewAdapter(Context context_, int[] imgs_, String[] tvs_) {
            this.context = context_;
            this.imgs = imgs_;
            this.tvs = tvs_;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                convertView = mInflater.inflate(Resources.getLayoutId(context, "grid_view_item"), null, false);
                viewHolder.iv = (ImageView) convertView.findViewById(Resources.getId(context, "iv"));
                viewHolder.tv = (TextView) convertView.findViewById(Resources.getId(context, "tv"));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iv.setImageResource(imgs[position]);
            viewHolder.tv.setText(tvs[position]);
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}
