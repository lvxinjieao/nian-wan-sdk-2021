package com.mini.sdk.observer;

public interface TimeChange {

	public void addWatcher(SecondsWatcher seconds);

	public void removeWatcher(SecondsWatcher seconds);

	public void noticeCurrentSeconds(String seconds);

	public void removeAllWatcher();
}
