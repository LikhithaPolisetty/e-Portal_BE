package com.tabner.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.tabner.entities.EmployeeSpecific;


@Service
public class ExcelConverter {

	/*
	 * this methods takes the list of EmployeeSpecific objects and writes each object into the excel file
	 */
	public void writeToExcel(List<EmployeeSpecific> empList) throws IOException {
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet(" Employee Info ");

		// Create row object
		XSSFRow row;

		int rowid = 0;

		for (EmployeeSpecific emp : empList) {
			if (rowid == 0) {
				row = spreadsheet.createRow(rowid++);
				Cell cellA = row.createCell(1);
				cellA.setCellValue("Employee ID");
				Cell cellB = row.createCell(2);
				cellB.setCellValue("Last Name");
				Cell cellC = row.createCell(3);
				cellC.setCellValue("First Name");
				Cell cellD = row.createCell(4);
				cellD.setCellValue("Rate");
				Cell cellE = row.createCell(5);
				cellE.setCellValue("Hours");
				Cell cellF = row.createCell(6);
				cellF.setCellValue("Earnings");
				Cell cellG = row.createCell(7);
				cellG.setCellValue("Expenses");
				Cell cellH = row.createCell(8);
				cellH.setCellValue("Gross");
				Cell cellI = row.createCell(9);
				cellI.setCellValue("SOC");
				Cell cellJ = row.createCell(10);
				cellJ.setCellValue("MED");
				Cell cellK = row.createCell(11);
				cellK.setCellValue("FITWH");
				Cell cellL = row.createCell(12);
				cellL.setCellValue("STATE");
				Cell cellM = row.createCell(13);
				cellM.setCellValue("ADD STATE");
				Cell cellN = row.createCell(14);
				cellN.setCellValue("Advance");
				Cell cellO = row.createCell(15);
				cellO.setCellValue("Med125");
				Cell cellP = row.createCell(16);
				cellP.setCellValue("Partial");
				Cell cellQ = row.createCell(17);
				cellQ.setCellValue("401k");
				Cell cellR = row.createCell(18);
				cellR.setCellValue("Net");
				Cell cellS = row.createCell(19);
				cellS.setCellValue("NetPay");
				Cell cellT = row.createCell(20);
				cellT.setCellValue("Misc");

			}
			row = spreadsheet.createRow(rowid++);
			/* int cellid = 0; */
			Cell cell = row.createCell(1);
			cell.setCellValue(emp.getId());
			Cell cell1 = row.createCell(2);
			cell1.setCellValue(emp.getLastName());
			Cell cell2 = row.createCell(3);
			cell2.setCellValue(emp.getFirstName());
			Cell cell3 = row.createCell(4);
			cell3.setCellValue(emp.getRate());
			Cell cell4 = row.createCell(5);
			cell4.setCellValue(emp.getHours());
			Cell cell5 = row.createCell(6);
			cell5.setCellValue(emp.getEarnings());
			Cell cell6 = row.createCell(7);
			cell6.setCellValue(emp.getExpenses());
			Cell cell7 = row.createCell(8);
			cell7.setCellValue(emp.getGross());
			Cell cell8 = row.createCell(9);
			cell8.setCellValue(emp.getSoc());
			Cell cell9 = row.createCell(10);
			cell9.setCellValue(emp.getMed());
			Cell cell10 = row.createCell(11);
			cell10.setCellValue(emp.getFitwh());
			Cell cell11 = row.createCell(12);
			cell11.setCellValue(emp.getState());
			Cell cell12 = row.createCell(13);
			cell12.setCellValue(emp.getAddState());
			Cell cell13 = row.createCell(14);
			cell13.setCellValue(emp.getAdvance());
			Cell cell14 = row.createCell(15);
			cell14.setCellValue(emp.getMed125());
			Cell cell15 = row.createCell(16);
			cell15.setCellValue(emp.getPartial());
			Cell cell16 = row.createCell(17);
			cell16.setCellValue(emp.get_401k());
			Cell cell17 = row.createCell(18);
			cell17.setCellValue(emp.getNet());
			Cell cell18 = row.createCell(19);
			cell18.setCellValue(emp.getNetpay());
			Cell cell19 = row.createCell(20);
			cell19.setCellValue(emp.getMisc());
		}

		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Tabner\\Dropbox\\Excel_sheets\\"
				+ empList.get(0).getPayDate().toString().replaceAll("/", ".") + "_paydate.xlsx"));
		workbook.write(out);
		out.close();
		workbook.close();
		/* System.out.println("Writesheet.xlsx written successfully" ); */
	}
}
