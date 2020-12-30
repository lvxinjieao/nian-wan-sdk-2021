package com.mini.sdk.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class LoncentPagerAdapter extends FragmentPagerAdapter {
	public ArrayList<Fragment> list;

	public LoncentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> list_) {
		super(fragmentManager);
		this.list = list_;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).hashCode();
	}

}
