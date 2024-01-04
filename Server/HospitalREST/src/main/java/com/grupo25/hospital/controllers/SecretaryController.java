package com.grupo25.hospital.controllers;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo25.hospital.models.dtos.ExpedienteDTO;
import com.grupo25.hospital.models.dtos.MessageDTO;
import com.grupo25.hospital.models.dtos.SecretaryScheduleAppointmentDTO;
import com.grupo25.hospital.models.dtos.UpdatePassDTO;
import com.grupo25.hospital.models.entities.Appointment;
import com.grupo25.hospital.models.entities.Appointment_type;
import com.grupo25.hospital.models.entities.Area;
import com.grupo25.hospital.models.entities.Person;
import com.grupo25.hospital.models.entities.Shift;
import com.grupo25.hospital.models.entities.Test;
import com.grupo25.hospital.models.entities.Vaccine;
import com.grupo25.hospital.services.AppointmentService;
import com.grupo25.hospital.services.AppointmentTypeService;
import com.grupo25.hospital.services.AreaService;
import com.grupo25.hospital.services.InmunizationService;
import com.grupo25.hospital.services.PersonService;
import com.grupo25.hospital.services.ShiftService;
import com.grupo25.hospital.services.TestService;
import com.grupo25.hospital.services.VaccineService;

@RestController
@RequestMapping("/secretary")
@CrossOrigin(origins = "*")
public class SecretaryController {
	
	@Autowired
	private VaccineService vaccService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private AppointmentTypeService appService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private InmunizationService inmuService;
	
	@Autowired
	private ShiftService shiftService;
	
	@Autowired
	private PersonService personService;
	
	
	
	@GetMapping("/appointment-types")
	public ResponseEntity<?> getAllAppointmentTypes(){
		try {
			List<Appointment_type> appsTypes = appService.findAll();
			
			return new ResponseEntity<>(
						appsTypes,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/shifts")
	public ResponseEntity<?> getAllShifts(){
		try {
			List<Shift> shifts = shiftService.findAll();
			
			return new ResponseEntity<>(
						shifts,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/vaccines")
	public ResponseEntity<?> getAllVaccines(){
		try {
			List<Vaccine> vaccines = vaccService.findAll();
			
			return new ResponseEntity<>(
						vaccines,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/areas")
	public ResponseEntity<?> getAllAreas(){
		try {
			List<Area> areas = areaService.findAll();
			
			return new ResponseEntity<>(
						areas,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/tests")
	public ResponseEntity<?> getAllTests(){
		try {
			List<Test> tests = testService.findAll();
			
			return new ResponseEntity<>(
						tests,
						HttpStatus.OK
					);
		} catch (Exception e) {
			return new ResponseEntity<>(
						null,
						HttpStatus.INTERNAL_SERVER_ERROR
					);
		}
	}
	
	@GetMapping("/appointments/today")
	public ResponseEntity<?> getTodayAppointments(){
		LocalDateTime timestamp = (LocalDate.now().atStartOfDay());
		LocalDateTime timestamp2 = (timestamp.plusDays(1)).truncatedTo(ChronoUnit.DAYS);
		try {
			List<Appointment> appointments = appointmentService.findTodayAppointments(timestamp.truncatedTo(ChronoUnit.DAYS), timestamp2);
			
			if(appointments.size() == 0) {
				return new ResponseEntity<>(
						new MessageDTO("No hay citas para hoy"),
						HttpStatus.NOT_FOUND
					);
			}
			
			return new ResponseEntity<>(
					appointments,
					HttpStatus.OK
				);
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/schedule-appointment")
	public ResponseEntity<?> bookAppointment(@Valid @RequestBody SecretaryScheduleAppointmentDTO newSchedule,BindingResult result){
		try {
			if(result.hasErrors()) {
				String errors = result.getAllErrors().toString();
				return new ResponseEntity<>(
						new MessageDTO("Errores en validacion" + errors),
						HttpStatus.BAD_REQUEST);
			}
			
			Person foundPerson = personService.findOneByIdentifier(newSchedule.getUsername());
			
			if(foundPerson == null) {
				return new ResponseEntity<MessageDTO>(
						new MessageDTO("Persona no encontrada"),
						HttpStatus.NOT_FOUND
					);
			}
			
			Appointment_type type = appService.findOneById(newSchedule.getType()); 
			///////////////////Id de doctor se asigna hasta que el doctor atiende a la persona
			
			if(type.getId_appointment_type() == 1) {
				Vaccine vaccine = vaccService.findOneById(newSchedule.getIdVAT());
				
				appointmentService.registerSInmu(newSchedule, type, vaccine, foundPerson);
				
				return new ResponseEntity<MessageDTO>(
						new MessageDTO("Cita agendada"),
						HttpStatus.CREATED
					);
			}
			if(type.getId_appointment_type() == 2) {
				Area area = areaService.findOneById(newSchedule.getIdVAT());
		    	
				
				appointmentService.registerSArea(newSchedule, type, area, foundPerson);
				
				return new ResponseEntity<MessageDTO>(
						new MessageDTO("Cita agendada"),
						HttpStatus.CREATED
					);
			}
			if(type.getId_appointment_type() == 3) {
				Test test = testService.findOneById(newSchedule.getIdVAT());
				
				appointmentService.registerSTest(newSchedule, type, test, foundPerson);
				
				return new ResponseEntity<MessageDTO>(
						new MessageDTO("Cita agendada"),
						HttpStatus.CREATED
					);
			}
			
			return new ResponseEntity<MessageDTO>(
					new MessageDTO("Tipo de cita inválido"),
					HttpStatus.BAD_REQUEST
				);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/my-info/updatepassword")
	public ResponseEntity<?> updateOwnPassword(@Valid @RequestBody UpdatePassDTO newPassInfo, BindingResult result){
		
		try {
			if(result.hasErrors()) {
				String errors = result.getAllErrors().toString();
				return new ResponseEntity<>(
						new MessageDTO("Errores en validacion" + errors),
						HttpStatus.BAD_REQUEST);
			}
			if(!newPassInfo.getNew_password().equals(newPassInfo.getConfirm_password())) {
				return new ResponseEntity<>(
						new MessageDTO("Contraseñas no son iguales"),
						HttpStatus.BAD_REQUEST);
			}
			Person foundPerson = personService.getPersonAuthenticated();
			if(personService.comparePassword(foundPerson, newPassInfo.getCurrent_password())==false) {
				return new ResponseEntity<>(
						new MessageDTO("Contraseña actual equivocada"),
						HttpStatus.BAD_REQUEST);
			}
			personService.updatePersonPassword(newPassInfo, foundPerson);
			return new ResponseEntity<>(
					new MessageDTO("Contraseña actualizada"),
					HttpStatus.OK
				);
			
		} catch (Exception e) {
			return new ResponseEntity<>(
					null,
					HttpStatus.INTERNAL_SERVER_ERROR
				);
		}
	}

	/*@GetMapping("/citas-dia")
	public ResponseEntity<List<CitasDiaDTO>> getDayAppointments(BindingResult result){
		try {
			//TODO implementar logica de obtener usuarios
			List<CitasDiaDTO> listaUsers= new ArrayList<>();
			return new ResponseEntity<List<CitasDiaDTO>>(
					listaUsers,
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@GetMapping("/citas-dia/consulta/expediente/{id}")
	public ResponseEntity<List<ExpedienteDTO>> getUserExpedienteSecretary(@PathVariable Long id){
		try {
			
			List<Appointment> GetAppointments = appointmentService.getAllAppointments(id);
			List<ExpedienteDTO> Expediente = new ArrayList<>();
			GetAppointments.forEach(appointment ->{
				
				ExpedienteDTO a = new ExpedienteDTO();
				a.setTipo(appointment.getId_appointment_type().getType_name());
				a.setDetallesCita(appointment.getAppointment_details());
				a.setFecha(appointment.getTimestamp());
				
				Expediente.add(a);
				
			});

			return new ResponseEntity<List<ExpedienteDTO>>(
					Expediente,
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/*
	@PostMapping("/agendar-cita")
	public ResponseEntity<MessageDTO> bookAppointment(ScheduleAppointmentDTO nuevaCita,BindingResult result){
		try {
			//TODO implementar logica de reservar citas
			
			return new ResponseEntity<MessageDTO>(
					new MessageDTO("Reservada correctamente"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
}
