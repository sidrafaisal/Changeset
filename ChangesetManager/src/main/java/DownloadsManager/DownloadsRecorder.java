package DownloadsManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadsRecorder {

	public static String getFolderURL(String fileName)
	{
		String folderURL = "";

	try {
		
		FileInputStream fs = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.contains("FolderURL="))
			{
				int startPosURL = line.indexOf("=") + 1;
				folderURL = line.substring(startPosURL);
				break;
			}
		}	 
		br.close();
		fs.close();

	} catch (Exception exp) {
		System.out.println("FolderURL can not be retrieved" + exp);
	}

	return folderURL;
	}
	
	public static String getLastSequenceNo(String fileName)
	{
		String sequenceNo = "";

	try {
		
		FileInputStream fs = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.contains("LastSequenceNumber="))
			{
				int startPosURL = line.indexOf("=") + 1;
				sequenceNo = line.substring(startPosURL);
				break;
			}
		}
	 
		br.close();
		fs.close();

	} catch (Exception exp) {
		System.out.println("Last Sequence Number can not be retrieved" + exp);
	}

	return sequenceNo;
	}
	
	public static Date getLastDownloadDate(String fileName) {
		Date LastDownloadDate = null;

		try {
			String lastDownloadDate="";

			FileInputStream fsLastDownloadDateFile = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(fsLastDownloadDateFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				if(line.contains("Date="))
				{
					int startPosURL = line.indexOf("=") + 1;
					lastDownloadDate = line.substring(startPosURL);
					break;
				}
			}
			br.close();
			fsLastDownloadDateFile.close();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			LastDownloadDate = dateFormat.parse(lastDownloadDate);

		} catch (Exception exp) {
			System.out.println("Last edit date can not be retrieved" + exp);
		}

		return LastDownloadDate;
	}
	
	public static void saveDownloadsRecord(String fileName, Date date, String lastSequenceNumber, String folderURL)
	{
		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			FileOutputStream fsLastDownloadDateFile = new FileOutputStream(fileName);
			OutputStreamWriter osWriter = new OutputStreamWriter(fsLastDownloadDateFile);
			String metadata = "Date="+ dateFormat.format(date) + "\n" +
								"LastSequenceNumber=" + lastSequenceNumber + "\n" +
								"FolderURL=" + folderURL;
			
			osWriter.write(metadata);

			osWriter.flush();
			osWriter.close();
			fsLastDownloadDateFile.close();
		} catch (Exception exp) {
			System.out.println("Downloads record cannot be saved" + exp);
		}	
	}
	
	    public static void saveLastDownloadDate (Date date, String fileName)
	    {
	    	String lastSequenceNumber = getLastSequenceNo(fileName),
	    			folderURL = getFolderURL(fileName);
	    	
	    	saveDownloadsRecord(fileName, date, lastSequenceNumber, folderURL);
	    }
	
}
