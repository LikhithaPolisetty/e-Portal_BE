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

import com.tabner.DAOImpl.EmployeeDAOImpl;
import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.NetEmployee;

/*
 * this class automates the parsing process without asking the user to upload the payroll pdf
 */

@Service
@EnableScheduling
public class AutomaticPayrollPdfRead implements ApplicationContextAware {

	public static boolean check = false;
	

	private static FileTime fileTime = null;

	@Autowired
	private ExcelConverter excelConverter;

	@Autowired
	private EmpToEmpSpecific empToEmpSpecific;

	@Autowired
	private EmployeeDAOImpl employeeDAOImpl;

	private ApplicationContext context;

	public ExcelConverter getExcelConverter() {
		return excelConverter;
	}

	public void setExcelConverter(ExcelConverter excelConverter) {
		this.excelConverter = excelConverter;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
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

	
	/*
	 * this method continuously checks the dropbox for the availability of the new payroll pdf.
	 * if new payroll pdf is available it parses the pdf and stores the records into the database
	 */
	@Scheduled(fixedRate = 10000)
	public void checkForPdf() {
		System.out.println("Checking For The Pdf File this is running");
		File dir = new File("C:\\Users\\Tabner\\Dropbox\\Payroll_journals" );
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
				PdfReaderService service = context.getBean("pdfReaderService", PdfReaderService.class);
				List<EmployeeSpecific> list = empToEmpSpecific.convert(service.readPdf(mostRecent.toString()));
				employeeDAOImpl.save(list);
				employeeDAOImpl.saveNetEmployee(new NetEmployee(empToEmpSpecific.payDate, empToEmpSpecific.netCash, empToEmpSpecific.employeeTaxes, empToEmpSpecific._401k));
				try {
					excelConverter.writeToExcel(list);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileTime = t;
			} else if (t.compareTo(fileTime) > 0) {
				PdfReaderService service = context.getBean("pdfReaderService", PdfReaderService.class);
				List<EmployeeSpecific> list = empToEmpSpecific.convert(service.readPdf(mostRecent.toString()));
				employeeDAOImpl.save(list);
				employeeDAOImpl.saveNetEmployee(new NetEmployee(empToEmpSpecific.payDate, empToEmpSpecific.netCash, empToEmpSpecific.employeeTaxes, empToEmpSpecific._401k));
				try {
					excelConverter.writeToExcel(list);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileTime = t;
			}

		}

	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.context = arg0;

	}
}
