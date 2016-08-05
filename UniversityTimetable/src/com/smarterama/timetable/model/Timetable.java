package com.smarterama.timetable.model;

import java.util.ArrayList;
import java.util.Calendar;

public class Timetable {

	public int period;
	public ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	public ArrayList<Lesson> lessonsForWeek = getLessonsForWeek(lessons, period);
	public ArrayList<Lesson> lessonsForMonth = getLessonsForMonth(lessons, period);
	
	public static ArrayList<Lesson> getLessonsForMonth(ArrayList<Lesson> lessons, int period) {
			
		ArrayList<Lesson> allLessons = lessons;
		ArrayList<Lesson> lessonsForMonth = new ArrayList<Lesson>();
		
		if(allLessons.isEmpty() == false){
			for(int i=0; i<lessons.size(); i++){
				Lesson lesson = allLessons.get(i);
				if((lesson.startOfLesson.get(Calendar.MONTH) + 1) == period){
						lessonsForMonth.add(lesson);	
				}
			}
		}
		return lessonsForMonth;
	}

	public static ArrayList<Lesson> getLessonsForWeek(ArrayList<Lesson> lessons,int period) {
		
		ArrayList<Lesson> lessonsForWeek = new ArrayList<Lesson>();
		
		if(lessons.isEmpty() == false){
			for(Lesson lesson: lessons){
				if(lesson.startOfLesson.get(Calendar.WEEK_OF_YEAR) == period){
						lessonsForWeek.add(lesson);	
				}
			}
		}
		return lessonsForWeek;
	}

	public static ArrayList<Lesson> addLesson(ArrayList<Lesson> lessons, Lesson lesson) {
		lessons.add(lesson);
		return lessons;
	}

	public static ArrayList<Lesson> deleteLesson(ArrayList<Lesson> lessons, Lesson lesson) {
		if (lessons.contains(lesson)){
			lessons.remove(lesson);
		}
		return lessons;
	}

				
}