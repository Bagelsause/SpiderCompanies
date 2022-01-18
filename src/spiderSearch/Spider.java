package spiderSearch;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	private String link;
	private String[] outputArray;
	private String companyName;
	private String companyURL;
	private String founders;
	private String location;
	private String description;
	private String batch;
	private String status;
	private String founded;
	private String teamSize;
	private String categories;
	private String facebookURL;
	private String twitterURL;
	private String linkedinURL;
	private String crunchbaseURL;
	private String socialMedias;
	Document website;
	
	//N, Company name, Company URL, Founder(s), Location, Description, Batch, Industry, Status
	public Spider (int number, String URL, String[] array) {
		link = URL + Integer.toString(number);
		outputArray = array;
		
		try {
			website = Jsoup.connect(link).get();
		} catch (IOException e) {
			System.out.println("Company does not exist on website " + link);
		}
		
		try {
			//Created getters to match with the setters, but used them only for debug, not included in this code.
			setCompanyName();
			setCompanyURL();
			setFounders();
			setLocationSizeFounded();
			setDescription();
			setBatchCategoriesStatus();
			setSocialMedia();
		} catch (Exception e) {
			//Most likely due to the 404 error, so the company physically doesn't exist
			number = -1;
			companyName = "Does not exist";
			companyURL = "";
			founders = "";
			location = "";
			description = "";
			batch = "";
			status = "";
		}
		
		//How it should be ordered
		//Timestamp
		//Email Address
		//Company Name
		//Company URL
		//Company Phone Number
		//Company Address
		//Country
		//Company Founding date
		//Company founder's name and position
		//Company full description
		//Number of Employees
		//Startup Categories
		//Total Funding ($USD)
		//Number of Funding rounds
		//Investor(s)
		//Company Blog URL / RSS Feed
		//Facebook URL
		//Twitter URL
		//Other social pages
		
		
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		outputArray[0] = timestamp.toString();
		outputArray[1] = ""; //Email Address
		outputArray[2] = companyName;
		outputArray[3] = companyURL;
		outputArray[4] = ""; //Company Phone number
		outputArray[5] = location;
		outputArray[6] = ""; //Country <--- see if we can determine country from location variable
		outputArray[7] = founded;
		outputArray[8] = founders;
		outputArray[9] = description;
		outputArray[10] = teamSize;
		outputArray[11] = categories;
		outputArray[12] = status;
		outputArray[13] = batch;
		outputArray[14] = ""; //Funding
		outputArray[15] = ""; //Rounds of funding
		outputArray[16] = ""; //Investors
		outputArray[17] = ""; //Company blog url/rss feed
		outputArray[18] = facebookURL;
		outputArray[19] = twitterURL;
		outputArray[20] = socialMedias;
		
		
		
		/*outputArray[0] = Integer.toString(number);
		outputArray[1] = companyName;
		outputArray[2] = companyURL;
		outputArray[3] = founders;
		outputArray[4] = location;
		outputArray[5] = description;
		outputArray[6] = batch;
		outputArray[7] = status;
		*/
		array = outputArray;
	}
	
	/*
	 * Since the output file is a .csv, data with commas in it won't work, so we have to surround it with quotes for the data to be read literally
	 * 
	 * Example:
	 * if (companyFeature.indexOf(",") > -1) {  <--- Checks if there is a comma in the writing
			String companyFeatureConcat = "\"" + companyFeature + "\""; <--- if it does, creates a new string to surround it with quotes
			companyFeature = companyFeatureConcat; <--- sets the feature equal to the feature with quotes
		}
	 * 
	 */
	
	
	//Setters for data
	public void setCompanyName() {
		Elements elements = website.select("div[class=heavy]");
		companyName = elements.first().text();
		
		if (companyName.indexOf(",") > -1) {
			String companyNameConcat = "\"" + companyName + "\"";
			companyName = companyNameConcat;
		}
		
	}
	
	public void setCompanyURL() {
		//Note: not the ycombinator url, their actual URL, such as amazon.com
		Elements elements = website.select("div[class=links] a[target=_blank]");
		companyURL = elements.first().text();
		if (companyURL.indexOf(",") > -1) {
			String companyURLConcat = "\"" + companyURL + "\"";
			companyURL = companyURLConcat;
		}
	}
	
	public void setFounders() {
		try {
			Elements elements = website.select("div[class=founder-info flex-row]");
			String firstFounder = elements.first().text();
			founders = elements.next().text();
			
			firstFounder = firstFounder.substring(0, firstFounder.indexOf(companyName));
			ArrayList<String> founderList = new ArrayList<String>();
			founderList.add(firstFounder);
			
			while(founders.indexOf(companyName) != -1) {
				founderList.add(founders.substring(0, (founders.indexOf(companyName))));
				founders = founders.substring(founders.indexOf(companyName)+companyName.length());
			}
			founders = "";
			
			for (int i = 0; i < founderList.size(); i++) {
				String thisFounder = founderList.get(i);
				try {
					thisFounder = thisFounder.substring(0, thisFounder.indexOf(", "));
					founders += thisFounder.trim() + ", ";
				} catch (Exception e) {
					//In the case of "Jason Freedman" with no comma separation
					int count = 0;
					int endIndex = 0;
					for (int j = 0; j < founderList.get(i).length(); j++) {
						if (founderList.get(i).trim().charAt(j) == ' ') {
							count++;
							if (count == 2) {
								endIndex = j;
								break;
							}
						}
					}
					founders += founderList.get(i).trim().substring(0, endIndex) + ", ";
				}
			}
			
			//trim the end of the ", "
			founders = founders.substring(0, founders.length()-2);
			if (founders.indexOf(",") > -1) {
				String foundersConcat = "\"" + founders + "\"";
				founders = foundersConcat;
			}

		} catch (Exception e) {
			founders = "";
		}
	}
	
	public void setLocationSizeFounded() {

		//Went to the common class and grabbed all of the text that I had available, looking something like
		//"Founded2012 Team Size30 LocationUnited Kingdom"
		//Separated all of the pieces of data from one another
		//Parsed each one if it had a comma inside of it
		Elements elements = website.select("div[class=highlight-box] div[class=facts]");
		try {
			
			String factBox = elements.first().text();
			founded = factBox.substring(0, factBox.indexOf(" "));
			founded = founded.substring(7);
			if (founded.indexOf(",") > -1) {
				String locationConcat = "\"" + founded + "\"";
				founded = locationConcat;
			}
		} catch (Exception e) {
			founded = "";
		}
		try {
			
			String factBox = elements.first().text();
			teamSize = factBox.substring(factBox.indexOf("Team Size"), factBox.indexOf("Location"));
			teamSize = teamSize.substring(9);
			if (teamSize.indexOf(",") > -1) {
				String locationConcat = "\"" + teamSize + "\"";
				teamSize = locationConcat;
			}
		} catch (Exception e) {
			teamSize = "";
		}
		try {
			
			String factBox = elements.first().text();
			location = factBox.substring(factBox.indexOf("Location"));
			location = location.substring(8);
			if (location.indexOf(",") > -1) {
				String locationConcat = "\"" + location + "\"";
				location = locationConcat;
			}
		} catch (Exception e) {
			location = "";
		}
	}
	
	public void setDescription() {
		Elements elements = website.select("div[class=main-box] h3");
		description = elements.first().text();
		try {
			elements = website.select("div[class=main-box] p[class=pre-line]");
			if (elements.first().text().length() != 0) {
				description += " | " + elements.first().text();
			}
		} catch (Exception e) {
			description += "";
		}
		
		if (description.indexOf('"') != -1) {
			description = description.replace('"', '\'');
		}
		
		if (description.indexOf(",") > -1) {
			String descriptionConcat = "\"" + description + "\"";
			description = descriptionConcat;
		}
	}
	
	public void setBatchCategoriesStatus() {
		//Returned values for batch would be W11, S12, etc. based on what batch they applied in.
		//checkstring contains all of the elements in the pills, which include the batch, their status, and their industries
		//Example:
		//checkString = "W12 HardwareMachine LearningSoftware Active"
		//Separates each category and industry into separate sections, separated by a comma, and will surround them with quotation marks
		String checkString;
		Elements elements = website.select("div[class=flex-row]");
		checkString = elements.first().text();
		batch = checkString.substring(0, 3);
		if (checkString.contains("Active")) {
			status = "Active";
		} else if (checkString.contains("Public")) {
			status = "Public";
		} else if (checkString.contains("Inactive")) {
			status = "Inactive";
		} else {
			status = "Acquired";
		}
		try {
			categories = checkString.substring(4, checkString.indexOf(status)-1);
			for (int i = 0; i < categories.length()-1; i++) {
				if (Character.isLowerCase(categories.charAt(i)) && Character.isUpperCase(categories.charAt(i+1))) {
					categories = categories.substring(0, i+1) + ", " + categories.substring(i+1);
				}
			}
			
			categories = categories.replace("Io, T", "IoT, ");
			
			categories = categories.replace("Saa, S", "SaaS, ");
			if (categories.indexOf("SaaS, ") == categories.length()-6) {
				categories = categories.substring(0, categories.length()-2);
				categories = categories.replace("B2BSaaS", "B2B, SaaS");
				System.out.println(categories);
			}
			
			if (categories.indexOf(",") > -1) {
				String categoriesConcat = "\"" + categories + "\"";
				categories = categoriesConcat;
			}
		} catch (Exception e) {
			categories = "";
		}
	}
	
	public void setSocialMedia() {
		//Grabs all possible links that they can have, according to ycombinator, which are facebook, twitter, linkedin, and crunchbase
		//Will add more if there are others.
		try {
			Elements elements = website.select("div[class=social-links] a[class=social facebook]");
			facebookURL = elements.first().absUrl("href");
		} catch (Exception e) {
			facebookURL = "";
		}
		try {
			Elements elements = website.select("div[class=social-links] a[class=social twitter]");
			twitterURL = elements.first().absUrl("href");
		} catch (Exception e) {
			twitterURL = "";
		}
		try {
			Elements elements = website.select("div[class=social-links] a[class=social linkedin]");
			linkedinURL = elements.first().absUrl("href") + ",";
		} catch (Exception e) {
			linkedinURL = "";
		}
		try {
			Elements elements = website.select("div[class=social-links] a[class=social crunchbase]");
			crunchbaseURL = elements.first().absUrl("href");
		} catch (Exception e) {
			crunchbaseURL = "";
		}
		
		socialMedias = "\"" + linkedinURL + crunchbaseURL + "\"";
	}
}
