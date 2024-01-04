package com.grupo25.hospital.models.entities;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "shift")
public class Shift {
	@Id
	@Column(name = "id_shift")
	@SequenceGenerator(name = "shift_id_shift_gen", sequenceName = "shift_id_shift_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_id_shift_gen")
	private Long id_shift;
	
	@Column(name = "start_hour")
	private LocalTime start_hour;
	
	@Column(name = "finish_hour")
	private LocalTime finish_hour;
	
	public Shift() {
		super();
	}

	public Shift(LocalTime start_hour, LocalTime finish_hour) {
		super();
		this.start_hour = start_hour;
		this.finish_hour = finish_hour;
	}

	public Long getId_shift() {
		return id_shift;
	}

	public void setId_shift(Long id_shift) {
		this.id_shift = id_shift;
	}

	public LocalTime getStart_hour() {
		return start_hour;
	}

	public void setStart_hour(LocalTime start_hour) {
		this.start_hour = start_hour;
	}

	public LocalTime getFinish_hour() {
		return finish_hour;
	}

	public void setFinish_hour(LocalTime finish_hour) {
		this.finish_hour = finish_hour;
	}
	
	
}	
