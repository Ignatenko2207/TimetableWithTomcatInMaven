package com.smarterama.timetable.domain;

public class Student extends Man {
	
	public int courseOfStudy;
	
	public void setCourseOfStudy(int newCourseOfStudy){
		courseOfStudy = newCourseOfStudy;
    }
    
	public int getCourseOfStudy(){
        return courseOfStudy;
	}
    

}