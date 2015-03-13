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
import org.jsoup.select.Elements;

import ChangesetsDownloader.FileDownloader;
import DownloadsManager.DownloadsRecorder;

public class LGDReleases extends CheckReleases{
	
	List<String> url = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> filename = new ArrayList<String>();
	int noOfDownloads = 0;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	String lastSequenceNumber = "", folderURL = "", lastDownloadsRecordFile="";
	boolean monitor = false;
	
	public void checkReleasesLGD(final String srcFolderURL, final String orgDestFolderLocation, final String lastDownloadsRecord, final int frequency) {
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
			boolean mainIndex = true;

			for (Element table : doc.select("table")) {
				for (Element row : table.select("tr")) {
					for (Element tds : row.select("td")) {
						Elements links = tds.select("a");
						for (Element link : links) {
							if (mainIndex == true)
								mainIndex = false;
							else {
								String title = trim(link.text(), 35),
										extension = link.attr("href"), 
										fileURL = srcFolderURL	+ extension;

									if (!(extension.contains("."))) {
//										String diffFromPrevURL = fileURL.substring(srcFolderURL.length(),fileURL.length()), 
//												newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;

										checkReleases(fileURL, orgDestFolderLocation);
										
										if (monitor == true) {	
											int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
				
											StringBuilder seqNo = new StringBuilder();
											seqNo.append(lastSequenceNo);
				
											lastSequenceNumber = seqNo.toString();
										}
										
									} else if (monitor == true && (fileURL.endsWith(".bz2") || fileURL.endsWith(".gz"))) {
	
										Element node = tds.nextElementSibling();

										Date newAvailableDate = parseDate(node.text());
										Date lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadsRecordFile);
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
						}
					}
				}
		} catch (IOException ioe) {
			System.out.println("New releases cannot be checked" + ioe);
		}
		return available;
	}
	
}
