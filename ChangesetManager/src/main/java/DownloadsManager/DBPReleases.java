package DownloadsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MINUTES;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import ChangesetsDownloader.FileDownloader;

public class DBPReleases extends CheckReleases{
	
	List<String> url = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> orgFilename = new ArrayList<String>();
	List<String> modFilename = new ArrayList<String>();
	int noOfDownloads = 0;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	String lastSequenceNumber = "", folderURL = "", lastDownloadsRecordFile="";
	boolean monitor = false;
	   
	   public boolean checkReleasesDBP(final String srcFolderURL, final String orgDestFolderLocation, final int frequency) {	
	       try{
	    	   final Runnable checker = new Runnable() {
	       
                public void run() { 
                	lastDownloadsRecordFile = orgDestFolderLocation+"Config";
				   lastSequenceNumber = DownloadsRecorder.getLastSequenceNo(lastDownloadsRecordFile);	
				   folderURL = DownloadsRecorder.getFolderURL(lastDownloadsRecordFile);	
				   
		
				   boolean available = checkReleases(srcFolderURL, orgDestFolderLocation, "0");	
					if (available == false)
					{
						System.out.println("No new releases available");
						DownloadsRecorder.saveLastDownloadDate(new Date(), lastDownloadsRecordFile);
					}
					else
					{
//						DownloadsIndex.addInIndex(orgDestFolderLocation + "index.xml", url, date, orgFilename, modFilename, noOfDownloads);
						DownloadsRecorder.saveDownloadsRecord(lastDownloadsRecordFile, new Date(), lastSequenceNumber, folderURL); 
					}
                }
	    	};
	    	final ScheduledFuture<?> checkerHandle = scheduler.scheduleAtFixedRate(checker, 0, frequency, MINUTES);
	    	scheduler.schedule(new Runnable() { public void run() { checkerHandle.cancel(true); }}, 2 * frequency, MINUTES);
		   } catch(Exception e){
		   return false;
		   }
	       return true;
	   }

		   public boolean checkReleases(String srcFolderURL, String orgDestFolderLocation, String depth) {
			boolean available = false;
			int breadth = 0;
			try {
				if (folderURL.equals(srcFolderURL)) 
					monitor = true;

				Document doc = Jsoup.connect(srcFolderURL).get();
				Elements links = doc.select("a");

				boolean mainIndex = true;

				for (Element link : links) {
					String title = trim(link.text(), 35),
							fileURL = link.attr("abs:href");

					if (mainIndex == true) {
						String parentURL = getParent(srcFolderURL);
						if (!fileURL.equals(parentURL))
							mainIndex = false;
					}

					if (mainIndex != true) {

						if (!title.contains(".nt")) {
//							String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()), 
//									newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;
							
							lastSequenceNumber = depth + "-" + breadth;
							available = checkReleases(fileURL, orgDestFolderLocation, lastSequenceNumber);
							breadth++;
							
//							if (monitor == true) {	
//								int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
//	
//								StringBuilder seqNo = new StringBuilder();
//								seqNo.append(lastSequenceNo);
//	
//								lastSequenceNumber = seqNo.toString();
//							}
						} else if (monitor == true){
							lastSequenceNumber = depth + "-" + breadth;
							
							Node node = link.nextSibling();
							String metadata = node.toString().trim();
							
							int spacePosition = 17;
							String inputDate = metadata.substring(0, spacePosition);
							
							Date newAvailableDate = parseDate(inputDate),
								 lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadsRecordFile);
							int value = lastDownloadDate.compareTo(newAvailableDate);
							System.out.println("date= "+  newAvailableDate );
							System.out.println("date= "+  lastDownloadDate );
							if (value < 0) {
								available = true;
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation, lastSequenceNumber);
								folderURL = srcFolderURL;
								
								url.add(fileURL);
								date.add(new Date().toString());
								orgFilename.add(title);
								
								int lastSlashPos = fileURL.lastIndexOf("/");
								if (lastSlashPos < 0)
									return false;

								String modFileName = lastSequenceNumber + "-" + fileURL.substring(lastSlashPos + 1);							
								modFilename.add(modFileName);
								noOfDownloads++;
							}							
						}
					}
				}
			} catch (IOException ioe) {
				System.out.println("New releases cannot be checked" + ioe);

			}
			if (available)
			{
				DownloadsIndex.addInIndex(orgDestFolderLocation, url, date, orgFilename, modFilename, noOfDownloads);
					
				url.clear();
				date.clear();
				orgFilename.clear();
				modFilename.clear();
				noOfDownloads = 0;
			}
			return available;
		    }
}
