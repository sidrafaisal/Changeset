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

	String lastSequenceNumber = "", folderURL = "";
	List<String> url = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> filename = new ArrayList<String>();
	int noOfDownloads = 0;

	public void downloadFolderDBP(String srcFolderURL, String orgDestFolderLocation) {		
		File theDir = new File(orgDestFolderLocation);
		if (!theDir.exists())
			theDir.mkdir();

		File configFile = new File(orgDestFolderLocation +"Config");
		if (!configFile.exists())
			lastSequenceNumber = "0";
		else	
		{
			lastSequenceNumber = DownloadsRecorder.getLastSequenceNo(orgDestFolderLocation +"Config");	
			folderURL = DownloadsRecorder.getFolderURL(orgDestFolderLocation +"Config");	
		}
		boolean downloaded = downloadFolder(srcFolderURL, orgDestFolderLocation);
		if (downloaded)
		{
			DownloadsRecorder.saveDownloadsRecord(orgDestFolderLocation+"Config", new Date(), lastSequenceNumber, folderURL);
			DownloadsIndex.createIndex(orgDestFolderLocation + "index.xml", url, date, filename, noOfDownloads);
		}	
	}
	
	public boolean downloadFolder(String srcFolderURL, String orgDestFolderLocation) {
		boolean downloaded = true;

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
								if (folderURL != "")		
								{
									int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
									
									StringBuilder seqNo = new StringBuilder();
									seqNo.append(lastSequenceNo);									
									
									lastSequenceNumber = seqNo.toString();
								}
								downloadFolder(fileURL, orgDestFolderLocation);
							} else {								
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation, lastSequenceNumber);
								
								folderURL = srcFolderURL;
								
								url.add(fileURL);
								date.add(new Date().toString());
								filename.add(title);
								noOfDownloads++;
							}
						} 
				}				
		} catch (IOException ioe) {
			System.out.println("Folder cannot be downloaded" + ioe);
			downloaded = false;
		}
		return downloaded;
		}
	
}
