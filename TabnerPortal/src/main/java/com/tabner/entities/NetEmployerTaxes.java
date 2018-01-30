package com.tabner.entities;

public class NetEmployerTaxes {
	private String payDate;
	private String employerTaxes;
	
	
	
	public NetEmployerTaxes() {
		super();
	}

	public NetEmployerTaxes(String payDate, String employerTaxes) {
		super();
		this.payDate = payDate;
		this.employerTaxes = employerTaxes;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getEmployerTaxes() {
		return employerTaxes;
	}

	public void setEmployerTaxes(String employerTaxes) {
		this.employerTaxes = employerTaxes;
	}
	
	
}
