package ChangesetsDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DownloadsManager.DownloadsRecorder;

public class LGDDownloader extends FileDownloader{
	
	public static void downloadFolderLGDMain(String srcFolderURL, String orgDestFolderLocation) {
		downloadFolderLGD(srcFolderURL, orgDestFolderLocation);
		DownloadsRecorder.saveLastDownloadDate (new Date(), orgDestFolderLocation+"History");
	}
	public static boolean downloadFolderLGD(String srcFolderURL, String orgDestFolderLocation) {
		boolean isEmpty=true;
		
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
										String 	extension = link.attr("href"),
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
											String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()),
													newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;
		
											File theDir = new File(newDestFolderLocation);
											if (!theDir.exists())
												theDir.mkdir();
											downloadFolderLGD(fileURL, newDestFolderLocation);

										}
										else if (fileURL.endsWith(".bz2") || fileURL.endsWith(".gz") ) {
												FileDownloader.downloadFile(fileURL, orgDestFolderLocation);
												isEmpty=false;
										}	
										if(isEmpty==true)
											new File (orgDestFolderLocation).delete();
											}
								}
							}
						}
					}
			}							
		} catch (IOException ioe) {
			System.out.println("Folder cannot be downloaded" + ioe);
		}
		return isEmpty;
	}
	
}
