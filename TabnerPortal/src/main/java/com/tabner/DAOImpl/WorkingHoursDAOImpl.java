package com.tabner.DAOImpl;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.Insurance;
import com.tabner.entities.WorkingHours;

/*
 * this class manages the working_hours and vacation_hours tables in the database
 */
@Component
public class WorkingHoursDAOImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * this method takes in the list of WorkingHours entities if there is any new
	 * entity which it inserts that entity in both working_hours and vacation_hours
	 * tables otherwise, it inserts the entity in the working_hours table and
	 * updates the record in the vacation_hours table
	 */
	public void save(List<WorkingHours> hours) {

		// checking whether this is a fresh excel sheets or not

		String queryCheck = "select count(*) from working_hours where emp_id = ? and date = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck,
				new Object[] { hours.get(0).getEmp_id(), hours.get(0).getDate() }, Integer.class);
		System.out.println("no of records in working hours .... " + i);

		// if the excel coming is a fresh excel sheet
		if (i == 0) {
			for (WorkingHours wh : hours) {

				String queryCheck1 = "select count(*) from vacation_hours where emp_id = ?";
				Integer i1 = jdbcTemplate.queryForObject(queryCheck1, new Object[] { wh.getEmp_id() }, Integer.class);

				// if the record is a new one

				if (i1 == 0) {
					String queryForWH = "insert into working_hours values (?,?,?,?)";
					jdbcTemplate.update(queryForWH,
							new Object[] { wh.getEmp_id(), wh.getName(), wh.getDate(), wh.getHours() });

					String query = "insert into vacation_hours values(?,?,?)";
					jdbcTemplate.update(query, new Object[] { wh.getEmp_id(), wh.getHours() * 0.0769, 0 });
				}
				// if the record already exists
				else {
					String queryForWH = "insert into working_hours values (?,?,?,?)";
					jdbcTemplate.update(queryForWH,
							new Object[] { wh.getEmp_id(), wh.getName(), wh.getDate(), wh.getHours() });

					String queryCheck2 = "select hours_remaining from vacation_hours where emp_id = ?";
					Double remaining_hours = jdbcTemplate.queryForObject(queryCheck2, new Object[] { wh.getEmp_id() },
							Double.class);
					Double updated_remaining_hours = remaining_hours + (wh.getHours() * 0.0769);

					String queryCheck3 = "update vacation_hours set hours_remaining = ? where emp_id = ?";
					jdbcTemplate.update(queryCheck3, new Object[] { updated_remaining_hours, wh.getEmp_id() });
				}

			}
			System.out.println("--------------------------------");
			System.out.println("data inserted succesfully");
			System.out.println("--------------------------------");
		}

	}
	
	public String checkEmployeeType(String emp_id, String date) {
		
		String query = "select type from emp_type where emp_id = ? and start_date <= ? and end_date >= ?";
		return jdbcTemplate.queryForObject(query, new Object[] {emp_id, date, date},String.class);
		
	}
	
	public double getPayRate(String emp_id, String date) {
		String query = "select pay_rate from pay_rate where emp_id = ? and start_date <= ? and end_date >= ?";
		return jdbcTemplate.queryForObject(query, new Object[] {emp_id, date, date}, Double.class);
	}
	
	public double getAvailableVacation(String emp_id) {
		String query = "select hours_remaining from vacation_hours where emp_id = ? ";
		return jdbcTemplate.queryForObject(query, new Object[] {emp_id}, Double.class);
	}
	
	public double getUsedVacation(String emp_id) {
		String query = "select hours_used from vacation_hours where emp_id = ? ";
		return jdbcTemplate.queryForObject(query, new Object[] {emp_id}, Double.class);
	}
	
	public void updateVactionHours(String emp_id, double hours_remaining, double hours_used) {
		String query = "update vacation_hours set hours_remaining = ? , hours_used = ? where emp_id = ?";
		jdbcTemplate.update(query, new Object[] {hours_remaining, hours_used, emp_id});
	}
	public boolean checkHoliday(String date) {
		String queryCheck = "select count(*) from holiday where date = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck,
				new Object[] { date }, Integer.class);
		if (i == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	//--------------------------------------------------------------------------------------------
	//					insurance part
	//--------------------------------------------------------------------------------------------
	
	public Insurance getMedInsuranceDetails(String emp_id, String date) {
		String queryCheck = "select count(*) from med_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
		Integer i = jdbcTemplate.queryForObject(queryCheck,
				new Object[] { emp_id, date, date, date, date, date, date, date }, Integer.class);
		if(i == 1) {
			String query = "select plan, emp_con from med_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
			return jdbcTemplate.queryForObject(query, new Object[] { emp_id, date, date, date, date, date, date, date }, new InsuranceMapper() );
		} else {
			return new Insurance("NA", 0.0);
		}
	}
	
	public Insurance getDenInsuranceDetails(String emp_id, String date) {
		String queryCheck = "select count(*) from den_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
		Integer i = jdbcTemplate.queryForObject(queryCheck,
				new Object[] { emp_id, date, date, date, date, date, date, date }, Integer.class);
		if(i == 1) {
			String query = "select plan, emp_con from den_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
			return jdbcTemplate.queryForObject(query, new Object[] { emp_id, date, date, date, date, date, date, date }, new InsuranceMapper() );
		} else {
			return new Insurance("NA", 0.0);
		}
	}
	
	
	private static final class InsuranceMapper implements RowMapper<Insurance>{
		@Override
		public Insurance mapRow(ResultSet r, int arg1) throws SQLException {
			Insurance i = new Insurance ();
			i.setPlan(r.getString(1));
			i.setEmp_con(r.getDouble(2));
			return i ;
		}
		
	}
	
	public Insurance getVisInsuranceDetails(String emp_id, String date) {
		String queryCheck = "select count(*) from vis_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
		Integer i = jdbcTemplate.queryForObject(queryCheck,
				new Object[] { emp_id, date, date, date, date, date, date, date }, Integer.class);
		if(i == 1) {
			String query = "select plan, emp_con from vis_ins where emp_id = ? and plan_start_date <= ? and plan_end_date >= ? and ((susp_start_date > ? and susp_end_date > ?) or (susp_start_date < ? and susp_end_date < ?) or (susp_start_date is null and susp_end_date is null)) and (term_date is null or term_date > ?)";
			return jdbcTemplate.queryForObject(query, new Object[] { emp_id, date, date, date, date, date, date, date }, new InsuranceMapper() );
		} else {
			return new Insurance("NA", 0.0);
		}
	}
	
	public double getPlanPrice(String plan, String date) {
			String query = "select amount from insurance where plan_name = ? and start_date <= ? and end_date >= ?";
			return jdbcTemplate.queryForObject(query, new Object[] {plan, date, date}, Double.class);
	}
	
	
}
