package com.ehl.itspark.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtils {

	private static final int connectTimeout = 1000 * 60;/* 连接超时时间 */
    private static final int socketTimeout = 1000 * 180;/* 读取数据超时时间 */
     /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
public static String sendPost(String urlStr, String content) {
    URL url = null;
    HttpURLConnection connection = null;
    try {
        url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
        connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式  
        connection.connect();
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码  
        out.append(content);  
        out.flush();  
        out.close(); 
 
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));
 
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        String result = buffer.toString();
        return result;
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
    return null;
 }

public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
	String result = null;
	StringBuffer buffer = new StringBuffer();
	try {
		URL url = new URL(requestUrl);
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

		// 设置通用的请求属性
		httpUrlConn.setRequestProperty("accept", "*/*");
		httpUrlConn.setRequestProperty("connection", "Keep-Alive");
		httpUrlConn.setRequestProperty("Charset", "utf-8");

		httpUrlConn.setDoOutput(true);
		httpUrlConn.setDoInput(true);
		httpUrlConn.setUseCaches(false);

		// 设置请求方式（GET/POST）
		httpUrlConn.setRequestMethod(requestMethod);

		if ("GET".equalsIgnoreCase(requestMethod))
			httpUrlConn.connect();

		// 当有数据需要提交时
		if (null != outputStr) {
			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 注意编码格式，防止中文乱码
			outputStream.write(outputStr.getBytes("UTF-8"));
			outputStream.close();
		}

		// 将返回的输入流转换成字符串
		InputStream inputStream = httpUrlConn.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// 释放资源
		inputStream.close();
		inputStream = null;
		httpUrlConn.disconnect();
		result = buffer.toString();
		// jsonObject = JSONObject.fromObject(buffer.toString());
	} catch (ConnectException ce) {
		ce.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}
}
