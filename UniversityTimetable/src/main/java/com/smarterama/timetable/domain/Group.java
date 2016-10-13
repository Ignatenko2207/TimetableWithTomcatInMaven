package com.smarterama.timetable.domain;

import java.util.ArrayList;

public class Group {
	
	public String name = new String();
	public ArrayList<Student> students = new ArrayList<Student>();
	
	    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addStudent(Student student){
    	students.add(student);
    }
    public void deleteStudent(Student studentMustBeRemooved){
    	if(students.isEmpty() == false){
    		for (Student student : students) {
    			if(student.name == studentMustBeRemooved.name){
    				if(student.surname == studentMustBeRemooved.surname){
    					if(student.dateOfBirth == studentMustBeRemooved.dateOfBirth){
    	    				students.remove(student);
    	    			}
        			}
    			}
    		}
    	}
    	
    }
	
}