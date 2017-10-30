package com.ehl.itspark.controller.collector;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.LicenseInfo;
import com.ehl.itspark.server.CollectorServer;


@Controller
@RequestMapping("/collector/license")
public class CollectorLicenseController {
	@Autowired
	private CollectorServer collectorServer;
	
	@RequestMapping("/analysis")
	public @ResponseBody CommonResponse analysis(@RequestBody LicenseInfo info) throws IOException{
		return collectorServer.sendLicense(info);
	}

}
