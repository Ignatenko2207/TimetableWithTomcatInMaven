package com.smarterama.timetable.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.smarterama.timetable.domain.Group;

public class GroupDAO {

	private static Logger log = Logger.getLogger(GroupDAO.class.getName());
	
	public static void craete(String name, int facultyID) throws DAOException{
	
		//Check for the same group in DB
		Group groupInDB = getGroup(name);
		if(groupInDB != null){
			int n = JOptionPane.showConfirmDialog(null, "Do you want to add the same group?", "This group already exists!", JOptionPane.YES_NO_OPTION);
			if(n==1){
				log.log(Level.INFO, "Group "+name+" wasn't created! This group already exists!");
				return;
			} else {
				log.log(Level.INFO, "Group "+name+" was created! The same group added!");
			}
		}

		Connection connection = null;
		PreparedStatement statement = null;
		
		String sql = "INSERT INTO groups (name, facultyID) VALUES (?,?)";
		
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
				statement.setInt(2, facultyID);
				statement.executeUpdate();
								
				log.log(Level.INFO, "New group "+name+" added to DB");
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct.");
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
				throw new DAOException("Finally block isn't correct.");
			}
		}
	}
	
	public static Group getGroup(String name) throws DAOException{
		
		Group foundGroup = new Group();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		
		String sql = "SELECT name "
					+ "FROM groups "
					+ "WHERE name='"+name+"'";

		try{
			connection = ConnectionToDB.getConnectionToDB();
			if(connection != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to get group is not established!");
			}
			try {
				statement = connection.prepareStatement(sql);
				rSet = statement.executeQuery();
				
				while(rSet.next()){
					if(rSet.wasNull() == false){
						log.log(Level.INFO, "Group "+name+" has found.");
						
						foundGroup.name = rSet.getString(1);
						return foundGroup;
					} else {
						log.log(Level.INFO, "Group "+name+" has not found.");
						throw new DAOException("ResultSet returned nothing.");
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct.");
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
				throw new DAOException("Finally block isn't correct.");
			}
		}
		throw new DAOException("Group was notc created.");
	}

	public static int getID(String name, int facultyID) throws DAOException{
		int groupID = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rSet = null;
		
		String sql = "SELECT * "
				+ "FROM groups "
				+ "WHERE name='"+name+"'";

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
					if(rSet.getInt(3) == facultyID){
						log.log(Level.INFO, "Group "+name+" has found.");
						groupID = rSet.getInt(1);
						return groupID;
					} else {
						log.log(Level.INFO, "Group "+name+" has not found.");
						throw new DAOException("ResultSet returned nothing.");
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
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("Finally block isn't correct");
			}
		}
		throw new DAOException("ID of group "+name+" has not found.");
	}

	public static void edit(String name, int facultyID, String newName, int newFacultyID) throws DAOException{

		//Check for group in DB
		Group groupInDB = getGroup(name);
		if(groupInDB == null){
			log.log(Level.INFO, "Group "+name+" doesn't exist in DB!");
			throw new DAOException("Group "+name+" has not found.");
		}
		
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		
		String sql1 = "SELECT * "
				+ "FROM groups "
				+ "WHERE name='"+name+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to edit group is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				
				while(rSet1.next()){
					if(rSet1.getInt(3) == facultyID){
						log.log(Level.INFO, "Group "+name+" has found.");
						int groupID = rSet1.getInt(1);
						String sql2 = "UPDATE groups "
									+ "SET  name='"+newName+"', "
									+ "SET  facultyID='"+newFacultyID+"', "
									+ "WHERE groupID='"+groupID+"'";
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
						log.log(Level.INFO, "Group "+name+" has not found.");
						throw new DAOException("ResultSet returned nothing");
					}
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct");
			}
		}
		finally{
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

	public static void delete(String name, int facultyID) throws DAOException{
		
		//Check for group in DB
		Group groupInDB = getGroup(name);
		if(groupInDB == null){
			log.log(Level.INFO, "Group "+name+" doesn't exist in DB!");
			return;
		}
				
		Connection connection1 = null;
		PreparedStatement statement1 = null;
		ResultSet rSet1 = null;
		
		Connection connection2 = null;
		PreparedStatement statement2 = null;
		
		String sql1 = "SELECT * "
				+ "FROM groups "
				+ "WHERE name='"+name+"'";

		try{
			connection1 = ConnectionToDB.getConnectionToDB();
			if(connection1 != null){
				log.log(Level.INFO, "Connection is established.");
			} else {
				log.log(Level.SEVERE, "Connection is not established!");
				throw new DAOException("Connection to delete group is not established!");
			}
			try {
				statement1 = connection1.prepareStatement(sql1);
				rSet1 = statement1.executeQuery();
				
				while(rSet1.next()){
					if(rSet1.getInt(3) == facultyID){
						log.log(Level.INFO, "Group "+name+" has found.");

						//Control question									
						int n = JOptionPane.showConfirmDialog(null, "Do you want to delete group?", "This group exists!", JOptionPane.YES_NO_OPTION);
						if(n==1){
							log.log(Level.INFO, "Group "+name+" wasn't deleted!");
							return;
						} else {
							log.log(Level.INFO, "Group "+name+" deleted");
						}
						
						int groupID = rSet1.getInt(1);
						String sql2 = "DELETE FROM groups WHERE groupID='"+groupID+"'";
						connection2 = ConnectionToDB.getConnectionToDB();
						if(connection2 != null){
							log.log(Level.INFO, "Connection for delete is established.");
						} else {
							log.log(Level.SEVERE, "Connection for delete is not established!");
							throw new DAOException("Connection to delete group is not established!");
						}
						statement2 = connection2.prepareStatement(sql2);
						statement2.executeUpdate();
					} else {
						log.log(Level.INFO, "Group "+name+" has not found.");
						throw new DAOException("ResultSet returned nothing.");
					}
				}
			}catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
				throw new DAOException("SQL request isn't correct.");
			}
		}
		finally{
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
}
