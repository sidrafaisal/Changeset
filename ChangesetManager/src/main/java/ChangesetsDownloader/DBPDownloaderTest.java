package ChangesetsDownloader;

import static org.junit.Assert.*;

import org.junit.Test;

public class DBPDownloaderTest {
	String  folderLocationDBP = "DBPChangesetsAllTest/", 
			folderURLDBP = "http://live.dbpedia.org/changesets/";//2015/03/20/18/
	@Test
	public void testDownloadFolderDBP() {
		DBPDownloader tester = new DBPDownloader();
	    assertEquals("must be true", true, tester.downloadFolderDBP(folderURLDBP,folderLocationDBP));	

//		fail("Not yet implemented");
	}

	@Test
	public void testDownloadFolder() {
//		DBPDownloader tester = new DBPDownloader();
//	    assertEquals("must be true", true, tester.downloadFolder(folderURLDBP,folderLocationDBP,"0"));
		//fail("Not yet implemented");
	}

}
