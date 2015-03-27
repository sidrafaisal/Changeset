package DownloadsManager;

import java.io.FileWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import java.util.Date;

import javax.xml.namespace.QName;

import ChangesetsDownloader.DBPDownloader;
import ChangesetsDownloader.LGDDownloader;

public class main {
	
	public static void main (String[] args) throws Exception  {
	
		String folderLocationLGD = "LGDChangesets/", 
				folderURLLGD = "http://downloads.linkedgeodata.org/releases/2011-04-06/",
				folderLocationDBP = "DBPChangesets/", 
				folderURLDBP = "http://live.dbpedia.org/changesets/";//2015/03/20/18/
		int frequency = 30;

//		DBPDownloader dbpd=new DBPDownloader();
//		dbpd.downloadFolderDBP(folderURLDBP, folderLocationDBP);
//		
//		DBPReleases dbp = new DBPReleases();
//		dbp.checkReleasesDBP(folderURLDBP, folderLocationDBP, frequency);

//		LGDDownloader lgdd= new LGDDownloader();
//		lgdd.downloadFolderLGD(folderURLLGD, folderLocationLGD);
		
//		LGDReleases lgd = new LGDReleases();
//		lgd.checkReleasesLGD(folderURLLGD, folderLocationLGD, frequency);		
		}
}
