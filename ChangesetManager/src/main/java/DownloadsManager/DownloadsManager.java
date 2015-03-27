package DownloadsManager;

import java.io.File;

public class DownloadsManager {

public static boolean deleteFile(String folderLocation, String filename) {
	boolean isDeleted = (new File(folderLocation+filename)).delete();
	if(isDeleted)
	{
		DownloadsIndex.deleteFromIndexUsingName(folderLocation,  filename) ;
		System.out.println("File deleted successflly");
	} else
		System.out.println("File cannot be deleted");
	return isDeleted ;
}

}
