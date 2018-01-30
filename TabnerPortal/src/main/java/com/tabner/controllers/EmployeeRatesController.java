package com.tabner.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tabner.DAOImpl.EmployeeRatesDAOImpl;
import com.tabner.entities.Employee;
import com.tabner.entities.EmployeeRates;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.Response;
import com.tabner.metadata.UserValidator;
import com.tabner.services.PdfReaderService;

@RestController
public class EmployeeRatesController {
	
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private EmployeeRatesDAOImpl employeeRatesDaoImpl;
	
	
	public UserValidator getUserValidator() {
		return userValidator;
	}


	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}


	public EmployeeRatesDAOImpl getEmployeeRatesDaoImpl() {
		return employeeRatesDaoImpl;
	}


	public void setEmployeeRatesDaoImpl(EmployeeRatesDAOImpl employeeRatesDaoImpl) {
		this.employeeRatesDaoImpl = employeeRatesDaoImpl;
	}
 

	@RequestMapping(value = "/employeerates", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getEmployeeRates(HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  List<EmployeeRates> list = employeeRatesDaoImpl.getEmployeeRates();
			response.setResponse(list);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	

	@RequestMapping(value = "/editemployeerate", method = RequestMethod.POST, produces = "application/json")
	public  ResponseEntity<Response> addEmployeeRate(@RequestBody EmployeeRates employeeRates, HttpServletRequest req) {
		Response response = new Response();
		
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  System.out.println("------------printing the rates from front end-----------------");
				System.out.println(employeeRates.getEmp_id());
				System.out.println(employeeRates.getFirst_name() +", " + employeeRates.getLast_name());
				System.out.println(employeeRates.getType());
				System.out.println(employeeRates.getPay_rate());
				System.out.println(employeeRates.getBill_rate());
				System.out.println(employeeRates.getStart_date());
				
				String start_dt = employeeRates.getStart_date();
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
				Date date = null;
				try {
					date = (Date)formatter.parse(start_dt);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(date);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, -1);
				Date dateBefore1Day = cal.getTime();
				System.out.println(dateBefore1Day);
				SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
				String old_date = newFormat.format(dateBefore1Day);
			  
			  
			  
			  
			List<EmployeeRates> list = employeeRatesDaoImpl.addEmployeeRate(employeeRates, old_date);
			 response.setResponse(list);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
		
		
		
		
		
		
		 
	}
	
	
}
