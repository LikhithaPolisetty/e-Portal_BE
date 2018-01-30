package com.tabner.entities;

public class NetEmployee {

	private String payDate;
	private String netCash;
	private String employeeTaxes;
	private String _401k;
	
	
	public NetEmployee() {
		super();
	}
	
	public NetEmployee(String payDate, String netCash, String employeeTaxes, String _401k) {
		super();
		this.payDate = payDate;
		this.netCash = netCash;
		this.employeeTaxes = employeeTaxes;
		this._401k = _401k;
	}
	
	
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getNetCash() {
		return netCash;
	}
	public void setNetCash(String netCash) {
		this.netCash = netCash;
	}
	public String getEmployeeTaxes() {
		return employeeTaxes;
	}
	public void setEmployeeTaxes(String employeeTaxes) {
		this.employeeTaxes = employeeTaxes;
	}

	public String get_401k() {
		return _401k;
	}

	public void set_401k(String _401k) {
		this._401k = _401k;
	}
	
	
}
