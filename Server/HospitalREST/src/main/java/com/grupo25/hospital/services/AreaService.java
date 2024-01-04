package com.grupo25.hospital.services;

import java.util.List;

import com.grupo25.hospital.models.dtos.CreateAreaDTO;
import com.grupo25.hospital.models.dtos.EditAreaDTO;
import com.grupo25.hospital.models.entities.Area;

public interface AreaService {
	void insert(CreateAreaDTO areaInfo) throws Exception;
	void update(EditAreaDTO areaInfo, Area area) throws Exception;
	void delete(Area area) throws Exception;
	Area findOneById(Long id) throws Exception;
	Area findOneByIdentifier(String name) throws Exception;
	List<Area> findAll() throws Exception;
}
