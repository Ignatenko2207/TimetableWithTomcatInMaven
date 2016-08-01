package com.smarterama;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class GroupTest {
	
	@Test
	public void addStudentTest() {
		
		
		Student student = new Student();
		student.setName("Ivan");
		student.setSurname("Ivanov");
		student.setDateOfBirth(1983, 5, 10);
		student.setCourseOfStudy(1);
		
		Group groupForTest = new Group();
		int sizeOfGroup = groupForTest.students.size();
		groupForTest.addStudent(student);
		
		assertEquals(groupForTest.students.size(), sizeOfGroup+1);
		assertTrue(groupForTest.students.contains(student));
	}

	@Test
	public void deleteStudentTest() {
		
		Student student1 = new Student();
		student1.setName("Ivan");
		student1.setSurname("Ivanov");
		student1.setDateOfBirth(1983, 5, 10);
		student1.setCourseOfStudy(1);
		
		Student student2 = new Student();
		student2.setName("Petr");
		student2.setSurname("Petrov");
		student2.setDateOfBirth(1982, 8, 21);
		student2.setCourseOfStudy(1);
		
		Group groupForTest = new Group();
		groupForTest.addStudent(student1);
		groupForTest.addStudent(student2);
		int sizeOfGroup = groupForTest.students.size();
				
		assertTrue(groupForTest.students.contains(student1));
		
		groupForTest.deleteStudent(student1);
		assertEquals(groupForTest.students.size(), sizeOfGroup-1);
		assertFalse(groupForTest.students.contains(student1));
	}

	
}
