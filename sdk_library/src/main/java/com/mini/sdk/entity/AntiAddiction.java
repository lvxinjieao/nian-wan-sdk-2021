package com.mini.sdk.entity;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AntiAddiction {

	/**
	 * status : 1 data :
	 * {"hours":"2","contents_off":"未实名认证提示内容","hours_off_one":"3","hours_off_two":"2","contents_one":"第一次提示内容","contents_two":"第二次提示内容","bat":"0","hours_cover":"2","name":"age_prevent","on-off":"0","age_status":"0"}
	 */

	/**
	 * hours : 2 contents_off : 未实名认证提示内容 hours_off_one : 3 hours_off_two : 2
	 * contents_one : 第一次提示内容 contents_two : 第二次提示内容 bat : 0 hours_cover : 2 name :
	 * age_prevent on-off : 0 age_status : 0
	 */

	// 未实名认证提示时间
	private String hours;
	// 未实名认证提示内容
	private String contents_off;
	// 未满18周岁防沉迷时间第一次
	private String hours_off_one;
	// 未满18周岁放沉迷时间第二次
	private String hours_off_two;
	// 第一次提示内容
	private String contents_one;
	private String contents_two;
	// 是否强制下线0是 1 否
	private String bat;
	// 恢复时间
	private String hours_cover;

	private String name;
	// 实名认证开关0是1否
	private String onoff;

	// -1该用户ID不存在 0未审核 (1审核未通过) 2 审核通过成年 3通过未成年
	private int age_status;

	// 游戏时间
	private int play_time;
	// 下线时间
	private int down_time;

	public int getPlay_time() {
		return play_time;
	}

	public void setPlay_time(int play_time) {
		this.play_time = play_time;
	}

	public int getDown_time() {
		return down_time;
	}

	public void setDown_time(int down_time) {
		this.down_time = down_time;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getContents_off() {
		return contents_off;
	}

	public void setContents_off(String contents_off) {
		this.contents_off = contents_off;
	}

	public String getHours_off_one() {
		return hours_off_one;
	}

	public void setHours_off_one(String hours_off_one) {
		this.hours_off_one = hours_off_one;
	}

	public String getHours_off_two() {
		return hours_off_two;
	}

	public void setHours_off_two(String hours_off_two) {
		this.hours_off_two = hours_off_two;
	}

	public String getContents_one() {
		return contents_one;
	}

	public void setContents_one(String contents_one) {
		this.contents_one = contents_one;
	}

	public String getContents_two() {
		return contents_two;
	}

	public void setContents_two(String contents_two) {
		this.contents_two = contents_two;
	}

	public String getBat() {
		return bat;
	}

	public void setBat(String bat) {
		this.bat = bat;
	}

	public String getHours_cover() {
		return hours_cover;
	}

	public void setHours_cover(String hours_cover) {
		this.hours_cover = hours_cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOnoff() {
		return onoff;
	}

	public void setOnoff(String onoff) {
		this.onoff = onoff;
	}

	public int getAge_status() {
		return age_status;
	}

	public void setAge_status(int age_status) {
		this.age_status = age_status;
	}
}
