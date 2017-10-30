package com.ehl.itspark.common;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;



public class AlipayPayment {
	
	/*
	 * APP支付
	 */
    public String appPay(String paidMoney, String detail, String outTradeNo){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
        		AlipayConfigUtil.CHARSET, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数
        //以下方法为sdk的model入参方式（model和biz_content同时存在的情况下取biz_content）
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        //model.setPassbackParams("APP支付");  //附加数据，此数据将在回调通知时原样返回
        model.setBody("APP支付");//交易类别
        model.setSubject(detail); //商品标题
        model.setOutTradeNo(outTradeNo); //商家订单编号
        model.setTimeoutExpress("30m"); //超时关闭该订单时间
        model.setTotalAmount(paidMoney);  //订单总金额
        model.setProductCode("QUICK_MSECURITY_PAY"); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfigUtil.NOTIFY_URL);  //回调地址
        String orderStr = "";
        String signedOrder = "";
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println("appPay->result is " + response.getCode());
            if(response.isSuccess()){
                orderStr = response.getTradeNo();
                System.out.println("appPay->orderStr is " + orderStr);
                signedOrder = response.getBody();
                System.out.println("appPay->signedOrder is " + signedOrder);
            }
       } catch (AlipayApiException e) {
            e.printStackTrace();
       }
        return signedOrder;
    }
    
    /*
     * 扫码支付
     */
    public String scanPay(String paidMoney, String detail, String outTradeNo){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
        		AlipayConfigUtil.CHARSET, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
       request.setBizContent("{" +
        		"\"out_trade_no\":"  +  "\"" + outTradeNo + "\","   +
        		"\"subject\":" + "\"" + detail + "\"," +
        		"\"body\":" + "\"" + "扫码支付" + "\"," +
        		"\"store_id\":" + "\"" + AlipayConfigUtil.STORE_ID + "\"," +
        		"\"timeout_express\":\"30m\"," +
        		"\"total_amount\":" + "\"" + paidMoney + "\"" +
        		"  }"); //设置业务参数
        request.setNotifyUrl(AlipayConfigUtil.NOTIFY_URL);  //回调地址      
        String codeUrl = "";
        try {
        	AlipayTradePrecreateResponse response = alipayClient.execute(request);
            System.out.println("scanPay->result is " + response.getQrCode());
            System.out.println("scanPay->reponse body is " + response.getParams().toString());
            if(response.isSuccess()){
            	codeUrl = response.getQrCode();
                System.out.println("scanPay->codeUrl is " + codeUrl);
            }
       } catch (AlipayApiException e) {
            e.printStackTrace();
       }
        return codeUrl;
    }
    
    /*
     * 条码支付
     */
    public void codePay(String paidMoney, String detail, String outTradeNo,String authCode) throws Exception{
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
        		AlipayConfigUtil.CHARSET, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setNotifyUrl(AlipayConfigUtil.NOTIFY_URL);
        request.setBizContent("{" +
        		"\"out_trade_no\":"  +  "\"" + outTradeNo + "\","   +
        		"\"scene\":\"bar_code\"," + 
        		"\"auth_code\":" + "\"" + authCode + "\"," +
        		"\"subject\":" + "\"" + detail + "\"," +
        		"\"body\":" + "\"" + "条码支付" + "\"," +
        		"\"store_id\":" + "\"" + AlipayConfigUtil.STORE_ID + "\"," +
        		"\"timeout_express\":\"2m\"," +
        		"\"total_amount\":" + "\"" + paidMoney + "\"" +
        		"  }"); //设置业务参数
        try {
        	AlipayTradePayResponse response = alipayClient.execute(request);
            System.out.println("codePay->tradeNo is " + response.getTradeNo() + " and outTradeNo is " + outTradeNo);
            System.out.println("codePay->reponse params is " + response.getParams().toString());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } 
    }
    
    /*
     * 网站支付,生成表单
     */
    public String webPay(String outTradeNo,BigDecimal amount,String subject,String body) throws Exception{
        AlipayClient alipayClient=new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
              AlipayConfigUtil.CHARSET, AlipayConfigUtil.APP_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        AlipayTradePagePayRequest alipayRequest=new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfigUtil.PAY_RETURN_URL);
        alipayRequest.setNotifyUrl(AlipayConfigUtil.NOTIFY_URL);
        alipayRequest.setBizContent("{" +
              "\"out_trade_no\":"  +  "\"" + outTradeNo + "\","   +
              "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
              "\"total_amount\":\"" +  amount.setScale(2, BigDecimal.ROUND_HALF_UP) + "\"," +
              "\"subject\":" + "\"" + subject + "\"," +
              "\"body\":" + "\"" + body + "\"," +
              "\"passback_params\":\""+URLEncoder.encode("对账补缴支付", "UTF-8")+"\"" +
              "  }");
        String form="";
      form=alipayClient.pageExecute(alipayRequest).getBody();//调用SDK生成表单
        return form;
      }
    
    public boolean rsaCheck(Map<String,String> params) throws Exception{
    	try {
			return AlipaySignature.rsaCheckV1(params, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.CHARSET, AlipayConfigUtil.SIGN_TYPE);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			throw e;
		} //调用SDK验证签名
    }
    
    /*
     * 支付结果查询
     */
    public void query(String type, Integer paymentPattern, String outTradeNo,String tradeNo){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
        		AlipayConfigUtil.CHARSET, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setNotifyUrl(AlipayConfigUtil.NOTIFY_URL);
        request.setBizContent("{" +
        		"\"out_trade_no\":"  +  "\"" + outTradeNo + "\","   +
        		"\"trade_no\":" + "\"" +  tradeNo +"\"" +
        		"  }"); //设置业务参数
        try {
        	boolean booleanQuery = false;
            int queryCount = 6;
        	while(queryCount-->0){
            	AlipayTradeQueryResponse response = alipayClient.execute(request);
                System.out.println("query->tradeNo is " + response.getTradeNo() + " and outTradeNo is " + outTradeNo);
                System.out.println("query->reponse params is " + response.getParams().toString());
                System.out.println("query->tradeStatus is " + response.getTradeStatus());
            	if("10000".equals(response.getTradeStatus())){//用户支付成功
            		queryCount = 0;
            		booleanQuery = true;
            	}else if("10003".equals(response.getTradeStatus())){//等待用户支付
            		queryCount = queryCount - 1;
            	}else if("10004".equals(response.getTradeStatus())){//用户支付失败
            		queryCount = 0;
            	}
            	if(queryCount>0){
                    Thread.sleep(5000);
            	}
        	}
            if(!booleanQuery && queryCount==0){
        		cancel(outTradeNo,tradeNo);
            }
            if(booleanQuery){
            	//此处考虑在服务端未能接收支付宝回调时触发回调
            }
        } catch (AlipayApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 取消支付订单
     */
    public void cancel(String outTradeNo,String tradeNo){
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfigUtil.PAY_URL, AlipayConfigUtil.APP_ID, AlipayConfigUtil.APP_PRIVATE_KEY , AlipayConfigUtil.FORMAT, 
        		AlipayConfigUtil.CHARSET, AlipayConfigUtil.APP_PUBLIC_KEY, AlipayConfigUtil.SIGN_TYPE);
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizContent("{" +
        		"\"out_trade_no\":"  +  "\"" + outTradeNo + "\","   +
        		"\"trade_no\":" + "\"" + tradeNo + "\"," +
        		"  }"); //设置业务参数
        try {
        	AlipayTradeCancelResponse response = alipayClient.execute(request);
            System.out.println("cancel->flag is " + response.getRetryFlag() + " action is " + response.getAction());
        } catch (AlipayApiException  e) {
            e.printStackTrace();
        }
    }
}
