package com.ehl.itspark.controller.collector;

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
import com.ehl.itspark.server.CollectorServer;


@Controller
@RequestMapping("/collector/leavingInformation")
public class CollectorLeavingController {
	@Autowired
	private CollectorServer collectorServer;
	
	@RequestMapping("/queryExpense")
	public @ResponseBody CommonResponse queryExpense(@RequestBody SettleAccountInfo info) throws IOException{
		return collectorServer.settleAccount(info);
	}
	
	@RequestMapping("/pay")
	public @ResponseBody CommonResponse updatePaymentPattern(@RequestBody PaymentInfo info) throws IOException{
		return collectorServer.pay(info);
	}
	
	@RequestMapping("/queryResult")
	public @ResponseBody CommonResponse queryResult(@RequestBody QueryResultInfo info) throws IOException{
		return collectorServer.queryResult(info);
	}
}
