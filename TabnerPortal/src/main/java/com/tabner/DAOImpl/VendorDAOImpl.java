package com.tabner.DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tabner.entities.NewVendor;
import com.tabner.entities.VendorEmployees;
import com.tabner.entities.VendorInvoices;

@Component
public class VendorDAOImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<NewVendor> getTabnerVendors() {
		
		String query = "select * from vendors";
		return jdbcTemplate.query(query, new TabnerVendorMappper());
	}

	private static final class TabnerVendorMappper implements RowMapper<NewVendor> {

		@Override
		public NewVendor mapRow(ResultSet resultSet, int row) throws SQLException {
			NewVendor newVendor = new NewVendor();
			newVendor.setVendor_id(resultSet.getString(1));
			newVendor.setName(resultSet.getString(2));
			newVendor.setReg_state(resultSet.getString(3));
			newVendor.setInvoice_freq(resultSet.getString(4));
			newVendor.setPayment_freq(resultSet.getString(5));
			newVendor.setStatus(resultSet.getString(12));
			/*newVendor.setAddress_line_1(resultSet.getString(6));
			newVendor.setAddress_line_2(resultSet.getString(7));
			newVendor.setSuite_apt(resultSet.getString(8));
			newVendor.setCity(resultSet.getString(9));
			newVendor.setState(resultSet.getString(10));
			newVendor.setZipcode(resultSet.getString(11));*/

			return newVendor;
		}

	}

	public boolean createNewVendor(String id_number, String name, String reg_state, String invoice_freq,
			String payment_freq, String address_line1, String address_line2, String suite_apt, String city, String state, String zipcode) {
		String queryCheck = "select count(*) from vendors where name = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] { name }, Integer.class);
		if (i > 0) {
			return false;
		} else {
			String query = "insert into vendors values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(query,
					new Object[] { id_number, name, reg_state, invoice_freq, payment_freq, address_line1, address_line2, suite_apt, city, state, zipcode });
			return true;
		}

	}

	public boolean deleteVendor(String id_number) {
		System.out.println("------------------------------------------------------------------");
		System.out.println("Deleting vendor with ID: " + id_number);
		System.out.println("------------------------------------------------------------------");

		String queryCheck = "select count(*) from vendors where id_number = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] { id_number }, Integer.class);
		if (i > 0) {
			String query = "delete from vendors where id_number = ?";
			jdbcTemplate.update(query, new Object[] { id_number });
			return true;
		} else {
			return false;
		}
	}
	
	public List<VendorEmployees> getVendorEmployees(String vendor_id) {
		System.out.println("**********************************************************************************************************");
		String query = "SELECT emp_detail.emp_id, emp_detail.first_name, emp_detail.last_name, emp_detail.email_id, emp_detail.mobile_num FROM emp_detail INNER JOIN vendors ON emp_detail.vendor_id = vendors.vendor_id where vendors.vendor_id =?";		
		return jdbcTemplate.query(query, new VendorEmployeesMappper(), new Object[]{vendor_id});
	}

	private static final class VendorEmployeesMappper implements RowMapper<VendorEmployees> {

		@Override
		public VendorEmployees mapRow(ResultSet resultSet, int row) throws SQLException {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			VendorEmployees vendorEmployees = new VendorEmployees();
			vendorEmployees.setEmp_id(resultSet.getString(1));
			vendorEmployees.setFirst_name(resultSet.getString(2));
			vendorEmployees.setLast_name(resultSet.getString(3));
			vendorEmployees.setEmail_id(resultSet.getString(4));
			vendorEmployees.setMobile_num(resultSet.getString(5));

			return vendorEmployees;
		}

	}
	
	public List<VendorInvoices> getVendorInvoices(String vendor_id) {
		String query = "SELECT * FROM emp_invoices INNER JOIN vendors ON emp_invoices.vendor_id = vendors.vendor_id where vendors.vendor_id =?";
		return jdbcTemplate.query(query, new VendorInvoicesMappper(), new Object[]{vendor_id});
		
	}

	private static final class VendorInvoicesMappper implements RowMapper<VendorInvoices> {
		
		@Override
		public VendorInvoices mapRow(ResultSet resultSet, int row) throws SQLException {
			VendorInvoices vendorInvoices = new VendorInvoices();
			vendorInvoices.setInv_id(resultSet.getInt(1));
			vendorInvoices.setEmp_id(resultSet.getString(2));
			vendorInvoices.setEmp_name(resultSet.getString(3));
			vendorInvoices.setVendor_id(resultSet.getString(4));
			vendorInvoices.setVendor_name(resultSet.getString(5));
			vendorInvoices.setHours(resultSet.getDouble(6));
			vendorInvoices.setAmount(resultSet.getDouble(7));
			vendorInvoices.setStart_date(resultSet.getDate(8));
			vendorInvoices.setEnd_date(resultSet.getDate(9));

			return vendorInvoices;
		}

	}
	
	public List<NewVendor> getVendorAddress(String vendor_id) {
		String query = "SELECT address_line_1, address_line_2, suite_apt, city, state, zipcode FROM vendors where vendor_id =?";
		return jdbcTemplate.query(query, new VendorAddressMappper(), new Object[]{vendor_id});
		
	}

	private static final class VendorAddressMappper implements RowMapper<NewVendor> {
		
		@Override
		public NewVendor mapRow(ResultSet resultSet, int row) throws SQLException {
			NewVendor newVendor = new NewVendor();
			newVendor.setAddress_line_1(resultSet.getString(1));
			newVendor.setAddress_line_2(resultSet.getString(2));
			newVendor.setSuite_apt(resultSet.getString(3));
			newVendor.setCity(resultSet.getString(4));
			newVendor.setState(resultSet.getString(5));
			newVendor.setZipcode(resultSet.getString(6));

			return newVendor;
		}

	}

}