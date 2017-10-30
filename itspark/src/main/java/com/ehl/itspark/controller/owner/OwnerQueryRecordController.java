package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.RecordSearchInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/queryRecord")
public class OwnerQueryRecordController {

	@Autowired
	private OwnerServer ownerServer;
	
	@RequestMapping("/query")
	public @ResponseBody CommonResponse queryRecord(@RequestBody RecordSearchInfo info) throws Exception{
		return ownerServer.searchRecord(info);
	}

}
