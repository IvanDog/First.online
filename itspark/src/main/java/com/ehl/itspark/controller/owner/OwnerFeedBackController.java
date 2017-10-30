package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.FeedBackInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/feedback")
public class OwnerFeedBackController {
	
	    @Autowired
	    private OwnerServer ownerServer;
	    
		@RequestMapping("/feedback")
		public @ResponseBody CommonResponse feedBack(@RequestBody FeedBackInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.feedBack(info);
		}
		
	}
