package com.grupo25.hospital.services;

import java.util.List;

import com.grupo25.hospital.models.entities.Shift;

public interface ShiftService {
	List<Shift> findAll() throws Exception;
}
