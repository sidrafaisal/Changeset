package ChangesetsDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DownloadsManager.DownloadsIndex;
import DownloadsManager.DownloadsRecorder;

public class DBPDownloader extends FileDownloader{

	String lastSequenceNumber = "";
	String folderURL = "";
	List<String> url = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> orgFilename = new ArrayList<String>();
	List<String> modFilename = new ArrayList<String>();
	int noOfDownloads = 0;

	public boolean downloadFolderDBP(String srcFolderURL, String orgDestFolderLocation) {	
		File theDir = new File(orgDestFolderLocation);
		if (!theDir.exists())
			theDir.mkdir();

//		File configFile = 
				new File(orgDestFolderLocation +"Config");
//		if (!configFile.exists())
//			lastSequenceNumber = "0";
//		else	
//		{
//			lastSequenceNumber = DownloadsRecorder.getLastSequenceNo(orgDestFolderLocation +"Config");	
//			folderURL = DownloadsRecorder.getFolderURL(orgDestFolderLocation +"Config");	
//		}
		boolean downloaded = downloadFolder(srcFolderURL, orgDestFolderLocation, "0");
		if (downloaded)
		{
			DownloadsRecorder.saveDownloadsRecord(orgDestFolderLocation+"Config", new Date(), lastSequenceNumber, folderURL);
//			DownloadsIndex.addInIndex(orgDestFolderLocation + "index.xml", url, date, orgFilename, modFilename, noOfDownloads);

		}

			return downloaded;
	}
	
	public boolean downloadFolder(String srcFolderURL, String orgDestFolderLocation, String depth) {
		boolean downloaded = true;
		int breadth = 0;

		try {
				Document doc = Jsoup.connect(srcFolderURL).get();
				//System.out.println(doc.toString());
				Elements links = doc.select("a[href]");
				boolean mainIndex = true;

				for (Element link : links) {
						String title = trim(link.text(), 35), 
								fileURL = link.attr("abs:href");
						if (mainIndex == true) {		
								String parentURL = getParent(srcFolderURL);
								if (!fileURL.equals(parentURL)) 
									mainIndex=false;
						}

						if (mainIndex!=true) {
							if (!title.contains(".nt")) {
//								String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()),
//										newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;
//
//								File theDir = new File(newDestFolderLocation);
//								if (!theDir.exists())
//									theDir.mkdir();
								
//								if (folderURL != "")		
//								{
//									int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
//									
//									StringBuilder seqNo = new StringBuilder();
//									seqNo.append(lastSequenceNo);									
//									
//									lastSequenceNumber = seqNo.toString();
//								}
								lastSequenceNumber = depth + "-" + breadth;
								downloaded = downloadFolder(fileURL, orgDestFolderLocation, lastSequenceNumber);								
								breadth++;
							} else {	
								lastSequenceNumber = depth + "-" + breadth;
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation,  lastSequenceNumber);
//								System.out.println("download file =  "+lastSequenceNumber);
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
		} catch (IOException ioe) {
			System.out.println("Folder cannot be downloaded" + ioe);
			downloaded = false;
		}
		if (downloaded)
		{
//			File indexFile = new File(orgDestFolderLocation + "index");
//			if (!indexFile.exists())
//				DownloadsIndex.createIndex(orgDestFolderLocation + "index.xml", url, date, orgFilename, modFilename, noOfDownloads);
//			else	
				DownloadsIndex.addInIndex(orgDestFolderLocation, url, date, orgFilename, modFilename, noOfDownloads);
				
			url.clear();
			date.clear();
			orgFilename.clear();
			modFilename.clear();
			noOfDownloads = 0;
		}
		return downloaded;
		}
	
}
