package com.ehl.itspark.controller.owner;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.QueryBalanceInfo;
import com.ehl.itspark.info.QueryResultInfo;
import com.ehl.itspark.info.RechargeInfo;
import com.ehl.itspark.server.OwnerServer;

@Controller
@RequestMapping("/owner/recharge")
public class OwnerRechargeController {

	@Autowired
	private OwnerServer ownerServer;
	
	@RequestMapping("/queryBalance")
	public @ResponseBody CommonResponse queryBalance(@RequestBody QueryBalanceInfo info) throws IOException{
		return ownerServer.queryBalance(info);
	}

	@RequestMapping("/recharge")
	public @ResponseBody CommonResponse queryResult(@RequestBody RechargeInfo info) throws IOException{
		return ownerServer.recharge(info);
	}
	
	@RequestMapping("/queryResult")
	public @ResponseBody CommonResponse queryResult(@RequestBody QueryResultInfo info) throws IOException{
		return ownerServer.queryResult(info);
	}
}
