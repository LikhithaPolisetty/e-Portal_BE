package com.tabner.DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.tabner.entities.NewClient;

@Component
public class ClientDAOImpl {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<NewClient> getTabnerClients() {
		String query = "select * from client";
		return jdbcTemplate.query(query, new TabnerClientMappper());
	}

	private static final class TabnerClientMappper implements RowMapper<NewClient> {

		@Override
		public NewClient mapRow(ResultSet resultSet, int row) throws SQLException {
			NewClient newClient = new NewClient();
			newClient.setIdclient(resultSet.getInt(1));
			newClient.setClientname(resultSet.getString(2));
			newClient.setPhone(resultSet.getString(3));
			newClient.setEmail(resultSet.getString(4));
			newClient.setLocation(resultSet.getString(5));
			newClient.setDomain(resultSet.getString(6));

			return newClient;
		}

	}

	public boolean createNewClient(int idclient, String clientname, String phone, String email, String location,
			String domain) {
		String queryCheck = "select count(*) from client where clientname = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] { clientname }, Integer.class);
		if (i > 0) {
			return false;
		} else {
			String query = "insert into client values (?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(query, new Object[] { idclient, clientname, phone, email, location, domain });
			return true;
		}

	}

	public boolean deleteClient(int idclient) {
		System.out.println("------------------------------------------------------------------");
		System.out.println("Deleting client with ID: " + idclient);
		System.out.println("------------------------------------------------------------------");

		String queryCheck = "select count(*) from client where idclient = ?";
		Integer i = jdbcTemplate.queryForObject(queryCheck, new Object[] { idclient }, Integer.class);
		if (i > 0) {
			String query = "delete from client where idclient = ?";
			jdbcTemplate.update(query, new Object[] { idclient });
			return true;
		} else {
			return false;
		}
	}

}