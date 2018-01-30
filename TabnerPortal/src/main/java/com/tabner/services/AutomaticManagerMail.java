package com.tabner.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.Mail;
import com.tabner.entities.NetEmployee;
import com.tabner.entities.NetEmployerTaxes;

@Service
@EnableScheduling
public class AutomaticManagerMail {	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MailService emailService;
	@Autowired
	private EmpToEmpSpecific empToEmpSpecific;
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	public MailService getEmailService() {
		return emailService;
	}


	public void setEmailService(MailService emailService) {
		this.emailService = emailService;
	}


	public EmpToEmpSpecific getEmpToEmpSpecific() {
		return empToEmpSpecific;
	}


	public void setEmpToEmpSpecific(EmpToEmpSpecific empToEmpSpecific) {
		this.empToEmpSpecific = empToEmpSpecific;
	}


	@Scheduled(fixedRate = 10000)
	public void sendMail() {
		if(AutomaticPayrollPdfRead.check && AutomaticExcelRead.check) {
			String query1 = "select * from net_employee where pay_date = ? ";
			String query2 = "select * from net_employer_taxes where pay_date = ?";
			NetEmployee netEmployee = jdbcTemplate.queryForObject(query1, new Object[] {empToEmpSpecific.payDate}, new NetEmployeeMappper() );
			NetEmployerTaxes netEmployerTaxes = jdbcTemplate.queryForObject(query2, new Object[] {empToEmpSpecific.payDate}, new NetEmployerTaxesMappper());
			Mail mail = new Mail();
			mail.setNetCash(netEmployee.getNetCash());
			mail.setEmployeeTaxes(netEmployee.getEmployeeTaxes());
			mail.setEmployerTaxes(netEmployerTaxes.getEmployerTaxes());
			mail.set_401k(netEmployee.get_401k());
			emailService.sendMailToManager(mail);
		}
	}
	
	private static final class NetEmployeeMappper implements RowMapper<NetEmployee>{

		@Override
		public NetEmployee mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			NetEmployee netEmployee = new NetEmployee();
			netEmployee.setNetCash(resultSet.getString("net_cash"));
			netEmployee.setEmployeeTaxes(resultSet.getString("employee_taxes"));
			netEmployee.setPayDate(resultSet.getString("pay_date"));
			netEmployee.set_401k(resultSet.getString("401k"));
			
			return netEmployee;
		}
		
	}
	
	private static final class NetEmployerTaxesMappper implements RowMapper<NetEmployerTaxes>{

		@Override
		public NetEmployerTaxes mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			NetEmployerTaxes netEmployerTaxes = new NetEmployerTaxes();
			netEmployerTaxes.setEmployerTaxes(resultSet.getString("employer_taxes"));
			netEmployerTaxes.setPayDate(resultSet.getString("pay_date"));
			return netEmployerTaxes;
		}
		
	}
	
}
