package com.ehl.itspark.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.entity.MessageEntity;
import com.ehl.itspark.data.service.intf.MessageService;
import com.ehl.itspark.service.intf.MessageApiService;

@Service
@Transactional
public class MessageApiServiceImpl implements MessageApiService{

	@Autowired
	private MessageService messageService;
	
	@Override
	public List<MessageEntity> findMessages(MessageEntity message,Date queryTime) {
		// TODO Auto-generated method stub
		return messageService.findMessages(message, queryTime);
	}

	@Override
	public String findRecentMessageID(Date queryTime) {
		// TODO Auto-generated method stub
		return messageService.findRecentMessageID(queryTime);
	}

	@Override
	public int updateMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
		return messageService.updateMessage(message);
	}

	@Override
	public int editorMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
		return messageService.editorMessage(message);
	}
	
	@Override
	public int saveMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
	    return messageService.saveMessage(message);
	}
	
	@Override
	public int deleteMessage(String messageID) throws Exception{
		// TODO Auto-generated method stub
		return messageService.deleteMessage(messageID);
	}

}
