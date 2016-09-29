package com.smarterama.timetable.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.smarterama.timetable.domain.Faculty;


public class FacultyDAO {

private static Logger log = Logger.getLogger(FacultyDAO.class.getName());
	
	public static void craete(String name) throws DAOException{
	
		//Check for the same faculty in DB
		Faculty facultyInDB = getFaculty(name);
		if(facultyInDB != null){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to add the same faculty?", "This faculty already exists!", JOptionPane.YES_NO_OPTION);
			if(n==1){
				log.log(Level.INFO, "Faculty "+name+" wasn't created! This faculty already exists!");
				return;
			} else {
				log.log(Level.INFO, "Faculty "+name+" was created! The same faculty added!");
			}
		}

		Connection connection = null;
		PreparedStatement statement = null;
		
		String sql = "INSERT INTO faculties (name) VALUES (?)";
		
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
				statement.executeUpdate();
								
				log.log(Level.INFO, "New faculty "+name+" added to DB");
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
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
				log.log(Level.SEVERE, e.getMessage());
			}
		}
	}
	
	public static Faculty getFaculty(String name) throws DAOException{
		
		Faculty foundFaculty = new Faculty();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		
		String sql = "SELECT name "
				+ "FROM faculties "
				+ "WHERE name='"+name+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				
				while(rSet.next()){
					if(rSet.wasNull() == false){
						log.log(Level.INFO, "Faculty "+name+" has found.");
						
						foundFaculty.name = rSet.getString(1);
						return foundFaculty;
					} else {
						log.log(Level.INFO, "Faculty "+name+" has not found.");
						throw new DAOException("Faculty has not found.");
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Faculty has not found.");
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
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Faculty has not found.");
			}
		}
		throw new DAOException("Faculty has not found.");
	}

	public static int getID(String name){
		
		int facultyID = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		
		String sql = "SELECT * "
				+ "FROM faculties "
				+ "WHERE name='"+name+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				return facultyID;
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				
				while(rSet.next()){
					if(rSet.wasNull() == false){
						log.log(Level.INFO, "Faculty "+name+" has found.");
						facultyID = rSet.getInt(1);
					} else {
						log.log(Level.INFO, "Faculty "+name+" has not found.");
						return facultyID;
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				return facultyID;
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
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				return facultyID;
			}
		}
		return facultyID;
	}

	public static void edit(String name, String newName) throws DAOException{

		//Check for faculty in DB
		Faculty facultyInDB = getFaculty(name);
		if(facultyInDB == null){
			log.log(Level.INFO, "Faculty "+name+" doesn't exist in DB!");
			return;
		}
		
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		
		String sql1 = "SELECT * "
				+ "FROM faculties "
				+ "WHERE name='"+name+"'";

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
					if(rSet1.wasNull() == false){
						log.log(Level.INFO, "Faculty "+name+" has found.");
						int facultyID = rSet1.getInt(1);
						String sql2 = "UPDATE faculties "
									+ "SET  name='"+newName+"', "
									+ "WHERE facultyID='"+facultyID+"'";
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
						log.log(Level.INFO, "Faculty "+name+" has not found.");
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
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		
	}

	public static void delete(String name) throws DAOException{
		
		//Check for faculty in DB
		Faculty facultyInDB = getFaculty(name);
		if(facultyInDB == null){
			log.log(Level.INFO, "Faculty "+name+" doesn't exist in DB!");
			return;
		}
				
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		
		String sql1 = "SELECT * "
				+ "FROM faculties "
				+ "WHERE name='"+name+"'";
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
					if(rSet1.wasNull() == false){
						log.log(Level.INFO, "Faculty "+name+" has found.");

						//Control question									
						int n = JOptionPane.showConfirmDialog(null, "Do you want to delete faculty?", "This faculty exists!", JOptionPane.YES_NO_OPTION);
						if(n==1){
							log.log(Level.INFO, "Faculty "+name+" wasn't deleted!");
							return;
						} else {
							log.log(Level.INFO, "Faculty "+name+" deleted");
						}
						
						int facultyID = rSet1.getInt(1);
						String sql2 = "DELETE FROM faculties WHERE facultyID='"+facultyID+"'";
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
						log.log(Level.INFO, "Faculty "+name+" has not found.");
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
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				return;
			}
		}							
	}
}
