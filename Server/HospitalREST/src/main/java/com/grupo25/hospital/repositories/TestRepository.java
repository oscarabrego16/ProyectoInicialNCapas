package com.grupo25.hospital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupo25.hospital.models.entities.Appointment;
import com.grupo25.hospital.models.entities.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
	Test findByName(String name);
	
}
