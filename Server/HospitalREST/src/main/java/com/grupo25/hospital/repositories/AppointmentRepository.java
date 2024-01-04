package com.grupo25.hospital.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.grupo25.hospital.models.entities.Appointment;
import com.grupo25.hospital.models.entities.Inmunization;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	List<Appointment> findAllByTimestampBetween(LocalDateTime timestamp_Start, LocalDateTime timestamp_End);
	
	@Query(value = "select *from appointment where id_patient = ?", nativeQuery = true)
	List<Appointment> patientAppointments(Long id);
	
	
	@Query(value = "select *from appointment where id_appointment = ?", nativeQuery = true)
	Appointment getAppointmentById(Long id);
	
}
