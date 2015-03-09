package ChangesetsDownloader;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownloader {

	public static boolean downloadFile(String fileURL, String folderPath) {
		try {
			int lastSlashPos = fileURL.lastIndexOf("/");
			if (lastSlashPos < 0)
				return false;

			File parentFolder = new File(folderPath);
			if (parentFolder != null)
				parentFolder.mkdirs();
			String fullFileName = folderPath + fileURL.substring(lastSlashPos + 1);

			URL u = new URL(fileURL);
			InputStream is = u.openStream();
			FileOutputStream output = new FileOutputStream(fullFileName);

			byte data[] = new byte[1024];
			int size = 0;

			while ((size = is.read(data)) != -1) {
				output.write(data, 0, size);
			}
			output.flush();
			output.close();
			is.close();

			System.out.println(fileURL + ": File downloaded successfully");
			FileDecompressor.decompressZipFile(fullFileName);
			
		} catch (MalformedURLException mue) {
			System.out.println("File cannot be downloaded" + mue);
			return false;

		} catch (InterruptedIOException iioe){
			System.out.println("File cannot be downloaded iioe" + iioe);
			return false;
		}catch (IOException ioe) {
			System.out.println("File cannot be downloaded" + ioe);
			return false;
		} 
		
		return true;
	}
	
	public static String getParent(String srcFolderURL)
	{
		int lastSlashPos = srcFolderURL.lastIndexOf("/");
		int secLastSlashPos = srcFolderURL.lastIndexOf("/", lastSlashPos-1);
		return srcFolderURL.substring(0, secLastSlashPos+1);	
	}
	
	public static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}
