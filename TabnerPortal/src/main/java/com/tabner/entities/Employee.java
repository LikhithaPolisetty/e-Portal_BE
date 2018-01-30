package com.tabner.entities;


import java.util.ArrayList;
import java.util.List;

/*
 * this is a simple pojo class to create objects for different employees present the payroll pdf
 */

public class Employee {
	
	private String name;
	private String id;
	private String rate;
	private String hours;
	private String earnings;
	private String expenses;
	private String gross;
	private List<String> taxes = new ArrayList<String> ();
	private List<String> deductions= new ArrayList<String> ();
	private List<String> netPay = new ArrayList<String> ();
	private String periodEnding;
	
	
	private String payDate;
	

	
	public String getPeriodEnding() {
		return periodEnding;
	}
	public void setPeriodEnding(String periodEnding) {
		this.periodEnding = periodEnding;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getEarnings() {
		return earnings;
	}
	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}
	public String getExpenses() {
		return expenses;
	}
	public void setExpenses(String expenses) {
		this.expenses = expenses;
	}
	public String getGross() {
		return gross;
	}
	public void setGross(String gross) {
		this.gross = gross;
	}
	public List<String> getTaxes() {
		return taxes;
	}
	public void setTaxes(List<String> taxes) {
		this.taxes = taxes;
	}
	public List<String> getDeductions() {
		return deductions;
	}
	public void setDeductions(List<String> deductions) {
		this.deductions = deductions;
	}
	public List<String> getNetPay() {
		return netPay;
	}
	public void setNetPay(List<String> netPay) {
		this.netPay = netPay;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	
	
	
	
	
	
}

