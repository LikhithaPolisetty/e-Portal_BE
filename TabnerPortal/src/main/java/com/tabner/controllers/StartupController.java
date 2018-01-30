package com.tabner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class StartupController {

	
	
	   @RequestMapping(method = RequestMethod.GET)
	   public String printHello(ModelMap model) {
		   System.out.println("--------------");
	     System.out.println("coming");
	     System.out.println("--------------");
	      return "static/html/index";
	   }
	
}
