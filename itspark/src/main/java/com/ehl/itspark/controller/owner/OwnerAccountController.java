package com.ehl.itspark.controller.owner;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.AnalysisInfo;
import com.ehl.itspark.info.LoginInfo;
import com.ehl.itspark.info.RegisterInfo;
import com.ehl.itspark.server.OwnerServer;


@Controller
@RequestMapping("/owner/login")
public class OwnerAccountController {

	@Autowired
	private OwnerServer ownerServer;
		
	@RequestMapping("/login")
	public @ResponseBody CommonResponse login(@RequestBody LoginInfo loginInfo) throws IOException{	
        System.out.println(loginInfo.toString());
		return ownerServer.login(loginInfo);
	}
    
    
    @RequestMapping("/analysis")
	public @ResponseBody CommonResponse analysis(@RequestBody AnalysisInfo analysisInfo) throws IOException{
        System.out.println(analysisInfo.toString());
		return ownerServer.analysis(analysisInfo);
	}
    
	@RequestMapping("/register")
	public @ResponseBody CommonResponse register(@RequestBody RegisterInfo registerInfo) throws Exception{
        System.out.println(registerInfo.toString());
		return ownerServer.register(registerInfo);
	}

}
