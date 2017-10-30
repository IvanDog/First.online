package com.ehl.itspark.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ClientCustomSSL {
public void reverse(String requestXML) throws Exception {
		
		String certpath = ClientCustomSSL.class.getClassLoader().getResource("").getPath()+"apiclient_cert.p12";
		String mchid = "";
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(certpath));
        try {
            keyStore.load(instream, mchid.toCharArray()); //mch_id
        } catch(Exception e){ 
        	e.printStackTrace();
        }  finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
        	HttpPost httpPost = new HttpPost(WechatPayConfigUtil.REVERSE_URL);     	
            System.out.println("executing request" + httpPost.getRequestLine());
            StringEntity  reqEntity  = new StringEntity(requestXML);
            // 设置类型
            reqEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(reqEntity);
           
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        System.out.println(text = new String(text.getBytes("gbk"),"utf-8"));
                    }
                }
                EntityUtils.consume(entity);
            } catch(Exception e){ 
            	e.printStackTrace();
            } finally {
                response.close();
            }
        } catch(Exception e){ 
        	e.printStackTrace();
        } finally {
            httpclient.close();
        }
    }
}
