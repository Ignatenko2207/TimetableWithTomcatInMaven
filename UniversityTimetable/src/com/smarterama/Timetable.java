package com.smarterama;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Timetable {

	
	
	public static void main(String[] args) {
		
		int period;
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	
	}

	private static ArrayList<Lesson> getLessonsForMonth(ArrayList<Lesson> lessons, int period) {
			
		ArrayList<Lesson> allLessons = lessons;
		ArrayList<Lesson> lessonsForMonth = new ArrayList<Lesson>();
		
		if(allLessons.isEmpty() == false){
			for(int i=0; i<lessons.size(); i++){
				Lesson lesson = allLessons.get(i);
				Date timeOfLesson = lesson.startOfLesson;
				Calendar dateOfLesson = Calendar.getInstance();
				dateOfLesson.setTime(timeOfLesson);
				if (period<=12){
					if((dateOfLesson.MONTH + 1) == period){
						lessonsForMonth.add(lesson);	
					}
				}
				else{
					System.out.println("Input correct month!!!");
				}
			}
		}
		return lessonsForMonth;
		
	}

	private static ArrayList<Lesson> getLessonsForWeek(ArrayList<Lesson> lessons,int period) {
		
		ArrayList<Lesson> allLessons = lessons;
		ArrayList<Lesson> lessonsForWeek = new ArrayList<Lesson>();
		
		if(allLessons.isEmpty() == false){
			for(int i=0; i<lessons.size(); i++){
				Lesson lesson = allLessons.get(i);
				Date timeOfLesson = lesson.startOfLesson;
				Calendar dateOfLesson = Calendar.getInstance();
				dateOfLesson.setTime(timeOfLesson);
				if (period<=53){
					if(dateOfLesson.WEEK_OF_YEAR == period){
						lessonsForWeek.add(lesson);	
					}
				}
				else{
					System.out.println("Input correct week!!!");
				}
			}
		}
		return lessonsForWeek;
	}

	private static ArrayList<Lesson> addLesson(ArrayList<Lesson> lessons) {
		lessons.add(new Lesson());
		return lessons;
	}

	private static ArrayList<Lesson> deleteLesson(ArrayList<Lesson> lessons, Lesson lesson) {
		if (lessons.contains(lesson)){
			lessons.remove(lesson);
		}
		return lessons;
	}

				
}