package com.smarterama;

import java.util.Date;

public class Lesson {

	String name = new String();
	Teacher teacher = new Teacher();
	Group group = new Group();
	public int room;
	Date startOfLesson = new Date();
	int durationOfLesson;
	
	public int getDurationOfLesson() {
		return durationOfLesson;
	}
	public void setDurationOfLesson(int durationOfLesson) {
		this.durationOfLesson = durationOfLesson;
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
	public Date getStartOfLesson() {
		return startOfLesson;
	}
	public void setStartOfLesson(Date startOfLesson) {
		this.startOfLesson = startOfLesson;
	}
	
}