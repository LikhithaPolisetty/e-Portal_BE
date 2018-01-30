package com.tabner.services;


import org.springframework.stereotype.Component;
import com.tabner.entities.Employee;

@Component
public class ExtractionMethods {

	/*
	 * this method takes each employee String and parses it to create the employee
	 * object with all the attributes such as name, id, hours, rate, gross, taxes,
	 * netpay
	 */
	public Employee parse(String string, String periodEnding, String payDate) {
	
		Employee e = new Employee();
		e.setPeriodEnding(periodEnding);
		e.setPayDate(payDate);

		String[] s1 = string.split("-");

		String[] s2 = s1[0].split("100 ");

		e.setName(s2[0]);
		
		
		String[] s3 = s2[1].split(" ");
		for (String s : s3) {
			if (s.contains("ExpNt")) {
				Employee emp = this.empWithExpenses(string, periodEnding, payDate);
				return emp;
			}
		}

		int i = s3.length;
		if (i == 8) {
			e.setName(e.getName() + s3[0]);
			e.setRate(s3[1]);
			e.setHours(s3[2]);
			e.setEarnings(s3[3]);
			e.setGross(s3[4]);
			e.getTaxes().add(s3[5]);
			e.getDeductions().add(s3[6]);
			e.getNetPay().add(s3[7]);
		}
		if (i == 7) {

			/*
			 * added to solve the name breakage issue
			 */
			if (s3[s3.length - 2].contains("SOC") || s3[s3.length - 2].contains("MED")
					|| s3[s3.length - 2].contains("FITWH")) {
				e.setName(e.getName() + s3[0]);
				e.setRate(s3[1]);
				e.setHours(s3[2]);
				e.setEarnings(s3[3]);
				e.setGross(s3[4]);
				e.getTaxes().add(s3[5]);
				e.getNetPay().add(s3[6]);
			} else {
				e.setRate(s3[0]);
				e.setHours(s3[1]);
				e.setEarnings(s3[2]);
				e.setGross(s3[3]);
				e.getTaxes().add(s3[4]);
				e.getDeductions().add(s3[5]);
				e.getNetPay().add(s3[6]);
			}
		}
		if (i == 6) {
			e.setRate(s3[0]);
			e.setHours(s3[1]);
			e.setEarnings(s3[2]);
			e.setGross(s3[3]);
			e.getTaxes().add(s3[4]);
			e.getNetPay().add(s3[5]);
		}

		if(s1[1].contains(" 100 ")) {
			
			String[] s4 = s1[1].split(" ");
			e.setId(s4[0] + " " + s4[1]);
			e.getNetPay().add(s4[(s4.length) - 2] + " " + s4[(s4.length) - 1]);
			e.setHours(e.getHours() + " | " + s4[4]);
			e.setRate(e.getRate() + " | " + s4[3]);
			e.setEarnings(Double.parseDouble(e.getEarnings().replaceAll(",", "")) + Double.parseDouble(s4[5].replaceAll(",", "")) + "");
			if(s4.length == 10) {
				e.getTaxes().add(s4[6]);
				e.getDeductions().add(s4[7]);
			}
			if(s4.length == 9) {
				if (s4[s4.length - 3].contains("Med125") || s4[s4.length - 3].contains("Misc") || s4[s4.length - 3].contains("Partial")
						|| s4[s4.length - 3].contains("401k") || s4[s4.length - 3].contains("Net")
						|| s4[s4.length - 3].contains("401kRoth")) {
					e.getDeductions().add(s4[s4.length - 3]);
				} else {
					e.getTaxes().add(s4[s4.length - 3]);
				}
			}
			
		
			
		} else {
			
			String[] s4 = s1[1].split(" ");
			e.setId(s4[0] + " " + s4[1]);

			e.getNetPay().add(s4[(s4.length) - 2] + " " + s4[(s4.length) - 1]);
			int j = s4.length;
			if (j == 9) {
				e.getTaxes().add(s4[5]);
				e.getDeductions().add(s4[6]);
				return e;
			}
			if (j == 8) {
				if (s4[s4.length - 3].contains("Med125") || s4[s4.length - 3].contains("Misc") || s4[s4.length - 3].contains("Partial")
						|| s4[s4.length - 3].contains("401k") || s4[s4.length - 3].contains("Net")
						|| s4[s4.length - 3].contains("401kRoth")) {
					e.getDeductions().add(s4[s4.length - 3]);
				} else {
					e.getTaxes().add(s4[s4.length - 3]);
				}
			}

			if (j == 6) {
				e.getTaxes().add(s4[2]);
				e.getDeductions().add(s4[3]);
			}
			if (j == 5) {
				if (s4[s4.length - 3].contains("SOC") || s4[s4.length - 3].contains("MED")
						|| s4[s4.length - 3].contains("FITWH")) {
					e.getTaxes().add(s4[s4.length - 3]);
				} else {
					e.getDeductions().add(s4[s4.length - 3]);
				}
			}
		}
		

		int k = s1.length;
		if (k == 3) {

			/*
			 * processing line 3
			 */
			String hours = e.getHours();
			String[] s5 = s1[2].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 1) {
					if (s6[0].contains("Med125") || s6[0].contains("Misc") || s6[0].contains("Partial") || s6[0].contains("401k")
							|| s6[0].contains("Net") || s6[0].contains("401kRoth")) {
						e.getDeductions().add(s6[0]);
					} else {
						e.getTaxes().add(s6[0]);
					}
				}
				if (s6.length == 2) {
					e.getTaxes().add(s6[0]);
					e.getTaxes().add(s6[1]);
				}
			}
		}
		if (k == 4) {

			/*
			 * processing line 3
			 */

			String[] s7 = s1[2].split(" ");
			int n = s7.length;
			if (n == 1) {
				if (s7[0].contains("Med125") || s7[0].contains("Misc") || s7[0].contains("Partial") || s7[0].contains("401k")
						|| s7[0].contains("Net") || s7[0].contains("401kRoth")) {
					e.getDeductions().add(s7[0]);
				} else {
					e.getTaxes().add(s7[0]);
				}
			}
			if (n == 2) {
				e.getTaxes().add(s7[0]);
				e.getDeductions().add(s7[1]);
			}

			/*
			 * processing line 4
			 */
			String hours = e.getHours();
			String[] s5 = s1[3].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 1) {
					if (s6[0].contains("Med125") ||s6[0].contains("Misc") || s6[0].contains("Partial") || s6[0].contains("401k")
							|| s6[0].contains("Net") || s6[0].contains("401kRoth")) {
						e.getDeductions().add(s6[0]);
					} else {
						e.getTaxes().add(s6[0]);
					}
				}
				if (s6.length == 2) {
					e.getTaxes().add(s6[0]);
					e.getTaxes().add(s6[1]);
				}
			}

		}
		if (k == 5) {

			/*
			 * processing line 4
			 */

			String[] s8 = s1[2].split(" ");
			int o = s8.length;
			if (o == 1) {
				if (s8[0].contains("Med125") || s8[0].contains("Misc") || s8[0].contains("Partial") || s8[0].contains("401k")
						|| s8[0].contains("Net") || s8[0].contains("401kRoth")) {
					e.getDeductions().add(s8[0]);
				} else {
					e.getTaxes().add(s8[0]);
				}
			}
			if (o == 2) {
				e.getTaxes().add(s8[0]);
				e.getDeductions().add(s8[1]);
			}

			/*
			 * processing line 4
			 */

			String[] s7 = s1[3].split(" ");
			int n = s7.length;
			if (n == 1) {
				if (s7[0].contains("Med125") ||s7[0].contains("Misc") || s7[0].contains("Partial") || s7[0].contains("401k")
						|| s7[0].contains("Net") || s7[0].contains("401kRoth")) {
					e.getDeductions().add(s7[0]);
				} else {
					e.getTaxes().add(s7[0]);
				}
			}
			if (n == 2) {
				e.getTaxes().add(s7[0]);
				e.getDeductions().add(s7[1]);
			}

			/*
			 * processing line 5
			 */
			String hours = e.getHours();
			String[] s5 = s1[4].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 1) {
					if (s6[0].contains("Med125") ||s6[0].contains("Misc") || s6[0].contains("Partial") || s6[0].contains("401k")
							|| s6[0].contains("Net") || s6[0].contains("401kRoth")) {
						e.getDeductions().add(s6[0]);
					} else {
						e.getTaxes().add(s6[0]);
					}
				}
				if (s6.length == 2) {
					e.getTaxes().add(s6[0]);
					e.getTaxes().add(s6[1]);
				}
			}
		}

		return e;
	}

	/*
	 * parsing the employee who has expenses
	 */

	public Employee empWithExpenses(String string, String periodEnding, String payDate) {
		Employee e = new Employee();
		e.setPeriodEnding(periodEnding);
		e.setPayDate(payDate);

		String[] s1 = string.split("-");

		String[] s2 = s1[0].split("100 ");

		e.setName(s2[0]);
		/*
		 * int i = s2[1].length(); System.out.println(i);
		 */
		String[] s3 = s2[1].split(" ");

		int i = s3.length;
		if (i == 9) {
			e.setName(e.getName() + s3[0]);
			e.setRate(s3[1]);
			e.setHours(s3[2]);
			e.setEarnings(s3[3]);
			e.setExpenses(s3[4]);
			/*
			 * Double exp = Double.parseDouble(s3[4].split("ExpNt")[0]) +
			 * Double.parseDouble(s3[5]); e.setGross(exp.toString());
			 */
			e.setGross(s3[3] + " + " + s3[4]);
			e.getTaxes().add(s3[6]);
			e.getDeductions().add(s3[7]);
			e.getNetPay().add(s3[8]);
		}
		if (i == 8) {

			/*
			 * added to solve the name breakage issue
			 */
			if (s3[s3.length - 2].contains("SOC") || s3[s3.length - 2].contains("MED")
					|| s3[s3.length - 2].contains("FITWH")) {
				e.setName(e.getName() + s3[0]);
				e.setRate(s3[1]);
				e.setHours(s3[2]);
				e.setEarnings(s3[3]);
				e.setExpenses(s3[4]);
				/*
				 * Double exp = Double.parseDouble(s3[4].split("ExpNt")[0]) +
				 * Double.parseDouble(s3[5]); e.setGross(exp.toString());
				 */
				e.setGross(s3[3] + " + " + s3[4]);
				e.getTaxes().add(s3[6]);
				e.getNetPay().add(s3[7]);
			} else {
				e.setRate(s3[0]);
				e.setHours(s3[1]);
				e.setEarnings(s3[2]);
				e.setExpenses(s3[3]);
				/*
				 * Double exp = Double.parseDouble(s3[3].split("ExpNt")[0]) +
				 * Double.parseDouble(s3[4]); e.setGross(exp.toString());
				 */
				e.setGross(s3[2] + " + " + s3[3]);
				e.getTaxes().add(s3[5]);
				e.getDeductions().add(s3[6]);
				e.getNetPay().add(s3[7]);
			}
		}
		if (i == 7) {
			e.setRate(s3[0]);
			e.setHours(s3[1]);
			e.setEarnings(s3[2]);
			e.setExpenses(s3[3]);
			/*
			 * Double exp = Double.parseDouble(s3[3].split("ExpNt")[0]) +
			 * Double.parseDouble(s3[4]); e.setGross(exp.toString());
			 */
			e.setGross(s3[2] + " + " + s3[3]);
			e.getTaxes().add(s3[5]);
			e.getNetPay().add(s3[6]);
		}

		String[] s4 = s1[1].split(" ");
		e.setId(s4[0] + " " + s4[1]);

		e.getNetPay().add(s4[(s4.length) - 2] + " " + s4[(s4.length) - 1]);
		int j = s4.length;
		if (j == 10) {
			e.getTaxes().add(s4[6]);
			e.getDeductions().add(s4[7]);
			return e;
		}
		if (j == 9) {
			if (s4[s4.length - 3].contains("Med125") || s4[s4.length - 3].contains("Misc") || s4[s4.length - 3].contains("Partial")
					|| s4[s4.length - 3].contains("401k") || s4[s4.length - 3].contains("Net")
					|| s4[s4.length - 3].contains("401kRoth")) {
				e.getDeductions().add(s4[s4.length - 3]);
			} else {
				e.getTaxes().add(s4[s4.length - 3]);
			}
		}

		if (j == 7) {
			e.getTaxes().add(s4[3]);
			e.getDeductions().add(s4[4]);
		}

		// commented

		/*
		 * if(j==6) { if(s4[s4.length-3].contains("SOC") ||
		 * s4[s4.length-3].contains("MED") || s4[s4.length-3].contains("FITWH")) {
		 * e.getTaxes().add(s4[s4.length-3]); } else {
		 * e.getDeductions().add(s4[s4.length-3]); } }
		 */

		if (j == 6) {
			if (s4[s4.length - 3].contains("Med125") || s4[s4.length - 3].contains("Misc") || s4[s4.length - 3].contains("Partial")
					|| s4[s4.length - 3].contains("401k") || s4[s4.length - 3].contains("Net")
					|| s4[s4.length - 3].contains("401kRoth")) {
				e.getDeductions().add(s4[s4.length - 3]);
			} else {
				e.getTaxes().add(s4[s4.length - 3]);
			}
		}

		int k = s1.length;
		if (k == 3) {

			/*
			 * processing line 3
			 */
			String hours = e.getHours();
			String[] s5 = s1[2].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 2) {
					if (s6[1].contains("Med125") || s6[1].contains("Misc") || s6[1].contains("Partial") || s6[1].contains("401k")
							|| s6[1].contains("Net") || s6[1].contains("401kRoth")) {
						e.getDeductions().add(s6[1]);
					} else {
						e.getTaxes().add(s6[1]);
					}
				}
				if (s6.length == 3) {
					e.getTaxes().add(s6[1]);
					e.getTaxes().add(s6[2]);
				}
			}
		}
		if (k == 4) {

			/*
			 * processing line 3
			 */

			String[] s7 = s1[2].split(" ");
			int n = s7.length;
			if (n == 2) {
				if (s7[1].contains("Med125") || s7[1].contains("Misc") || s7[1].contains("Partial") || s7[1].contains("401k")
						|| s7[1].contains("Net") || s7[1].contains("401kRoth")) {
					e.getDeductions().add(s7[1]);
				} else {
					e.getTaxes().add(s7[1]);
				}
			}
			if (n == 3) {
				e.getTaxes().add(s7[1]);
				e.getDeductions().add(s7[2]);
			}

			/*
			 * processing line 4
			 */
			String hours = e.getHours();
			String[] s5 = s1[3].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 1) {
					if (s6[0].contains("Med125") || s6[0].contains("Misc") || s6[0].contains("Partial") || s6[0].contains("401k")
							|| s6[0].contains("Net") || s6[0].contains("401kRoth")) {
						e.getDeductions().add(s6[0]);
					} else {
						e.getTaxes().add(s6[0]);
					}
				}
				if (s6.length == 2) {
					e.getTaxes().add(s6[0]);
					e.getTaxes().add(s6[1]);
				}
			}

		}
		if (k == 5) {

			/*
			 * processing line 3
			 */

			String[] s8 = s1[2].split(" ");
			int o = s8.length;
			if (o == 2) {
				if (s8[1].contains("Med125") ||s8[1].contains("Misc") || s8[1].contains("Partial") || s8[1].contains("401k")
						|| s8[1].contains("Net") || s8[1].contains("401kRoth")) {
					e.getDeductions().add(s8[1]);
				} else {
					e.getTaxes().add(s8[1]);
				}
			}
			if (o == 3) {
				e.getTaxes().add(s8[1]);
				e.getDeductions().add(s8[2]);
			}

			/*
			 * processing line 4
			 */

			String[] s7 = s1[3].split(" ");
			int n = s7.length;
			if (n == 1) {
				if (s7[0].contains("Med125") || s7[0].contains("Misc") || s7[0].contains("Partial") || s7[0].contains("401k")
						|| s7[0].contains("Net") || s7[0].contains("401kRoth")) {
					e.getDeductions().add(s7[0]);
				} else {
					e.getTaxes().add(s7[0]);
				}
			}
			if (n == 2) {
				e.getTaxes().add(s7[0]);
				e.getDeductions().add(s7[1]);
			}

			/*
			 * processing line 5
			 */
			String hours = e.getHours();
			String[] s5 = s1[4].split(hours + " ");
			int l = s5.length;
			if (l == 2) {
				String[] s6 = s5[1].split(" ");
				if (s6.length == 1) {
					if (s6[0].contains("Med125") || s6[0].contains("Misc") || s6[0].contains("Partial") || s6[0].contains("401k")
							|| s6[0].contains("Net") || s6[0].contains("401kRoth")) {
						e.getDeductions().add(s6[0]);
					} else {
						e.getTaxes().add(s6[0]);
					}
				}
				if (s6.length == 2) {
					e.getTaxes().add(s6[0]);
					e.getTaxes().add(s6[1]);
				}
			}
		}

		return e;
	}

}
