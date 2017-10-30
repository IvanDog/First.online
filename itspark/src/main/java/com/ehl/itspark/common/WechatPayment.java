package com.ehl.itspark.common;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class WechatPayment {
	
	/*
	 * APP支付、扫码支付
	 */
	public HashMap<String,Object> pay(int paymentPattern,  String ip, int paidMoney, String detail, String tradeNo) throws Exception {  
        // 账号信息  
        String appid = WechatPayConfigUtil.APP_ID;
        //String appsecret = WechatPayConfigUtil.APP_SECRET; 
        String mch_id = WechatPayConfigUtil.MCH_ID; // 商业号  
        String key = WechatPayConfigUtil.API_KEY; // key  
        String currTime = WechatPayCommonUtil.getCurrTime();  
        String strTime = currTime.substring(8, currTime.length());  
        String strRandom = WechatPayCommonUtil.buildRandom(4) + "";  
        String nonce_str = strTime + strRandom;         
        int total_fee = paidMoney; // 价格（注意：价格的单位是分）  
        String body = detail;   // 商品名称  
        String out_trade_no = tradeNo; // 订单号  
          
        // 回调接口   
        String notify_url = WechatPayConfigUtil.NOTIFY_URL;  
        String trade_type = null;
        String spbill_create_ip = null;// 获取发起电脑IP 
        if(paymentPattern==2){
            trade_type = "APP";  
            spbill_create_ip = ip;  
        }else if(paymentPattern==4){
            trade_type = "NATIVE";  
            spbill_create_ip = WechatPayConfigUtil.CREATE_IP;  
        }
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
        packageParams.put("appid", appid);  
        packageParams.put("mch_id", mch_id);  
        packageParams.put("nonce_str", nonce_str);  
        packageParams.put("body", body);  
        packageParams.put("out_trade_no", out_trade_no);  
        packageParams.put("total_fee", String.valueOf(total_fee));  
        packageParams.put("spbill_create_ip", spbill_create_ip);  
        packageParams.put("notify_url", notify_url);  
        packageParams.put("trade_type", trade_type);  
  
        String sign = WechatPayCommonUtil.createSign("UTF-8", packageParams,key);  
        packageParams.put("sign", sign);  
          
        String requestXML = WechatPayCommonUtil.getRequestXml(packageParams);  
        System.out.println(requestXML);  
        String prepay_id = null;  
        String urlCode = null;   
        try{
        	String resXml = HttpUtil.postData(WechatPayConfigUtil.COMMON_PAY_URL, requestXML); 
            Map returnMap = XMLUtil.doXMLParse(resXml); 
            prepay_id = (String) returnMap.get("prepay_id");  
            urlCode = (String) returnMap.get("code_url");   
        }catch (ConnectException ce){
            System.out.println("连接超时："+ce.getMessage());
        }catch (Exception e){
            System.out.println("https请求异常："+e.getMessage());
        }
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        if(paymentPattern==2){//微信支付
        	resultMap.put("appid", appid);
        	resultMap.put("partnerid", mch_id);
        	resultMap.put("prepayid", prepay_id);
        	resultMap.put("package", "Sign=WXPay");
        	resultMap.put("noncestr", nonce_str);
        	resultMap.put("timestamp", currTime);
        	resultMap.put("sign", sign);
        }else if(paymentPattern==4){//微信扫码支付
        	resultMap.put("code_url", urlCode);
        }
        return resultMap;
    }
	
	
	/*
	 * 刷卡支付
	 */
	public void pay(int paymentPattern,  String ip, int paidMoney, String detail, String tradeNo ,String authCode) throws Exception {  
        // 账号信息  
        String appid = WechatPayConfigUtil.APP_ID;
        String mch_id = WechatPayConfigUtil.MCH_ID; // 商业号  
        String key = WechatPayConfigUtil.API_KEY; // key  
        String currTime = WechatPayCommonUtil.getCurrTime();  
        String strTime = currTime.substring(8, currTime.length());  
        String strRandom = WechatPayCommonUtil.buildRandom(4) + "";  
        String nonce_str = strTime + strRandom;         
        int total_fee = paidMoney; // 价格（注意：价格的单位是分）  
        String body = detail;   // 商品名称  
        String out_trade_no = tradeNo; // 订单号  
        String anth_code = authCode;
        String spbill_create_ip = WechatPayConfigUtil.CREATE_IP;// 获取发起电脑IP 
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
        packageParams.put("appid", appid);  
        packageParams.put("mch_id", mch_id);  
        packageParams.put("nonce_str", nonce_str);  
        packageParams.put("body", body);  
        packageParams.put("out_trade_no", out_trade_no);  
        packageParams.put("total_fee", String.valueOf(total_fee));  
        packageParams.put("spbill_create_ip", spbill_create_ip);  
        packageParams.put("auth_code", anth_code);  
  
        String sign = WechatPayCommonUtil.createSign("UTF-8", packageParams,key);  
        packageParams.put("sign", sign);  
          
        String requestXML = WechatPayCommonUtil.getRequestXml(packageParams);  
        System.out.println(requestXML);  
        String resXml = null;
        try{
            resXml = HttpUtil.postData(WechatPayConfigUtil.MICRO_PAY_URL, requestXML);  
            Map returnMap = XMLUtil.doXMLParse(resXml);
            System.out.println(returnMap.get("result_code"));
        }catch (ConnectException ce){
            System.out.println("连接超时："+ce.getMessage());
        }catch (Exception e){
            System.out.println("https请求异常："+e.getMessage());
        }
        int queryCount = 10;
        boolean booleanquery = false;
        while(queryCount>0){
            if(orderQuery(packageParams)!=true && queryCount>0){
            	queryCount = queryCount - 1;
            }else{
            	queryCount = 0;
                booleanquery = true;
            } 
            Thread.sleep(500);
        }
        if(!booleanquery && queryCount==0){
            orderCancel(packageParams);
        }else if(booleanquery){
            try{
        	    HttpUtil.postData(WechatPayConfigUtil.NOTIFY_URL, resXml); 
            }catch (Exception e){
                System.out.println("https请求异常："+e.getMessage());
            }
        }
    }
	
	
	/*
	 * 查询支付结果（刷卡支付）
	 */
	public boolean orderQuery(SortedMap<Object,Object> packageParams) throws Exception {
        int successCode = 0;  //"1"表示成功，“0”代表失败
        String requestXML = WechatPayCommonUtil.getRequestXml(packageParams);
        Map returnMap = null;
        try{
            String resXml = HttpUtil.postData(WechatPayConfigUtil.QUERY_URL, requestXML);
            returnMap = XMLUtil.doXMLParse(resXml);
            System.out.println("return_code is " + returnMap.get("return_code") + " result_code is " + returnMap.get("result_code")
                                                 + " trade_state is " + returnMap.get("trade_state"));
        }catch (ConnectException ce){
            System.out.println("连接超时："+ce.getMessage());
        }catch (Exception e){
            System.out.println("https请求异常："+e.getMessage());
        }
        if(returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")){
            if(returnMap.get("trade_state").equals("SUCCESS")){
            	successCode = 1;
            }else if(returnMap.get("trade_state").equals("USERPAYING")){
            	successCode = 0;
            }
        }else{
        	successCode = 0;
        }
        if(successCode == 1){
            return true;
        }else{
            return false;
        }
    }
	
	
	/*
	 *取消支付（刷卡支付）
	 */
	public void orderCancel(SortedMap<Object,Object> packageParams) throws Exception {
        String requestXML = WechatPayCommonUtil.getRequestXml(packageParams);
        System.out.println("Cancel requestXml is " + requestXML);
        ClientCustomSSL clientCustomSSL = new ClientCustomSSL();
        clientCustomSSL.reverse(requestXML);
    }
}
