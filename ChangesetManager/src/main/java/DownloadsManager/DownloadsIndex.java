package DownloadsManager;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.w3c.dom.Attr;


public class DownloadsIndex {
	
	public static void addInIndex(String fileLocation, List<String> url, List<String> downloadDate, List<String> filename, int noOfRecords) {
		   try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(fileLocation);
		 
				Element rootElement = doc.getDocumentElement();

				for(int i = 0; i < noOfRecords; i++)
				{	 
					Element file = doc.createElement("File");
					rootElement.appendChild(file);
					
					Attr attr = doc.createAttribute("url");
					attr.setValue(url.get(i));
					file.setAttributeNode(attr);
			 
					Element date = doc.createElement("downloadDate");
					date.appendChild(doc.createTextNode(downloadDate.get(i)));
					file.appendChild(date);
			 
					Element fname = doc.createElement("fileName");
					fname.appendChild(doc.createTextNode(filename.get(i)));
					file.appendChild(fname);	 
				}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(fileLocation));
				transformer.transform(source, result);
		 
				System.out.println("Index file saved succcessfully");
		 
			   } catch (ParserConfigurationException pce) {
				   System.out.println("Index file cannot be saved" + pce);
			   } catch (TransformerException te) {
				   System.out.println("Index file cannot be saved" + te);
			   } catch (IOException ioe) {
				   System.out.println("Index file cannot be saved" + ioe);
			   } catch (SAXException se) {
			   System.out.println("Index file cannot be saved" + se);
			   }
			
	}
	public static void createIndex(String fileLocation, List<String> url, List<String> downloadDate, List<String> filename, int noOfRecords) {
		 
		  try {
	 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Downloads");
			doc.appendChild(rootElement);
			
			for(int i = 0; i < noOfRecords; i++)
			{	 
				Element file = doc.createElement("File");
				rootElement.appendChild(file);
				
				Attr attr = doc.createAttribute("url");
				attr.setValue(url.get(i));
				file.setAttributeNode(attr);
		 
				Element date = doc.createElement("downloadDate");
				date.appendChild(doc.createTextNode(downloadDate.get(i)));
				file.appendChild(date);
		 
				Element fname = doc.createElement("fileName");
				fname.appendChild(doc.createTextNode(filename.get(i)));
				file.appendChild(fname);	 
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileLocation));
	 
			transformer.transform(source, result);
	 
			System.out.println("Index file saved succcessfully");

		  } catch (ParserConfigurationException pce) {
			  System.out.println("Index file cannot be saved" + pce);
		  } catch (TransformerException te) {
			  System.out.println("Index file cannot be saved" + te);
		  }
		}
	
}
