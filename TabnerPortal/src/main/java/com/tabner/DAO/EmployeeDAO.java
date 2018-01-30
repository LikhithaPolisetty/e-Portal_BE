package com.tabner.DAO;

import java.util.List;

/*
 * this class lists all the methods required for our database operations in our application
 */

import com.tabner.entities.EmployeeSpecific;

public interface EmployeeDAO {
	
	public void save(List<EmployeeSpecific> e);
	
	public List<EmployeeSpecific> getEmployeeList(String payDate);
	
	public List<String> getDistinctPayDates();
	
	public boolean login(String username, String password);
	
	public boolean signUp(String username, String password, String secret, String user_role, String emp_id);
	
}
