package com.ehl.itspark.common;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonUtil {

	private static ObjectMapper mapper;

	/**
	 * 获取ObjectMapper实例
	 * 
	 * @param createNew 方式：true，新实例；false,存在的mapper实例
	 * @return
	 */
	public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
		if (createNew) {
			return new ObjectMapper();
		} else if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	/**
	 * 将java对象转换成json字符
	 * 
	 * @param obj 准备转换的对象
	 * @return json字符
	 * @throws Exception
	 */
	public static String beanToJsonByCharset(Object obj) {
		return beanToJson(obj, false, "ISO-8859-1");
	}

	/**
	 * 将java对象转换成json字符
	 * 
	 * @param obj 准备转换的对象
	 * @return json字符
	 * @throws Exception
	 */
	public static String beanToJson(Object obj) {
		return beanToJson(obj, false, null);
	}

	/**
	 * 将java对象转换成json字符
	 * 
	 * @param obj 准备转换的对象
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return json字符
	 * @throws Exception
	 */
	public static String beanToJson(Object obj, boolean createNew, String chartset) {
		try {
			ObjectMapper objectMapper = getMapperInstance(createNew);
			String json = objectMapper.writeValueAsString(obj);
			
			String result = json;
			if(!StringUtils.isEmpty(chartset)){
				result =  new String(json.getBytes("UTF-8"),"ISO-8859-1");
			}
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param json 准备转换的json字符
	 * @param cls 准备转换的类
	 * @return
	 * @throws Exception
	 */
	public static Object jsonToBean(String json, Class<?> cls) {
		try {
			ObjectMapper objectMapper = getMapperInstance(false);
			Object vo = objectMapper.readValue(json, cls);
			return vo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将json字符串转换成java对象
	 * 
	 * @param json 准备转换的json字符
	 * @param cls 准备转换的类
	 * @param createNew ObjectMapper实例方式:true，新实例;false,存在的mapper实例
	 * @return
	 * @throws Exception
	 */
	public static Object jsonToBean(String json, Class<?> cls, Boolean createNew) {
		try {
			ObjectMapper objectMapper = getMapperInstance(createNew);
			Object vo = objectMapper.readValue(json, cls);
			return vo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	 /**
	  * 将json字符串转换成java对象
	  * 
	  * @param json 准备转换的json字符
	  * @param cls 准备转换的类
	  * @return
	  * @throws Exception
	  */
	public static Object jsonToBean(String json, TypeReference<?> t) {
		try {
			ObjectMapper objectMapper = getMapperInstance(false);
			Object vo = objectMapper.readValue(json, t);
			return vo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取泛型的Collection Type
	 * @param jsonStr json字符串
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses 元素类型
	 */
	public static <T> T jsonToBean(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) {

		try {
		       ObjectMapper mapper = new ObjectMapper();

		       JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
		       T vo = mapper.readValue(jsonStr, javaType);
			   return vo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
