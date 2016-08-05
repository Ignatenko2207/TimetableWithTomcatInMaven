package com.smarterama.timetable.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Lesson {

	String name = new String();
	public Teacher teacher = new Teacher();
	public Group group = new Group();
	public int room;
	Calendar startOfLesson;
	public int durationOfLesson;
	
	public int getDurationOfLesson() {
		return durationOfLesson;
	}
	public void setDurationOfLesson(int durationOfLessonInMinutes) {
		this.durationOfLesson = durationOfLessonInMinutes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public int getRoom() {
		return room;
	}
	public void setRoom(int room) {
		this.room = room;
	}
	public Calendar getStartOfLesson() {
		return startOfLesson;
	}
	public void setStartOfLesson(int year, int month, int day, int hour, int minute, int second) {
		this.startOfLesson = new GregorianCalendar(year, (month-1), day, hour, minute, second);
	}
	
}