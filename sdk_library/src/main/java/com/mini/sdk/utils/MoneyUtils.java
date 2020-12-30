package com.mini.sdk.utils;

import java.math.BigDecimal;

public class MoneyUtils {

	public static float priceToFloat(String price) {
		float p = 0;
		try {
			p = Float.parseFloat(price);
		} catch (NumberFormatException e) {
			Logs.e("fun#priceToFloat NumberFormatException:" + e);
		}
		return p;
	}

	 
	public static double getCeil(double d, int n) {
		BigDecimal b = new BigDecimal(String.valueOf(d));
		b = b.divide(BigDecimal.ONE, n, BigDecimal.ROUND_CEILING);
		return b.doubleValue();
	}

 
	public static String real(String old, String discount) {
		try {
			Double d1 = Double.parseDouble(old);
			Double d2 = Double.parseDouble(discount);
			Double d3 = d2 * d1;
			String real = (d3 / 10) + "";
			return real;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
 
	public static String re(String old, String discount) {
		try {
			String real = real(old, discount);
			Double dreal = Double.parseDouble(real);
			dreal = getCeil(dreal, 2);
			return dreal.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
