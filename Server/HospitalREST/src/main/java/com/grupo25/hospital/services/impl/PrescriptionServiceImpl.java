package com.grupo25.hospital.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo25.hospital.models.entities.Appointment;
import com.grupo25.hospital.models.entities.Inmunization;
import com.grupo25.hospital.models.entities.Prescription;
import com.grupo25.hospital.repositories.AppointmentRepository;
import com.grupo25.hospital.repositories.PersonRepository;
import com.grupo25.hospital.repositories.PrescriptionRepository;
import com.grupo25.hospital.services.PrescriptionService;

@Service
public class PrescriptionServiceImpl implements PrescriptionService{

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private PrescriptionRepository prescriptionRepository;
	
	
	@Override
	public List<Prescription> getPatientPrescriptions(Long id_patient) throws Exception {
		
		List<Appointment> appointments = appointmentRepository.patientAppointments(id_patient);
		List<Prescription> allPrescriptions = new ArrayList<>();
		
		appointments.forEach( appointment ->{
			
			List<Prescription> a = prescriptionRepository
					.getPrescriptionsByIdAppointment(appointment.getId_appointment());
				System.out.println("Correctoxd");
				a.forEach(b -> {
					allPrescriptions.add(b);

				});

		
		});
		
		
		return allPrescriptions;
	}

}
