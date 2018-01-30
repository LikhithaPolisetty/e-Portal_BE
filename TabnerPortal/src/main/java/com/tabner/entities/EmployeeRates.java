package com.tabner.entities;

import java.util.Date;


public class EmployeeRates {
	
	
	private String emp_id;
	private String first_name;
	private String last_name;
	private String type;
	private String start_date;
	private String end_date;
	private Double pay_rate;
	private Double bill_rate;
	
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public Double getPay_rate() {
		return pay_rate;
	}
	public void setPay_rate(Double pay_rate) {
		this.pay_rate = pay_rate;
	}
	public Double getBill_rate() {
		return bill_rate;
	}
	public void setBill_rate(Double bill_rate) {
		this.bill_rate = bill_rate;
	}
	
	
	
	
	
	
}
