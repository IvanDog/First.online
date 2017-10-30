package com.ehl.itspark.data.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ehl.itspark.common.MyBatisRepository;
import com.ehl.itspark.data.entity.MessageEntity;


@MyBatisRepository
public interface MessageDao {

	List<MessageEntity> findAll(Map<String, Object> para);
	
	List<MessageEntity> findRecentMessageIDs(Map<String, Object> para);
	
	int save(MessageEntity entity);
	
	int update(MessageEntity entity);
	
	int editor(MessageEntity entity);
	
	long count(Map<String, Object> para);
	
	int deleteByMessageID(@Param("messageID")String messageID);
}
