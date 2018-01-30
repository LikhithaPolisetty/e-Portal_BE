package com.tabner.DAO;

import java.util.List;

import com.tabner.entities.EmployerTaxes;

public interface EmployerTaxesDAO {
	
	public void save(List<EmployerTaxes> e);
	
	public List<EmployerTaxes> getEmployeeList(String payDate);
	
	
}