package com.tabner.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tabner.DAOImpl.EmployeeDAOImpl;
import com.tabner.entities.Employee;
import com.tabner.entities.EmployeeDetails;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.NewUser;
import com.tabner.entities.Paydate;
import com.tabner.entities.Response;
import com.tabner.entities.User;
import com.tabner.entities.UserRole;
import com.tabner.metadata.TimeBasedOneTimePasswordUtil;
import com.tabner.metadata.TokenGenerator;
import com.tabner.metadata.UserValidator;
import com.tabner.services.EmpToEmpSpecific;
import com.tabner.services.ExcelConverter;
import com.tabner.services.PdfReaderService;


/*
 * This class is exclusively used from the front end application(React Application) to login, sign up, retrieve employees, retrieve paydates
 */



@RestController
public class EmployeeController implements ApplicationContextAware {
	
	@Autowired
	private FileUploadController fileUploadController;
	
	@Autowired
	private ExcelConverter excelConverter;
	
	@Autowired
	private EmpToEmpSpecific empToEmpSpecific;
	
	@Autowired
	private EmployeeDAOImpl employeeDAOImpl;
	
	
	@Autowired
	private UserValidator userValidator;
	
	
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


	public EmployeeDAOImpl getEmployeeDAOImpl() {
		return employeeDAOImpl;
	}

	public void setEmployeeDAOImpl(EmployeeDAOImpl employeeDAOImpl) {
		this.employeeDAOImpl = employeeDAOImpl;
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		this.context = arg0;
	}
	

	/*
	 * this method retrieves the list of employees from the database based on paydate 
	 */
	@RequestMapping(value = "/employees", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> getEmployeeList(@RequestBody Paydate paydate, HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		System.out.println("token is" + token);
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  List<EmployeeSpecific> l = employeeDAOImpl.getEmployeeList(paydate.getDate());
				/*try {
					excelConverter.writeToExcel(l);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				response.setResponse(l);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	/*
	 * this method is used to read the payroll pdf and  save the list of employees into the database
	 */
	@RequestMapping(value = "/save", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> save(HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  PdfReaderService service = context.getBean("pdfReaderService", PdfReaderService.class);
				List<Employee> list = service.readPdf(FileUploadController.UPLOADED_FOLDER + FileUploadController.fileName);
				List<EmployeeSpecific> empList = empToEmpSpecific.convert(list);
				employeeDAOImpl.save(empList);
				response.setResponse("saved successfully");
				try {
					excelConverter.writeToExcel(empList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	/*
	 * this method is used to retrieve the distinct paydates from the database
	 */
	@RequestMapping(value = "/paydates", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getDistinctPayDates(HttpServletRequest req) {
		System.out.println("hit it.........");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		if(isValidatedToken) {
			List<String> payDates =  employeeDAOImpl.getDistinctPayDates();
			response.setResponse(payDates);
		}else {
			response.setResponse("No Token");
		}
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	
	/*
	 * this method checks the database to know whether the user is a valid user or not, And returns the response
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> login(@RequestBody User user) {
		Response response = new Response();
		boolean check = employeeDAOImpl.login(user.getUsername(), user.getPassword());
		
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/totp", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> totp(@RequestBody User user) {
		Response response = new Response();
		boolean check = employeeDAOImpl.totp(user.getUsername(), user.getPassword());
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
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
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> signUp(@RequestBody UserRole user) {
		Response response = new Response();
		String secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();
		String user_role = "user";
		System.out.println("......................................................" + user.getEmp_id() + user.getUsername() + user.getPassword());
		boolean check = employeeDAOImpl.signUp(user.getUsername(), user.getPassword(), secret, user_role, user.getEmp_id());
		if(check == true) {
			String token = TokenGenerator.generateToken();
			response.setToken(token);
			response.setSecret(TimeBasedOneTimePasswordUtil.qrImageUrl(user.getUsername(), secret));
			userValidator.saveToken(token);
		}
		response.setResponse(check);
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newuser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> createNewUser(@RequestBody NewUser user, HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if(isValidatedToken) {
			boolean check = employeeDAOImpl.createNewUser(user.getEmp_id(), user.getFirst_name(), user.getLast_name(), user.getEmail_id(), user.getMobile_num());
			response.setResponse(check);
		}else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/tabneremployees", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getTabnerEmployees(HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  List<EmployeeDetails> list = employeeDAOImpl.getTabnerEmployees();
			response.setResponse(list);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/inactiveemployees", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getInactiveEmployees(HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  List<EmployeeDetails> list = employeeDAOImpl.getInactiveEmployees();
			response.setResponse(list);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/defaulttabneremployee", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Response> getDefaultTabnerEmployee(@RequestBody User user, HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			  EmployeeDetails employeeDetails = employeeDAOImpl.getDefaultTabnerEmployee(user.getUsername());
			response.setResponse(employeeDetails);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/newemployee", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> createNewEmployee(@RequestBody NewUser user, HttpServletRequest req) {
		System.out.println("printing new user: " + user.getEmp_id() + user.getFirst_name() + user.getLast_name() + user.getEmail_id() + user.getMobile_num() + user.getPassport() +
													user.getVisa() + user.getEducation() + user.getExperience() + user.getSkills() + user.getAddress() + user.getVendor_id());
		Response response = new Response();
		String token = req.getHeader("tabner_token");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			boolean check = employeeDAOImpl.createNewEmployee(user.getEmp_id(), user.getFirst_name(), user.getLast_name(), user.getEmail_id(), user.getMobile_num(), user.getPassport(),
					user.getVisa(), user.getEducation(), user.getExperience(), user.getSkills(), user.getAddress(), user.getVendor_id());
			//response.setResponse(check);
			if(check) {
				List<EmployeeDetails> list = employeeDAOImpl.getTabnerEmployees();
				response.setResponse(list);
			}else {
				response.setResponse(false);
			}
		} else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/userrole", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> getUserRole(@RequestBody User user, HttpServletRequest req) {
		System.out.println("............Getting User Role.............");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		  boolean isValidatedToken = userValidator.validateToken(token);
		  if(isValidatedToken) {
			 UserRole role = employeeDAOImpl.getUserRole(user.getUsername());
			 System.out.println("........................................................." + role.getUser_role() + " , " + role.getEmp_id());
			response.setResponse(role);
		  }else {
			  response.setResponse("No Token");
		  }
		
		return new ResponseEntity<Response>(response,HttpStatus.OK);
		
	}
	
}
