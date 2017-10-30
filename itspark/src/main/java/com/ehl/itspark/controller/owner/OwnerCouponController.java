package com.ehl.itspark.controller.owner;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.QueryCouponInfo;
import com.ehl.itspark.server.OwnerServer;


@Controller
@RequestMapping("/owner/queryCoupon")
public class OwnerCouponController {

	@Autowired
	private OwnerServer ownerServer;
	
	@RequestMapping("/query")
	public @ResponseBody CommonResponse queryCoupon(@RequestBody QueryCouponInfo info) throws IOException{
		return ownerServer.queryCoupon(info);
	}

}
