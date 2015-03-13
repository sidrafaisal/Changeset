package DownloadsManager;

import java.io.File;

public class DownloadsManager {

public static boolean deleteFile(String filename) {
	boolean isDeleted = (new File(filename)).delete();
	if(isDeleted)
		System.out.println("File deleted successflly");
	else
		System.out.println("File cannot be deleted");
	return isDeleted ;
}

}
