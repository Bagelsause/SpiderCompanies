package spiderSearch;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {
	
	public static void main(String args[]) {
        
    	Document website = null;
    	String companyName = "SendHub";
    	String founders;
    	String firstFounder;
    	
		try {
			website = Jsoup.connect("https://www.ycombinator.com/companies/11").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Elements elements = website.select("div[class=founder-info flex-row]");
			firstFounder = elements.first().text();
			founders = elements.next().text();
			System.out.println(firstFounder);
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
			e.printStackTrace();
		}
		
		System.out.println(founders);
			/*Elements elements = website.select("div[class=founder-info flex-row]");
		String firstFounder = elements.first().text();
		String founders = elements.next().text();
		
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
		System.out.println(founders);*/
		
		
		
		
		
		
		
		
		
		
		
		
		/*String topFounder;
		String honorific = "";
		topFounder = elements.first().text();
		//Removing honorifics
		if (topFounder.indexOf("Chief") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("Chief"));
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("CEO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("CEO"));
			honorific = "CEO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("CTO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("CTO"));
			honorific = "CTO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("COO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("COO"));
			honorific = "COO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("CFO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("CFO"));
			honorific = "CFO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("CMO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("CMO"));
			honorific = "CMO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("CHRO") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("CHRO"));
			honorific = "CHRO";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		} else if (topFounder.indexOf("Founder") != -1) {
			topFounder = topFounder.substring(0, topFounder.indexOf("Founder"));
			honorific = "Founder";
			if (topFounder.indexOf(", ") != -1) {
				topFounder = topFounder.substring(0, topFounder.indexOf(", "));
			}
		}
		elements = website.select("div[class=content]");
		String content;
		String founders;
		int savedIndex = 1;
		content = elements.first().text();
		founders = content.substring(content.indexOf(topFounder));
		System.out.println(founders);
		//Kyle VogtCruise Daniel Kan, COO Daniel KanCruise
		for (int i = 0; i < founders.length()-1; i++) {
			if (Character.isLowerCase(founders.charAt(i)) && Character.isUpperCase(founders.charAt(i+1))) {
				savedIndex = i;
				break;
			}
		}
		founders = founders.substring(savedIndex+1-topFounder.length());
		for (int i = 0; i < founders.length()-1; i++) {
			if (Character.isLowerCase(founders.charAt(i)) && Character.isUpperCase(founders.charAt(i+1))) {
				founders = founders.substring(0, i+1) + ", " + founders.substring(i+1);
			}
		}
		System.out.println(founders);
		//ArrayList<String> founderList = new ArrayList<String>();
		while (founders.indexOf(", " + companyName) != -1) {
			founders = founders.substring(0, founders.indexOf(", "+ companyName)) + founders.substring(founders.indexOf(", " + companyName) + companyName.length()+2);
		}
		//Kyle Vogt Daniel Kan, COO Daniel Kan
		
		System.out.println(founders);
		*/
		
    }
}
