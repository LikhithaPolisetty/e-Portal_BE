package com.tabner.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tabner.DAOImpl.ClientDAOImpl;
import com.tabner.entities.ClientDelete;
import com.tabner.entities.NewClient;
import com.tabner.entities.NewUser;
import com.tabner.entities.Response;
import com.tabner.metadata.UserValidator;


@RestController
public class ClientController {

	@Autowired
	private ClientDAOImpl clientDAOImpl;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value = "/tabnerclients", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getTabnerClients(HttpServletRequest req) {
		System.out
				.println("------------------------------------------------------------------------------------------");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			List<NewClient> list = clientDAOImpl.getTabnerClients();
			response.setResponse(list);
		} else {
			response.setResponse("No token available");
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/newclient", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> createNewClient(@RequestBody NewClient client, HttpServletRequest req) {
		System.out.println("printing new client: " + client.getIdclient() + client.getClientname() + client.getPhone()
				+ client.getEmail() + client.getLocation() + client.getDomain());
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			boolean check = clientDAOImpl.createNewClient(client.getIdclient(), client.getClientname(),
					client.getPhone(), client.getEmail(), client.getLocation(), client.getDomain());
			response.setResponse(check);
		} else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteclient", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> deleteClient(@RequestBody ClientDelete delClient, HttpServletRequest req) {
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Deleting Client");
		System.out.println("--------------------------------------------------------------------");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			boolean check = clientDAOImpl.deleteClient(delClient.getIdclient());
			response.setResponse(check);
		} else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}