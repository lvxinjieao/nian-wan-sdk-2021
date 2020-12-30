package com.mini.sdk.entity;

import java.io.Serializable;

/**
 * 描述：微信支付订单
 */
public class WXOrderInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String tag;
	private String url;
	private String orderNo;
	private String cal_url;

	public String getCal_url() {
		return cal_url;
	}

	public void setCal_url(String cal_url) {
		this.cal_url = cal_url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
