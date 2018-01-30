package com.tabner.DAOImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tabner.DAO.EmployerTaxesDAO;
import com.tabner.entities.EmployerTaxes;
import com.tabner.entities.NetEmployerTaxes;
import com.tabner.services.AutomaticExcelRead;

@Component
public class EmployerTaxesDAOImpl implements EmployerTaxesDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void save(List<EmployerTaxes> emp) {
		String queryCheck = "select count(*) from employer_taxes where emp_id = ? and pay_date = ? " ;
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {emp.get(0).getEmpId(), emp.get(0).getPaydate()}, Integer.class);
		System.out.println("no of records " + i);
		
		if(i == 0) {
		for(EmployerTaxes e: emp) {
				
				String query="insert into employer_taxes values(?,?,?,?,?,?,?,?,?)"; 
				jdbcTemplate.update(query, new Object[] {e.getEmpId(), e.getPaydate(), e.getName(), e.getFui(), e.getMeder(), e.getSocer(), e.getState(), e.getAmount(), e.getAddt()});
				
				
				/*try {
					String email = jdbcTemplate.queryForObject(mail_query, new Object[] {e.getId()}, String.class);
					if(!email.isEmpty()) {
						emailService.sendMail("rohit.addagalla@tabnergc.com", email, "Your Salary Has Been Credited", e);
					}
				}
				catch (EmptyResultDataAccessException exception) {
					
				}*/
			}
		}
		
	}

	@Override
	public List<EmployerTaxes> getEmployeeList(String payDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void saveNetEmployerTaxes(NetEmployerTaxes netEmployerTaxes) {
		String queryCheck = "select count(*) from net_employer_taxes where pay_date = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] {netEmployerTaxes.getPayDate()}, Integer.class);
		if(i == 0) {
			String query = "insert into net_employer_taxes values (?,?)";
			jdbcTemplate.update(query, new Object[] {netEmployerTaxes.getPayDate(), netEmployerTaxes.getEmployerTaxes()});
			AutomaticExcelRead.check = true;
		}
	}
}
