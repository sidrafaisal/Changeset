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
	List<String> filename = new ArrayList<String>();
	int noOfDownloads = 0;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	String lastSequenceNumber = "", folderURL = "", lastDownloadsRecordFile="";
	boolean monitor = false;
	   
	   public void checkReleasesDBP(final String srcFolderURL, final String orgDestFolderLocation, final String lastDownloadsRecord, final int frequency) {	
	        final Runnable checker = new Runnable() {
                public void run() { 
				   lastSequenceNumber = DownloadsRecorder.getLastSequenceNo(orgDestFolderLocation+"Config");	
				   folderURL = DownloadsRecorder.getFolderURL(orgDestFolderLocation+"Config");	
				   lastDownloadsRecordFile = lastDownloadsRecord;
		
				   boolean available = checkReleases(srcFolderURL, orgDestFolderLocation);	
					if (available == false)
					{
						System.out.println("No new releases available");
						DownloadsRecorder.saveLastDownloadDate(new Date(), lastDownloadsRecordFile);
					}
					else
					{
						DownloadsIndex.addInIndex(orgDestFolderLocation + "index.xml", url, date, filename, noOfDownloads);
						DownloadsRecorder.saveDownloadsRecord(lastDownloadsRecordFile, new Date(), lastSequenceNumber, folderURL); 
					}
                }
	    	};
	    	final ScheduledFuture<?> checkerHandle = scheduler.scheduleAtFixedRate(checker, 0, frequency, MINUTES);
	    	scheduler.schedule(new Runnable() { public void run() { checkerHandle.cancel(true); }}, 2 * frequency, MINUTES);
		   }

		   public boolean checkReleases(String srcFolderURL, String orgDestFolderLocation) {
			boolean available = false;
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

							checkReleases(fileURL, orgDestFolderLocation);
							
							if (monitor == true) {	
								int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
	
								StringBuilder seqNo = new StringBuilder();
								seqNo.append(lastSequenceNo);
	
								lastSequenceNumber = seqNo.toString();
							}
						} else if (monitor == true){

							Node node = link.nextSibling();
							String metadata = node.toString().trim();
							
							int spacePosition = 17;
							String inputDate = metadata.substring(0, spacePosition);
							
							Date newAvailableDate = parseDate(inputDate),
								 lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadsRecordFile);
							int value = lastDownloadDate.compareTo(newAvailableDate);

							if (value < 0) {
								available = true;
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation, lastSequenceNumber);
								folderURL = srcFolderURL;
								
								url.add(fileURL);
								date.add(new Date().toString());
								filename.add(title);
								noOfDownloads++;
							}							
						}
					}
				}
			} catch (IOException ioe) {
				System.out.println("New releases cannot be checked" + ioe);

			}
			return available;
		    }
}
