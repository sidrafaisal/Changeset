package DownloadsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckReleases {
	 
	public static Date parseDate(String inputDate ) {
		Date outDate = null;
		try {
			Pattern pattern = Pattern.compile("([0-9]{2})[\\-]{0,1}([a-zA-Z]{3})[\\-]{0,1}([0-9]{4})[\\ ]{0,1}([0-9]{2})[:]{0,1}([0-9]{2})");
			Matcher matcher = pattern.matcher(inputDate);
			matcher.find();
			
			StringBuilder date = new StringBuilder();
			for (int i = 1; i <= matcher.groupCount(); i++) {
				date.append(matcher.group(i));
			}
			
			SimpleDateFormat format = new SimpleDateFormat("ddMMMyyyyHHmm");
			outDate = format.parse(date.toString());
		} catch (ParseException pe) {
			System.out.println("Date cannot be parsed" + pe);
		}
		return outDate;	  
	}
   
		public static String getParent(String srcFolderURL)
		{
			int lastSlashPos = srcFolderURL.lastIndexOf("/");
			int secLastSlashPos = srcFolderURL.lastIndexOf("/", lastSlashPos-1);
			return srcFolderURL.substring(0, secLastSlashPos+1);	
		}
		
		public static String trim(String s, int width) {
			if (s.length() > width)
				return s.substring(0, width - 1) + ".";
			else
				return s;
		}
}
