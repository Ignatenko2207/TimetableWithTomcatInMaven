package com.smarterama.timetable.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOException extends Exception{
	
	public DAOException(String exception){
		Logger log = Logger.getLogger(DAOException.class.getName());
		log.log(Level.SEVERE, exception);
	}
}
