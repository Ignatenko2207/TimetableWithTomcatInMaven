package com.smarterama;

import java.util.ArrayList;

public class Faculty {
	
	String name = new String();
	ArrayList<Group> groups = new ArrayList<Group>();
	
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addGroup(Group group){
    	groups.add(group);
    }
    public void deleteGroup(Group group){
    	if(groups.isEmpty() == false){
    		for (Group item : groups) {
    			if(item.name == group.name){
    				groups.remove(item);
       			}
    		}
    	}
    }
}