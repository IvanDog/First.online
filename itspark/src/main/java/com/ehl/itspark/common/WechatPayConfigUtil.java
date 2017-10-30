package com.ehl.itspark.common;

public class WechatPayConfigUtil {
	public static String APP_ID = "";//微信开放平台审核通过的应用APPID
	public static String APP_SECRET = "";//应用对应的凭证
	public static String MCH_ID= "";//商业号
	public static String API_KEY = "";//API_KEY
	public static String COMMON_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//支付平台服务端接口地址(应用支付、扫码支付)
	public static String MICRO_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";//支付平台服务端接口地址(刷卡支付)
	public static String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";//支付平台服务端接口地址(刷卡支付查询结果)
	public static String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";//支付平台服务端接口地址(取消订单)
	public static String NOTIFY_URL = "http://1747v751y2.51mypc.cn/itspark/common/payment/wechatNotify";//回调地址
	public static String CREATE_IP = "";//发起支付ip(服务端)
	public static String REFUND_URL="https://api.mch.weixin.qq.com/secapi/pay/refund";//微信申请退款地址
	public static String DOWNLOADBILL_URL="https://api.mch.weixin.qq.com/pay/downloadbill";//微信下载对账单地址
}
