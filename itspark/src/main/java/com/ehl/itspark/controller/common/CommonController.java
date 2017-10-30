package com.ehl.itspark.controller.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ehl.itspark.common.AlipayConfigUtil;
import com.ehl.itspark.common.WechatPayCommonUtil;
import com.ehl.itspark.common.WechatPayConfigUtil;
import com.ehl.itspark.common.XMLUtil;
import com.ehl.itspark.server.CommonServer;


@Controller
@RequestMapping("/common/payment")
public class CommonController {
	
	@Autowired
	private CommonServer commonServer;
	private Logger logger=LoggerFactory.getLogger(CommonController.class);
	@RequestMapping(value = "/wechatNotify", method = RequestMethod.POST)
    public void weixin_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        //读取参数  
        InputStream inputStream ;  
        StringBuffer sb = new StringBuffer();  
        inputStream = request.getInputStream();  
        String s ;  
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        while ((s = in.readLine()) != null){  
            sb.append(s);  
        }  
        in.close();  
        inputStream.close();  
  
        //解析xml成map  
        Map<String, String> map = new HashMap<String, String>();
         map = XMLUtil.doXMLParse(sb.toString());  
          
        //过滤空设置 TreeMap  
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();        
        Iterator it = map.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();  
            String parameterValue = map.get(parameter);               
            String v = "";  
            if(null != parameterValue) {  
                v = parameterValue.trim();  
            }  
            packageParams.put(parameter, v);  
        }  
          
        // 账号信息  
        String key = WechatPayConfigUtil.API_KEY; // key  
        //判断签名是否正确  
        if(WechatPayCommonUtil.isTenpaySign("UTF-8", packageParams,key)) {  
            String resXml = "";  
            if("SUCCESS".equals(packageParams.get("result_code"))){  
                String mch_id = (String)packageParams.get("mch_id");  
                String openid = (String)packageParams.get("openid");  
                String is_subscribe = (String)packageParams.get("is_subscribe");  
                String out_trade_no = (String)packageParams.get("out_trade_no");       
                String total_fee = (String)packageParams.get("total_fee");                   
                String time_end = (String)packageParams.get("time_end");     
                String transaction_id = (String)packageParams.get("transaction_id");  
                String trade_type = (String)packageParams.get("trade_type");  
                int paymentPattern = -1;
                if("APP".equals(trade_type)){
                	paymentPattern = 2;
                }else if("NATIVE".equals(trade_type)){
                	paymentPattern = 4;
                }
                System.out.print("mch_id:"+mch_id + "openid:"+openid +"is_subscribe:"+is_subscribe + "out_trade_no:"+out_trade_no + "total_fee:"+total_fee );
                System.out.print("支付成功");
                commonServer.setResult(null,paymentPattern, out_trade_no,time_end,transaction_id);//暂时将subject置为null
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";           
            } else {  
            	 System.out.print("支付失败,错误信息：" + packageParams.get("err_code"));  
                 resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";  
            }
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());  
            out.write(resXml.getBytes());  
            out.flush();  
            out.close();  
        } else{  
        	 System.out.print("通知签名验证失败");  
        }  
          
    }  
	
	
	@RequestMapping(value = "/alipayNotify", method = RequestMethod.POST)
    public void aliPay_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		Map requestParams = request.getParameterMap();
        System.out.println("支付宝支付结果通知->requestParams: "+requestParams.toString());
        
        //获取支付宝POST过来的反馈信息
        Map<String,String> params = new HashMap<String,String>();     
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
          }
          //以下代码在出现乱码时使用
          //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
          params.put(name, valueStr);
          System.out.println("支付宝支付结果通知->params: "+params.toString());
         }
        try {
        	//判断签名是否正确  
            boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfigUtil.ALIPAY_PUBLIC_KEY, AlipayConfigUtil.CHARSET, AlipayConfigUtil.SIGN_TYPE);
            System.out.println("签名验证->flag is  : " + flag);
            if(flag){
                if("TRADE_SUCCESS".equals(params.get("trade_status"))){
                    //商户订单号
                    String out_trade_no = params.get("out_trade_no");
                    //支付宝交易号
                    String transaction_id = params.get("trade_no");
                    //附加数据
                    String passback_params = null;
                    if(params.get("passback_params")!=null && !"".equals(params.get("passback_params"))){
                        passback_params = URLDecoder.decode(params.get("passback_params"));
                    }
                    //交易类型
                    String body = null;
                    if(params.get("body")!=null && !"".equals(params.get("body"))){
                        body = URLDecoder.decode(params.get("body"));
                    }
                    //交易付款时间
                    String time_end = params.get("gmt_payment");  
                    //商品标题
                    String subject = params.get("subject");
                    int paymentPattern = -1;
                    /*if("对账补缴支付".equals(passback_params)){
                    	String orderNo=subject.split(":")[1];
                    	commonServer.setRechargeResult(orderNo, out_trade_no, transaction_id);
                    }*/
                    if("APP支付".equals(body)){
                    	paymentPattern = 3;
                    }else if("扫码支付".equals(body)){
                    	paymentPattern = 5;
                    }else  if("条码支付".equals(body)){
                    	paymentPattern = 7;
                    }
                    System.out.print("支付成功");
                    commonServer.setResult(subject,paymentPattern, out_trade_no,time_end,transaction_id);
                    response.setContentType("text/html;charset=utf-8"); // 设置响应报文的编码格式  
                    response.getWriter().append("success").flush();//相应“success”即可
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }catch (Exception e) {
			// TODO: handle exception
        	logger.error(e.getMessage(),e);
		}
    }
}
