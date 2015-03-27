package ChangesetsDownloader;

import static org.junit.Assert.*;

import org.junit.Test;

public class LGDDownloaderTest {
	String folderLocationLGD = "LGDChangesetsTest/", 
			folderURLLGD = "http://downloads.linkedgeodata.org/releases/2011-04-06/";
	@Test
	public void testDownloadFolderLGD() {
		LGDDownloader tester = new LGDDownloader();
	    assertEquals("must be true", true, tester.downloadFolderLGD(folderURLLGD,folderLocationLGD));	
	
//		fail("Not yet implemented");
	}

}
