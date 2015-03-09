package DownloadsManager;

import ChangesetsDownloader.DBPDownloader;
import ChangesetsDownloader.LGDDownloader;

public class main {

	public static void main (String[] args)  {
	
		String folderLocationLGD = "/Users/Sidra/Documents/LGD/Changesets/", 
				folderURLLGD = "http://downloads.linkedgeodata.org/releases/2011-04-06/",
				folderLocationDBP = "/Users/Sidra/Documents/DBPedia/Changesets/", 
				folderURLDBP = "http://live.dbpedia.org/changesets/2014/11/12/07/";

		LGDDownloader.downloadFolderLGDMain(folderURLLGD, folderLocationLGD);
		DBPDownloader.downloadFolderDBPMain(folderURLDBP, folderLocationDBP);

		DBPReleases.checkReleasesDBPMain(folderURLDBP, folderLocationDBP, folderLocationDBP + "History");
		LGDReleases.checkReleasesLGDMain(folderURLLGD, folderLocationLGD, folderLocationLGD + "History");
	}

}