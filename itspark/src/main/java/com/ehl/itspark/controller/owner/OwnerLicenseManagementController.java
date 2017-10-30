package com.ehl.itspark.controller.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ehl.itspark.common.CommonResponse;
import com.ehl.itspark.info.BindLicenseInfo;
import com.ehl.itspark.info.QueryLicenseInfo;
import com.ehl.itspark.info.UnBindLicenseInfo;
import com.ehl.itspark.server.OwnerServer;


@Controller
@RequestMapping("/owner/license")
public class OwnerLicenseManagementController {

	@Autowired
	private OwnerServer ownerServer;
	
	@RequestMapping("/query")
	public @ResponseBody CommonResponse query(@RequestBody QueryLicenseInfo info) throws Exception{
		return ownerServer.queryLicense(info);
	}
	
	@RequestMapping("/unbind")
	public @ResponseBody CommonResponse unbind(@RequestBody UnBindLicenseInfo info) throws Exception{
		return ownerServer.unBindLicense(info);
	}
	
	@RequestMapping("/bind")
	public @ResponseBody CommonResponse bind(@RequestBody BindLicenseInfo info) throws Exception{
		return ownerServer.bindLicense(info);
	}
}
