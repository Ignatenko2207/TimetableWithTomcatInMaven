package com.smarterama.timetable.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Man {
	
	String name;
	String surname;
	Calendar dateOfBirth;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(int year, int month, int day) {
		this.dateOfBirth = new GregorianCalendar(year, month, day);
	}	
	
}