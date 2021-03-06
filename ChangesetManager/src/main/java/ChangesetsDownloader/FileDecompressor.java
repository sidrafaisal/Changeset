package ChangesetsDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public class FileDecompressor {

	public static boolean decompressZipFile(String filename, String sequenceNo) {

		if (filename.contains(".gz")) {
			if (filename.endsWith(".tar.gz"))
				return decompressTarGzipFile(filename, sequenceNo);
			else
				return decompressGZipFile(filename);
		} else if (filename.contains(".bz2")) {
			if (filename.endsWith(".tar.bz2")) 
				return decompressTarBzipFile(filename, sequenceNo);
			else
				return decompressBZipFile(filename);
		} else
			return false;
	}

	public static boolean decompressBZipFile(String filename) {
		String outFilename = "";

		try {
			int lastDotPosition = filename.lastIndexOf(".");
			outFilename = filename.substring(0, lastDotPosition);

			FileInputStream inStream = new FileInputStream(filename);
			BZip2CompressorInputStream bzInStream = new BZip2CompressorInputStream(
					inStream);
			FileOutputStream outStream = new FileOutputStream(outFilename);

			final byte data[] = new byte[1024];
			int size = 0;

			while ((size = bzInStream.read(data)) != -1) {
				outStream.write(data, 0, size);
			}

			System.out.print("File decompressed successfully");

			bzInStream.close();
			outStream.close();
		} catch (IOException ioe) {
			System.out.print("File cannot be decompressed" + ioe);
			return false;
		} finally {
			(new File(filename)).delete();
		}
		return true;

	}

	public static boolean decompressGZipFile(String filename) {

		String outFilename = "";

		try {
			int lastDotPosition = filename.lastIndexOf(".");
			outFilename = filename.substring(0, lastDotPosition);

			FileInputStream inStream = new FileInputStream(filename);
			GZIPInputStream gInStream = new GZIPInputStream(inStream);
			FileOutputStream outStream = new FileOutputStream(outFilename);

			final byte data[] = new byte[1024];
			int size = 0;

			while ((size = gInStream.read(data)) != -1) {
				outStream.write(data, 0, size);
			}

			System.out.print("File decompressed successfully");

			gInStream.close();
			outStream.close();

		} catch (IOException ioe) {
			System.out.print("File cannot be decompressed" + ioe);
			return false;
		} finally {
			(new File(filename)).delete();
		}
		return true;
	}
	
	public static boolean decompressTarBzipFile(String filename, String sequenceNo) {

		try {						
			int lastDotPosition = filename.lastIndexOf(".");
			String outFilename = filename.substring(0, lastDotPosition);
			
			lastDotPosition = outFilename.lastIndexOf(".");
			outFilename = outFilename.substring(0, lastDotPosition);
			int initialSequenceNo = -1; 

			FileInputStream inStream = new FileInputStream(filename);
			BZip2CompressorInputStream bzInStream = new BZip2CompressorInputStream(inStream);
			
			TarArchiveInputStream tarIn = new TarArchiveInputStream(bzInStream);

			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				String newFilename = outFilename + "-" + initialSequenceNo;; // = outFilename + entry.getName();
				
				if (entry.isDirectory()) {
					initialSequenceNo = initialSequenceNo + 1;

					//File f = new File(newFilename);
					//f.mkdirs();
	
				} else {		
					if (entry.getName().endsWith(".nt")) {
						FileOutputStream outStream = new FileOutputStream(newFilename);
						final byte data[] = new byte[1024];
						int size = 0;
						while ((size = tarIn.read(data)) != -1) {
							outStream.write(data, 0, size);
						}					
						outStream.close();
						LGDDownloader.setTarFilename(outFilename, newFilename);
					}
				}
			}
			bzInStream.close();
			tarIn.close();
			System.out.println("File decompressed successfully");

		} catch (IOException ioe) {
			System.out.print("File cannot be decompressed" + ioe);
			return false;
		} finally {
				(new File(filename)).delete();
			}
		return true;
	}

	public static boolean decompressTarGzipFile(String filename, String sequenceNo) {
		try {
			int lastDotPosition = filename.lastIndexOf(".");
			String outFilename = filename.substring(0, lastDotPosition);
			
			lastDotPosition = outFilename.lastIndexOf(".");
			outFilename = outFilename.substring(0, lastDotPosition);
			int initialSequenceNo = -1; 
			
			FileInputStream inStream = new FileInputStream(filename);
			GzipCompressorInputStream gInStream = new GzipCompressorInputStream(inStream);
			TarArchiveInputStream tarIn = new TarArchiveInputStream(gInStream);

			TarArchiveEntry entry = null;

			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				String newFilename = outFilename + "-" + initialSequenceNo;; // = outFilename + entry.getName();

				if (entry.isDirectory()) {
					initialSequenceNo = initialSequenceNo + 1;

					//File f = new File(newFilename);
					//f.mkdirs();
				} else {
					
					if (filename.endsWith(".nt")) {
						FileOutputStream outStream = new FileOutputStream(newFilename);

						final byte data[] = new byte[1024];
						int size = 0;

						while ((size = tarIn.read(data)) != -1) {
							outStream.write(data, 0, size);
						}
						outStream.close();
						LGDDownloader.setTarFilename(outFilename, newFilename);
					}
				}
			}
			gInStream.close();
			tarIn.close();
			System.out.println("File decompressed successfully");

		} catch (IOException ioe) {
			System.out.print("File cannot be decompressed" + ioe);
			return false;
		} finally {
			(new File(filename)).delete();
		}
		return true;
	}

}