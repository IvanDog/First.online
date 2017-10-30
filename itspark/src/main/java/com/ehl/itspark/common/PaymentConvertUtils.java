package com.ehl.itspark.common;

public class PaymentConvertUtils {
	public static String covertToString(Integer payment){
		String type = null;
		switch(payment){
		case 1:
			type = "pos机支付";
			break;
		case 2:
			type = "微信支付";
			break;
		case 3:
			type = "支付宝付";
			break;
		case 4:
			type = "微信扫码支付";
			break;
		case 5:
			type = "支付宝扫码付";
			break;
		case 6:
			type = "微信刷卡支付";
			break;
		case 7:
			type = "支付宝条码付";
			break;
		case 8:
			type = "余额支付";
			break;
		case 9:
			type = "逃费";
			break;
		case 10:
			type = "其他支付";
			break;
		case 0:
			type = "未付";
			break;
		}
		return type;
	}
	
}
