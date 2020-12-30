package com.mini.sdk.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CertificateVpAdapter extends FragmentPagerAdapter {

	private List<Fragment> views;

	public CertificateVpAdapter(FragmentManager fragmentManager, List<Fragment> views) {
		super(fragmentManager);
		this.views = views;
	}

	@Override
	public Fragment getItem(int position) {
		return views.get(position);
	}

	@Override
	public int getCount() {
		return views.size();
	}

}
