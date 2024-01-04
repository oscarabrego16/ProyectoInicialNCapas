package com.grupo25.hospital.models.dtos;

import javax.validation.constraints.NotBlank;

public class CreateAreaDTO {
	@NotBlank
	private String name;
	
	private Character gender;
	
	private Integer start_age;
	
	private Integer frequency;

	public CreateAreaDTO() {
		super();
	}

	public CreateAreaDTO(@NotBlank String name, Character gender, Integer start_age, Integer frequency) {
		super();
		this.name = name;
		this.gender = gender;
		this.start_age = start_age;
		this.frequency = frequency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public Integer getStart_age() {
		return start_age;
	}

	public void setStart_age(Integer start_age) {
		this.start_age = start_age;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	
}
