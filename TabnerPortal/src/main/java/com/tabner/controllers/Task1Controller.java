package com.tabner.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tabner.DAOImpl.EmployeeDAOImpl;
import com.tabner.DAOImpl.EmployeeRatesDAOImpl;
import com.tabner.DAOImpl.Task1EmployeeDAOImpl;
import com.tabner.entities.Employee;
import com.tabner.entities.EmployeeRates;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.NewUser;
import com.tabner.entities.Paydate;
import com.tabner.entities.Response;
import com.tabner.entities.Task1User;
import com.tabner.entities.Task1UserDetails;
import com.tabner.entities.User;
import com.tabner.metadata.Task1TokenGenerator;
import com.tabner.metadata.TimeBasedOneTimePasswordUtil;
import com.tabner.metadata.TokenGenerator;
import com.tabner.metadata.UserValidator;
import com.tabner.services.EmpToEmpSpecific;
import com.tabner.services.ExcelConverter;
import com.tabner.services.PdfReaderService;
import com.tabner.services.SmsService;
import com.tabner.services.Task1MailService;


/*
 * This class is exclusively used from the front end application(React Application) to login, sign up, retrieve employees, retrieve paydates
 */

//@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
public class Task1Controller implements ApplicationContextAware {
	
	@Autowired
	private FileUploadController fileUploadController;
	
	@Autowired
	private ExcelConverter excelConverter;
	
	@Autowired
	private EmpToEmpSpecific empToEmpSpecific;
	
	@Autowired
	private Task1EmployeeDAOImpl task1employeeDAOImpl;
	
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private Task1MailService task1MailService;
	
	
	private ApplicationContext context;
	

	
	public FileUploadController getFileUploadController() {
		return fileUploadController;
	}

	public void setFileUploadController(FileUploadController fileUploadController) {
		this.fileUploadController = fileUploadController;
	}
	
	public ExcelConverter getExcelConverter() {
		return excelConverter;
	}

	public void setExcelConverter(ExcelConverter excelConverter) {
		this.excelConverter = excelConverter;
	}
	
	public EmpToEmpSpecific getEmpToEmpSpecific() {
		return empToEmpSpecific;
	}

	public void setEmpToEmpSpecific(EmpToEmpSpecific empToEmpSpecific) {
		this.empToEmpSpecific = empToEmpSpecific;
	}


	
	public Task1MailService getTask1MailService() {
		return task1MailService;
	}

	public void setTask1MailService(Task1MailService task1MailService) {
		this.task1MailService = task1MailService;
	}

	public Task1EmployeeDAOImpl getTask1employeeDAOImpl() {
		return task1employeeDAOImpl;
	}

	public void setTask1employeeDAOImpl(Task1EmployeeDAOImpl task1employeeDAOImpl) {
		this.task1employeeDAOImpl = task1employeeDAOImpl;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		this.context = arg0;
	}

	
	

	
	
	/*
	 * this method checks the database to know whether the user is a valid user or not, And returns the response
	 */
	
	@RequestMapping(value = "/login1", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> login(@RequestBody User user) {
		 Response response = task1employeeDAOImpl.login(user.getUsername(), user.getPassword());
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/qrimg", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> qrImg(@RequestBody User user) {
		Response response = new Response();
		String secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();
		String qrimg = TimeBasedOneTimePasswordUtil.qrImageUrl(user.getUsername(), secret);
		boolean check = task1employeeDAOImpl.saveSecret(user.getUsername(), secret);
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(qrimg);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/removeqrimg", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> removeQrImg(@RequestBody User user) {
		Response response = new Response();
		boolean check = task1employeeDAOImpl.removeSecret(user.getUsername());
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/retrievedetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> retrieveDetails(@RequestBody User user) {
		Response response = new Response();
		Task1UserDetails task1UserDetails = task1employeeDAOImpl.retrieveDetails(user.getUsername());
		if(true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(task1UserDetails);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/savedetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> saveDetails(@RequestBody Task1UserDetails user) {
		System.out.println("ohhhh noooo");
		Response response = new Response();
		boolean check = task1employeeDAOImpl.saveDetails(user.getUsername(), user.getFirstname(), user.getLastname(), user.getPhone() );
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/savedetailswithoutotp", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> saveDetailsWithoutOtp(@RequestBody Task1UserDetails user) {
		System.out.println("-----------------");
		System.out.println("coming to savedetailswithoutotp");
		System.out.println("---------------------");
		Response response = new Response();
		boolean check = task1employeeDAOImpl.saveDetailsWithoutOtp(user.getUsername(), user.getFirstname(), user.getLastname(), user.getPhonekey() );
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/totp1", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> totp1(@RequestBody User user) {
		Response response = new Response();
		boolean check = task1employeeDAOImpl.totp1(user.getUsername(), user.getPassword());
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logout1", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> logOut(HttpServletRequest req) {
		System.out.println("reaching logout");
		System.out.println("---------------------");
		System.out.println(req.getHeader("tabner_token"));
		Response response = new Response();
		userValidator.removeToken(req.getHeader("tabner_token"));
		response.setResponse("logged out");
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	/*
	 * this method is used to create the login credentials for the new user
	 */
	@RequestMapping(value = "/signup1", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> signUp(@RequestBody User user) {
		Response response = new Response();
		String ver_token =  Task1TokenGenerator.generateToken();
		boolean check = task1employeeDAOImpl.signUp(user.getUsername(), user.getPassword(), ver_token );
		if(check == true) {
			task1MailService.sendMailToUser(user.getUsername(), ver_token );
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{username}/{key}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getDistinctPayDates(@PathVariable("username") String username, @PathVariable("key") String key, HttpServletRequest req, HttpServletResponse res) {
		System.out.println("hit it.........");
		Response response = new Response();
		String token = req.getHeader("tabner_token");
		  boolean isValidatedToken = userValidator.validateToken(token);
		if(true) {
			boolean check =  task1employeeDAOImpl.verifyMail(username, key);
			if(check) {
				try {
					res.sendRedirect("http://localhost:4200/success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println("not redirected");
			}
		}else {
			response.setResponse("No Token");
		}
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/insertphonekey", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> insertPhoneKey(@RequestBody User user) {
		System.out.println("..................");
		System.out.println("insert phone key called");
		Response response = new Response();
		Random rnd = new Random();
		int number = 100000 + rnd.nextInt(900000);
		System.out.println("----------------");
		System.out.println("generate random numbwer" + "...." + number);
		System.out.println("----------------");

		boolean check = task1employeeDAOImpl.insertPhoneKey(user.getUsername(), user.getPassword(), number);
		if(check) {
			SmsService.sendTextMessage(user.getPassword(), "Verification code is " + number);
		}
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	
	
	
	
	
}
