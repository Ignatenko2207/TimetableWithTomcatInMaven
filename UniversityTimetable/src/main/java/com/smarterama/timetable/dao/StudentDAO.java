package com.smarterama.timetable.dao;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import com.smarterama.timetable.domain.Student;

public class StudentDAO {
	
	private static Logger log = Logger.getLogger(StudentDAO.class.getName());
	
	public static void create(String name, String surname, Calendar date, int course, int groupID) throws DAOException{
		
		//Check for the same student in DB
		Student studentInDB = getStudent(name, surname, date, course);
		if(studentInDB != null){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to add the same student?", "This student already exists!", JOptionPane.YES_NO_OPTION);
			if(n==1){
				log.log(Level.INFO, "Student "+surname+" wasn't created! This student already exists!");
				return;
			} else {
				log.log(Level.INFO, "Student "+surname+" was created! The same student added!");
			}
		}
		
		Connection connection = null;
		PreparedStatement statement = null;
		Long timeInMillis = date.getTimeInMillis();
		Timestamp dateToDB = new Timestamp(timeInMillis);
				
		String sql = "INSERT INTO students (name, surname, dateofbirth, courseofstudy, groupID) VALUES (?, ?, ?, ?, ?)";
		
        try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.INFO, "Connection is not established.");
				throw new DAOException("Connection to create is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setString(2, surname);
				statement.setTimestamp(3, dateToDB);
				statement.setInt(4, course);
				statement.setInt(5, groupID);
				statement.executeUpdate();
								
				log.log(Level.INFO, "New student "+name+" "+surname+" added to DB");
			}catch (SQLException e) {
				throw new DAOException("SQL request isn't correct");
			}
		}finally{
			try {
			    if(statement!=null){
			    	statement.close();
			    }
			    if(connection!=null){
			    	connection.close();
			    }
			}catch (SQLException e) {
				throw new DAOException("Finally block isn't correct.");
			}
		}
	}
		
	public static Student getStudent(String name, String surname, Calendar date, int course) throws DAOException{
		
		Student foundStudent = new Student(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		Timestamp dateToDB = getTimestampFromCalendar(date);
		
		String sql = "SELECT name, surname, dateofbirth, courseofstudy "
				+ "FROM students "
				+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.INFO, "Connection is established.");
				throw new DAOException("Connection to get is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				while(rSet.next()){
					if(rSet.getString(1).equals(name)){
						if(rSet.getInt(4) == course){
							log.log(Level.INFO, "Student "+surname+" has found.");
							foundStudent.name = rSet.getString(1);
							foundStudent.surname = rSet.getString(2);
							foundStudent.dateOfBirth = date;
							foundStudent.courseOfStudy = rSet.getInt(4);
							return foundStudent;
						} else {
							log.log(Level.INFO, "Course of student "+surname+" does not match.");
							throw new DAOException("Student "+surname+" has found.");
						}
					} else {
						log.log(Level.INFO, "Name of student "+surname+" does not match.");
						throw new DAOException("Student "+surname+" has found.");
					}
				}
			} catch (SQLException e) {
				throw new DAOException("SQL request isn't correct.");
			}
		}
		finally{
			try {
				if(rSet!=null){
					rSet.close();
				}
			    if(statement!=null){
			    	statement.close();
			    }
			    if(connection!=null){
			    	connection.close();
			    }
			} catch (SQLException e) {
				throw new DAOException("Finally block isn't correct.");
			}
		}
		throw new DAOException("Student has not found.");
	}
	
	public static void edit(String name, String surname, Calendar date, int course, int groupID,
							String newName, String newSurname, Calendar newDate, int newCourse, int newGroupID) throws DAOException{

		Student studentInDB = getStudent(name, surname, date, course);
		if(studentInDB == null){
			System.out.println("Student has not found!");
			log.log(Level.INFO, "Student "+name+" "+surname+" doesn't exist in DB!");
			return;
		}
		
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		Long timeInMillis1 = date.getTimeInMillis();
		Timestamp dateToDB1 = new Timestamp(timeInMillis1);

		String sql1 = "SELECT * "
				+ "FROM students "
				+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB1+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.INFO, "Connection is not established.");
				throw new DAOException("Connection to edit is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				while(rSet1.next()){
					if(rSet1.getString(2).equals(name)){
						if(rSet1.getInt(4) == course){
							log.log(Level.INFO, "Student "+surname+" has found.");
							int studentID = rSet1.getInt(1);
							Timestamp dateToDB2 = getTimestampFromCalendar(newDate);
							
							String sql2 = "UPDATE students "
									+ "SET  name='"+newName+"', "
									+ "surname='"+newSurname+"', "
									+ "dateofbirth='"+dateToDB2+"', "
									+ "courseofstudy='"+newCourse+"', "
									+ "groupID='"+newGroupID+"' "
									+ "WHERE studentid='"+studentID+"'";
							
							connection2 = ConnectionToDB.getConnectionToDB();
							if(connection2 != null){
								log.log(Level.INFO, "Connection for update is established.");
							} else {
								log.log(Level.INFO, "Connection is not established.");
								throw new DAOException("Connection for update is not established!");
							}
							statement2 = connection2.prepareStatement(sql2);
							statement2.executeUpdate();
						} else {
							log.log(Level.INFO, "Coutse of student "+surname+" does not match.");
							return;
						}
					} else {
						log.log(Level.INFO, "Name of student "+surname+" does not match.");
						return;
					}
				}
			}catch (SQLException e) {
				throw new DAOException("SQL request isn't correct");
			}
		}finally{
			try {
				if(statement2!=null){
					statement1.close();
				}
			    if(connection2!=null){
			    	connection1.close();
			    }
				if(rSet1!=null){
					rSet1.close();
				}
			    if(statement1!=null){
			    	statement1.close();
			    }
			    if(connection1!=null){
			    	connection1.close();
			    }
			} catch (SQLException e) {
				throw new DAOException("Finally block isn't correct");
			}
		}
	}
	
	public static void delete(String name, String surname, Calendar date, int course, int groupID) throws DAOException{
		//Check for student in DB
		Student studentInDB = getStudent(name, surname, date, course);
		if(studentInDB == null){
			log.log(Level.INFO, "Student "+name+" "+surname+" doesn't exist in DB!");
			return;
		}
		
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		Long timeInMillis1 = date.getTimeInMillis();
		Timestamp dateToDB1 = new Timestamp(timeInMillis1);

		String sql1 = "SELECT * "
					+ "FROM students "
					+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB1+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.INFO, "Connection is not established.");
				throw new DAOException("Connection to delete is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				
				while(rSet1.next()){
					if(rSet1.getString(2).equals(name)){
						if(rSet1.getInt(4) == course){
							if(rSet1.getInt(6)==groupID){
								log.log(Level.INFO, "Student "+surname+" has found.");
								int studentID = rSet1.getInt(1);
							
								//Control question									
								int n = JOptionPane.showConfirmDialog(null, "Do you want to delete student?", "This student exists!", JOptionPane.YES_NO_OPTION);
								if(n==1){
									log.log(Level.INFO, "Student "+surname+" wasn't deleted!");
									return;
								} else {
									log.log(Level.INFO, "Student "+surname+" deleted");
								}
								
								String sql2 = "DELETE FROM students WHERE studentid='"+studentID+"'";
							
								connection2 = ConnectionToDB.getConnectionToDB();
								if(connection2 != null){
									log.log(Level.INFO, "Connection for update is established.");
								} else {
									log.log(Level.INFO, "Connection is not established.");
									throw new DAOException("Connection for update is not established!");
								}
								statement2 = connection2.prepareStatement(sql2);
								statement2.executeUpdate();
							
							
							} else {
								log.log(Level.INFO, "Group ID for student "+surname+" does not match.");
								return;
							}
						} else {
							log.log(Level.INFO, "Course of student "+surname+" does not match.");
							return;
						}
					} else {
						log.log(Level.INFO, "Name of student "+surname+" does not match.");
						return;
					}
				} 
			}catch (SQLException e) {
				throw new DAOException("SQL request isn't correct");
			}
		}finally{
			try {
				if(statement2!=null){
					statement1.close();
				}
			    if(connection2!=null){
			    	connection1.close();
			    }
				if(rSet1!=null){
					rSet1.close();
				}
			    if(statement1!=null){
			    	statement1.close();
			    }
			    if(connection1!=null){
			    	connection1.close();
			    }
			} catch (SQLException e) {
				throw new DAOException("Finally block isn't correct");
			}
		}
	}
	
	private static Timestamp getTimestampFromCalendar(Calendar dateOfBirth) {
		Calendar dateWithCorrectMonth = dateOfBirth;
		dateWithCorrectMonth.add(Calendar.MONTH, -1);
		Long timeInMillis = dateWithCorrectMonth.getTimeInMillis();
		Timestamp dateToDB = new Timestamp(timeInMillis);
        return dateToDB;
	}
}
