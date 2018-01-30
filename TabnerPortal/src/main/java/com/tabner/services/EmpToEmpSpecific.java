package com.tabner.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Service;
import com.tabner.entities.Employee;
import com.tabner.entities.EmployeeSpecific;

@Service
public class EmpToEmpSpecific {

	/*
	 * this method takes in the list of Employee objects and converts them into the
	 * list of EmployeeSpecific objects which has detailed information of taxes and
	 * deductions
	 */
	String netCash;
	String employeeTaxes;
	String payDate;
	String _401k;
	public static String round(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return String.format( "%.2f", bd.doubleValue() );	   
	}
	
	public List<EmployeeSpecific> convert(List<Employee> empList) {
		this.netCash = "";
		this.employeeTaxes = "";
		this.payDate = "";
		this._401k = "";
		double net_cash = 0;
		double employee_taxes = 0;
		double k401 = 0;
		List<EmployeeSpecific> list = new ArrayList<EmployeeSpecific>();
		for (Employee e : empList) {
			double totalTaxes = 0;
			double totalSalary = 0;
			EmployeeSpecific emp = new EmployeeSpecific();
			emp.setLastName(e.getName().split(", ")[0].toUpperCase());
			emp.setFirstName(e.getName().split(", ")[1].toUpperCase());
			emp.setId(e.getId().replace(" ", ""));
			emp.setRate("$" + e.getRate());	
			emp.setHours(e.getHours());
			emp.setEarnings("$" + e.getEarnings());
			
			if (e.getExpenses() != null) {
				emp.setExpenses("$" + e.getExpenses().split("ExpNt")[0]);
			}
			if (e.getExpenses() != null) {
				double d = Double.parseDouble(e.getEarnings().replace(",", ""))
						+ Double.parseDouble(e.getExpenses().split("ExpNt")[0].replace(",", ""));
				emp.setGross("$" + round(d));
			} else {
				
					emp.setGross("$" + round(Double.parseDouble(e.getGross().replace(",", ""))));
			}
			
			for (String s : e.getTaxes()) {
				if (s.contains("SOC")) {
					totalTaxes = totalTaxes + Double.parseDouble(s.split("SOC")[0].replaceAll(",", ""));
					emp.setSoc("$" + s.split("SOC")[0]);
				} else if (s.contains("MED")) {
					totalTaxes = totalTaxes + Double.parseDouble(s.split("MED")[0].replaceAll(",", ""));
					emp.setMed("$" + s.split("MED")[0]);
				} else if (s.contains("FITWH")) {
					totalTaxes = totalTaxes + Double.parseDouble(s.split("FITWH")[0].replaceAll(",", ""));
					emp.setFitwh("$" + s.split("FITWH")[0]);
				} else if (s.contains("SDI")) {
					totalTaxes = totalTaxes + Double.parseDouble(
							s.split("SDI")[0].substring(0, s.split("SDI")[0].length() - 2).replaceAll(",", ""));
					emp.setAddState("$" + s.split("SDI")[0].substring(0, s.split("SDI")[0].length() - 2));
				} else if (s.contains("WCW")) {
					totalTaxes = totalTaxes + Double.parseDouble(
							s.split("WCW")[0].substring(0, s.split("WCW")[0].length() - 2).replaceAll(",", ""));
					emp.setAddState("$" + s.split("WCW")[0].substring(0, s.split("WCW")[0].length() - 2));
				} else if (s.contains("029R")) {
					totalTaxes = totalTaxes + Double.parseDouble(
							s.split("029R")[0].substring(0, s.split("029R")[0].length() - 2).replaceAll(",", ""));
					emp.setAddState("$" + s.split("029R")[0].substring(0, s.split("029R")[0].length() - 2));
				} else if (s.contains("EUC")) {
					totalTaxes = totalTaxes + Double.parseDouble(
							s.split("EUC")[0].substring(0, s.split("EUC")[0].length() - 2).replaceAll(",", ""));
					emp.setAddState("$" + s.split("EUC")[0].substring(0, s.split("EUC")[0].length() - 2));
				} else {
					if (s.equals("VA")) {

					} else {
						totalTaxes = totalTaxes
								+ Double.parseDouble(s.substring(0, s.length() - 2).replaceAll(",", ""));
						emp.setState("$" + s.substring(0, s.length() - 2));
					}
				}

			}

			for (String s : e.getDeductions()) {
				if (s.contains("Med125")) {
					emp.setMed125("$" + s.split("Med125")[0]);
				} else if (s.contains("Partial")) {
					totalSalary = totalSalary + Double.parseDouble(s.split("Partial")[0].replaceAll(",", ""));
					emp.setPartial("$" + s.split("Partial")[0]);
				} else if (s.contains("Advance")) {
					emp.setAdvance("$" + s.split("Advance")[0]);
				} else if (s.contains("Misc")) {
					emp.setMisc("$" + s.split("Misc")[0]);
				}else if (s.contains("401k")) {
					double d = Double.parseDouble(s.split("401k")[0].replaceAll(",", ""));
					k401 = k401 + d;
					emp.set_401k("$" + d);
					for (String s1 : e.getDeductions()) {
						if (s1.contains("401kRoth")) {
							double d1 =  d + Double.parseDouble(s1.split("401kRoth")[0].replace(",", ""));
							k401 = k401 + Double.parseDouble(s1.split("401kRoth")[0].replace(",", ""));
							emp.set_401k(("$" + d1));
						}
					}

				} else if (s.contains("Net")) {
					totalSalary = totalSalary + Double.parseDouble(s.split("Net")[0].replaceAll(",", ""));
					emp.setNet("$" + s.split("Net")[0]);
				}

			}
			emp.setNetpay("$" + e.getNetPay().get(0));
			totalSalary = totalSalary + Double.parseDouble(e.getNetPay().get(0).replaceAll(",", ""));
			emp.setPayDate(e.getPayDate());
			this.payDate = e.getPayDate();
			
			
			
			net_cash = net_cash + totalSalary;
			employee_taxes = employee_taxes + totalTaxes;
			
			
			emp.setTotalSalary("$" + round(totalSalary));

			String s = e.getPeriodEnding();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date date = null;
			try {
				date = dateFormat.parse(s);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			java.sql.Date ending = new java.sql.Date(date.getTime());
			String[] endingArray = ending.toString().split("-");
			
			emp.setPeriodEnding(endingArray[1]+ "-" + endingArray[2] + "-" + endingArray[0]);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, -13);
			java.util.Date date1 = cal.getTime();
			java.sql.Date starting = new java.sql.Date(date1.getTime());
			String[] startingArray = starting.toString().split("-");
		
			emp.setPeriodStarting(startingArray[1]+ "-" + startingArray[2] + "-" + startingArray[0]);
			
			
			emp.setTotalTaxes("$" + round(totalTaxes));
			
			if(emp.getHours() == null) {
				emp.setHours("NA");
			}
			if(emp.getRate() == null) {
				emp.setRate("NA");
			}
			if (emp.getExpenses() == null) {
				emp.setExpenses("NA");
			}
			if (emp.getSoc() == null) {
				emp.setSoc("NA");
			}
			if (emp.getMed() == null) {
				emp.setMed("NA");
			}
			if (emp.getFitwh() == null) {
				emp.setFitwh("NA");
			}
			if (emp.getState() == null) {
				emp.setState("NA");
			}
			if (emp.getAddState() == null) {
				emp.setAddState("NA");
			}
			if (emp.getAdvance() == null) {
				emp.setAdvance("NA");
			}
			if (emp.getMed125() == null) {
				emp.setMed125("NA");
			}
			if (emp.getMisc() == null) {
				emp.setMisc("NA");
			}
			if (emp.getPartial() == null) {
				emp.setPartial("NA");
			}
			if (emp.get_401k() == null) {
				emp.set_401k("NA");
			}
			if (emp.getNet() == null) {
				emp.setNet("NA");
			}
			
			
			list.add(emp);
			
		}
		this.netCash = round(net_cash);
		this.employeeTaxes = round(employee_taxes);
		this._401k = round(k401);
		return list;
	}
	
	

	/*
	 * public void printAll() { for(EmployeeSpecific e: list) {
	 * System.out.println("empid: " + e.getId()); System.out.println("f name: " +
	 * e.getFirstName()); System.out.println("l name: " + e.getLastName());
	 * System.out.println("hours: " + e.getHours()); System.out.println("rate: " +
	 * e.getRate()); System.out.println("earnings: " + e.getEarnings());
	 * System.out.println("expenses: " + e.getExpenses());
	 * System.out.println("gross: " + e.getGross()); System.out.println("soc: " +
	 * e.getSoc()); System.out.println("med: " + e.getMed());
	 * System.out.println("fitwh: " + e.getFitwh()); System.out.println("state: " +
	 * e.getState()); System.out.println("additional: " + e.getAddState());
	 * System.out.println("med125: " + e.getMed125());
	 * System.out.println("partials: " + e.getPartial());
	 * System.out.println("401k: " + e.get_401k()); System.out.println("Net: " +
	 * e.getNet()); System.out.println("NetPay: " + e.getNetpay());
	 * System.out.println("paydate: " + e.getPayDate());
	 * System.out.println("period ending: "+ e.getPeriodEnding());
	 * System.out.println("----------------------------------------");
	 * System.out.println("----------------------------------------");
	 * 
	 * } }
	 */
}
