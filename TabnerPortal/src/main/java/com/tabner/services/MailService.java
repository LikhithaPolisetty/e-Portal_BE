package com.tabner.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tabner.entities.EmployeeSpecific;
import com.tabner.entities.Mail;

/*
 * this class is written to send the email notifications to the employees to let them know that the payroll has been processed 
 */

@Service
public class MailService {

	@Autowired
	private JavaMailSenderImpl mailSender;

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
	public static String round(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return String.format( "%.2f", bd.doubleValue() );
	}
	
	public void sendMailToManager(Mail mail) {
		MimeMessage message = mailSender.createMimeMessage();
		double d = Double.parseDouble(mail.getNetCash()) + Double.parseDouble(mail.getEmployerTaxes()) + Double.parseDouble(mail.getEmployeeTaxes())+ Double.parseDouble(mail.get_401k()) ;
		
		String total = "$" + round(d);
		try {
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo("addagallarohithkumar@gmail.com");
		/*helper.setCc(new String[] {"gopalakrishna.yerramsetty@tabnergc.com", "mohan.kovvali@tabnerglobal.com", "sumita.singh@tabnergc.com"});*/
		helper.setSubject("ALERT: PAYROLL PROCESSED");
			helper.setText("<html lang=\"en\">\r\n" + 
					"<head>\r\n" + 
					"    <meta charset=\"UTF-8\">\r\n" + 
					"    <title>Title</title>\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"    <div>\r\n" + 
					"        <h4 style=\"font-family: 'Bookman Old Style';color: #2d60a3\">Hi Sumita,</h4>\r\n" + 
					"\r\n" + 
					"        <P style=\"margin-left: 20px; font-family: 'Bookman Old Style';color: #2d60a3\">The Payroll has been processed today. Please make sure you have sufficient amount available in the account. The amount details are as follows:</P>\r\n" + 
					"\r\n" + 
					"        <br>\r\n" + 
					"        <div style=\"margin-left: auto; margin-right: auto; width: 50%\">\r\n" + 
					"            <table style=\"border: 1px solid black; border-collapse: collapse; text-align: center\">\r\n" + 
					"                <tr>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">Net Cash:</td>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">"
					+ "$" + mail.getNetCash() + "</td>\r\n" + 
					"                </tr>\r\n" +
					"                <tr>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">401k:</td>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">"
					+ "$" + mail.get_401k() + "</td>\r\n" + 
					"                </tr>\r\n" +
					"                <tr>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">Employee Taxes:</td>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">"
					+ "$" +mail.getEmployeeTaxes() + "</td>\r\n" + 
					"                </tr>\r\n" + 
					"                <tr>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">Employer Taxes:</td>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">"
					+ "$" +mail.getEmployerTaxes() + "</td>\r\n" + 
					"                </tr>\r\n" + 
					"                <tr>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: #2d60a3\">Total:</td>\r\n" + 
					"                    <td style=\"border: 1px solid black; border-collapse: collapse; padding: 10px; font-family: 'Bookman Old Style';color: red\">"
					+ total + "</td>\r\n" + 
					"                </tr>\r\n" + 
					"            </table>\r\n" + 
					"        </div>\r\n" + 
					"\r\n" + 
					"    </div>\r\n" + 
					"</body>\r\n" + 
					"</html>", true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSender.send(message);
		AutomaticExcelRead.check = false;
		AutomaticPayrollPdfRead.check = false;
	}

	/*
	 * this method takes in the receivers mail id, subject, content and then send
	 * the email to the respective employee
	 */
	public void sendMail(String from, String to, String subject, EmployeeSpecific emp) {

		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(emp.getFirstName() + " " + emp.getLastName() + " | " + "Payroll Processed" + " | "
					+ emp.getPeriodStarting() + " to " + emp.getPeriodEnding());

			helper.setText("<html>\r\n" + "    <body>\r\n" + "        <div style=\" border: 2px solid black;\">\r\n"
					+ "            <div style=\"text-align: center; background-color: deepskyblue; border-bottom: 2px solid black\" >\r\n"
					+ "                <img style=\"margin-left: auto\" src=\"https://assets.dice.com/external/images/empLogos/cef41e085dd712f7a461e1d8a1a6d015.png\", height=\"90px\", width=\"200px\">\r\n"
					+ "            </div>\r\n" + "            <div style=\" text-align: center; \">\r\n"
					+ "                <p style=\"color: #2d60a3; font-family: 'Bookman Old Style';\">Hi" + " "
					+ emp.getFirstName() + " " + emp.getLastName() + ",</p>\r\n"
					+ "                <p style=\"color: #2d60a3; font-family: 'Bookman Old Style';\"> The payroll for the period of"
					+ " " + emp.getPeriodStarting() + " to " + emp.getPeriodEnding()
					+ " has been processed. Please log into <span><a href=\"https://secure.paycor.com/Accounts/Authentication/Signin\" style=\"color: red;\">PAYCOR</a>\r\n"
					+ "</span> portal for detailed paystub.  </p>\r\n" + "\r\n" + "            </div>\r\n" + "\r\n"
					+ "\r\n" + "                <div style= \"text-align: center; display: block;\">\r\n"
					+ "                    <table style=\"margin-top: 20px; margin-left: auto;margin-right: auto; border: 1px solid black; border-collapse: collapse\">\r\n"
					+ "                       <tbody>\r\n" + "                           <tr>\r\n"
					+ "                               <td style=\"text-align: center; color:  #2d60a3; border: 1px solid black; border-collapse: collapse \"><p style=\"padding: 0px; font-family: 'Bookman Old Style';\">Name</p></td>\r\n"
					+ "\r\n"
					+ "                               <td style=\"text-align: center; width: 200px;color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"padding: 0px; font-family: 'Bookman Old Style';\">"
					+ emp.getFirstName() + " " + emp.getLastName() + "</p></td>\r\n"
					+ "                           </tr>\r\n" + "                           <tr>\r\n"
					+ "                               <td style=\"text-align: center; color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">Gross Salary</p></td>\r\n"
					+ "\r\n"
					+ "                               <td style=\"text-align: center;color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">"
					+ emp.getGross() + "</p></td>\r\n" + "                           </tr>\r\n"
					+ "                           <tr>\r\n"
					+ "                               <td style=\"text-align: center; color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">Tax Deductions</p></td>\r\n"
					+ "\r\n"
					+ "                               <td style=\"text-align: center;color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">"
					+ emp.getTotalTaxes() + "</p></td>\r\n" + "                           </tr>\r\n"
					+ "                           <tr>\r\n"
					+ "                               <td style=\"text-align: center; color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">Medical Insurance</p></td>\r\n"
					+ "\r\n"
					+ "                               <td style=\"text-align: center;color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">"
					+ emp.getMed125() + "</p></td>\r\n" + "                           </tr>\r\n" + "\r\n"
					+ "                           <tr>\r\n"
					+ "                               <td style=\"text-align: center; color: #2d60a3; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">Net Salary</p></td>\r\n"
					+ "\r\n"
					+ "                               <td style=\"text-align: center;color: red; border: 1px solid black; border-collapse: collapse\"><p style=\"font-family: 'Bookman Old Style';\">"
					+ emp.getTotalSalary() + "</p></td>\r\n" + "                           </tr>\r\n"
					+ "                       </tbody>\r\n" + "                    </table>\r\n"
					+ "                </div>\r\n" + "\r\n"
					+ "                <div style=\"margin-top: 20px; text-align: center\">\r\n"
					+ "                    <p style=\"display: inline; color: #2d60a3;font-family: 'Bookman Old Style';\">For any concerns or questions, please send an email to </p> <a href=\"mailto:support@tabnerglobal.com\" style=\"color: red\">SUPPORT</a>\r\n"
					+ "                </div>\r\n" + "        </div>\r\n" + "    </body>\r\n" + "</html>", true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);

	}

}
