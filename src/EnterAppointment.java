package src;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Scanner;

/**
 * Date 15-January-2020
 * 
 * @author 8180041, 8180150, 8180111, 8180108, 8180073, 8180070, 8180074,
 *         8180152
 * 
 * 
 *         This class handles the booking of the appointments. An appointment
 *         can be booked based on its date or based on both its date and the
 *         doctor the client will choose. If the client has difficulty finding
 *         something available, the center can suggest him available hours and
 *         days by making that way the booking process easier.
 *
 */
public class EnterAppointment {

	static Scanner m = new Scanner(System.in);
	static JFrame frame;

	/**
	 * This method allows the client to choose what type of examination he wants to
	 * book, he can search for the examination he is looking for and finally he
	 * chooses the criterion based on which he will book the appointment (date or
	 * both date and doctor).
	 * 
	 * @return = returns a string table of size three which contains the criterion,
	 *         the category and the examination
	 */
	public static String[] questionsToBegin() {

		String[] t1 = new String[3];
		boolean found = false;

		String eidikotita = Menu.chooseService();
		t1[1] = eidikotita;

		String search = JOptionPane.showInputDialog(frame, "Search for the examination you are looking for. \n"
				+ "(write ALL to show all the examinations of the category):");
		do {
			if (Menu.filterAndPrintServices(search, eidikotita) == false) {

				String a = JOptionPane.showInputDialog(frame,
						"Are you sure you want this service? : " + eidikotita
								+ "\r\n *yes (try again to search the examination)"
								+ "\r\n *no (choose another category of examinations)");

				if (a.contains("yes")) {

					search = JOptionPane.showInputDialog(frame, "Search for the examination you are looking for \n"
							+ "(write ALL to show all the examinations of the category):");
				} else {
					eidikotita = Menu.chooseService();
					t1[1] = eidikotita;

					search = JOptionPane.showInputDialog(frame, "Search for the examination you are looking for \n"
							+ "(write ALL to show all the examinations of the category):");
				}

			} else {
				found = true;
			}
		} while (!found);

		String choice = JOptionPane.showInputDialog(frame, "Insert the examination you want:");
		t1[2] = choice;

		String ans = Menu.chooseCriterion();
		t1[0] = ans;

		return t1;
	}

	/**
	 * This method finds the duration and the cost of the examination which the
	 * client finally chose.
	 * 
	 * @param t1 = the table containing criterion, category and examination
	 * 
	 * @return = returns a double table of size two that contains the duration and
	 *         the cost of the examination
	 */
	public static double[] choiceDuration(String[] t1) {

		double[] duration = new double[2];

		for (int j = 0; j < 22; j++) {

			if (t1[1].toLowerCase().contains(Services.eidikotites.get(j))) {

				for (int i = 0; i < Services.eidikotitesoles.get(j).size(); i++) {

					if (Services.eidikotitesoles.get(j).get(i).getName().contains(t1[2])) {
						duration[0] = Services.eidikotitesoles.get(j).get(i).getDuration();
						duration[1] = Services.eidikotitesoles.get(j).get(i).getCost();
						break;
					}
				}
			}
		}
		return duration;
	}

	/**
	 * This method books an appointment based on the chosen date.
	 * 
	 * @param t1       = table containing criterion, category and examination
	 * @param duration = table containing duration and cost of the examination
	 * @param calendar = the appointment table
	 * @param kat      = verifies the booking of the appointment
	 */
	public static void basedOnDate(String[] t1, double[] duration, Table[][] calendar, String kat) { // BASED ON DATE

		int validcode = 0;
		String stringid = "";
		String stringvalidcode = "";
		String stringcode = "";
		String eidikotita = t1[1];
		String anstime = "OTHER";
		String[][] data = new String[16][2];
		String protasi = "no suggestion";
		int day = 0;
		int month = 0;
		int existsnew = 0;
		double totalcells;
		int hours = 0;

		while ((anstime.toLowerCase().contains("OTHER")) || (existsnew == 0)) {
			String md[][] = new String[18][2];

			if (protasi.contains("SUGGESTION")) { // SUGGESTION

				md = suggestion(month, day, eidikotita, duration[0], calendar);
				day = Integer.parseInt(md[1][0]);
				month = Integer.parseInt(md[0][0]);
				for (int o = 2; o < 18; o++) {
					data[o - 2][0] = md[o][0];
					data[o - 2][1] = md[o][1];
				}

			} else { // NO SUGGESTION

				JOptionPane.showMessageDialog(frame, "Please insert the date you wish for.");

				String mon = JOptionPane.showInputDialog(frame, "Month:");
				month = Integer.parseInt(mon);

				String d = JOptionPane.showInputDialog(frame, "Day:");
				day = Integer.parseInt(d);
				data = calendar[month][day].checkingFreehours(calendar[month][day].getTable(), eidikotita, duration[0]);
			}

			for (int t = 1; t <= 16; t++) { // make sure that there's content in table data
				if (data[0][0].contains(calendar[month][day].getTable()[t][0])) {
					hours++;
					break;
				}
			}

			if (hours != 0) { // THERE ARE FREE HOURS

				anstime = JOptionPane.showInputDialog(frame,
						"Choose the time of the appointment (choose OTHER if none of the available hours is convenient to you):");

				if (!(anstime.contains("OTHER"))) { // ONE OF THE AVAILABLE HOURS IS SELECTED

					for (int kk = 0; kk < 16; kk++) { // the real j(= doctor)
						int num = Integer.parseInt(anstime.substring(0, 1));
						int dat = Integer.parseInt(data[kk][0].substring(0, 1));
						if (num == dat) {
							existsnew = Integer.parseInt(data[kk][1]);
							break;
						}
					}

					for (int k = 1; k <= 16; k++) {

						if (duration[0] <= 30) {
							if (calendar[month][day].getTable()[k][0].contains(anstime)) {

								validcode = (int) (Math.random()*100000 + 99999);
								stringvalidcode = String.valueOf(validcode);
								stringid = String.valueOf(Client.clients.get(Client.numOfClients - 1).getId());
								stringcode = stringvalidcode + stringid;
								calendar[month][day].getTable()[k][existsnew] = "  " + stringcode + "     ";
								break;
							}
						} else {
							totalcells = (duration[0] / 30);
							if (calendar[month][day].getTable()[k][0].contains(anstime)) {
								validcode = (int) (Math.random()*100000 + 99999);
								for (int l = 0; l < totalcells; l++) {
									stringvalidcode = String.valueOf(validcode);
									stringid = String.valueOf(Client.clients.get(Client.numOfClients - 1).getId());
									stringcode = stringvalidcode + stringid;
									calendar[month][day].getTable()[k + l][existsnew] =  stringcode + " " + t1[2] + "   ";
								}
								break;
							}
						}
					}
				} else { // NONE OF THE AVAILABLE HOURS IS SELECTED BY THE CLIENT

					protasi = JOptionPane.showInputDialog(frame,
							"We understand that our available hours may not have been what you are looking for,"
									+ "\r\n please insert new appoinment information or let us suggest you several available"
									+ "\r\n hours at nearby dates.\nChoose:\nNEW APPOINTMENT\nSUGGESTION");
					existsnew = 0;
				}
			} else { // DID NOT FIND FREE HOURS

				protasi = JOptionPane.showInputDialog(frame,
						"We didn't find available hours for you at thie day,"
								+ "\r\n if you want choose another date or let us suggest you"
								+ "\r\n available hours at nearby dates.\nChoose:\nNEW APPOINTMENT\nSUGGESTION");

			}
		}

		if (kat.contains("katachorish")) {

			JOptionPane.showMessageDialog(frame,
					"Hello " + Client.clients.get(Client.numOfClients - 1).getName() + " "
							+ Client.clients.get(Client.numOfClients - 1).getSurname()
							+ "!\r\nYour appointment has been booked at " + day + "/" + month + "/2019\n" + "Time: "
							+ anstime + "\r\nExamination: " + t1[2] + " (category: " + t1[1] + ")\n" + "Price: "
							+ duration[1] + "$" + "\r\nValidation Code: " + stringvalidcode);

			Menu.sumPlus(t1[1]);
		} else {

			JOptionPane.showMessageDialog(frame, "Your appointment has been transfered for " + day + "/" + month
					+ "/2019\n" + "Time: " + anstime + "\r\nYour new Validation Code is: " + stringvalidcode);
			Menu.sumPlus(t1[1]);
		}
	}

	/**
	 * This method books an appointment based on the date and doctor that the client
	 * has chosen.
	 * 
	 * @param t1       = table containing criterion, category and examination
	 * @param duration = table containing duration and cost of the examination
	 * @param calendar = the appointment table
	 * @param kat      = verifies the booking of the appointment
	 * @param doc      = the name of the chosen doctor
	 */
	public static void basedOnDateAndEmp(String[] t1, double[] duration, String kat, String doc, Table[][] calendar) {
		// BASED ON DATE AND EMPLOYEE

		int validcode = 0;
		String stringid = null;
		String stringvalidcode = null;
		String stringcode = null;
		String anstime = "OTHER";
		int exists = 0;
		String protasi = "no ";
		String eidikotita = t1[1];
		ArrayList<String> doc_names = new ArrayList<String>();
		doc_names = Employees.returnDocNames(eidikotita);
		int doc_num = doc_names.size();
		int day = 0;
		int month = 0;
		int md[] = new int[3];
		String doctor = null;
		int empid = 0;

		while ((anstime.contains("OTHER")) || (exists == 0)) {

			if (!(protasi.contains("SUGGESTION"))) {

				JOptionPane.showMessageDialog(frame, "Please insert the date you wish for.");
				String mon = JOptionPane.showInputDialog(frame, "Month: ");
				month = Integer.parseInt(mon);
				String da = JOptionPane.showInputDialog(frame, "Day: ");
				day = Integer.parseInt(da);

				if (kat.contains("katachorish")) {

					StringBuilder text = new StringBuilder();
					text.append("Choose the doctor you wish for:" + "\r\n");
					for (int d = 0; d < doc_num; d++) {
						text.append(doc_names.get(d) + "\r\n");
					}
					doctor = JOptionPane.showInputDialog(frame, text.toString());

				} else {
					doctor = doc;
				}

				for (int g = 0; g < Employees.employees.size(); g++) {
					if (Employees.employees.get(g).getEmp_name().contains(doctor)) {
						empid = g;
						break;
					}
				}

				exists = calendar[month][day].checkingFreehours(calendar[month][day].getTable(), eidikotita,
						duration[0], empid); // the doctor
			} else {
				if (kat.contains("katachorish")) {

					StringBuilder text = new StringBuilder();
					text.append("Choose the doctor you wish for:" + "\r\n");
					for (int d = 0; d < doc_num; d++) {
						text.append(doc_names.get(d) + "\r\n");
					}
					doctor = JOptionPane.showInputDialog(frame, text.toString());
				} else {
					doctor = doc;
				}

				for (int g = 0; g < Employees.employees.size(); g++) {
					if (Employees.employees.get(g).getEmp_name().equals(doctor)) {
						empid = g;
					}
				}
				md = suggestion(month, day, eidikotita, duration[0], empid, calendar);
				day = md[1];
				month = md[0];
				exists = md[2];
			}

			double totalcells = 0;

			if (exists != 0) { // THERE ARE FREE HOURS

				if (kat.contains("katachorish")) {

					anstime = JOptionPane.showInputDialog(frame,
							"Choose the time you wish for (choose OTHER if none of the available hours is convenient to you)!");
				} else {

					anstime = JOptionPane.showInputDialog(frame,
							"Choose the time you wish for (choose OTHER if none of the available hours is convenient to you)!");
				}

				if (!(anstime.contains("OTHER"))) { // ONE OF THE AVAILABLE HOURS IS SELECTED

					for (int k = 1; k <= 16; k++) {

						if (duration[0] <= 30) {
							if (calendar[month][day].getTable()[k][0].contains(anstime)) {

								validcode = (int) (Math.random()*100000 + 99999);
								stringvalidcode = String.valueOf(validcode);
								stringid = String.valueOf(Client.clients.get(Client.numOfClients - 1).getId());
								stringcode = stringvalidcode + stringid;
								calendar[month][day].getTable()[k][exists] = "   " + stringcode;
								break;
							}
						} else {
							totalcells = (duration[0] / 30);
							if (calendar[month][day].getTable()[k][0].contains(anstime)) {
								validcode = (int) (Math.random()*100000 + 99999);
								for (int l = 0; l < totalcells; l++) {
									stringvalidcode = String.valueOf(validcode);
									stringid = String.valueOf(Client.clients.get(Client.numOfClients - 1).getId());
									stringcode = stringvalidcode + stringid;
									calendar[month][day].getTable()[k + l][exists] = "   " + stringcode;
								}
								break;
							}
						}
					}
				} else { // NONE OF THE AVAILABLE HOURS IS SELECTED BY THE CLIENT

					protasi = JOptionPane.showInputDialog(frame,
							"We understand that our available hours may not have been what you are looking for,"
									+ "\r\nplease insert new appoinment information or let us suggest you several "
									+ "\r\navailable hours at nearby dates.\nChoose:\nNEW APPOINTMENT\nSUGGESTION");
				}
			} else { // DID NOT FIND FREE HOURS

				protasi = JOptionPane.showInputDialog(frame,
						"We didn't find available hours for you at thie day,"
								+ "\r\nif you want choose another date or let us suggest you"
								+ "\r\navailable hours at nearby dates.\nChoose:\nNEW APPOINTMENT\nSUGGESTION");
			}
		}

		if (kat.contains("katachorish")) {

			JOptionPane.showMessageDialog(frame,
					"Hello " + Client.clients.get(Client.numOfClients - 1).getName() + " "
							+ Client.clients.get(Client.numOfClients - 1).getSurname()
							+ "!\nYour appointment has been booked for " + day + "/" + month + "/2019\n" + "Time: "
							+ anstime + "\nExamination: " + t1[2] + " (category: " + t1[1] + ")\n" + "Price: "
							+ duration[1] + "$" + "\nDoctor: " + doctor + "\nValidation Code: " + stringvalidcode);

			Menu.sumPlus(t1[1]);
		} else {

			JOptionPane.showMessageDialog(frame, "Your appointment has been ranfered for " + day + "/" + month
					+ "/2019\n" + "Time: " + anstime + "\r\n Your new Validation Code is: " + stringvalidcode);
			Menu.sumPlus(t1[1]);
		}
	}

	/**
	 * This method suggests available hours at a certain day for the examination
	 * requested. This happens only if the client asks to.
	 * 
	 * @param month      = month requested
	 * @param day        = day requested
	 * @param eidikotita = category of the examination requested
	 * @param duration   = duration of the examination
	 * @param calendar   = the appointment table
	 * 
	 * @return = returns
	 */
	public static String[][] suggestion(int month, int day, String eidikotita, double duration, Table[][] calendar) { // BASED
																														// ON
																														// DATE

		String[][] md = new String[18][2];
		int ex1 = 0;
		String data1[][] = new String[16][2];

		while (ex1 == 0) { // FIND THE NEXT DAY'S FREE HOURS

			if (day == 30) {

				data1 = calendar[month + 1][1].checkingFreehours(calendar[month + 1][1].getTable(), eidikotita,
						duration);

				if (!(data1[0][0].equals(null))) { // check if there is available hours
					md[1][0] = "1";
					md[0][0] = String.valueOf(month + 1);
					ex1 = 1;
					JOptionPane.showMessageDialog(frame,
							"These are the available hours for " + md[1][0] + "/" + md[0][0] + "!");
				}
				day = 1;

			} else {

				data1 = calendar[month][day + 1].checkingFreehours(calendar[month][day + 1].getTable(), eidikotita,
						duration);

				if (!(data1[0][0].equals(null))) { // check if there are any available hours
					md[1][0] = String.valueOf(day + 1);
					md[0][0] = String.valueOf(month);
					ex1 = 1;
					JOptionPane.showMessageDialog(frame,
							"These are the available hours for " + md[1][0] + "/" + md[0][0] + "!");

				}
				day = day + 1;
			}
		}
		md[0][1] = "0";
		md[1][1] = "0";
		for (int l = 2; l < 18; l++) {
			md[l][0] = data1[l - 2][0];
			md[l][1] = data1[l - 2][1];
		}
		return md;
	}

	/**
	 * This method suggests available hours at a certain day and for certain doctor
	 * for the examination requested. This happens only if the client asks to.
	 * 
	 * @param month      = month requested
	 * @param day        = day requested
	 * @param eidikotita = category of the examination requested
	 * @param duration   = duration of the examination
	 * @param empid      = the column of the doctor at the appointment table
	 * @param calendar   = the appointment table
	 * 
	 * @return
	 */
	public static int[] suggestion(int month, int day, String eidikotita, double duration, int empid,
			Table[][] calendar) { // BASED ON DATE AND EMPLOYEE

		int[] md = new int[3];
		int ex1 = 0;
		int d = 0;

		while (ex1 == 0) { // FIND THE NEXT'S DAY FREE HOURS

			if (day == 30) {
				ex1 = calendar[month + 1][1].checkingFreehours(calendar[month + 1][1].getTable(), eidikotita, duration,
						empid);
				if (ex1 != 0) {

					md[1] = 1;
					md[0] = month + 1;
					JOptionPane.showMessageDialog(frame,
							"These are the available hours for " + md[1] + "/" + md[0] + "!");
				}
				day = 1;

			} else {
				ex1 = calendar[month][day + d + 1].checkingFreehours(calendar[month][day + d + 1].getTable(),
						eidikotita, duration, empid);

				if (ex1 != 0) {
					md[1] = day + d + 1;
					md[0] = month;
					JOptionPane.showMessageDialog(frame,
							"These are the available hours for " + md[1] + "/" + md[0] + "!");

				}
				day = day + 1;
			}
		}
		md[2] = ex1;
		return md;
	}

}
