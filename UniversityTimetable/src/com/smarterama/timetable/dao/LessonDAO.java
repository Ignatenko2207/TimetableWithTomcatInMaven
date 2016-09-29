package com.smarterama.timetable.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.smarterama.timetable.domain.Lesson;


public class LessonDAO {

	/*
	length in mills
	45 min    = 2700000 ms
	1 hour    = 3600000 ms
	1,5 hours = 5400000 ms
	3 hours   = 10800000 ms
	*/
	
	private static Logger log = Logger.getLogger(LessonDAO.class.getName());
	
	public static void create(String name,int room, Calendar startOfLesson, int length, int teacherID, int groupID) throws DAOException{
		
		Lesson lessonInDB = getLesson(name, room, startOfLesson, length);
		
		if(lessonInDB != null){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to add the same lesson?", "This lesson already exists!", JOptionPane.YES_NO_OPTION);
			if(n==1){
				log.log(Level.INFO, "Lesson "+name+" wasn't created! This lesson already exists!");
				return;
			} else {
				log.log(Level.INFO, "Lesson "+name+" was created! The same lesson added!");
			}
		}	
		Connection connection = null;
		PreparedStatement statement = null;
		
		Long timeInMillis = startOfLesson.getTimeInMillis();
		Timestamp timeToDB = new Timestamp(timeInMillis);
		
		String sql = "INSERT INTO lessons (name, room, startOfLesson, duration, teacherID, teacherID) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
		
        try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				statement.setString(1, name);
				statement.setInt(2, room);
				statement.setTimestamp(3, timeToDB);
				statement.setInt(4, length);
				statement.setInt(5, teacherID);
				statement.setInt(6, teacherID);
				statement.executeUpdate();
								
				log.log(Level.INFO, "New lesson "+name+" added to DB");
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		finally{
			try {
			    if(statement!=null){
			    	statement.close();
			    }
			    if(connection!=null){
			    	connection.close();
			    }
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	public static Lesson getLesson(String name, int room, Calendar startOfLesson, int length) throws DAOException{
		
		Lesson foundLesson = new Lesson(); 
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
			
		Timestamp timeToDB = getTimestampFromCalendar(startOfLesson);
		
		String sql = "SELECT name, room, startOfLesson, duration "
				+ "FROM lessons "
				+ "WHERE name='"+name+"' AND room='"+room+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Lesson has not found.");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				
				while(rSet.next()){
					if(rSet.getTimestamp(3).equals(timeToDB)){
						if(rSet.getInt(4) == length){
							log.log(Level.INFO, "Lesson "+name+" has found.");
							
							foundLesson.name = rSet.getString(1);
							foundLesson.room = rSet.getInt(2);
							foundLesson.startOfLesson = startOfLesson;
							foundLesson.durationOfLesson = rSet.getInt(4);
							return foundLesson;		
						} else {
							log.log(Level.INFO, "Lesson "+name+" has not found.");
							throw new DAOException("Lesson has not found.");
						}
					} else {
						log.log(Level.INFO, "Lesson "+name+" has not found.");
						throw new DAOException("Lesson has not found.");
					}
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Lesson has not found.");
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
				throw new DAOException("Lesson has not found.");
			}
		}
		throw new DAOException("Lesson has not found.");
	}
	
	public static void edit(String name, int room, Calendar startOfLesson, int length, int teacherID, int groupID, 
							String newName, int newRoom, Calendar newStartOfLesson, int newLength, int newTeacherID, int newGroupID) throws DAOException{
		
		//Check for lesson in DB
		Lesson lessonInDB = getLesson(name, room, startOfLesson, length);
		if(lessonInDB == null){
			System.out.println("Lesson has not found!");
			log.log(Level.INFO, "Lesson "+name+" doesn't exist in DB!");
			return;
		}
			
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
			
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		
		Long timeInMillis1 = startOfLesson.getTimeInMillis();
		Timestamp timeToDB1 = new Timestamp(timeInMillis1);
		String sql1 = "SELECT * "
					+ "FROM lessons "
					+ "WHERE name='"+name+"' AND startOfLesson='"+timeToDB1+"'";
		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				return;
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
			
				while(rSet1.next()){
					if(rSet1.getInt(3) == room){
						if(rSet1.getInt(5) == length){
							if(rSet1.getInt(6) == teacherID){
								if(rSet1.getInt(7) == groupID){
									log.log(Level.INFO, "Lesson "+name+" has found.");
									int lessonID = rSet1.getInt(1);
								
									Timestamp timeToDB2 = getTimestampFromCalendar(newStartOfLesson);
							
									String sql2 = "UPDATE lessons "
												+ "SET  name='"+newName+"', "
												+ "room='"+newRoom+"', "
												+ "startOfLesson='"+timeToDB2+"', "
												+ "duration='"+newLength+"',"
												+ "teacherID='"+newTeacherID+"',"
												+ "groupID='"+newGroupID+"' "
												+ "WHERE lessonID='"+lessonID+"'";
									
									connection2 = ConnectionToDB.getConnectionToDB();
									if(connection2 != null){
										log.log(Level.INFO, "Connection for update is established.");
									} else {
										log.log(Level.SEVERE, "Connection for update is not established!");
										return;
									}
									statement2 = connection2.prepareStatement(sql2);
									statement2.executeUpdate();
								} else {
									log.log(Level.INFO, "Lesson "+name+" has not found.");
									return;
								}
							} else {
								log.log(Level.INFO, "Lesson "+name+" has not found.");
								return;
							}
						} else {
							log.log(Level.INFO, "Lesson "+name+" has not found.");
							return;
						}
					} else {
						log.log(Level.INFO, "Lesson "+name+" has not found.");
						return;
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				return;
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
				return;
			}
		}

	}

	public static void delete(String name, int room, Calendar startOfLesson, int length, int teacherID, int groupID) throws DAOException{
		//Check for lesson in DB
		Lesson lessonInDB = getLesson(name, room, startOfLesson, length);
		if(lessonInDB == null){
			System.out.println("Lesson has not found!");
			log.log(Level.INFO, "Lesson "+name+" doesn't exist in DB!");
			return;
		}
			
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
					
		Connection connection2 = null;
		PreparedStatement statement2 = null;
			
		Long timeInMillis1 = startOfLesson.getTimeInMillis();
		Timestamp timeToDB1 = new Timestamp(timeInMillis1);
		String sql1 = "SELECT * "
					+ "FROM lessons "
					+ "WHERE name='"+name+"' AND startOfLesson='"+timeToDB1+"'";
		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				return;
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				
				while(rSet1.next()){
					if(rSet1.getInt(3) == room){
						if(rSet1.getInt(5) == length){
							if(rSet1.getInt(6) == teacherID){
								if(rSet1.getInt(7) == groupID){
									log.log(Level.INFO, "Lesson "+name+" has found.");
							
									//Control question									
									int n = JOptionPane.showConfirmDialog(null, "Do you want to delete lesson?", "This lesson exists!", JOptionPane.YES_NO_OPTION);
									if(n==1){
									log.log(Level.INFO, "Lesson "+name+" wasn't deleted!");
									return;
								} else {
									log.log(Level.INFO, "Lesson "+name+" deleted");
								}
								int lessonID = rSet1.getInt(1);
												
								String sql2 = "DELETE FROM lessons "
											+ "WHERE lessonID='"+lessonID+"'";
								
								connection2 = ConnectionToDB.getConnectionToDB();
								if(connection2 != null){
									log.log(Level.INFO, "Connection for update is established.");
								} else {
									log.log(Level.SEVERE, "Connection for update is not established!");
									return;
								}
								statement2 = connection2.prepareStatement(sql2);
								statement2.executeUpdate();
							} else {
								log.log(Level.INFO, "Lesson "+name+" has not found.");
								return;
							}
						} else {
							log.log(Level.INFO, "Lesson "+name+" has not found.");
							return;
						}
					} else {
						log.log(Level.INFO, "Lesson "+name+" has not found.");
						return;
					}
				} else {
					log.log(Level.INFO, "Lesson "+name+" has not found.");
					return;
				}
			}
		}catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage());
				return;
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
				return;
			}
		}
	}
	
	private static Timestamp getTimestampFromCalendar(Calendar time) {
		Calendar timeWithCorrectMonth = time;
		timeWithCorrectMonth.add(Calendar.MONTH, -1);
		Long timeInMillis = timeWithCorrectMonth.getTimeInMillis();
		Timestamp timeToDB = new Timestamp(timeInMillis);
        return timeToDB;
	}

}