package com.smarterama.timetable.tests;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.smarterama.timetable.model.Faculty;
import com.smarterama.timetable.model.Group;

public class FacultyTest {

	@Test
	public void addGroupTest() {
		
		Group group = new Group();
		group.setName("Begginers");
				
		Faculty facultyForTest = new Faculty();
		int sizeOfFaculty = facultyForTest.groups.size();
		
		facultyForTest.addGroup(group);
		
		assertEquals(facultyForTest.groups.size(), sizeOfFaculty+1);
		assertTrue(facultyForTest.groups.contains(group));
	}

	@Test
	public void deleteGroupTest() {
		
		Group group1 = new Group();
		group1.setName("Begginers");
		
		Group group2 = new Group();
		group2.setName("Seniors");
		
		Faculty facultyForTest = new Faculty();
		facultyForTest.addGroup(group1);
		facultyForTest.addGroup(group2);
		int sizeOfFaculty = facultyForTest.groups.size();
		
		assertTrue(facultyForTest.groups.contains(group1));
		
		facultyForTest.deleteGroup(group1);
		assertEquals(facultyForTest.groups.size(), sizeOfFaculty-1);
		assertFalse(facultyForTest.groups.contains(group1));
	
	}

}
