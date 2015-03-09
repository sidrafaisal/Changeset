package DownloadsManager;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import ChangesetsDownloader.FileDownloader;

public class DBPReleases extends CheckReleases{
	
	   public static void checkReleasesDBPMain(String srcFolderURL, String orgDestFolderLocation, String lastDownloadDateFile) {	
		   
			boolean available = checkReleasesDBP(srcFolderURL, orgDestFolderLocation, lastDownloadDateFile);	
			if (available == false)
				System.out.println("No new releases available");
			
			DownloadsRecorder.saveLastDownloadDate(new Date(), lastDownloadDateFile); 
			
		   }

		   public static boolean checkReleasesDBP(String srcFolderURL, String orgDestFolderLocation, String lastDownloadDateFile) {
			boolean available = false;
			try {
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
							String diffFromPrevURL = fileURL.substring(srcFolderURL.length(), fileURL.length()), 
									newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;

							checkReleasesDBP(fileURL, newDestFolderLocation, lastDownloadDateFile);
						} else {
							Node node = link.nextSibling();
							String metadata = node.toString().trim();
							
							int spacePosition = 17;
							String inputDate = metadata.substring(0, spacePosition);
							
							Date newAvailableDate = parseDate(inputDate),
								 lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadDateFile);
							int value = lastDownloadDate.compareTo(newAvailableDate);

							if (value >= 0)
								;
							else if (value < 0) {
								available = true;
								FileDownloader.downloadFile(fileURL, orgDestFolderLocation);
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
