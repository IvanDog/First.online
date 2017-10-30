package com.ehl.itspark.work;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ehl.itspark.common.DateUtils;
import com.ehl.itspark.data.entity.ModifyCalendarEntity;
import com.ehl.itspark.data.entity.SchedulingCalendarEntity;
import com.ehl.itspark.data.entity.ShiftworkEntity;
import com.ehl.itspark.data.service.intf.ModifyCalendarService;
import com.ehl.itspark.data.service.intf.SchedulingCalendarService;
import com.ehl.itspark.data.service.intf.ShiftworkService;


@Service
public class SchedulingCalendarBusiness {

	@Autowired
	private ModifyCalendarService modifyCalendarService;
	@Autowired
	private SchedulingCalendarService schedulingCalendarService;
	@Autowired
	private ShiftworkService shiftworkService;
	private Logger logger=LoggerFactory.getLogger(SchedulingCalendarBusiness.class);
/*	*//**
	 * 根据人员与选择的周时间统计该周人员的排班信息
	 * @param persionName 人员姓名
	 * @param selectTime 周时间 格式：年-月-周，如2017-9-5表示2017年9月第5周
	 * @param pageIndex 页起始
	 * @param pageSize 每页数量
	 * @return 人员排班信息
	 *//*
	public PageDTO<SchedulingCalendarEntity> findPersionScheduleCalendarEntities(String persionName,String selectTime,Integer pageIndex,Integer pageSize){
		Date modifyTime = getModifyTime(selectTime);
		PageDTO<SchedulingCalendarEntity> pageDTO=schedulingCalendarService.findCalendarPagesByPersionView(persionName, pageIndex, pageSize);
		if(pageDTO.getData()!=null&&pageDTO.getData().size()!=0){
			List<SchedulingCalendarEntity> calendarEntities=pageDTO.getData();
			for (SchedulingCalendarEntity schedulingCalendarEntity : calendarEntities) {
				updateScheduleCalendarEntity(schedulingCalendarEntity,modifyTime);
			}
		}
		return pageDTO;
	}
	*//**
	 * 根据班次名称和选择的周时间统计本班次在该周的值班人数
	 * @param shiftworkName 班次名称
	 * @param selectTime 周时间 格式：年-月-周，如2017-9-5表示2017年9月第5周
	 * @param pageIndex 页起始
	 * @param pageSize 每页数量
	 * @return 各班次值班人数
	 *//*
	public PageDTO<ShiftworkSchedulingCalendarEntity> findShiftworkScheduleCalendarEntities(String shiftworkName,String selectTime,Integer pageIndex,Integer pageSize){
		Date modifyTime = getModifyTime(selectTime);
		PageDTO<ShiftworkSchedulingCalendarEntity> pageDTO=schedulingCalendarService.findCalendarPagesByShiftworkView(shiftworkName, pageIndex, pageSize);
		if(pageDTO.getData()!=null&&pageDTO.getData().size()!=0){
			List<ShiftworkSchedulingCalendarEntity> shiftworkSchedulingCalendarEntities=pageDTO.getData();
			for (ShiftworkSchedulingCalendarEntity shiftworkSchedulingCalendarEntity : shiftworkSchedulingCalendarEntities) {
				updateShiftworkShceduleCalendarEntity(shiftworkSchedulingCalendarEntity,modifyTime);
			}
		}
		return pageDTO;
	}
	//如果有调班信息则更新人员排班信息
	private void updateScheduleCalendarEntity(SchedulingCalendarEntity schedulingCalendarEntity,Date modifyTime){
		List<ModifyCalendarEntity> mondayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), modifyTime);
		if(mondayModifyCalendarEntities!=null&&mondayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setMondayId(mondayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(mondayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setMondayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> tuesdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 1));
		if(tuesdayModifyCalendarEntities!=null&&tuesdayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setTuesdayId(tuesdayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(tuesdayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setTuesdayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> wednesdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 2));
		if(wednesdayModifyCalendarEntities!=null&&wednesdayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setWednesdayId(wednesdayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(wednesdayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setWednesdayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> thursdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 3));
		if(thursdayModifyCalendarEntities!=null&&thursdayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setThursdayId(thursdayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(thursdayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setThursdayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> fridayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 4));
		if(fridayModifyCalendarEntities!=null&&fridayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setFridayId(fridayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(fridayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setFridayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> saturdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 5));
		if(saturdayModifyCalendarEntities!=null&&saturdayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setSaturdayId(saturdayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(saturdayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setSaturdayName(shiftworkEntity.getName());
			}
		}
		List<ModifyCalendarEntity> sundayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntity.getPersionId(), DateUtils.addDay(modifyTime, 6));
		if(sundayModifyCalendarEntities!=null&&sundayModifyCalendarEntities.size()!=0){
			schedulingCalendarEntity.getSchedulingEntity().setSundayId(sundayModifyCalendarEntities.get(0).getNewShiftworkId());
			ShiftworkEntity shiftworkEntity=shiftworkService.findById(sundayModifyCalendarEntities.get(0).getNewShiftworkId());
			if(shiftworkEntity!=null){
				schedulingCalendarEntity.getSchedulingEntity().setSundayName(shiftworkEntity.getName());
			}
		}
	}
	//如果有调班信息则更新该班次的人员数量
	private void updateShiftworkShceduleCalendarEntity(ShiftworkSchedulingCalendarEntity shiftworkSchedulingCalendarEntity,Date modifyTime){
		List<ModifyCalendarEntity> mondayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, modifyTime);
		if(mondayModifyCalendarEntities!=null&&mondayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : mondayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setMondayCount(shiftworkSchedulingCalendarEntity.getMondayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setMondayCount(shiftworkSchedulingCalendarEntity.getMondayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> tuesdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 1));
		if(tuesdayModifyCalendarEntities!=null&&tuesdayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : tuesdayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setTuesdayCount(shiftworkSchedulingCalendarEntity.getTuesdayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setTuesdayCount(shiftworkSchedulingCalendarEntity.getTuesdayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> wednesdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 2));
		if(wednesdayModifyCalendarEntities!=null&&wednesdayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : wednesdayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setWednesdayCount(shiftworkSchedulingCalendarEntity.getWednesdayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setWednesdayCount(shiftworkSchedulingCalendarEntity.getWednesdayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> thursdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 3));
		if(thursdayModifyCalendarEntities!=null&&thursdayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : thursdayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setThursdayCount(shiftworkSchedulingCalendarEntity.getThursdayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setThursdayCount(shiftworkSchedulingCalendarEntity.getThursdayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> fridayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 4));
		if(fridayModifyCalendarEntities!=null&&fridayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : fridayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setFridayCount(shiftworkSchedulingCalendarEntity.getFridayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setFridayCount(shiftworkSchedulingCalendarEntity.getFridayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> saturdayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 5));
		if(saturdayModifyCalendarEntities!=null&&saturdayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : saturdayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setSaturdayCount(shiftworkSchedulingCalendarEntity.getSaturdayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setSaturdayCount(shiftworkSchedulingCalendarEntity.getSaturdayCount()+1);
					continue;
				}
			}
		}
		List<ModifyCalendarEntity> sundayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(null, DateUtils.addDay(modifyTime, 6));
		if(sundayModifyCalendarEntities!=null&&sundayModifyCalendarEntities.size()!=0){
			for (ModifyCalendarEntity modifyCalendarEntity : sundayModifyCalendarEntities) {
				if(shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getOriShiftworkId()){
					shiftworkSchedulingCalendarEntity.setSundayCount(shiftworkSchedulingCalendarEntity.getSundayCount()-1);
					continue;
				}
				if (shiftworkSchedulingCalendarEntity.getShiftworkId()==modifyCalendarEntity.getNewShiftworkId()) {
					shiftworkSchedulingCalendarEntity.setSundayCount(shiftworkSchedulingCalendarEntity.getSundayCount()+1);
					continue;
				}
			}
		}
	}
	
	*//**
	 * 保存或更新人员调班信息
	 * @param dto
	 * @throws Exception
	 *//*
	@Transactional(rollbackFor={Exception.class})
	public void saveOrUpdateModifyCalendar(ModifyCalendarDto dto) throws Exception{
		List<SchedulingCalendarEntity> schedulingCalendarEntities= schedulingCalendarService.findCalendarByPersionView(dto.getPersionName());
		if(schedulingCalendarEntities==null||schedulingCalendarEntities.size()==0){
			return;
		}
		Date modifyTime = getModifyTime(dto.getModifyTime());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getMondayId(),dto.getMondayId(),modifyTime,dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getTuesdayId(),dto.getTuesdayId(),DateUtils.addDay(modifyTime, 1),dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getWednesdayId(),dto.getWednesdayId(),DateUtils.addDay(modifyTime, 2),dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getThursdayId(),dto.getThursdayId(),DateUtils.addDay(modifyTime, 3),dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getFridayId(),dto.getFridayId(),DateUtils.addDay(modifyTime, 4),dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getSaturdayId(),dto.getSaturdayId(),DateUtils.addDay(modifyTime, 5),dto.getPersionId());
		goSaveOrUpdate(schedulingCalendarEntities.get(0).getSchedulingEntity().getSundayId(),dto.getSundayId(),DateUtils.addDay(modifyTime, 6),dto.getPersionId());
	}
	//根据周时间算出该周周一日期
	private Date getModifyTime(String modifyTimeStr) {
		String[] tempTime=modifyTimeStr.split("-");
		Calendar calendar=Calendar.getInstance();
//		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.YEAR, Integer.valueOf(tempTime[0]));
		calendar.set(Calendar.MONTH, Integer.valueOf(tempTime[1])-1);
		calendar.set(calendar.WEEK_OF_MONTH, Integer.valueOf(tempTime[2]));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date modifyTime=calendar.getTime();
		return modifyTime;
	}
	
	private void goSaveOrUpdate(Long oldShiftworkId,Long newShiftworkId,Date modifyTime,Long persionId) throws Exception{
		if(newShiftworkId!=oldShiftworkId){
			ModifyCalendarEntity monday=new ModifyCalendarEntity();
			monday.setPersionId(persionId);
			monday.setModifyTime(modifyTime);
			monday.setOriShiftworkId(oldShiftworkId);
			monday.setNewShiftworkId(newShiftworkId);
			monday.setCreateTime(new Date());
			List<ModifyCalendarEntity> modifyCalendarEntities=modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(persionId, modifyTime);
			if(modifyCalendarEntities==null||modifyCalendarEntities.size()==0){
				modifyCalendarService.saveModifyCalendarRecord(monday);
			}else {
				modifyCalendarService.updateModifyCalendarRecord(monday);
			}
		}else {
			modifyCalendarService.deleteByPersionIdAndModifyTime(persionId, modifyTime);
		}
	}*/
	/**
	 * 获取指定人员在指定日期的班次信息
	 * @param persionName 人员姓名
	 * @param workDate 日期：yyyyMMdd
	 * @return 班次信息
	 * @throws Exception
	 */
	public ShiftworkEntity findShiftworkByPersionAndDate(String persionName,String workDate) throws Exception{
		ShiftworkEntity result=null;
		try {
			Date selectDate=DateUtils.StringToDate(workDate+"000000", "yyyyMMddHHmmss");
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(selectDate);
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			int dayWeek=calendar.get(Calendar.DAY_OF_WEEK);
			List<SchedulingCalendarEntity> schedulingCalendarEntities=schedulingCalendarService.findCalendarByPersionView(persionName);
			if(schedulingCalendarEntities==null||schedulingCalendarEntities.size()==0){
				throw new Exception("未找到【"+persionName+"】在【"+workDate+"】的排班信息！");
			}
			List<ModifyCalendarEntity> mondayModifyCalendarEntities= modifyCalendarService.findModifyCalendarsByPersionIdAndModifyTime(schedulingCalendarEntities.get(0).getPersionId(), selectDate);
			if(mondayModifyCalendarEntities!=null&&mondayModifyCalendarEntities.size()!=0){
				result= shiftworkService.findById(mondayModifyCalendarEntities.get(0).getNewShiftworkId());
			}else{
				switch (dayWeek) {
				case 1:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getSundayId());break;
				case 2:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getMondayId());break;
				case 3:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getTuesdayId());break;
				case 4:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getWednesdayId());break;
				case 5:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getThursdayId());break;
				case 6:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getFridayId());break;
				case 7:
					result= shiftworkService.findById(schedulingCalendarEntities.get(0).getSchedulingEntity().getSaturdayId());break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("获取【"+persionName+"】在【"+workDate+"】的排班信息失败，"+e.getMessage(),e);
			throw new Exception("获取【"+persionName+"】在【"+workDate+"】的排班信息失败，"+e.getMessage());
		}
		return result;
	}
}
