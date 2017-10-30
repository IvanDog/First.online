package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.SetNickNameInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/nick")
public class OwnerSetNickNameController {
	
	    @Autowired
	    private OwnerServer ownerServer;
	    
		@RequestMapping("/set")
		public @ResponseBody CommonResponse setNickName(@RequestBody SetNickNameInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.setNickName(info);
		}
		
	}
