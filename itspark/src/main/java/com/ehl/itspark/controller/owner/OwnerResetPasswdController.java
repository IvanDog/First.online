package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.ResetPasswdInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/resetPasswd")
public class OwnerResetPasswdController {
	
	    @Autowired
	    private OwnerServer ownerServer;
	    
		@RequestMapping("/reset")
		public @ResponseBody CommonResponse reset(@RequestBody ResetPasswdInfo info) throws Exception{
	        System.out.println(info.toString());
			return ownerServer.resetPasswd(info);
		}
		
	}
