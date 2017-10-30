package com.ehl.itspark.controller.collector;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.LogoutInfo;
import com.ehl.itspark.server.CollectorServer;



@Controller
@RequestMapping("/collector/userCenter")
public class CollectorUserCenterController {
	@Autowired
	private CollectorServer collectorServer;
	
	@RequestMapping("/logout")
	public @ResponseBody CommonResponse logout(@RequestBody LogoutInfo info) throws Exception{
        System.out.println(info.toString());
		return collectorServer.logout(info);
	}
	}
