package com.ehl.itspark.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigUtil {

	private static final String EMPTY = "";
    public static Map<String, String> allParam = new HashMap<String, String>();

    @SuppressWarnings("rawtypes")
	public  static void init(String confPath) {
        File confFile = new File(confPath);
        if(!confFile.exists()) { 
            throw new RuntimeException("配置文件未找到："+confPath);
        }
        Properties p = new Properties();
        
        try {
        	p.load(new InputStreamReader(new FileInputStream(confFile), "UTF-8"));
            Set keys = p.keySet();
            Iterator it = keys.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                allParam.put(key, p.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    /**
     * @Title: trim 
     * @Description:
     */
    private  static String trim(Object o) {
    	return (o==null)?EMPTY:(o.toString().trim());
    }
    /**
     * @Title: getPropertyValueString 
     * @Description: 获取指定key的值
     */
    public  static String getPropertyValueString(String key) {
        return getPropertyValueString(key, "");
    }
    /**
     * @Title: getPropertyValueString 
     * @Description: 获取指定key的值，如果值不存在则返回默认值
     */
    public  static String getPropertyValueString(String key, String defaultValue) {
        String resString = trim(allParam.get(key));
        return "".equals(resString) ? defaultValue : resString;
    }
    /**
     * @Title: getPropertyValueInt 
     * @Description: 获取指定key的整形值
     */
    public  static int getPropertyValueInt(String key) {
        return getPropertyValueInt(key, 0);
    }
    /**
     * @Title: getPropertyValueInt 
     * @Description: 获取指定key的整形值，如果值不存在则返回默认值
     */
    public  static int getPropertyValueInt(String key, int defaultValue) {
        return Integer.parseInt(getPropertyValueString(key, defaultValue + ""));
    }
    
    /**
     * @Title: getPropertyValueBool 
     * @Description: 获取指定key的布尔值
     */
    public  static boolean getPropertyValueBool(String key) {
        return getPropertyValueBool(key, false);
    }
    /**
     * @Title: getPropertyValueBool 
     * @Description: 获取指定key的布尔值，如果值不存在则返回默认值
     */
    public  static boolean getPropertyValueBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getPropertyValueString(key, defaultValue + ""));
    }
    /**
     * @Title: getParamsSize 
     * @Description: 获取键值对集合大小
     */
    public  static int getParamsSize(){
    	return allParam.size();
    }
    /**
     * @Title: getPropertyStringList 
     * @Description: 获取指定key的包换分隔符值，
     */
    public  static List<String> getPropertyStringList(String key,String separator){
    	List<String> list = new ArrayList<>();
    	String value = getPropertyValueString(key);
    	if(value!=null){
    		for(String field : value.split(separator)){
    			String trimField = trim(field);
    			if(!trimField.equals("")){
    				list.add(trimField);
    			}
    		}
    	}
    	return list;
    }
    /**
     * @Title: addParam 
     * @Description: 添加key-value键值对
     */
    public  static void addParam(String key,String value){
    	allParam.put(key, value);
    }
    /**
     * @Title: getPrefixedProperty 
     * @Description: 获取具有指定前缀key的值
     */
    public  static List<String> getPrefixedProperty(String prefix){
    	List<String> values = new ArrayList<>();
    	Set<String> keySet = allParam.keySet();
    	for(String key : keySet){
    		if(key.startsWith(prefix)){
    			String value = allParam.get(key);
    			String trimValue = trim(value);
    			if(!trimValue.equals("")){
    				values.add(trimValue);
    			}
    		}
    	}
    	return values;
    }
    /**
     * 将指定路径下的配置文件转换成map
     * @param file
     * @return
     */
    public static Map<String,String> readFile2Map(String file){
    	File f = new File(file);
    	return readFile2Map(f);
    }/**
     * 将配置文件转换成map
     * @param file
     * @return
     */
    public static Map<String,String> readFile2Map(File file){
    	
    	if(!file.exists()){
    		throw new RuntimeException("file  not  found "+file.getPath());
    	}
    	Map<String,String> map = new HashMap<>();
    	Properties p = new Properties(); 
    	
    	try ( FileReader fr = new FileReader(file)){
    		p.load(fr);
    		Set keys = p.keySet();
    		Iterator it = keys.iterator();
    		while (it.hasNext()) {
    			String key = (String) it.next();
    			map.put(key, p.getProperty(key).trim());
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    	return map;
    }
}
