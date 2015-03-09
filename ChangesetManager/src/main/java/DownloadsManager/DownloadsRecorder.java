package DownloadsManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadsRecorder {

//	 public static void writeInCatalogue(String cataloguename, String filename)
//	    {
//		 	int lastSlashPosition = filename.lastIndexOf("/"),
//	            lengthFilename =  filename.length();
//	        String outFilename = filename.substring(lastSlashPosition+1, lengthFilename);
//	            
//	            
//	        FileOutputStream fsCatalogueFile = null;
//	        OutputStreamWriter osWriter = null;
//
//	        try{
//	        	fsCatalogueFile = new FileOutputStream(cataloguename, true);
//	            osWriter = new OutputStreamWriter(fsCatalogueFile);
//	            osWriter.write(outFilename);
//	            osWriter.flush();
//	        }
//	        catch(Exception exp){
//	        	System.out.println("Catalogue maintenance unsuccessful"); 
//	        }
//	        finally {
//	            try{
//	                if(osWriter != null)
//	                    osWriter.close();
//
//	                if(fsCatalogueFile != null)
//	                	fsCatalogueFile.close();
//	            }
//	            catch (Exception exp){
//	            	System.out.println("Catalogue file cannot be saved");
//	            }
//	        }
//	    }
	    

	    public static void saveLastDownloadDate (Date date, String fileName)
	    {
		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			FileOutputStream fsLastDownloadDateFile = new FileOutputStream(
					fileName);
			OutputStreamWriter osWriter = new OutputStreamWriter(
					fsLastDownloadDateFile);
			osWriter.write(dateFormat.format(date));

			osWriter.flush();
			osWriter.close();
			fsLastDownloadDateFile.close();
		} catch (Exception exp) {
			System.out.println("Last edit date cannot be saved" + exp);
		}
	    }
	    
	public static Date getLastDownloadDate(String fileName) {
		Date LastDownloadDate = null;

		try {
			int ch;
			StringBuilder lastDownloadDate = new StringBuilder();

			FileInputStream fsLastDownloadDateFile = new FileInputStream(
					fileName);
			while ((ch = fsLastDownloadDateFile.read()) != -1)
				lastDownloadDate.append((char) ch);
			fsLastDownloadDateFile.close();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			LastDownloadDate = dateFormat.parse(lastDownloadDate.toString());

		} catch (Exception exp) {
			System.out.println("Last edit date can not be retrieved" + exp);
		}

		return LastDownloadDate;
	}
	
}
