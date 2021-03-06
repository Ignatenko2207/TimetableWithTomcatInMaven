package com.smarterama.timetable.dao;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import com.smarterama.timetable.domain.Teacher;

public class TeacherDAO {
	
	private static Logger log = Logger.getLogger(TeacherDAO.class.getName());
	
	public static void create(String name, String surname, Calendar date, String degree) throws DAOException{
		
		//Check for the same teacher in DB
		Teacher teacherInDB = getTeacher(name, surname, date, degree);
		if(teacherInDB != null){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to add the same teacher?", "This teacher already exists!", JOptionPane.YES_NO_OPTION);
			if(n==1){
				log.log(Level.INFO, "Teacher "+surname+" wasn't created! This teacher already exists!");
				return;
			} else {
				log.log(Level.INFO, "Teacher "+surname+" was created! The same teacher added!");
			}
		}
		Connection connection = null;
		PreparedStatement statement = null;
		Long timeInMillis = date.getTimeInMillis();
		Timestamp dateToDB = new Timestamp(timeInMillis);
				
		String sql = "INSERT INTO teachers (name, surname, dateofbirth, academicdegree) VALUES (?, ?, ?, ?)";
		
        try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to create is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setString(2, surname);
				statement.setTimestamp(3, dateToDB);
				statement.setString(4, degree);
				statement.executeUpdate();
								
				log.log(Level.INFO, "New teacher "+name+" "+surname+" added to DB");
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
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
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct");
			}
		}
	}
		
	public static Teacher getTeacher(String name, String surname, Calendar date, String degree) throws DAOException{
		
		Teacher foundTeacher = new Teacher(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		Timestamp dateToDB = getTimestampFromCalendar(date);
		
		String sql = "SELECT name, surname, dateofbirth, academicdegree "
				+ "FROM teachers "
				+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			}
			 else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to get is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				while(rSet.next()){
					if(rSet.getString(1).equals(name)){
						if(rSet.getString(4).equals(degree)){
							log.log(Level.INFO, "Teacher "+surname+" has found.");
							foundTeacher.name = rSet.getString(1);
							foundTeacher.surname = rSet.getString(2);
							foundTeacher.dateOfBirth = date;
							foundTeacher.academicDegree = rSet.getString(4);
							return foundTeacher;
						} else {
							log.log(Level.INFO, "Degree of teacher "+surname+" does not match.");
							throw new DAOException("Degree of teacher "+surname+" does not match.");
						}
					} else {
						log.log(Level.INFO, "Name of teacher "+surname+" does not match.");
						throw new DAOException("Name of teacher "+surname+" does not match.");
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct");
			}
		}finally{
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
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct.");
			}
		}
		throw new DAOException("Teacher has not found.");
	}
	
	public static int getID(String name, String surname, Calendar date, String degree) throws DAOException{
		
		int teacherID=0;
		//Check for teacher in DB
		Teacher teacherInDB = getTeacher(name, surname, date, degree);
		if(teacherInDB == null){
			log.log(Level.INFO, "Teacher "+name+" "+surname+" doesn't exist in DB!");
			return teacherID;
		}
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		Long timeInMillis = date.getTimeInMillis();
		Timestamp dateToDB = new Timestamp(timeInMillis);
		
		String sql = "SELECT * "
					+ "FROM teachers "
					+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to get ID is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				while(rSet.next()){
					if(rSet.getString(2).equals(name)){
						if(rSet.getString(5).equals(degree)){
							log.log(Level.INFO, "Teacher "+surname+" has found.");
							teacherID = rSet.getInt(1);
							return teacherID;					
						} else {
							log.log(Level.INFO, "Degree of teacher "+surname+" does not match.");
							return teacherID;
						}
					} else {
						log.log(Level.INFO, "Name of teacher "+surname+" does not match.");
						return teacherID;
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct");
			}
		}finally{
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
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct.");
			}
		}
		return teacherID;
	}	
	
	public static void edit(String name, String surname, Calendar date,  String degree, 
							String newName, String newSurname, Calendar newDate,  String  newDegree) throws DAOException{
		
		//Check for teacher in DB
		Teacher teacherInDB = getTeacher(name, surname, date, degree);
		if(teacherInDB == null){
			log.log(Level.INFO, "Teacher "+name+" "+surname+" doesn't exist in DB!");
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
				+ "FROM teachers "
				+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB1+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to edit is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				while(rSet1.next()){
					if(rSet1.getString(2).equals(name)){
						if(rSet1.getString(5).equals(degree)){
							log.log(Level.INFO, "Teacher "+surname+" has found.");
							int teacherID = rSet1.getInt(1);
							Timestamp dateToDB2 = getTimestampFromCalendar(newDate);
							
							String sql2 = "UPDATE teachers "
									+ "SET  name='"+newName+"', "
									+ "surname='"+newSurname+"', "
									+ "dateofbirth='"+dateToDB2+"', "
									+ "academicdegree='"+newDegree+"' "
									+ "WHERE teacherID='"+teacherID+"'";
							
							connection2 = ConnectionToDB.getConnectionToDB();
							if(connection2 != null){
								log.log(Level.INFO, "Connection for update is established.");
							} else {
								log.log(Level.SEVERE, "Connection for update is not established!");
								throw new DAOException("Connection to edit is not established!");
							}
							statement2 = connection2.prepareStatement(sql2);
							statement2.executeUpdate();
						} else {
							log.log(Level.INFO, "Degree of teacher "+surname+" does not match.");
							return;
						}
					} else {
						log.log(Level.INFO, "Name of teacher "+surname+" does not match.");
						return;
					}
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
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
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct");
			}
		}
	}
	
	public static void delete(String name, String surname, Calendar date, String degree) throws DAOException{
	
		//Check for teacher in DB
		Teacher teacherInDB = getTeacher(name, surname, date, degree);
		if(teacherInDB == null){
			log.log(Level.INFO, "Teacher "+name+" "+surname+" doesn't exist in DB!");
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
					+ "FROM teachers "
					+ "WHERE surname='"+surname+"' AND dateofbirth='"+dateToDB1+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to delete is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				while(rSet1.next()){
					if(rSet1.getString(2).equals(name)){
						if(rSet1.getString(5).equals(degree)){
							log.log(Level.INFO, "Teacher "+surname+" has found.");
							int teacherID = rSet1.getInt(1);
							//Control question									
							int n = JOptionPane.showConfirmDialog(null, "Do you want to delete teacher?", "This teacher exists!", JOptionPane.YES_NO_OPTION);
							if(n==1){
								log.log(Level.INFO, "Teacher "+surname+" wasn't deleted!");
								return;
							} else {
								log.log(Level.INFO, "Teacher "+surname+" deleted");
							}
							
							String sql2 = "DELETE FROM teachers "
										+ "WHERE teacherid='"+teacherID+"'";
								
							connection2 = ConnectionToDB.getConnectionToDB();
							if(connection2 != null){
								log.log(Level.INFO, "Connection is established.");
							} else {
								log.log(Level.SEVERE, "Connection is not established!");
								throw new DAOException("Connection to delete is not established!");
							}
							statement2 = connection2.prepareStatement(sql2);
							statement2.executeUpdate();
						} else {
							log.log(Level.INFO, "Degree of teacher "+surname+" does not match.");
							return;
						}
					} else {
						log.log(Level.INFO, "Name of teacher "+surname+" does not match.");
						return;
					}
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct.");
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
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct.");
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
