package ChangesetsDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DownloadsManager.DownloadsRecorder;

public class DBPDownloader extends FileDownloader{

	public static void downloadFolderDBPMain(String srcFolderURL, String orgDestFolderLocation) {
		boolean downloaded = downloadFolderDBP(srcFolderURL, orgDestFolderLocation);
		if (downloaded)
			DownloadsRecorder.saveLastDownloadDate (new Date(), orgDestFolderLocation+"History");
	}	
	
	public static boolean downloadFolderDBP(String srcFolderURL, String orgDestFolderLocation) {
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
								String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()),
										newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;

								File theDir = new File(newDestFolderLocation);
								if (!theDir.exists())
									theDir.mkdir();

								downloadFolderDBP(fileURL, newDestFolderLocation);
							} else {
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation);
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
