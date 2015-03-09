package DownloadsManager;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ChangesetsDownloader.FileDownloader;

public class LGDReleases extends CheckReleases{
	
	public static void checkReleasesLGDMain(String srcFolderURL, String orgDestFolderLocation, String lastDownloadDateFile) {

		int available = checkReleasesLGD(srcFolderURL, orgDestFolderLocation, lastDownloadDateFile);
		if (available == 0)
			System.out.println("No new releases available");

		DownloadsRecorder.saveLastDownloadDate(new Date(), lastDownloadDateFile);
	}

	public static int checkReleasesLGD(String srcFolderURL, String orgDestFolderLocation, String lastDownloadDateFile) {
		int available = 0;
		try {
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
								String extension = link.attr("href"), 
										fileURL = srcFolderURL	+ extension;

									if (!(extension.contains("."))) {
										String diffFromPrevURL = fileURL.substring(srcFolderURL.length(),fileURL.length()), 
												newDestFolderLocation = orgDestFolderLocation + diffFromPrevURL;

										checkReleasesLGD(fileURL, newDestFolderLocation, lastDownloadDateFile);

									} else if (fileURL.endsWith(".bz2") || fileURL.endsWith(".gz")) {
	
										Element node = tds.nextElementSibling();

										Date newAvailableDate = parseDate(node.text());
										Date lastDownloadDate = DownloadsRecorder.getLastDownloadDate(lastDownloadDateFile);
										int value = lastDownloadDate.compareTo(newAvailableDate);

										if (value >= 0)
											;
										else if (value < 0) {
											available = 1;
											FileDownloader.downloadFile(fileURL, orgDestFolderLocation);
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
