package com.ehl.itspark.controller.collector;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.ClockInfo;
import com.ehl.itspark.info.LocationInfo;
import com.ehl.itspark.info.TokenInfo;
import com.ehl.itspark.server.CollectorServer;



@Controller
@RequestMapping("/collector/workAttendance")
public class CollectorWorkAttendanceController {

	@Autowired
	private CollectorServer collectorServer;
	
	@RequestMapping("/requestInformation")
	public @ResponseBody CommonResponse getInformation(@RequestBody TokenInfo info) throws IOException{
		return collectorServer.requestInfo(info);
	}
	
	@RequestMapping("/reportLocation")
	public @ResponseBody CommonResponse getLocationState(@RequestBody LocationInfo info) throws IOException{
		return collectorServer.reportLocation(info);
	}
	
	@RequestMapping("/clock")
	public @ResponseBody CommonResponse getClockResult(@RequestBody ClockInfo info) throws IOException{
		return collectorServer.clock(info);
	}
}
