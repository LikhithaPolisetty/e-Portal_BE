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

import com.tabner.DAO.EmployeeDAO;
import com.tabner.entities.EmployeeDetails;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.NetEmployee;
import com.tabner.entities.NewUser;
import com.tabner.entities.NewVendor;
import com.tabner.entities.UserRole;
import com.tabner.metadata.TimeBasedOneTimePasswordUtil;
import com.tabner.services.AutomaticPayrollPdfRead;
import com.tabner.services.MailService;

/*
 * this class provides the implementation for all the abstract methods present in EmployeeDAO class
 */

@Component
public class EmployeeDAOImpl implements EmployeeDAO {
	
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
	 * this method takes in list of EmployeeSpecific objects and saves them into the employee table in the database
	 * @see com.tabner.DAO.EmployeeDAO#save(java.util.List)
	 * 
	 */
	
	public void save(List<EmployeeSpecific> emp) {
		String queryCheck = "select count(*) from employee where emp_id = ? and period_ending = ? " ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {emp.get(0).getId(), emp.get(0).getPeriodEnding()}, Integer.class);
		System.out.println("no of records " + i);
		System.out.println(emp.get(0).getId() + emp.get(0).getFirstName());
		if(i == 0) {
		for(EmployeeSpecific e: emp) {
				
				String query="insert into employee values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
				jdbcTemplate.update(query, new Object[] {e.getId(), e.getFirstName(), e.getLastName(), e.getRate(), e.getHours(), e.getEarnings(), e.getExpenses(), e.getGross(), e.getSoc(), e.getMed(), e.getFitwh(), e.getState(), e.getAddState(), e.getAdvance(), e.getMed125(), e.getPartial(), e.get_401k(), e.getNet(), e.getNetpay(), e.getPeriodEnding(), e.getPayDate(), e.getMisc()});
				String mail_query = "select email from emails where paycor_id = ?";
				
					if(e.getId().equals("EE#244")) {
						try {
							
							String email = jdbcTemplate.queryForObject(mail_query, new Object[] {e.getId()}, String.class);
							if(!email.isEmpty()) {
								emailService.sendMail("rohit.addagalla@tabnergc.com", email, "Your Salary Has Been Credited", e);
							}
						}
						catch (EmptyResultDataAccessException exception) {
							
						}
					}
				}
			
		}
	}
	
	
	/*
	 * this method takes the payDate as input and returns the list of EmployeeSpecific objects belonging to that paydate
	 * @see com.tabner.DAO.EmployeeDAO#getEmployeeList(java.lang.String)
	 */

	@Override
	public List<EmployeeSpecific> getEmployeeList(String payDate) {
		
		String query = "select * from employee where pay_date = ? order by last_name";
		return jdbcTemplate.query(query, new Object[] {payDate}, new EmployeeMappper());
	}
	
	private static final class EmployeeMappper implements RowMapper<EmployeeSpecific>{

		@Override
		public EmployeeSpecific mapRow(ResultSet r, int arg1) throws SQLException {
			EmployeeSpecific e = new EmployeeSpecific();
			e.setId(r.getString(1));
			e.setFirstName(r.getString(2));
			e.setLastName(r.getString(3));
			e.setRate(r.getString(4));
			e.setHours(r.getString(5));
			e.setEarnings(r.getString(6));
			e.setExpenses(r.getString(7));
			e.setGross(r.getString(8));
			e.setSoc(r.getString(9));
			e.setMed(r.getString(10));
			e.setFitwh(r.getString(11));
			e.setState(r.getString(12));
			e.setAddState(r.getString(13));
			e.setAdvance(r.getString(14));
			e.setMed125(r.getString(15));
			e.setPartial(r.getString(16));
			e.set_401k(r.getString(17));
			e.setNet(r.getString(18));
			e.setNetpay(r.getString(19));
			e.setPeriodEnding(r.getString(20));
			e.setPayDate(r.getString(21));
			e.setMisc(r.getString(22));
			return e;
		}
		
	}

	public void printAll(List<EmployeeSpecific> list) {
		for(EmployeeSpecific e: list) {
			System.out.println("empid: " + e.getId());
			System.out.println("f name: " + e.getFirstName());
			System.out.println("l name: " + e.getLastName());
			System.out.println("hours: " + e.getHours());
			System.out.println("rate: " + e.getRate());
			System.out.println("earnings: " + e.getEarnings());
			System.out.println("expenses: " + e.getExpenses());
			System.out.println("gross: " + e.getGross());
			System.out.println("soc: " + e.getSoc());
			System.out.println("med: " + e.getMed());
			System.out.println("fitwh: " + e.getFitwh());
			System.out.println("state: " + e.getState());
			System.out.println("additional: " + e.getAddState());
			System.out.println("med125: " + e.getMed125());
			System.out.println("partials: " + e.getPartial());
			System.out.println("401k: " + e.get_401k());
			System.out.println("Net: " + e.getNet());
			System.out.println("NetPay: " + e.getNetpay());
			System.out.println("paydate: " + e.getPayDate());
			System.out.println("period ending: "+ e.getPeriodEnding());
			System.out.println("----------------------------------------");
			System.out.println("----------------------------------------");

		}

	}
	
	/*
	 * this method returns all the distinct paydates present in the employee table in the database
	 * @see com.tabner.DAO.EmployeeDAO#getDistinctPayDates()
	 */

	@Override
	public List<String> getDistinctPayDates() {
		String query = "select distinct pay_date from employee";
		return jdbcTemplate.query(query, new PayDateMappper());
	}
	
	private static final class PayDateMappper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet r, int arg1) throws SQLException {
			String s = r.getString(1);
			return s;
		}

	}

	/*
	 * This method returns true if the username and password are present in the database and returns false if they are not present
	 * @see com.tabner.DAO.EmployeeDAO#login(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean login(String username, String password) {	
		String queryCheck = "select count(*) from admin where username = ? and password = ? " ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {username, password}, Integer.class);
		if(i > 0) {
			return true;
		}	
		else
			return false;
	}
	
	public boolean totp(String username, String password)  {	
		String queryCheck = "select secret from admin where username = ?" ;
		String s = jdbcTemplate.queryForObject(queryCheck, new Object[] {username}, String.class);
		String secret = "";
		try {
			secret = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(s);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------------------------");
		System.out.println("secrets are:: " + password +"......" + secret);
		System.out.println("------------------------");

		if(secret.equals(password)) {
			System.out.println("------------------------");
			System.out.println("secrets are:: " + password +"......" + secret);
			System.out.println("------------------------");
			return true;
		}
			
		else
			return true;
	}
	
	/*
	 * This method inserts the new username and password into the database if they are not already present in it
	 * @see com.tabner.DAO.EmployeeDAO#signUp(java.lang.String, java.lang.String)
	 */
	public boolean signUp(String username, String password, String secret, String user_role, String emp_id) {	
		String queryCheck = "select count(*) from admin where username = ? AND emp_id = ?" ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {username, emp_id}, Integer.class);
		if(i > 0) {
			return false;
		}
		else {
			String queryInsert = "insert into admin values (?,?,?,?,?)" ;
			jdbcTemplate.update(queryInsert, new Object[] {username, password, secret, user_role, emp_id});
			return true;
		}
		
	}
	
	public boolean createNewUser(String empId, String firstName, String lastName, String email, String mobile) {
		String queryCheck = "select count(*) from emd where emp_id = ?" ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {empId}, Integer.class);
		if(i > 0) {
			return false;
		}
		else {
			String query = "insert into emd values (?, ?, ?, ?, ?)";
			 jdbcTemplate.update(query, new Object[] {empId, firstName, lastName, email, mobile});
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
	
	
	public List<EmployeeDetails> getTabnerEmployees() {
		String query = "select * from emd where terminated_date is NULL order by first_name";
		return jdbcTemplate.query(query, new TabnerEmployeeMappper());
	}
	
	private static final class TabnerEmployeeMappper implements RowMapper<EmployeeDetails>{

		@Override
		public EmployeeDetails mapRow(ResultSet resultSet, int row) throws SQLException {
			EmployeeDetails employeeDetails = new EmployeeDetails();
			employeeDetails.setEmp_id(resultSet.getString(1));
			employeeDetails.setFirst_name(resultSet.getString(2));
			employeeDetails.setLast_name(resultSet.getString(3));
			employeeDetails.setEmail_id(resultSet.getString(4));
			employeeDetails.setMobile_num(resultSet.getString(5));
			String ssn = resultSet.getString(8);
			String[] ssn_split = ssn.split("-");			
			employeeDetails.setSsn("***-**-"+ssn_split[2]);
			employeeDetails.setDoj(resultSet.getDate(9));
			
			return employeeDetails;
		}
		
	}
	
	public List<EmployeeDetails> getInactiveEmployees() {
		String query = "select * from emd where terminated_date is NOT NULL order by first_name";
		return jdbcTemplate.query(query, new InactiveEmployeeMappper());
	}
	
	private static final class InactiveEmployeeMappper implements RowMapper<EmployeeDetails>{

		@Override
		public EmployeeDetails mapRow(ResultSet resultSet, int row) throws SQLException {
			EmployeeDetails employeeDetails = new EmployeeDetails();
			employeeDetails.setEmp_id(resultSet.getString(1));
			employeeDetails.setFirst_name(resultSet.getString(2));
			employeeDetails.setLast_name(resultSet.getString(3));
			employeeDetails.setEmail_id(resultSet.getString(4));
			employeeDetails.setMobile_num(resultSet.getString(5));
			String ssn = resultSet.getString(8);
			String[] ssn_split = ssn.split("-");			
			employeeDetails.setSsn("***-**-"+ssn_split[2]);
			employeeDetails.setDoj(resultSet.getDate(9));
			employeeDetails.setTerminated_date(resultSet.getDate(10));
			
			return employeeDetails;
		}
		
	}
	
	public EmployeeDetails getDefaultTabnerEmployee(String email) {
		System.out.println("mail id coming from frnt end is " + email);
		String query = "select * from emd where email_id = ?";
		return jdbcTemplate.queryForObject(query, new Object[] {email}, new TabnerEmployeeMappper());
	}
	
	public boolean createNewEmployee(String emp_id, String first_name, String last_name, String email_id,String mobile_num, String passport, String visa, String education, String experience, String skills, String address, String vendor_id) {
		String queryCheck = "select count(*) from emd where emp_id = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] { emp_id }, Integer.class);
		if (i > 0) {
			return false;
		} else {
			String query = "insert into emd values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(query,
					new Object[] { emp_id, first_name, last_name, email_id, mobile_num, passport, visa, education, experience, skills, address, vendor_id });
			String queryStatus = "select status from vendors INNER JOIN emd ON vendors.name = emd.vendor_name where vendor_name = ?";
			if(queryStatus == "In-active") {
				NewVendor newVendor = new NewVendor();
				newVendor.setStatus("Active");
			}
			return true;
		}

	}
	
	public UserRole getUserRole(String username) {
		System.out.println("mail id coming from frnt end is " + username);
		String queryRole = "select * from admin where username = ?";
		return jdbcTemplate.queryForObject(queryRole, new Object[] {username}, new UserRoleMapper());
	}
	
	private static final class UserRoleMapper implements RowMapper<UserRole>{

		@Override
		public UserRole mapRow(ResultSet resultSet, int row) throws SQLException {
			UserRole userRole = new UserRole();
			userRole.setUser_role(resultSet.getString(4));
			userRole.setEmp_id(resultSet.getString(5));		
		
			return userRole;
		}
		
	}
}
