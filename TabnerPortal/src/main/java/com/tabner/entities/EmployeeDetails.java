package com.tabner.entities;

import java.util.Date;

public class EmployeeDetails {
	
	private String emp_id;
	private String first_name;
	private String last_name;
	private String email_id;
	private String mobile_num;
	private String vendor_id;
	private Date invoice_end_date;
	private String ssn;
	private Date doj;
	private Date terminated_date;
	
	
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
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public String getMobile_num() {
		return mobile_num;
	}
	public void setMobile_num(String mobile_num) {
		this.mobile_num = mobile_num;
	}
	public String getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}
	public Date getInvoice_end_date() {
		return invoice_end_date;
	}
	public void setInvoice_end_date(Date invoice_end_date) {
		this.invoice_end_date = invoice_end_date;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public Date getDoj() {
		return doj;
	}
	public void setDoj(Date doj) {
		this.doj = doj;
	}
	public Date getTerminated_date() {
		return terminated_date;
	}
	public void setTerminated_date(Date terminated_date) {
		this.terminated_date = terminated_date;
	}

}
