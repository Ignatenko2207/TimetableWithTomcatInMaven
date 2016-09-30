package com.smarterama.timetable.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionToDB {
	
	static final String DBURL= "jdbc:postgresql://localhost:5432/universityDB";
	static final String DBUser = "postgres";
	static final String DBUserPassword= "248842";
	
	private static Logger log = Logger.getLogger(ConnectionToDB.class.getName());
	
	public static Connection getConnectionToDB() throws DAOException{
		Connection connection = null;
		try{
			connection = DriverManager.getConnection(DBURL, DBUser, DBUserPassword);
			if(connection!=null){
				return connection;
			}else{
				throw new DAOException("Connection is not established!");
			}
		}catch(Exception e){
			log.log(Level.WARNING, e.getMessage());
			throw new DAOException("Connection is not established!");				
		}
	}
}
