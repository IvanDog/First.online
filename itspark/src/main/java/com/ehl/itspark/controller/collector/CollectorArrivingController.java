package com.ehl.itspark.controller.collector;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.EntranceInfo;
import com.ehl.itspark.server.CollectorServer;


@Controller
@RequestMapping("/collector/insertArriving")
public class CollectorArrivingController {
	@Autowired
	private CollectorServer collectorServer;
	
	@RequestMapping("/insert")
	public @ResponseBody CommonResponse insert(@RequestBody EntranceInfo info) throws IOException{
		return collectorServer.insertEntrance(info);
	}
}
