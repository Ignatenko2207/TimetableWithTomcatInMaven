package com.smarterama.timetable.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.smarterama.timetable.model.Lesson;
import com.smarterama.timetable.model.Timetable;

public class TimetableTest {

	@Test
	public void getLessonsForMonthTest() {
		
		Lesson lesson1 = new Lesson();
		lesson1.setName("TestLesson1");
		lesson1.setStartOfLesson(2016, 1, 10, 8, 30, 00);
		
		Lesson lesson2 = new Lesson();
		lesson2.setName("TestLesson2");
		lesson2.setStartOfLesson(2016, 1, 10, 10, 0, 0);
		
		Lesson lesson3 = new Lesson();
		lesson3.setName("TestLesson3");
		lesson3.setStartOfLesson(2016, 1, 20, 8, 30, 0);
		
		Lesson lesson4 = new Lesson();
		lesson4.setName("TestLesson4");
		lesson4.setStartOfLesson(2016, 2, 4, 8, 30, 0);
		
		Lesson lesson5 = new Lesson();
		lesson5.setName("TestLesson5");
		lesson5.setStartOfLesson(2016, 2, 4, 10, 0, 0);
		
		Lesson lesson6 = new Lesson();
		lesson6.setName("TestLesson6");
		lesson6.setStartOfLesson(2016, 3, 2, 8, 30, 0);
		
		Timetable timetableForTest = new Timetable();
		ArrayList<Lesson> testListOfLessons = new ArrayList<>();
		testListOfLessons.add(lesson1);
		testListOfLessons.add(lesson2);
		testListOfLessons.add(lesson3);
		testListOfLessons.add(lesson4);
		testListOfLessons.add(lesson5);
		testListOfLessons.add(lesson6);
		
		assertEquals(testListOfLessons.size(), 6);
		
		ArrayList<Lesson> testListOfLessonsForWeek = new ArrayList<>();
		testListOfLessonsForWeek = Timetable.getLessonsForMonth(testListOfLessons, 2);
		
		assertEquals(testListOfLessonsForWeek.size(), 2);
		
		assertTrue(testListOfLessons.contains(lesson4));
		assertTrue(testListOfLessons.contains(lesson5));
	}

	@Test
	public void getLessonsForWeekTest() {
		
		Lesson lesson1 = new Lesson();
		lesson1.setName("TestLesson1");
		lesson1.setStartOfLesson(2016, 1, 8, 8, 30, 0);

		Lesson lesson2 = new Lesson();
		lesson2.setName("TestLesson2");
		lesson2.setStartOfLesson(2016, 1, 8, 10, 0, 0);
		
		Lesson lesson3 = new Lesson();
		lesson3.setName("TestLesson3");
		lesson3.setStartOfLesson(2016, 3, 20, 8, 30, 0);
		
		Lesson lesson4 = new Lesson();
		lesson4.setName("TestLesson4");
		lesson4.setStartOfLesson(2016, 3, 4, 8, 30, 0);
		
		Timetable timetableForTest = new Timetable();
		ArrayList<Lesson> testListOfLessons = new ArrayList<>();
		testListOfLessons.add(lesson1);
		testListOfLessons.add(lesson2);
		testListOfLessons.add(lesson3);
		testListOfLessons.add(lesson4);
		
		assertEquals(testListOfLessons.size(), 4);
			
		ArrayList<Lesson> testListOfLessonsForWeek = new ArrayList<>();
		testListOfLessonsForWeek = Timetable.getLessonsForWeek(testListOfLessons, 2);
		
		assertEquals(testListOfLessonsForWeek.size(), 2);
		
		assertTrue(testListOfLessons.contains(lesson1));
		assertTrue(testListOfLessons.contains(lesson2));
		
		
	}

	@Test
	public void addLessonTest() {
		
		Lesson lesson1 = new Lesson();
		lesson1.setName("TestLesson1");
		lesson1.setStartOfLesson(2016, 3, 4, 8, 30, 00);
		
		Lesson lesson2 = new Lesson();
		lesson2.setName("TestLesson2");
		lesson2.setStartOfLesson(2016, 3, 4, 8, 30, 00);
		
		Timetable timetableForTest = new Timetable();
		ArrayList<Lesson> testListOfLessons = new ArrayList<>();
		testListOfLessons = Timetable.addLesson(testListOfLessons, lesson1);
		
		assertEquals(testListOfLessons.size(), 1);
		assertTrue(testListOfLessons.contains(lesson1));
		
		testListOfLessons = Timetable.addLesson(testListOfLessons, lesson2);
		
		assertEquals(testListOfLessons.size(), 2);
		assertTrue(testListOfLessons.contains(lesson2));
		
	}

	@Test
	public void deleteLessonTest() {
		Lesson lesson1 = new Lesson();
		lesson1.setName("TestLesson1");
		lesson1.setStartOfLesson(2016, 3, 4, 8, 30, 00);
		
		Lesson lesson2 = new Lesson();
		lesson2.setName("TestLesson2");
		lesson2.setStartOfLesson(2016, 3, 4, 8, 30, 00);
		
		Timetable timetableForTest = new Timetable();
		ArrayList<Lesson> testListOfLessons = new ArrayList<>();
		testListOfLessons = Timetable.addLesson(testListOfLessons, lesson1);
		testListOfLessons = Timetable.addLesson(testListOfLessons, lesson2);
		
		assertEquals(testListOfLessons.size(), 2);
		
		testListOfLessons = Timetable.deleteLesson(testListOfLessons, lesson1);
		assertEquals(testListOfLessons.size(), 1);
		assertFalse(testListOfLessons.contains(lesson1));
				
	}

}
