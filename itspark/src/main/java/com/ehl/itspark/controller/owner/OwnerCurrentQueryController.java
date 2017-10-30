package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.QueryRecordInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/queryCurrent")
public class OwnerCurrentQueryController {
	
    @Autowired
    private OwnerServer ownerServer;
    
	@RequestMapping("/query")
	public @ResponseBody CommonResponse query(@RequestBody QueryRecordInfo info) throws Exception{
        System.out.println(info.toString());
		return ownerServer.queryRecordInfo(info);
	}
}
