package DownloadsManager;

import static org.junit.Assert.*;

import org.junit.Test;

public class LGDReleasesTest {
	String folderLocationLGD = "LGDChangesetsTest/", 
			folderURLLGD = "http://downloads.linkedgeodata.org/releases/2011-04-06/";
	@Test
	public void testCheckReleasesLGD() {
		LGDReleases tester = new LGDReleases();
		tester.checkReleasesLGD(folderURLLGD, folderLocationLGD, 20);
	    assertEquals("must be true", true, tester.checkReleasesLGD(folderURLLGD, folderLocationLGD, 20));
//		fail("Not yet implemented");
	}

}
