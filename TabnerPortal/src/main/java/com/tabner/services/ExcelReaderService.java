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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tabner.entities.EmployerTaxes;

@Service
@Scope("prototype")
public class ExcelReaderService {
	
	public String paydate;
	public String totalEmployerTaxes;
	
	List<EmployerTaxes> employerTaxes = new ArrayList<EmployerTaxes>();
	
	
	public String getPaydate() {
		return paydate;
	}


	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}


	public List<EmployerTaxes> getEmployerTaxes() {
		return employerTaxes;
	}


	public void setEmployerTaxes(List<EmployerTaxes> employerTaxes) {
		this.employerTaxes = employerTaxes;
	}
	
	public static String round(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return String.format( "%.2f", bd.doubleValue() );
	   
	}

	public List<EmployerTaxes> readExcel(String file){
		double totalTaxes = 0;
		try {
            FileInputStream excelFile = new FileInputStream(new File(file));
            System.out.println(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
           Sheet sheet = workbook.getSheetAt(0);
           int rowStart = sheet.getFirstRowNum();
           int rowEnd = sheet.getLastRowNum();
           Row row = sheet.getRow(1);
           java.sql.Date date = new java.sql.Date( row.getCell(0).getDateCellValue().getTime());
          String[] splitDate= date.toString().split("-");
           this.paydate = splitDate[1] + "-" + splitDate[2] + "-" + splitDate[0];
           
           for (int rowNum = rowStart + 1; rowNum <= rowEnd; rowNum++) {
        	   EmployerTaxes taxes = new EmployerTaxes();
        	   taxes.setPaydate(this.paydate);
               Row r = sheet.getRow(rowNum);
               taxes.setName(r.getCell(1).getStringCellValue());
               
               String s = Double.toString(r.getCell(2).getNumericCellValue());
      			String s1 = s.substring(0, s.length()-2);
      			
               taxes.setEmpId("EE#" + s1);
               
               
               
               if(r.getCell(6).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("CO");
            	   taxes.setAddt("$" +r.getCell(6).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(6).getNumericCellValue());
               }
               
               if(r.getCell(32).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("OR");
            	   taxes.setAddt("$" +r.getCell(32).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(32).getNumericCellValue());
               }
            
               if(r.getCell(33).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MO");
            	   taxes.setAddt("$" +r.getCell(33).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(33).getNumericCellValue());
               }
               
               
               
               if(r.getCell(3).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setFui("$" +r.getCell(3).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(3).getNumericCellValue();
               }
               if(r.getCell(4).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setMeder("$" +r.getCell(4).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(4).getNumericCellValue();
               }
               if(r.getCell(5).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setSocer("$" +r.getCell(5).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(5).getNumericCellValue();
               }
             
               if(r.getCell(7).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("WA");
            	   taxes.setAmount("$" +r.getCell(7).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(7).getNumericCellValue();
               }
               if(r.getCell(8).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("VA");
            	   taxes.setAmount("$" +r.getCell(8).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(8).getNumericCellValue();
               }
               if(r.getCell(9).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("TX");
            	   taxes.setAmount("$" +r.getCell(9).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(9).getNumericCellValue();
               }
               if(r.getCell(10).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("TN");
            	   taxes.setAmount("$" +r.getCell(10).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(10).getNumericCellValue();
               }
               if(r.getCell(11).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("SC");
            	   taxes.setAmount("$" +r.getCell(11).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(11).getNumericCellValue();

               }
               if(r.getCell(12).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("RI");
            	   taxes.setAmount("$" +r.getCell(12).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(12).getNumericCellValue();

               }
               if(r.getCell(13).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("PA");
            	   taxes.setAmount("$" +r.getCell(13).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(13).getNumericCellValue();

               }
               if(r.getCell(14).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("OR");
            	   taxes.setAmount("$" +r.getCell(14).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(14).getNumericCellValue();

               }
               if(r.getCell(15).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("OH");
            	   taxes.setAmount("$" +r.getCell(15).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(15).getNumericCellValue();

               }
               if(r.getCell(16).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("NY");
            	   taxes.setAmount("$" +r.getCell(16).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(16).getNumericCellValue();

               }
               if(r.getCell(17).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("NJ");
            	   taxes.setAmount("$" +r.getCell(17).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(17).getNumericCellValue();

               }
               if(r.getCell(18).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("NH");
            	   taxes.setAmount("$" +r.getCell(18).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(18).getNumericCellValue();

               }
               if(r.getCell(19).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("NC");
            	   taxes.setAmount("$" +r.getCell(19).getNumericCellValue());
            	   totalTaxes = totalTaxes + r.getCell(19).getNumericCellValue();

               }
               if(r.getCell(20).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MO");
            	   taxes.setAmount("$" +r.getCell(20).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(20).getNumericCellValue());

               }
               if(r.getCell(21).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MN");
            	   taxes.setAmount("$" +r.getCell(21).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(21).getNumericCellValue());
               }
               if(r.getCell(22).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MI");
            	   taxes.setAmount("$" +r.getCell(22).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(22).getNumericCellValue());
               }
               if(r.getCell(23).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("ME");
            	   taxes.setAmount("$" +r.getCell(23).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(23).getNumericCellValue());
               }
               if(r.getCell(24).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MD");
            	   taxes.setAmount("$" +r.getCell(24).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(24).getNumericCellValue());
               }
               if(r.getCell(25).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("MA");
            	   taxes.setAmount("$" +r.getCell(25).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(25).getNumericCellValue());
               }
               if(r.getCell(26).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("IN");
            	   taxes.setAmount("$" +r.getCell(26).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(26).getNumericCellValue());
               }
               if(r.getCell(27).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("CT");
            	   taxes.setAmount("$" +r.getCell(27).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(27).getNumericCellValue());
               }
               if(r.getCell(28).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("CO");
            	   taxes.setAmount("$" +r.getCell(28).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(28).getNumericCellValue());
               }
               if(r.getCell(29).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("CA");
            	   taxes.setAmount("$" +r.getCell(29).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(29).getNumericCellValue());
               }
               if(r.getCell(30).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("AZ");
            	   taxes.setAmount("$" +r.getCell(30).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(30).getNumericCellValue());
               }
               if(r.getCell(31).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("AL");
            	   taxes.setAmount("$" +r.getCell(31).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(31).getNumericCellValue());
               }
              
               if(r.getCell(34).getNumericCellValue() == 0.0) {
            	   
               } else {
            	   taxes.setState("IL");
            	   taxes.setAmount("$" +r.getCell(34).getNumericCellValue());
            	   totalTaxes = totalTaxes + (r.getCell(34).getNumericCellValue());
               }
               
               if(taxes.getFui() == null) {
            	   taxes.setFui("NA");
               }
               if(taxes.getMeder() == null) {
            	   taxes.setMeder("NA");
               }
               if(taxes.getSocer() == null) {
            	   taxes.setSocer("NA");
               }
               if(taxes.getState() == null) {
            	   taxes.setState("-");
               }
               if(taxes.getAmount() == null) {
            	   taxes.setAmount("NA");
               }
               if(taxes.getAddt() == null) {
            	   taxes.setAddt("NA");
               }
               
               
				 this.getEmployerTaxes().add(taxes);          
              
            
            }
           workbook.close();
           
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		this.totalEmployerTaxes =  round(totalTaxes);
		System.out.println("the total employer taxes is $" + this.totalEmployerTaxes);
		System.out.println("-----------------------------------------------------");
		return this.getEmployerTaxes();
	}
}
