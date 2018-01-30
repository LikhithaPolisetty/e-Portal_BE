package com.tabner.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tabner.DAOImpl.SemiMonthlyInvoiceDAOImpl;
import com.tabner.entities.EmployeeInvoices;
import com.tabner.entities.NewUser;
import com.tabner.entities.WorkingHours;

@Service
@EnableScheduling
public class InvoiceService {

	@Autowired
	private SemiMonthlyInvoiceDAOImpl semiMonthlyInvoiceDAOImpl;

	@Scheduled(fixedRate = 25000)
	public void getEmployeeSMInvoices() {
		System.out.println(
				"----------------------------------Calculating Employees Invoices----------------------------------");
		List<NewUser> empList = semiMonthlyInvoiceDAOImpl.getEmployees();
		// System.out.println(empList.size());
		for (NewUser s : empList) {
			Date empInv_enddate = s.getInvoice_end_date();
			System.out.println(empInv_enddate);

			if (empInv_enddate == null) {
				System.out.println("-----------------------No previous Invoice date-----------------------");
			} else {
				Calendar cal = Calendar.getInstance();
				cal.setTime(empInv_enddate);
				cal.add(Calendar.DATE, 1);
				Date invoice_startDate = cal.getTime();
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(invoice_startDate);
				Date invoice_endDate = calEnd.getTime();

				/*
				 * Calendar calEnd = Calendar.getInstance(); calEnd.setTime(empInv_enddate);
				 * calEnd.add(Calendar.DATE, 1);
				 */
				System.out.println(".................Invoice Start Date is ....." + invoice_startDate);
				int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
				System.out.println(".................Invoice Day of month ....." + day_of_month);
				int invoice_period = 0;
				GregorianCalendar mycal = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
				int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
				if (day_of_month == 1) {
					calEnd.add(Calendar.DATE, 14);
					invoice_endDate = calEnd.getTime();
					System.out.println("................... If invoice start date is 1 then Invoice End Date is ..... "
							+ invoice_endDate);
					invoice_period = invoice_period + 15;
				} else if (day_of_month == 16) {
					int daysLeftInMonth = daysInMonth - 15;
					calEnd.add(Calendar.DATE, daysLeftInMonth);
					invoice_endDate = calEnd.getTime();
					System.out.println("................... If invoice start date is 16 then Invoice End Date is ..... "
							+ invoice_endDate);
					invoice_period = invoice_period + daysLeftInMonth;
				} else {
					System.out.println("..........Inappropriate dates..........");
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				String start_date = dateFormat.format(invoice_startDate);
				String end_date = dateFormat.format(invoice_endDate);

				System.out.println("................... Invoice Start Date " + start_date);
				System.out.println("................... Invoice End Date " + end_date);

				Date today_date = new Date();
				System.out.println(".................today's date is " + today_date);
				System.out.println(".................last invoice date date is " + empInv_enddate);

				int daysForInvCalculation = (int) ((today_date.getTime() - empInv_enddate.getTime())
						/ (1000 * 60 * 60 * 24));
				System.out.println("---------" + daysForInvCalculation);

				if (daysForInvCalculation >= invoice_period) {

					List<WorkingHours> wHours = semiMonthlyInvoiceDAOImpl.getEmpWorkingHours(s.getEmp_id(), start_date,
							end_date);

					double totalHours = 0;
					double totalInvoiceAmount = 0;
					for (WorkingHours record : wHours) {
						float billRate = semiMonthlyInvoiceDAOImpl.getBillRates(record.getEmp_id(), record.getDate());
						double invoiceAmount = billRate * record.getHours();
						totalInvoiceAmount = totalInvoiceAmount + invoiceAmount;
						totalHours = totalHours + record.getHours();
					}

					System.out.println("*****Total Hours worked in this invoice period (" + start_date + " , "
							+ end_date + ") are: " + totalHours);
					System.out
							.println("*****Total Invoice Amount for " + s.getFirst_name() + " in this invoice period ("
									+ start_date + " , " + end_date + ") are: " + totalInvoiceAmount);

					EmployeeInvoices invoice = new EmployeeInvoices();
					invoice.setEmp_id(s.getEmp_id());
					invoice.setEmp_name(s.getFirst_name() + " , " + s.getLast_name());
					invoice.setVendor_id(s.getVendor_id());
					invoice.setHours(totalHours);
					invoice.setAmount(totalInvoiceAmount);
					invoice.setStart_date(start_date);
					invoice.setEnd_date(end_date);

					System.out.println("Employee Id is --- " + invoice.getEmp_id());
					System.out.println("Employee Name is --- " + invoice.getEmp_name());
					System.out.println("Vendor Id is --- " + invoice.getVendor_id());
					System.out.println("Total Hours are --- " + invoice.getHours());
					System.out.println("Total Invoice Amount is --- " + invoice.getAmount());
					System.out.println("Invoice Start Date is --- " + invoice.getStart_date());
					System.out.println("Invoice End Date is --- " + invoice.getEnd_date());

					int updateInvDate = semiMonthlyInvoiceDAOImpl.updateInvoiceEndDate(end_date, s.getEmp_id());
					if (updateInvDate == 1) {
						System.out.println("Successfully updated " + s.getEmp_id() + " " + s.getFirst_name()
								+ " Invoice End Date to -----" + end_date);
					} else {
						System.out.println("Invoice End Date Update failed");
					}

				} else {
					System.out.println("...............Insufficient Days to generate Invoice...............");
				}
			}
		}
	}

}
