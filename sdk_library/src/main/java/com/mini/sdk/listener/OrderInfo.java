package com.mini.sdk.listener;

import java.io.Serializable;

public class OrderInfo  implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 商品名称 */
	private String productName;
	/** 商品价格 单位：分 */
	private int productPrice;
	/** 商品描述 */
	private String productDesc;
	/** 游戏中的交易订单号 */
	private String extendInfo;
	/** 游戏服务器编号 */
	private String gameServerId;
	/** 区服名称 */
	private String serverName;
	/** 角色名称 */
	private String roleName;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getGameServerId() {
		return gameServerId;
	}

	public void setGameServerId(String gameServerId) {
		this.gameServerId = gameServerId;
	}

	private int getAmount() {
		return productPrice;
	}

	public void setAmount(int productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String name) {
		productName = name;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String desc) {
		productDesc = desc;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extend) {
		extendInfo = extend;
	}

	/**
	 * 商品价格Float格式
	 */
	public String getGoodsPriceYuan() {
		return String.format("%.2f", getFloatGoodsPriceYuan());
	}

	public String getGoodsPriceYuanByZK(int zknum) {
		return String.format("%.2f", getFloatGoodsPriceYuan() * zknum * 0.1f);
	}

	/**
	 * 商品价格int格式
	 */
	public float getFloatGoodsPriceYuan() {
		return (float) getAmount() / 100;
	}

	public String getGoodsPriceFen() {
		return getAmount() + "";
	}

}
