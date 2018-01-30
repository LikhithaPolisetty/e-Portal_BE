package com.tabner.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tabner.DAOImpl.VendorDAOImpl;
import com.tabner.entities.Employee_Vendor;
import com.tabner.entities.NewVendor;
import com.tabner.entities.Response;
import com.tabner.entities.VendorDelete;
import com.tabner.entities.VendorEmployees;
import com.tabner.entities.VendorInvoices;
import com.tabner.metadata.UserValidator;

@RestController
public class VendorController {

	@Autowired
	private VendorDAOImpl vendorDAOImpl;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value = "/tabnervendors", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response> getTabnerVendors(HttpServletRequest req) {
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			List<NewVendor> list = vendorDAOImpl.getTabnerVendors();
			response.setResponse(list);
		} else {
			response.setResponse("No token available");
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/vendordetails", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<Response> getVendorDetails(@RequestBody Employee_Vendor empVendor, HttpServletRequest req) {
		System.out.println("################################################################################");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			List<VendorEmployees> venEmp = vendorDAOImpl.getVendorEmployees(empVendor.getVendor_id());
			//response.setResponse(list);
			
			List<VendorInvoices> invList = vendorDAOImpl.getVendorInvoices(empVendor.getVendor_id());
			//response.setResponse(invList);
			
			List<NewVendor> venAddress = vendorDAOImpl.getVendorAddress(empVendor.getVendor_id());
			
			ArrayList<Object> venDetails = new ArrayList<Object>();
			venDetails.add(venEmp);
			venDetails.add(invList);
			venDetails.add(venAddress);
			
			response.setResponse(venDetails);
		} else {
			response.setResponse("No token available");
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/newvendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> createNewVendor(@RequestBody NewVendor vendor, HttpServletRequest req) {
		System.out.println("printing new vendor: " + vendor.getVendor_id() + vendor.getName() + vendor.getReg_state()
				+ vendor.getInvoice_freq() + vendor.getPayment_freq() + vendor.getAddress_line_1() + vendor.getAddress_line_2() + vendor.getSuite_apt() + vendor.getCity() + vendor.getState() + vendor.getZipcode());
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			boolean check = vendorDAOImpl.createNewVendor(vendor.getVendor_id(), vendor.getName(),
					vendor.getReg_state(), vendor.getInvoice_freq(), vendor.getPayment_freq(), 
					vendor.getAddress_line_1(), vendor.getAddress_line_2(), vendor.getSuite_apt(), 
					vendor.getCity(), vendor.getState(), vendor.getZipcode());
			response.setResponse(check);
		} else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/deletevendor", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<Response> deleteVendor(@RequestBody VendorDelete delVendor, HttpServletRequest req) {
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Deleting Vendor");
		System.out.println("--------------------------------------------------------------------");
		Response response = new Response();
		String token = req.getHeader("Authorization");
		boolean isValidatedToken = userValidator.validateToken(token);
		if (isValidatedToken) {
			boolean check = vendorDAOImpl.deleteVendor(delVendor.getId_number());
			response.setResponse(check);
		} else
			response.setResponse("No Token");
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}