package com.ehl.itspark.controller.collector;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.LoginInfo;
import com.ehl.itspark.server.CollectorServer;

@Controller
@RequestMapping("/collector/login")
public class CollectorAccountController {
	@Autowired
	private CollectorServer collectorServer;
		
	@RequestMapping("/login")
	public @ResponseBody CommonResponse login(@RequestBody LoginInfo loginInfo) throws IOException{	
        System.out.println(loginInfo.toString());
		return collectorServer.login(loginInfo);
	}
	
}
