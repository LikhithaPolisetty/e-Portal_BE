package com.tabner.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tabner.DAOImpl.EmployerTaxesDAOImpl;
import com.tabner.entities.EmployerTaxes;
import com.tabner.entities.NetEmployerTaxes;

/*
 * this class automates the parsing process without asking the user to upload the employer taxes excel file
 */

@Service
@EnableScheduling
public class AutomaticExcelRead implements ApplicationContextAware {

	public static boolean check = false;
	
	private static FileTime fileTime = null;

	@Autowired
	private EmployerTaxesDAOImpl employerTaxesDAOImpl;

	private ApplicationContext context;

	
	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	
	
	public EmployerTaxesDAOImpl getEmployerTaxesDAOImpl() {
		return employerTaxesDAOImpl;
	}

	public void setEmployerTaxesDAOImpl(EmployerTaxesDAOImpl employerTaxesDAOImpl) {
		this.employerTaxesDAOImpl = employerTaxesDAOImpl;
	}

	/*
	 * this method continuously checks the dropbox for the availability of the new employer taxes excel.
	 * if new employer taxes excel is available it parses the pdf and stores the records into the database
	 */
	@Scheduled(fixedRate = 25000)
	public void checkForPdf() {
		System.out.println("Checking For The Latest Excel File");
		File dir = new File("C:\\Users\\Tabner\\Dropbox\\Employer_taxes");
		File[] files = dir.listFiles();
		
		File mostRecent = null;
		if (files.length > 0) {
			mostRecent = files[0];
			for (int i = 1; i < files.length; i++) {
				Path file = Paths.get(mostRecent.toString());
				BasicFileAttributes attr = null;
				try {
					attr = Files.readAttributes(file, BasicFileAttributes.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileTime time1 = attr.creationTime();
				Path file1 = Paths.get(files[i].toString());
				BasicFileAttributes attr1 = null;
				try {
					attr1 = Files.readAttributes(file1, BasicFileAttributes.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileTime time2 = attr1.creationTime();
				if (time2.compareTo(time1) > 0) {
					mostRecent = new File(files[i].toString());
				}

			}
			Path file = Paths.get(mostRecent.toString());
			BasicFileAttributes attr = null;
			try {
				attr = Files.readAttributes(file, BasicFileAttributes.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
			FileTime t = attr.creationTime();

			if (fileTime == null) {
				ExcelReaderService service = context.getBean("excelReaderService", ExcelReaderService.class);
				List<EmployerTaxes> list = service.readExcel(mostRecent.toString());
				employerTaxesDAOImpl.save(list);
				employerTaxesDAOImpl.saveNetEmployerTaxes(new NetEmployerTaxes(service.paydate, service.totalEmployerTaxes));
				fileTime = t;
			} else if (t.compareTo(fileTime) > 0) {
				ExcelReaderService service = context.getBean("excelReaderService", ExcelReaderService.class);
				List<EmployerTaxes> list = service.readExcel(mostRecent.toString());
				employerTaxesDAOImpl.save(list);
				employerTaxesDAOImpl.saveNetEmployerTaxes(new NetEmployerTaxes(service.paydate, service.totalEmployerTaxes));
		
				fileTime = t;
			}

		}

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.context = arg0;

	}
}
