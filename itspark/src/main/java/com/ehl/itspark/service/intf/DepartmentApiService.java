package com.ehl.itspark.service.intf;

import java.util.List;

import com.ehl.itspark.data.entity.DepartmentEntity;

public interface DepartmentApiService {

	List<DepartmentEntity> findAll();
}
