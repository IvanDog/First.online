package com.ehl.itspark.controller.owner;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.PaymentInfo;
import com.ehl.itspark.info.QueryResultInfo;
import com.ehl.itspark.info.SettleAccountInfo;
import com.ehl.itspark.info.TokenInfo;
import com.ehl.itspark.server.OwnerServer;


@Controller
@RequestMapping("/owner/payment")
public class OwnerPaymentController {

	@Autowired
	private OwnerServer ownerServer;
	
	@RequestMapping("/queryExpense")
	public @ResponseBody CommonResponse queryExpense(@RequestBody SettleAccountInfo info) throws IOException{
		return ownerServer.settleAccount(info);
	}
	
	@RequestMapping("/pay")
	public @ResponseBody CommonResponse pay(@RequestBody PaymentInfo info) throws IOException{
		return ownerServer.pay(info);
	}
	
	@RequestMapping("/queryPassword")
	public @ResponseBody CommonResponse pay(@RequestBody TokenInfo info) throws IOException{
		return ownerServer.queryPaymentPasswd(info);
	}
	
	@RequestMapping("/queryResult")
	public @ResponseBody CommonResponse queryResult(@RequestBody QueryResultInfo info) throws IOException{
		return ownerServer.queryResult(info);
	}
}
