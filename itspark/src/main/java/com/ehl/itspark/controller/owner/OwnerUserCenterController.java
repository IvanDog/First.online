package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.LogoutInfo;
import com.ehl.itspark.info.QueryUserInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/userCenter")
public class OwnerUserCenterController {
	
	    @Autowired
	    private OwnerServer ownerServer;
	    
		@RequestMapping("/query")
		public @ResponseBody CommonResponse query(@RequestBody QueryUserInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.queryUserInfo(info);
		}
		
		@RequestMapping("/logout")
		public @ResponseBody CommonResponse logout(@RequestBody LogoutInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.logout(info);
		}
	}
