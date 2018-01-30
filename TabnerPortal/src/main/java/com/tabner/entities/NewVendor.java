package com.tabner.entities;

public class NewVendor {

	private String vendor_id;
	private String name;
	private String reg_state;
	private String invoice_freq;
	private String payment_freq;
	private String address_line_1;
	private String address_line_2;
	private String suite_apt;
	private String city;
	private String state;
	private String zipcode;
	private String status;

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReg_state() {
		return reg_state;
	}

	public void setReg_state(String reg_state) {
		this.reg_state = reg_state;
	}

	public String getInvoice_freq() {
		return invoice_freq;
	}

	public void setInvoice_freq(String invoice_freq) {
		this.invoice_freq = invoice_freq;
	}

	public String getPayment_freq() {
		return payment_freq;
	}

	public void setPayment_freq(String payment_freq) {
		this.payment_freq = payment_freq;
	}

	public String getAddress_line_1() {
		return address_line_1;
	}

	public void setAddress_line_1(String address_line_1) {
		this.address_line_1 = address_line_1;
	}

	public String getAddress_line_2() {
		return address_line_2;
	}

	public void setAddress_line_2(String address_line_2) {
		this.address_line_2 = address_line_2;
	}

	public String getSuite_apt() {
		return suite_apt;
	}

	public void setSuite_apt(String suite_apt) {
		this.suite_apt = suite_apt;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}