package DownloadsManager;

import static org.junit.Assert.*;

import org.junit.Test;

public class DBPReleasesTest {
	String folderLocationDBP = "DBPChangesetsTest/", 
			folderURLDBP = "http://live.dbpedia.org/changesets/2015/03/20/18/";
	
	@Test
	public void testCheckReleasesDBP() {
		DBPReleases tester = new DBPReleases ();
		tester.checkReleasesDBP(folderURLDBP, folderLocationDBP, 20);
	    assertEquals("must be true", true, tester.checkReleasesDBP(folderURLDBP,folderLocationDBP, 20));
	}

}
