package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.SetHeadPortraitInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/headportrait")
public class OwnerSetHeadPortraitController {
	
	    @Autowired
	    private OwnerServer ownerServer;
	    
		@RequestMapping("/set")
		public @ResponseBody CommonResponse query(@RequestBody SetHeadPortraitInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.setHeadPortrait(info);
		}
		
	}
