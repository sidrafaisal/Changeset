package DownloadsManager;

import ChangesetsDownloader.DBPDownloader;
import ChangesetsDownloader.LGDDownloader;


public class main {
	

	public static void main (String[] args)  {
	
		String folderLocationLGD = "LGDChangesets/", 
				folderURLLGD = "http://downloads.linkedgeodata.org/releases/2011-04-06/",
				folderLocationDBP = "DBPChangesets/", 
				folderURLDBP = "http://live.dbpedia.org/changesets/2015/03/13/15/";
		int frequency = 30;
		
//		LGDDownloader lgdd= new LGDDownloader();
//		lgdd.downloadFolderLGD(folderURLLGD, folderLocationLGD);
//		
//		DBPDownloader dbpd=new DBPDownloader();
//		dbpd.downloadFolderDBP(folderURLDBP, folderLocationDBP);

	//	DownloadsManager.deleteFile(folderLocationDBP+"1-000001.added.nt");

//		DBPReleases dbp = new DBPReleases();
//		dbp.checkReleasesDBP(folderURLDBP, folderLocationDBP, folderLocationDBP + "Config", frequency);

//		LGDReleases lgd = new LGDReleases();
//		lgd.checkReleasesLGDMain(folderURLLGD, folderLocationLGD, folderLocationLGD + "Config", frequency);
		}
}
