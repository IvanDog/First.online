package com.ehl.itspark.common;

import java.io.File;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil {

	private static CacheManager cacheManager=new CacheManager(EhcacheUtil.class.getClassLoader().getResource("").getPath()+"config"+File.separator+"ehcache.xml");
	private static Cache cache=null;
	private static Cache getCacheInstance(){
		if(cache==null){
			synchronized (EhcacheUtil.class) {
				if(cache==null){
					cache=cacheManager.getCache("appCache");
				}
			}
		}
		return cache;
	}
	
	//方便调试，暂时置为true
	public static synchronized boolean isValidKey(String key){
		return true;
		//return getCacheInstance().isElementInMemory(key)&&!getCacheInstance().isExpired(getCacheInstance().get(key));
	}
	
	public static synchronized String getKey(String value){
		List<String> keys=getCacheInstance().getKeys();
		for (String key : keys) {
			if(value.equals(getValue(key))){
				return key;
			}
		}
		return null;
	}
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public static synchronized String getValue(String key){
		Element element =getCacheInstance().get(key);
		if(element==null){
			return null;
		}
		return (String)element.getObjectValue();
	}
	/**
	 * 添加缓存信息
	 * @param key
	 * @param value
	 */
	public static synchronized void  putKeyValue(String key,String value){
		Element element=new Element(key, value);
		getCacheInstance().put(element);
	}
	
	public static synchronized void removeKeyValue(String key){
		getCacheInstance().remove(key);
	}
}
