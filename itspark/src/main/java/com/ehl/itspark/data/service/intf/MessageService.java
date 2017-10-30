package com.ehl.itspark.data.service.intf;

import java.util.Date;
import java.util.List;

import com.ehl.itspark.data.entity.MessageEntity;


public interface MessageService {

	List<MessageEntity> findMessages(MessageEntity message,Date queryTime);
	
	String findRecentMessageID(Date queryTime);
	
	int updateMessage(MessageEntity message) throws Exception;
	
	int editorMessage(MessageEntity message) throws Exception;
	
	int saveMessage(MessageEntity message) throws Exception;
	
	int deleteMessage(String messageID) throws Exception;
}
