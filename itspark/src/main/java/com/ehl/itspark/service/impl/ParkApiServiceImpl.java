package com.ehl.itspark.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.data.entity.ParkEntity;
import com.ehl.itspark.data.service.intf.ParkInService;
import com.ehl.itspark.service.intf.ParkApiService;

@Service
public class ParkApiServiceImpl implements ParkApiService{

	@Autowired
	private ParkInService parkInService;
	
	@Override
	public List<ParkEntity> findParks(ParkEntity park, Date startUpdateTime, Date endUpdateTime, Integer pageIndex,
			Integer pageSize) {
		// TODO Auto-generated method stub
		List<ParkEntity> parkEntities= parkInService.findParks(park, startUpdateTime, endUpdateTime, pageIndex, pageSize);
		if(parkEntities==null){
			return new ArrayList<ParkEntity>(0);
		}else {
			return parkEntities;
		}
	}

	@Override
	public boolean isInLocation(String parkNo,double lon, double lat, int addrRadius) throws Exception {
		// TODO Auto-generated method stub
		ParkEntity entity=new ParkEntity();
		entity.setNo(parkNo);
		List<ParkEntity> parkEntities= parkInService.findParks(entity, null, null);
		if(parkEntities==null||parkEntities.size()==0){
			throw new Exception("未获取到该停车场信息");
		}
		double parkLon=parkEntities.get(0).getLon();
		double parkLat=parkEntities.get(0).getLat();
		double distance=GetDistance(parkLon, parkLat, lon, lat);
		return distance<=addrRadius?true:false;
	}

	private static final  double EARTH_RADIUS = 6378137;//赤道半径
	private static double rad(double d){
	    return d * Math.PI / 180.0;
	}
	public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lon1) - rad(lon2);
	    double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
	    s = s * EARTH_RADIUS;    
	   return s;//单位米
	}

	@Override
	public ParkEntity findParkByNo(String no) {
		// TODO Auto-generated method stub
		ParkEntity entity=new ParkEntity();
		entity.setNo(no);
		List<ParkEntity> entities= parkInService.findParks(entity, null, null);
		if(entities==null||entities.size()==0){
			return null;
		}
		return entities.get(0);
	}
	
	@Override
	public int updatePark(ParkEntity park) throws Exception{
		return parkInService.updatePark(park);
	}
}
