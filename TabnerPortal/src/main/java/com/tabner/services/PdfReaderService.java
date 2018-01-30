
package com.tabner.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.tabner.entities.Employee;
/*
 * this class parses each employee separately from the payroll pdf and saves them into list
 */

@Service
@Scope("prototype")
public class PdfReaderService {

	@Autowired
	private ExtractionMethods extractionMethods;

	public ExtractionMethods getExtractionMethods() {
		return extractionMethods;
	}
	public void setExtractionMethods(ExtractionMethods extractionMethods) {
		this.extractionMethods = extractionMethods;
	}

	public List<String> text = new ArrayList<String>();
	private List<Employee> employeeList = new ArrayList<Employee>();
	public int increment = 0;
	public boolean check = true;
	public boolean pdfEndCheck = false;
	public String periodEnding;
	public String payDate;
	

	public List<String> getText() {
		return text;
	}

	/*
	 * this message takes the pdf file as the input and parses the pdf in such
	 * a way that each employee record(String) is stored as a list item in the ArrayList  
	 */
	public List<Employee> readPdf(String file) {
		PdfReader reader;
		
		List<Employee> empWithAdvance = new ArrayList<Employee>();

		try {

			reader = new PdfReader(file);
			int pages = reader.getNumberOfPages();

			for (int j = 1; j <= pages ; j++) {
				String textFromPage = PdfTextExtractor.getTextFromPage(reader, j);
				
				String[] lines = textFromPage.split("\\r?\\n");
				if(lines[6].contains("COMPANY TOTAL")) {
					System.out.println("------------------");
					System.out.println("breaked here check");
					System.out.println("------------------");
					break;
				}
				
				if (j == 1) {
					String s = lines[lines.length - 4];
					String[] s1 = s.split(" Pay Date: ");
					periodEnding = s1[0].split(" ")[2];
					//payDate = s1[1].split(" ")[0];
					String date = s1[1].split(" ")[0];
					String[] s2 = date.split("/");
					payDate = s2[0] + "-" + s2[1] + "-" + s2[2];

				}
				for (int i = 5; i < lines.length - 4; i++) {
					
					
						if(lines[i].split(" ")[0].contains("RATE")) {
							System.out.println("---------------------");
							System.out.println(lines[i]);
							System.out.println(lines[i+1]);
							System.out.println("---------------------");

							break;
						}
					
				
					
					
					if(lines[i].contains(" 100 ")) {
						if(lines[i].split(" 100 ")[1].split(" ")[0].contains("Advn")) {
							String adv = lines[i].split(" 100 ")[1].split(" ")[0].split("Advn")[0];
							Employee e = new Employee();
							e.setName(lines[i].split(" 100 ")[0]);
							e.setId(lines[i+1].split(" ")[0] + " " + lines[i+1].split(" ")[1]);
							e.getNetPay().add(adv);
							e.setPeriodEnding(this.periodEnding);
							e.setPayDate(this.payDate);
							e.setGross(adv);
							
							e.setRate("0.00");
							e.setHours("0.00");
							e.setEarnings("0.00");
							
							
							empWithAdvance.add(e);
							
							System.out.println("going into this ");	
							System.out.println(e.getName());
							System.out.println(e.getId());
							i = i+2;
							continue;
						}
					}
					// System.out.println(lines[i]);
					if (!lines[i].contains("Total Hours:")) {
						if (check == true) {
							getText().add(lines[i]);
							check = !check;
						} else {
							String s2 = getText().get(increment).concat("-" + lines[i]);
							getText().set(increment, s2);
						}

					} else {
						String s3 = getText().get(increment).concat("-" + lines[i]);
						getText().set(increment, s3);
						increment++;
						check = !check;
					}

				}
				
			}
			
			
			for (String s : text) {
				com.tabner.entities.Employee e = extractionMethods.parse(s, this.periodEnding, this.payDate);
				employeeList.add(e);
				
			}
			for(Employee e : empWithAdvance) {
				employeeList.add(e);
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return employeeList;

	}

}
