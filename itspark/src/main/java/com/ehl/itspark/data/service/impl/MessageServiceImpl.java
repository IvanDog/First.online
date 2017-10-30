package com.ehl.itspark.data.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehl.itspark.data.dao.MessageDao;
import com.ehl.itspark.data.entity.MessageEntity;
import com.ehl.itspark.data.service.intf.MessageService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageDao messageDao;
	
	@Override
	public List<MessageEntity> findMessages(MessageEntity message,Date queryTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(message,queryTime);
		return messageDao.findAll(para);
	}

	@Override
	public String findRecentMessageID(Date queryTime) {
		// TODO Auto-generated method stub
		Map<String, Object> para = convertEntityToMap(null,queryTime);
		List<MessageEntity> messageEntites = messageDao.findRecentMessageIDs(para);
		if(messageEntites!=null){
			return messageEntites.get(0).getMessageID();
		}else{
			return null;
		}
	}
	
	private Map<String, Object> convertEntityToMap(MessageEntity message,Date queryTime) {
		Map<String, Object> para=new HashMap<>();
		if(message!=null){
			if(message.getMessageID()!=null&&!"".equals(message.getMessageID())){
				para.put("messageID", message.getMessageID());
			}
			if(message.getMessageOwner()!=null&&!"".equals(message.getMessageOwner())){
				para.put("messageOwner", message.getMessageOwner());
			}
			if(message.getMessageState()!=null){
				para.put("messageState", message.getMessageState());
			}
			if(message.getMessageOwnerType()!=null&&!"".equals(message.getMessageOwnerType())){
				para.put("messageOwnerType", message.getMessageOwnerType());
			}
			if(queryTime != null){
				para.put("queryTime", queryTime);
			}
		}
		return para;
	}


	@Override
	public int updateMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
		if(message==null){
			throw new Exception("更新的数据为空！");
		}
		return messageDao.update(message);
	}

	@Override
	public int editorMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
		if(message==null){
			throw new Exception("更新的数据为空！");
		}
		return messageDao.editor(message);
	}
	
	@Override
	public int saveMessage(MessageEntity message) throws Exception{
		// TODO Auto-generated method stub
		if(message==null){
			throw new Exception("保存的数据为空！");
		}
		return messageDao.save(message);
	}
	
	@Override
	public int deleteMessage(String messageID) throws Exception{
		// TODO Auto-generated method stub
		if(messageID==null || "".equals(messageID)){
			throw new Exception("删除的数据为空！");
		}
		return messageDao.deleteByMessageID(messageID);
	}
}
