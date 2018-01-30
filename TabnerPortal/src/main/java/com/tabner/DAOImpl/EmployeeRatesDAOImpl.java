package com.tabner.DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tabner.DAO.EmployeeRatesDAO;
import com.tabner.entities.EmployeeRates;
import com.tabner.entities.EmployeeSpecific;

@Component
public class EmployeeRatesDAOImpl implements EmployeeRatesDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}



	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}



	
	public List<EmployeeRates> getEmployeeRates() {
		String query = "SELECT s.*, pr.pay_rate, br.bill_rate\r\n" + 
				"FROM (SELECT t1.*\r\n" + 
				"FROM emp_type t1\r\n" + 
				"WHERE t1.start_date = (SELECT MAX(t2.start_date)\r\n" + 
				"                 FROM emp_type t2\r\n" + 
				"                 WHERE t2.emp_id = t1.emp_id)) s\r\n" + 
				"INNER JOIN bill_rate br\r\n" + 
				"    on s.emp_id = br.emp_id and s.start_date = br.start_date\r\n" + 
				"INNER JOIN pay_rate pr\r\n" + 
				"    on s.emp_id = pr.emp_id and  s.start_date = pr.start_date \r\n" + 
				" ";
		return jdbcTemplate.query(query, new EmployeeRatesMappper());
	}
	
	private static final class EmployeeRatesMappper implements RowMapper<EmployeeRates>{

		@Override
		public EmployeeRates mapRow(ResultSet resultSet, int row) throws SQLException {
			EmployeeRates employeeRates = new EmployeeRates();
			employeeRates.setEmp_id(resultSet.getString(1));
			employeeRates.setFirst_name(resultSet.getString(2));
			employeeRates.setLast_name(resultSet.getString(3));
			employeeRates.setType(resultSet.getString(4));
			employeeRates.setStart_date(resultSet.getDate(5).toString());
			employeeRates.setEnd_date(resultSet.getDate(6).toString());
			employeeRates.setPay_rate(resultSet.getDouble(7));
			employeeRates.setBill_rate(resultSet.getDouble(8));
			return employeeRates;
		}
		
	}
	
	public List<EmployeeRates> addEmployeeRate(EmployeeRates employeeRates, String old_date) {
		
		
		/*
		 * update queries for 3 tables
		 */
		String update_query = "update emp_type set end_date = ? where emp_id = ? and end_date = '2099-12-31' ";
		jdbcTemplate.update(update_query, new Object[] {old_date, employeeRates.getEmp_id()});
		
		String update_query1 = "update pay_rate set end_date = ? where emp_id = ? and end_date = '2099-12-31' ";
		jdbcTemplate.update(update_query1, new Object[] {old_date, employeeRates.getEmp_id()});
		
		String update_query2 = "update bill_rate set end_date = ? where emp_id = ? and end_date = '2099-12-31' ";
		jdbcTemplate.update(update_query2, new Object[] {old_date, employeeRates.getEmp_id()});
		

		/*
		 * insert queries for 3 tables
		 */

		String insert_query = "insert into emp_type values (?,?,?,?,?,?)";
		jdbcTemplate.update(insert_query, new Object[] { employeeRates.getEmp_id(), employeeRates.getFirst_name(), employeeRates.getLast_name(), employeeRates.getType(), employeeRates.getStart_date(), "2099-12-31" });
		
		String insert_query1 = "insert into pay_rate values (?,?,?,?)";
		jdbcTemplate.update(insert_query1, new Object[] { employeeRates.getEmp_id(), employeeRates.getPay_rate(), employeeRates.getStart_date(), "2099-12-31" });

		String insert_query2 = "insert into bill_rate values (?,?,?,?)";
		jdbcTemplate.update(insert_query2, new Object[] { employeeRates.getEmp_id(), employeeRates.getBill_rate(), employeeRates.getStart_date(), "2099-12-31" });
		
		return getEmployeeRates();
	}
	
	

}
