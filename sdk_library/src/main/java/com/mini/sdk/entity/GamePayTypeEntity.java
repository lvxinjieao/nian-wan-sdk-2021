package com.mini.sdk.entity;

public class GamePayTypeEntity {

	private boolean isHaveZFB;
	private boolean isHaveWX;
	private boolean isHaveJBY;
	private boolean isHaveHFB;
	private boolean isHaveJFT;

	public GamePayTypeEntity() {
		isHaveZFB = false;
		isHaveWX = false;
		isHaveJBY = false;
		isHaveHFB = false;
		isHaveJFT = false;
	}

	public boolean isHaveZFB() {
		return isHaveZFB;
	}

	public void setHaveZFB(boolean haveZFB) {
		isHaveZFB = haveZFB;
	}

	public boolean isHaveWX() {
		return isHaveWX;
	}

	public void setHaveWX(boolean haveWX) {
		isHaveWX = haveWX;
	}

	public boolean isHaveJBY() {
		return isHaveJBY;
	}

	public void setHaveJBY(boolean haveJBY) {
		isHaveJBY = haveJBY;
	}

	public boolean isHaveHFB() {
		return isHaveHFB;
	}

	public void setHaveHFB(boolean haveHFB) {
		isHaveHFB = haveHFB;
	}

	public boolean isHaveJFT() {
		return isHaveJFT;
	}

	public void setHaveJFT(boolean haveJFT) {
		isHaveJFT = haveJFT;
	}

	@Override
	public String toString() {
		return "GamePayTypeEntity{" + "isHaveZFB=" + isHaveZFB + ", isHaveWX=" + isHaveWX + ", isHaveJBY=" + isHaveJBY
				+ ", isHaveHFB=" + isHaveHFB + ", isHaveJFT=" + isHaveJFT + '}';
	}
}
