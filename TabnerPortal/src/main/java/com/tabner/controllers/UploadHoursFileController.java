package com.tabner.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tabner.DAOImpl.WorkingHoursDAOImpl;
import com.tabner.entities.EmployeePay;
import com.tabner.entities.WorkingHours;
import com.tabner.services.WorkingHoursReaderService;

@RestController
public class UploadHoursFileController {

 //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\Tabner\\Dropbox\\Working_hours\\";
    public static String fileName = "";
   
    @Autowired
    private WorkingHoursReaderService service;
    
    @Autowired
	private WorkingHoursDAOImpl workingHoursDAOImpl;
   
    public WorkingHoursDAOImpl getWorkingHoursDAOImpl() {
		return workingHoursDAOImpl;
	}

	public void setWorkingHoursDAOImpl(WorkingHoursDAOImpl workingHoursDAOImpl) {
		this.workingHoursDAOImpl = workingHoursDAOImpl;
	}

	public WorkingHoursReaderService getService() {
		return service;
	}

	public void setService(WorkingHoursReaderService service) {
		this.service = service;
	}

	@RequestMapping(value = "/newfile", method = RequestMethod.POST)
    public void singleHoursFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Get the file and save it somewhere
        	fileName = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        List<EmployeePay> list = service.calculateEmployeePay(UPLOADED_FOLDER + fileName);
		List<WorkingHours> list1 = service.readExcel(UPLOADED_FOLDER + fileName);
		workingHoursDAOImpl.save(list1);

    }
	

    
    
 
}
