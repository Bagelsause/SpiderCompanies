package spiderSearch;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
	public static void main (String[] args) {
		ArrayList<String []> al = new ArrayList<String []>();
		int counter = 0;
		int nullCounter = 0;
		
		while (true) {
			String[] arr = new String[21];
			al.add(arr);
			Spider web = new Spider(counter + 5, "https://www.ycombinator.com/companies/", al.get(counter));
			
			if (al.get(counter)[2].equals("Does not exist")) {
				nullCounter++;
			} else {
				nullCounter = 0;
			}
			
			counter++;
			
			if (nullCounter > 500) { //Threshold for how many values we want to see as Non-existant before the program quits 
				int size = al.size();
				for (int i = size-1; i > (size-500); i--) {
					al.remove(i);
				}
				break;
				//Polishing end values
			}
		}
		
		//Polishing up the null values, and deleting them
		for (int i = al.size() - 1; i >= 0; i--) {
			if (al.get(i)[2].equals("Does not exist")){
				System.out.println(al.get(i));
				al.remove(i);
			}
		}
		
		System.out.println("DONE SCANNING WEBSITES");
		
		try {
			
			/*
			 * 
			 * Note about the only thing that you have to change
			 * 
			 * Please look below on lines 58 and 59
			 * 
			 * 
			 */
			
			
			//The only change to make this work on your computer would be to change this line below to any direct path
			FileWriter csvWriter = new FileWriter("\\Users\\TestUser\\Desktop\\Testing\\test.csv");
			csvWriter.append("Timestamp, Email Address, Company Name, Company URL, Company Phone Number, Company Address, Country, Company Founding date, Company founder\'s name and position, Company full description, Number of Employees, Startup Categories, Status, Batch, Total Funding ($USD), Number of Funding rounds, Investor(s), Company Blog URL / RSS Feed, Facebook URL, Twitter URL, Other social pages \n");
			
			for (int i = 0; i < al.size(); i++) {
				for (int j = 0; j < al.get(i).length; j++) {
					csvWriter.append(al.get(i)[j]);
					
					if (j != al.get(i).length-1) {
						csvWriter.append(", ");
					}
				}
				csvWriter.append("\n");
			}
			
			csvWriter.flush();
			csvWriter.close();
		} catch (Exception e) {
			System.out.println(e + "ERROR IN WRITING FILE.");
		}
		
		System.out.println("DONE WITH WRITING TO CSV");
		System.out.println("Exiting...");
	}
}

/*

Steps:
1) Get the url and get direct access to it
	a) starting at https://www.ycombinator.com/companies/5
2) Find the html element that matches the proper information
	a) information is roughly spread around, so lots of scanning needed
3) Save those elements in a string array, separated by commas
4) Add the string into the Arraylist to be built into a CSV file
	a) Using the label of "N, Company name, Company URL, Founder(s), Location, Description, Batch, Industry, Status"
	b) Build it to a .csv

*/
