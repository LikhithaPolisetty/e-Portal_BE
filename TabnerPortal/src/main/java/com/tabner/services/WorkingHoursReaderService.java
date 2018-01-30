package com.tabner.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tabner.DAOImpl.WorkingHoursDAOImpl;
import com.tabner.entities.EmployeePay;
import com.tabner.entities.Insurance;
import com.tabner.entities.WorkingHours;

/*
 * this class reads the excel file provided by the AutomaticHoursExcelRead.java class and parses all the rows in the excel 
 * to WorkingHours entities
 */

@Service
//@Scope("prototype")
public class WorkingHoursReaderService {

	@Autowired
	private WorkingHoursDAOImpl workingHoursDAOImpl;

	/*
	 * List<WorkingHours> workingHours = new ArrayList<WorkingHours>();
	 * 
	 * public List<WorkingHours> getWorkingHours() { return workingHours; }
	 * 
	 * public void setWorkingHours(List<WorkingHours> workingHours) {
	 * this.workingHours = workingHours; }
	 */

	public WorkingHoursDAOImpl getWorkingHoursDAOImpl() {
		return workingHoursDAOImpl;
	}

	public void setWorkingHoursDAOImpl(WorkingHoursDAOImpl workingHoursDAOImpl) {
		this.workingHoursDAOImpl = workingHoursDAOImpl;
	}

	public static String round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return String.format("%.2f", bd.doubleValue());
	}

	/*
	 * this methods reads the excel file provided by the
	 * AutomaticHoursExcelRead.java class and parses all the rows in the excel and
	 * returns the list of WorkingHours entities
	 */
	
	public double calculateInsuranceAmount(String emp_id, String day) {
		double ins_pay = 0.0;
		Insurance medinsurance = workingHoursDAOImpl.getMedInsuranceDetails(emp_id, day);
		if(!medinsurance.getPlan().equals("NA")) {
			double planPrice = workingHoursDAOImpl.getPlanPrice(medinsurance.getPlan(), day);
			ins_pay = ins_pay + ((planPrice * 12)/365) * (medinsurance.getEmp_con()/ 100);
			System.out.println("med price is..." + ((planPrice * 12)/365) * (medinsurance.getEmp_con()/ 100));
		}
		Insurance deninsurance = workingHoursDAOImpl.getDenInsuranceDetails(emp_id, day);
		if(!deninsurance.getPlan().equals("NA")) {
			double planPrice = workingHoursDAOImpl.getPlanPrice(deninsurance.getPlan(), day);
			ins_pay = ins_pay + ((planPrice * 12)/365) * (deninsurance.getEmp_con()/ 100);
			System.out.println("den price is..." +((planPrice * 12)/365) * (deninsurance.getEmp_con()/ 100));
		}
		Insurance visinsurance = workingHoursDAOImpl.getVisInsuranceDetails(emp_id, day);
		if(!visinsurance.getPlan().equals("NA")) {
			double planPrice = workingHoursDAOImpl.getPlanPrice(visinsurance.getPlan(), day);
			ins_pay = ins_pay + ((planPrice * 12)/365) * (visinsurance.getEmp_con()/ 100);
			System.out.println("vis price is..." +((planPrice * 12)/365) * (visinsurance.getEmp_con()/ 100));
		}
		return ins_pay;
	}

	public List<WorkingHours> readExcel(String file) {

		List<WorkingHours> workingHours = new ArrayList<WorkingHours>();

		String[] dates = new String[7];

		try {
			FileInputStream excelFile = new FileInputStream(new File(file));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			int rowStart = sheet.getFirstRowNum();
			int rowEnd = sheet.getLastRowNum();
			System.out.println(rowStart + "----" + rowEnd);
			Row row = sheet.getRow(0);

			// this loop stores all the dates of week present in the excel in an array
			for (int i = 2; i <= 8; i++) {
				java.sql.Date date = new java.sql.Date(row.getCell(i).getDateCellValue().getTime());
				dates[i - 2] = date.toString();
			}

			for (int rowNum = rowStart + 1; rowNum <= rowEnd; rowNum++) {

				for (int day = 0; day <= 6; day++) {
					WorkingHours hours = new WorkingHours();
					hours.setEmp_id(sheet.getRow(rowNum).getCell(0).getStringCellValue());
					hours.setName(sheet.getRow(rowNum).getCell(1).getStringCellValue());
					hours.setDate(dates[day]);
					hours.setHours(sheet.getRow(rowNum).getCell(day + 2).getNumericCellValue());
					workingHours.add(hours);
				}
			}
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workingHours;
	}

	public List<EmployeePay> calculateEmployeePay(String fileName) {

		System.out.println(" reading data from file..." + fileName);

		List<EmployeePay> employeePay = new ArrayList<EmployeePay>();

		String[] dates = new String[7];

		try {
			FileInputStream excelFile = new FileInputStream(new File(fileName));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			int rowStart = sheet.getFirstRowNum();
			int rowEnd = sheet.getLastRowNum();
			System.out.println(rowStart + "----" + rowEnd);
			Row row = sheet.getRow(0);

			// this loop stores all the dates of week present in the excel in an array
			System.out.println("printing dates");
			for (int i = 2; i <= 8; i++) {
				java.sql.Date date = new java.sql.Date(row.getCell(i).getDateCellValue().getTime());
				dates[i - 2] = date.toString();
				System.out.println(date.toString());
			}

			for (int rowNum = rowStart + 1; rowNum <= rowEnd; rowNum++) {
				EmployeePay ep = new EmployeePay();
				double pay = 0.0;
				double ins_pay = 0.0;
				double final_pay = 0.0;
				ep.setEmp_id(sheet.getRow(rowNum).getCell(0).getStringCellValue());
				System.out.println("employee number : " + rowNum);
				for (int day = 0; day <= 6; day++) {

					String employeeType = workingHoursDAOImpl.checkEmployeeType(ep.getEmp_id(), dates[day]);
					System.out.println(employeeType);

					if (employeeType.equals("W2")) {
						double payRate = workingHoursDAOImpl.getPayRate(ep.getEmp_id(), dates[day]);
						double hours = sheet.getRow(rowNum).getCell(day + 2).getNumericCellValue();
						pay = pay + payRate * hours;
						ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
					} else if (employeeType.equals("SAL")) {
						if (sheet.getRow(rowNum).getCell(9).getNumericCellValue() >= 40) {
							double payRate = workingHoursDAOImpl.getPayRate(ep.getEmp_id(), dates[day]);
							double hours = sheet.getRow(rowNum).getCell(day + 2).getNumericCellValue();
							if (day == 0 || day == 1) {
								pay = pay + payRate * hours;
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							} else if (workingHoursDAOImpl.checkHoliday(dates[day])) {
								pay = pay + (payRate * (hours + 8));
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							} else {
								pay = pay + payRate * hours;
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							}
						} else {
							double payRate = workingHoursDAOImpl.getPayRate(ep.getEmp_id(), dates[day]);
							double hours = sheet.getRow(rowNum).getCell(day + 2).getNumericCellValue();
							if (day == 0 || day == 1) {
								pay = pay + payRate * hours;
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							} else if (workingHoursDAOImpl.checkHoliday(dates[day])) {
								pay = pay + (payRate * (hours + 8));
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							} else if (hours >= 8) {
								pay = pay + payRate * hours;
								ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
							} else if (hours < 8) {
								double req_hours = 8 - hours;
								double available_vacation = workingHoursDAOImpl.getAvailableVacation(ep.getEmp_id());
								if (available_vacation >= req_hours) {
									hours = hours + req_hours;
									workingHoursDAOImpl.updateVactionHours(ep.getEmp_id(),
											available_vacation - req_hours,
											workingHoursDAOImpl.getUsedVacation(ep.getEmp_id()) + req_hours);
									pay = pay + payRate * hours;
									ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
								} else if (available_vacation < req_hours) {
									hours = hours + available_vacation;
									workingHoursDAOImpl.updateVactionHours(ep.getEmp_id(), 0,
											workingHoursDAOImpl.getUsedVacation(ep.getEmp_id()) + available_vacation);
									pay = pay + payRate * hours;
									ins_pay = ins_pay + calculateInsuranceAmount(ep.getEmp_id(), dates[day]);
								}
							}
						}
					}

				}
				ep.setPay(pay);
				ep.setIns_pay(ins_pay);
				ep.setFinal_pay(ep.getPay() - ep.getIns_pay());
				employeePay.add(ep);
				
			}
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (EmployeePay e : employeePay) {
			System.out.println(e.getEmp_id() + "..........." + e.getPay() + "..........." + e.getIns_pay() + "..........." + e.getFinal_pay());
		}

		return employeePay;

	}

}
