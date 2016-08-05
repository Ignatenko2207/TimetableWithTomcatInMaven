package com.smarterama.timetable.model;

public class Student extends Man {
	
	public int courseOfStudy;
	
	public void setCourseOfStudy(int newCourseOfStudy){
		courseOfStudy = newCourseOfStudy;
    }
    
	public int getCourseOfStudy(){
        return courseOfStudy;
	}
    

}