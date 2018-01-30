package com.tabner.DAOImpl;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import com.tabner.metadata.TimeBasedOneTimePasswordUtil;
import com.tabner.DAO.EmployeeDAO;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.NetEmployee;
import com.tabner.entities.Response;
import com.tabner.entities.Task1User;
import com.tabner.entities.Task1UserDetails;
import com.tabner.services.AutomaticPayrollPdfRead;
import com.tabner.services.MailService;

/*
 * this class provides the implementation for all the abstract methods present in EmployeeDAO class
 */

@Component
public class Task1EmployeeDAOImpl  {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MailService emailService;
	
	public MailService getEmailService() {
		return emailService;
	}

	public void setEmailService(MailService emailService) {
		this.emailService = emailService;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	


	/*
	 * This method returns true if the username and password are present in the database and returns false if they are not present
	 * @see com.tabner.DAO.EmployeeDAO#login(java.lang.String, java.lang.String)
	 */

	public Response login(String username, String password) {
		Response res= new Response();
		String queryCheck = "select count(*) from task1 where username = ? and password = ? " ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {username, password}, Integer.class);
		if(i > 0) {
			res.setResponse(true);
			String query = "select * from task1 where username = ? and password = ?";
			Task1User task1User = jdbcTemplate.queryForObject(query, new Object[] {username, password}, new UserMapper());
			res.setVerified(task1User.getVerified());
			res.setTfa(task1User.getTfa());
		}
			
		else {
			res.setResponse(false);
		}
		return res;	
	}
	
	private static final class UserMapper implements RowMapper<Task1User>{
		@Override
		public Task1User mapRow(ResultSet r, int arg1) throws SQLException {
			Task1User task1User = new Task1User();
			task1User.setUsername(r.getString(1));
			task1User.setPassword(r.getString(2));
			task1User.setVer_key(r.getString(3));
			task1User.setVerified(r.getString(4));
			task1User.setSecret_key(r.getString(5));
			task1User.setTfa(r.getString(6));
			task1User.setFirst_name(r.getString(7));
			task1User.setLast_name(r.getString(8));
			task1User.setPhone(r.getString(9));
			return task1User;
		}
		
	}
	
	public boolean totp1(String username, String password)  {	
		String queryCheck = "select secret_key from task1 where username = ?" ;
		String s = jdbcTemplate.queryForObject(queryCheck, new Object[] {username}, String.class);
		String secret = "";
		try {
			secret = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(s);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(secret.equals(password))
			return true;
		else
			return false;
	}
	
	
	public boolean insertPhoneKey(String username, String phone, int num)  {
		String queryCheck = " update task1 set phone = ?, phone_ver_key = ? where username = ?" ;
		int i = jdbcTemplate.update(queryCheck, new Object[] {phone, num, username});
		if(i == 1) {
			return true;
		} else
			return false;
	}
	
	public boolean saveDetails(String username, String firstname, String lastname, String phone)  {
		System.out.println("saveDetails");
		System.out.println(username);
		System.out.println(firstname);
		System.out.println(lastname);
		System.out.println(phone);
		String queryCheck = "update task1 set first_name = ?, last_name = ?, phone = ? where username = ?" ;
		int i = jdbcTemplate.update(queryCheck, new Object[] {firstname, lastname, phone, username});
		System.out.println("check here for upated value" + i);
		return true;
	}
	
	
	public boolean saveDetailsWithoutOtp(String username, String firstname, String lastname, String phonekey)  {
		System.out.println("saveDetailsWithoutOtp");
		System.out.println(username);
		System.out.println(firstname);
		System.out.println(lastname);
		System.out.println(phonekey);
		String checkKey = "select phone_ver_key from task1 where username = ?";
		String phone_ver_key = jdbcTemplate.queryForObject(checkKey, new Object[] {username}, String.class);
		if(phonekey.equals(phone_ver_key)) {
			String queryCheck = "update task1 set tfa = ?, first_name = ?, last_name = ? where username = ?" ;
			int i = jdbcTemplate.update(queryCheck, new Object[] {"N", firstname, lastname, username});
			return true;
		}else {
			String queryCheck = "update task1 set phone = ?, phone_ver_key = ? where username = ?" ;
			int i = jdbcTemplate.update(queryCheck, new Object[] {null, null, username});
			return false;
		}
	
	}
	
	public Task1UserDetails retrieveDetails(String username)  {
		String check = "select * from task1 where username = ?" ;
		 return jdbcTemplate.queryForObject(check, new Object[] { username}, new DetailsMapper());
		
	}
	
	private static final class DetailsMapper implements RowMapper<Task1UserDetails>{
		@Override
		public Task1UserDetails mapRow(ResultSet r, int arg1) throws SQLException {
			Task1UserDetails task1UserDetails = new Task1UserDetails();
			task1UserDetails.setUsername(r.getString(1));
			task1UserDetails.setFirstname(r.getString(7));
			task1UserDetails.setLastname(r.getString(8));
			task1UserDetails.setPhone(r.getString(9));
			return task1UserDetails;
		}
		
	}
	
	public boolean saveSecret(String username, String secret)  {
		String queryCheck = "update task1 set secret_key = ?, tfa = ? where username = ?" ;
		int i = jdbcTemplate.update(queryCheck, new Object[] {secret, "Y", username});
		System.out.println("check here for upated value" + i);
		return true;
	}
	
	public boolean removeSecret(String username)  {
		
		String queryCheck = "update task1 set secret_key = ?, tfa = ? where username = ?" ;
		int i = jdbcTemplate.update(queryCheck, new Object[] {null , "N" , username});
		System.out.println("check here for upated value" + i);
		return true;
	}
	
	/*
	 * This method inserts the new username and password into the database if they are not already present in it
	 * @see com.tabner.DAO.EmployeeDAO#signUp(java.lang.String, java.lang.String)
	 */
	public boolean signUp(String username, String password, String secret) {	
		String queryCheck = "select count(*) from task1 where username = ?" ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {username}, Integer.class);
		if(i > 0) {
			return false;
		}
		else {
			String queryInsert = "insert into task1(username, password, ver_key) values (?,?,?)" ;
			jdbcTemplate.update(queryInsert, new Object[] {username, password, secret});
			return true;
		}
		
	}
	
	public boolean verifyMail(String username, String key) {	
		String queryCheck = "select ver_key from task1 where username = ?" ;
		String ver_key = jdbcTemplate.queryForObject(queryCheck, new Object[] {username}, String.class);
		if(key.equals(ver_key)) {
			
			String query = "update task1 set verified = ? where username = ? ";
			jdbcTemplate.update(query, new Object[] {"Y", username});
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public boolean createNewUser(String paycorId, String firstName, String lastName, String email) {
		String queryCheck = "select count(*) from emails where paycor_id = ?" ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {paycorId}, Integer.class);
		if(i > 0) {
			return false;
		}
		else {
			System.out.println(paycorId + "....." + firstName + "..." + lastName + "...." + email);
			String query = "insert into emails values (?, ?, ?, ?)";
			 jdbcTemplate.update(query, new Object[] {paycorId, firstName, lastName, email});
			return true;
		}
		
	}
	
	public void saveNetEmployee(NetEmployee netEmployee) {
		String queryCheck = "select count(*) from net_employee where pay_date = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {netEmployee.getPayDate()}, Integer.class);
		if(i == 0) {
			String query = "insert into net_employee values (?,?,?,?)";
			jdbcTemplate.update(query, new Object[] {netEmployee.getPayDate(), netEmployee.getNetCash(), netEmployee.getEmployeeTaxes(), netEmployee.get_401k()});
			AutomaticPayrollPdfRead.check = true;
		}
	}
	
	
}
