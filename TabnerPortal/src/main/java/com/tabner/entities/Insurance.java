package com.tabner.entities;

public class Insurance {

	private String plan;
	private double emp_con;
	
	
	
	public Insurance() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Insurance(String plan, double emp_con) {
		super();
		this.plan = plan;
		this.emp_con = emp_con;
	}
	
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public double getEmp_con() {
		return emp_con;
	}
	public void setEmp_con(double emp_con) {
		this.emp_con = emp_con;
	}
	
	
}
