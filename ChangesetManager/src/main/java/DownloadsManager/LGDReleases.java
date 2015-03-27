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
	List<String> orgFilename = new ArrayList<String>();
	List<String> modFilename = new ArrayList<String>();
	int noOfDownloads = 0;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	String lastSequenceNumber = "", folderURL = "", lastDownloadsRecordFile="";
	boolean monitor = false;
		
	static boolean isTarFile = false;
	static List<String> orgTarFilename = new ArrayList<String>();
	static List<String> modTarFilename = new ArrayList<String>();
	
	public static void setTarFilename(String orgFilename, String modFilename){
		isTarFile = true;
		orgTarFilename.add(orgFilename);
		modTarFilename.add(modFilename);
	}
	
	public boolean checkReleasesLGD(final String srcFolderURL, final String orgDestFolderLocation, final int frequency) {
       try
       {
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
//					DownloadsIndex.addInIndex(orgDestFolderLocation + "index.xml", url, date, orgFilename, modFilename, noOfDownloads);
					DownloadsRecorder.saveDownloadsRecord(lastDownloadsRecordFile, new Date(), lastSequenceNumber, folderURL); 
				}
				}
		    };
		    final ScheduledFuture<?> checkerHandle = scheduler.scheduleAtFixedRate(checker, 0, frequency, MINUTES);
		    scheduler.schedule(new Runnable() { public void run() { checkerHandle.cancel(true); }}, 2 * frequency, MINUTES);
       } catch (Exception e){
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


										lastSequenceNumber = depth + "-" + breadth;
										available = checkReleases(fileURL, orgDestFolderLocation, lastSequenceNumber);								
										breadth++;
//										if (monitor == true) {	
//											int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
//				
//											StringBuilder seqNo = new StringBuilder();
//											seqNo.append(lastSequenceNo);
//				
//											lastSequenceNumber = seqNo.toString();
//										}
									} else if (monitor == true && (fileURL.endsWith(".bz2") || fileURL.endsWith(".gz"))) {
										lastSequenceNumber = depth + "-" + breadth;
										Element node = tds.nextElementSibling();

										Date newAvailableDate = parseDate(node.text());
										Date lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadsRecordFile);
										int value = lastDownloadDate.compareTo(newAvailableDate);

										if (value < 0) {
											available = true;
											FileDownloader.downloadFile(fileURL, orgDestFolderLocation, lastSequenceNumber);
											folderURL = srcFolderURL;
											if(isTarFile){ 
												for(int i = 0; i < orgTarFilename.size(); i++)
												{
													url.add(fileURL);
													date.add(new Date().toString());
													orgFilename.add(orgTarFilename.get(i));
													modFilename.add(modTarFilename.get(i));
													noOfDownloads++;
												}
												orgTarFilename.clear();
												modTarFilename.clear();
												isTarFile = false;
											} else {
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
