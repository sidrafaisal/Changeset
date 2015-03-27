package ChangesetsDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DownloadsManager.DownloadsIndex;
import DownloadsManager.DownloadsRecorder;

public class LGDDownloader extends FileDownloader{

	String lastSequenceNumber = "", folderURL = "";
	
	List<String> url = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> orgFilename = new ArrayList<String>();
	List<String> modFilename = new ArrayList<String>();
	int noOfDownloads = 0;
	
	static boolean isTarFile = false;
	static List<String> orgTarFilename = new ArrayList<String>();
	static List<String> modTarFilename = new ArrayList<String>();
	
	public static void setTarFilename(String orgFilename, String modFilename){
		isTarFile = true;
		orgTarFilename.add(orgFilename);
		modTarFilename.add(modFilename);
	}
	
	public boolean downloadFolderLGD(String srcFolderURL, String orgDestFolderLocation) {
		File theDir = new File(orgDestFolderLocation);
		if (!theDir.exists())
			theDir.mkdir();
		
//		File configFile = 
				new File(orgDestFolderLocation + "Config");
//		if (!configFile.exists())
//			lastSequenceNumber = "0";
//		else	
//		{
//			lastSequenceNumber = DownloadsRecorder.getLastSequenceNo(orgDestFolderLocation+"Config");	
//			folderURL = DownloadsRecorder.getFolderURL(orgDestFolderLocation+"Config");	
//		}
		
		boolean downloaded = downloadFolder(srcFolderURL, orgDestFolderLocation, "0");
		if (downloaded)
		{
//		DownloadsIndex.addInIndex(orgDestFolderLocation + "index.xml", url, date, orgFilename, modFilename, noOfDownloads);
		DownloadsRecorder.saveDownloadsRecord(orgDestFolderLocation + "Config", new Date(), lastSequenceNumber, folderURL);
		}
		return downloaded;

	}
	public boolean downloadFolder(String srcFolderURL, String orgDestFolderLocation, String depth) {
		boolean isEmpty=true;
		int breadth = 0;		
		try {			
				Document doc = Jsoup.connect(srcFolderURL).get();
			//	System.out.println(doc.html());
				boolean mainIndex = true;

				for (Element table : doc.select("table")) {
					for (Element row : table.select("tr")) {
						for (Element tds : row.select("td")) {
							Elements links = tds.select("a[href]");
							for (Element link : links) {
								if (mainIndex == true)
									mainIndex = false;
								else {								
										String  title = trim(link.text(), 35),
												extension = link.attr("href"),
												fileURL = srcFolderURL + extension;								
	if(
			!fileURL.contains("changesets/")
			&&
			!fileURL.contains("LGD-Dump-110406-NodePositions.sorted.nt.bz2") &&
					!fileURL.contains("LGD-Dump-110406-Ontology.nt.bz2")&&
							!fileURL.contains("RelevantNodes.sorted.nt.bz2")&&
									!fileURL.contains("RelevantWays.sorted.nt.bz2")&&
									//		!fileURL.contains("Interlinks-110406-DBpedia.tar.bz2") &&
													!fileURL.contains("Interlinks-110406-GeoNames.tar.bz2")
			
			)
	{
										if (!(extension.contains("."))) {											
//											String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()),
//													newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;
//		
//											File theDir = new File(newDestFolderLocation);
//											if (!theDir.exists())
//												theDir.mkdir();
											
//											if (folderURL != "")		
//											{
//												int lastSequenceNo = Integer.parseInt(lastSequenceNumber) + 1;
//												
//												StringBuilder seqNo = new StringBuilder();
//												seqNo.append(lastSequenceNo);									
//												
//												lastSequenceNumber = seqNo.toString();
//											}
											lastSequenceNumber = depth + "-" + breadth;
											isEmpty = downloadFolder(fileURL, orgDestFolderLocation, lastSequenceNumber);								
											breadth++;

										}
										else if (fileURL.endsWith(".bz2") || fileURL.endsWith(".gz") ) {
												lastSequenceNumber = depth + "-" + breadth;
												FileDownloader.downloadFile(fileURL, orgDestFolderLocation, lastSequenceNumber);
												folderURL = srcFolderURL;
												isEmpty = false;
												
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
//										if(isEmpty==true)
//											new File (orgDestFolderLocation).delete();
											}
								}
							}
						}
					}
			}							
		} catch (InterruptedIOException iioe){
			System.out.println("Folder cannot be downloaded" + iioe);
			//downloadFolder( srcFolderURL, orgDestFolderLocation);
		} catch (IOException ioe) {
			System.out.println("Folder cannot be downloaded" + ioe);
			//downloadFolder( srcFolderURL, orgDestFolderLocation);
		}
		if (!isEmpty)
		{
			DownloadsIndex.addInIndex(orgDestFolderLocation, url, date, orgFilename, modFilename, noOfDownloads);
				
			url.clear();
			date.clear();
			orgFilename.clear();
			modFilename.clear();
			noOfDownloads = 0;
		}
		return (!isEmpty);
	}
	
}
