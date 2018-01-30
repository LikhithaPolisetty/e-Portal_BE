package com.tabner.DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tabner.entities.NewUser;
import com.tabner.entities.WorkingHours;

@Component
public class SemiMonthlyInvoiceDAOImpl {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<NewUser> getEmployees(){
		System.out.println(".............................................................................................");
		String query = "select * from emd inner join vendors on emd.vendor_id = vendors.vendor_id where invoice_freq = 'SM'";
		return jdbcTemplate.query(query, new EmployeeInvoiceMapper());
		
	}
	
	public static final class EmployeeInvoiceMapper implements RowMapper<NewUser>{
		
		@Override
		public NewUser mapRow(ResultSet resultSet, int row) throws SQLException {
			
			NewUser newUser = new NewUser();
			newUser.setEmp_id(resultSet.getString(1));
			newUser.setFirst_name(resultSet.getString(2));
			newUser.setLast_name(resultSet.getString(3));
			newUser.setEmail_id(resultSet.getString(4));
			newUser.setMobile_num(resultSet.getString(5));
			newUser.setPassport(resultSet.getString(6));
			newUser.setVisa(resultSet.getString(7));
			newUser.setEducation(resultSet.getString(8));
			newUser.setExperience(resultSet.getString(9));
			newUser.setSkills(resultSet.getString(10));
			newUser.setAddress(resultSet.getString(11));
			newUser.setVendor_id(resultSet.getString(12));
			newUser.setInvoice_end_date(resultSet.getDate(13));
			
			return newUser;
		}
		
	}
	
	public List<WorkingHours> getEmpWorkingHours(String emp_id, String start_date, String end_date){
		String queryHours = "select * from working_hours where emp_id = ? AND date >= ? AND date <= ?";
		return jdbcTemplate.query(queryHours, new Object[] {emp_id, start_date, end_date}, new EmpWorkingHoursMapper());
	}
	
	public static final class EmpWorkingHoursMapper implements RowMapper<WorkingHours> {
		
		@Override
		public WorkingHours mapRow(ResultSet resultSet, int row) throws SQLException{
			WorkingHours hoursWorked = new WorkingHours();
			hoursWorked.setEmp_id(resultSet.getString(1));
			hoursWorked.setName(resultSet.getString(2));
			hoursWorked.setDate(resultSet.getString(3));
			hoursWorked.setHours(resultSet.getDouble(4));
			
			return hoursWorked;
		}
		
	}
	
	public float getBillRates(String emp_id, String date) {
		String queryBillRate = "select bill_rate from bill_rate where (emp_id = ?) AND (start_date <= ? AND end_date >= ?)";
		return jdbcTemplate.queryForObject(queryBillRate, new Object[] {emp_id, date, date}, float.class);
	}
	
	public int updateInvoiceEndDate(String end_date, String emp_id) {
		String queryUpdateInvEndDate = "UPDATE emd SET invoice_end_date = ? where emp_id = ?";
		return jdbcTemplate.update(queryUpdateInvEndDate, new Object[] {end_date, emp_id});
	}

}























