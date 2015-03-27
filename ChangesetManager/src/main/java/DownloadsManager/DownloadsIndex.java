package DownloadsManager;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.stream.XMLEventFactory;
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLEventWriter;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Characters;
//import javax.xml.stream.events.EndElement;
//import javax.xml.stream.events.StartDocument;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;

public class DownloadsIndex {
	
	public static void addInIndex(String fileName, List<String> url, List<String> downloadDate, List<String> orgFilename, List<String> modFilename, int noOfRecords) {	  
		try {
			fileName = fileName + "index";
			FileOutputStream fsLastDownloadDateFile = new FileOutputStream(fileName,true);
			OutputStreamWriter osWriter = new OutputStreamWriter(fsLastDownloadDateFile);
			
			for (int i = 0; i < noOfRecords; i++)
			{
			String data = modFilename.get(i) + "," + url.get(i) + "," + downloadDate.get(i)+ "," + orgFilename.get(i) +  "\n"; 		
			osWriter.write(data);
			}
			osWriter.flush();
			osWriter.close();
			fsLastDownloadDateFile.close();
		} catch (Exception exp) {
			System.out.println("Downloads record cannot be saved" + exp);
		}
			
		}
	
	public static void deleteFromIndexUsingName(String fileLocation,  String filename) {
		try {
			  fileLocation = fileLocation + "index";
			  File file = new File(fileLocation);
			    File temp = File.createTempFile(file.getName(), null);
			    BufferedReader reader = null;
			    PrintStream writer = null;

			    try {
			        reader = new BufferedReader(new FileReader(file));
			        writer = new PrintStream(temp);

			        String line;
			        while ((line = reader.readLine())!=null) {
			            
			        	if (!line.contains(filename))
			        		writer.println(line);
			        }
			    }
			    finally {
			        if (writer!=null) writer.close();
			        if (reader!=null) reader.close();
			    }
			    file.delete();
			    temp.renameTo(file);		
		} catch (Exception exp) {
			System.out.println("Downloads record cannot be deleted" + exp);
		}
	}
//	
//	public static void createIndex(String fileLocation, List<String> url, List<String> downloadDate, List<String> orgFilename, List<String> modFilename, int noOfRecords) {	  
//		String rootElement = "File";
//		try{	     				
//			FileOutputStream file= new FileOutputStream(fileLocation, true);
//
//			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
//
//			XMLEventWriter xmlEventWriter;
//
//			xmlEventWriter = xmlOutputFactory.createXMLEventWriter(file, "UTF-8");
//			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//			XMLEvent end = eventFactory.createDTD("\n");
//
////			StartDocument startDocument = eventFactory.createStartDocument();
////			xmlEventWriter.add(startDocument);
////			xmlEventWriter.add(end);
//			
//			
//			StartElement configStartElement = eventFactory.createStartElement("", "", rootElement+"s");
//
//				xmlEventWriter.add(configStartElement);
//				
//
//			for(int i = 0; i < noOfRecords; i++)
//			{
//				Map<String,String> elementsMap = new HashMap<String, String>();
//				elementsMap.put("url", url.get(i));
//				elementsMap.put("downloadDate", downloadDate.get(i));
//				elementsMap.put("orgFilename", orgFilename.get(i));
//				elementsMap.put("modFilename", modFilename.get(i));
//
//				writeXML(xmlEventWriter,eventFactory, end, file, rootElement, elementsMap);
//				//   System.out.println("Done................................"+noOfRecords); 
//			}
//			EndElement configEndElement = eventFactory.createEndElement("", "", rootElement+"s");
//			xmlEventWriter.add(configEndElement);
////			xmlEventWriter.add(eventFactory.createEndDocument());
//			xmlEventWriter.close();
//
//		} catch (XMLStreamException xse) {
//			System.out.println("Index file cannot be saved" + xse);
//		}catch (FileNotFoundException fnfe) {
//			System.out.println("Index file cannot be saved" + fnfe);
//		}
//
//	}
//
//	public static void addInIndext(String fileLocation, List<String> url, List<String> downloadDate, List<String> orgFilename, List<String> modFilename, int noOfRecords) {	  
//		String rootElement = "File";
//		try{	     
//			FileOutputStream file= new FileOutputStream(fileLocation, true);
//
//			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
//
////			XMLEventWriter xmlEventWriter;
//
////			xmlEventWriter = xmlOutputFactory.createXMLEventWriter(file, "UTF-8");
////			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
////			XMLEvent end = eventFactory.createDTD("\n");
//
////			StartDocument startDocument = eventFactory.createStartDocument();
////			xmlEventWriter.add(startDocument);
////			xmlEventWriter.add(end);
//			
//			
//
//			
//	 		InputStream in = new FileInputStream(fileLocation);
////            OutputStream out = System.out;
//
//        XMLInputFactory factory = XMLInputFactory.newInstance();
//        XMLOutputFactory xof = XMLOutputFactory.newInstance();
//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//
//        XMLEventReader reader = factory.createXMLEventReader(fileLocation, in);
//        XMLEventWriter xmlEventWriter = xof.createXMLEventWriter(file, "UTF-8");
//        XMLEvent end = eventFactory.createDTD("\n");
//        
//		StartElement configStartElement = eventFactory.createStartElement("", "", rootElement+"s");
//
//		xmlEventWriter.add(configStartElement);
//        
//        while (reader.hasNext()) {
//
//            XMLEvent event = (XMLEvent) reader.next();
//            if (event.isStartElement()) 
//            {
//            	  StartElement s = event.asStartElement();
//                String tagName = s.getName().getLocalPart();
//                if (tagName.equals("Files")) {
////                	System.out.println("try");
//
////              event = ef.createStartElement(new QName(newName), null,
////                      null);
////              writer.add(event);
////              writer.add(ef.createCharacters("\n          "));
////              event = ef.createComment("auto generated comment");
////              writer.add(event);
//          } else {
//        	  xmlEventWriter.add(event);
//          }
////      } 
//
////      else {
////              writer.add(event);
////          }
//  }
////  writer.flush();
//} 	
//			
//
//
//				
//
//			for(int i = 0; i < noOfRecords; i++)
//			{
//				Map<String,String> elementsMap = new HashMap<String, String>();
//				elementsMap.put("url", url.get(i));
//				elementsMap.put("downloadDate", downloadDate.get(i));
//				elementsMap.put("orgFilename", orgFilename.get(i));
//				elementsMap.put("modFilename", modFilename.get(i));
//
//				writeXML(xmlEventWriter,eventFactory, end, file, rootElement, elementsMap);
//				//   System.out.println("Done................................"+noOfRecords); 
//			}
//			EndElement configEndElement = eventFactory.createEndElement("", "", rootElement+"s");
//			xmlEventWriter.add(configEndElement);
////			xmlEventWriter.add(eventFactory.createEndDocument());
//			xmlEventWriter.close();
//
//		} catch (XMLStreamException xse) {
//			System.out.println("Index file cannot be saved" + xse);
//		}catch (FileNotFoundException fnfe) {
//			System.out.println("Index file cannot be saved" + fnfe);
//		}
//
//	}
//
//	public static void writeXML(XMLEventWriter xmlEventWriter,XMLEventFactory eventFactory, XMLEvent end, FileOutputStream file, String rootElement, Map<String, String> elementsMap){//	        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
//		try {	            
//			StartElement configStartElement = eventFactory.createStartElement("", "", rootElement);
//
////        Attribute idAttr = 
////        		configStartElement..getAttributeByName(new QName("id"));
//
//			xmlEventWriter.add(configStartElement);
//			xmlEventWriter.add(end);
//			// Write the element nodes
//			Set<String> elementNodes = elementsMap.keySet();
//			for(String key : elementNodes){
//				createNode(xmlEventWriter, key, elementsMap.get(key));
//			}
//
//			xmlEventWriter.add(eventFactory.createEndElement("", "", rootElement));
//			xmlEventWriter.add(end);	 
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		}
//	}  
//
//
//	private static void createNode(XMLEventWriter eventWriter, String element,
//			String value) throws XMLStreamException {
//		XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
//		XMLEvent end = xmlEventFactory.createDTD("\n");
//		XMLEvent tab = xmlEventFactory.createDTD("\t");
//		//Create Start node
//		StartElement sElement = xmlEventFactory.createStartElement("", "", element);
//		eventWriter.add(tab);
//		eventWriter.add(sElement);
//		//Create Content
//		Characters characters = xmlEventFactory.createCharacters(value);
//		eventWriter.add(characters);
//		// Create End node
//		EndElement eElement = xmlEventFactory.createEndElement("", "", element);
//		eventWriter.add(eElement);
//		eventWriter.add(end);
//
//	}
//	 
	
//	
//	public static void createIndex(String fileLocation, List<String> url, List<String> downloadDate, List<String> orgFilename, List<String> modFilename, int noOfRecords) {
//		 
//		  try {
//
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			Document doc = docBuilder.newDocument();
//			Element rootElement = doc.createElement("Downloads");
//			doc.appendChild(rootElement);
//			
//			for(int i = 0; i < noOfRecords; i++)
//			{	 
//				Element file = doc.createElement("File");
//				rootElement.appendChild(file);
//				
//				Attr attr = doc.createAttribute("url");
//				attr.setValue(url.get(i));
//				file.setAttributeNode(attr);
//		 
//				Element date = doc.createElement("downloadDate");
//				date.appendChild(doc.createTextNode(downloadDate.get(i)));
//				file.appendChild(date);
//		 
//				Element ofname = doc.createElement("originalFileName");
//				ofname.appendChild(doc.createTextNode(orgFilename.get(i)));
//				file.appendChild(ofname);
//
//				Element mfname = doc.createElement("modifiedFileName");
//				mfname.appendChild(doc.createTextNode(modFilename.get(i)));
//				file.appendChild(mfname);	 
//			}
//
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(new File(fileLocation));
//	 
//			transformer.transform(source, result);
//	 
//			System.out.println("Index file saved succcessfully");
//
//		  } catch (ParserConfigurationException pce) {
//			  System.out.println("Index file cannot be saved" + pce);
//		  } catch (TransformerException te) {
//			  System.out.println("Index file cannot be saved" + te);
//		  }
//		}

//		public static void addInIndex(String fileLocation, List<String> url, List<String> downloadDate, List<String> orgFilename, List<String> modFilename, int noOfRecords) {
//		   try {
//				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			
//				Document doc = docBuilder.parse(fileLocation);
//		 
//				Element rootElement = doc.getDocumentElement();
//
//				for(int i = 0; i < noOfRecords; i++)
//				{	 
//					Element file = doc.createElement("File");
//					rootElement.appendChild(file);
//					
//					Attr attr = doc.createAttribute("url");
//					attr.setValue(url.get(i));
//					file.setAttributeNode(attr);
//			 
//					Element date = doc.createElement("downloadDate");
//					date.appendChild(doc.createTextNode(downloadDate.get(i)));
//					file.appendChild(date);
//					
//					Element ofname = doc.createElement("originalFileName");
//					ofname.appendChild(doc.createTextNode(orgFilename.get(i)));
//					file.appendChild(ofname);
//
//					Element mfname = doc.createElement("modifiedFileName");
//					mfname.appendChild(doc.createTextNode(modFilename.get(i)));
//					file.appendChild(mfname);	 
//				}
//				
//				TransformerFactory transformerFactory = TransformerFactory.newInstance();
//				Transformer transformer = transformerFactory.newTransformer();
//				DOMSource source = new DOMSource(doc);
//				StreamResult result = new StreamResult(new File(fileLocation));
//				transformer.transform(source, result);
//		 
//				System.out.println("Index file saved succcessfully");
//		 
//			   } catch (ParserConfigurationException pce) {
//				   System.out.println("Index file cannot be saved" + pce);
//			   } catch (TransformerException te) {
//				   System.out.println("Index file cannot be saved" + te);
//			   } catch (IOException ioe) {
//				   System.out.println("Index file cannot be saved" + ioe);
//			   } catch (SAXException se) {
//			   System.out.println("Index file cannot be saved" + se);
//			   }
//			
//	}	    
	    
//	public static void deleteFromIndexUsingName(String fileLocation,  String filename) {
//		boolean modified = false;
//		   try {
//				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//				Document doc = docBuilder.parse(fileLocation);				
//		
//			    NodeList nodes = doc.getElementsByTagName("modifiedFileName");
//
//			    for (int i = 0; i < nodes.getLength(); i++) {
//			      Element file = (Element)nodes.item(i);
//			      if(file.getTextContent().equals(filename))
//			      {System.out.println("found"+file.getParentNode().getParentNode().getNodeName());
//			    	file.getParentNode().getParentNode().removeChild(file.getParentNode());
//			    	  modified = true;
//			      }
//			    }			      
//		 
//				TransformerFactory transformerFactory = TransformerFactory.newInstance();
//				Transformer transformer = transformerFactory.newTransformer();
//				DOMSource source = new DOMSource(doc);
//				StreamResult result = new StreamResult(new File(fileLocation));
//				transformer.transform(source, result);
//				
//				if (modified == true)
//					System.out.println("Index file modified succcessfully");
//				else
//					System.out.println("Specified url does not exists");
//		 
//			   } catch (ParserConfigurationException pce) {
//				   System.out.println("Index file cannot be modified" + pce);
//			   } catch (TransformerException te) {
//				   System.out.println("Index file cannot be modified" + te);
//			   } catch (IOException ioe) {
//				   System.out.println("Index file cannot be modified" + ioe);
//			   } catch (SAXException se) {
//			   System.out.println("Index file cannot be modified" + se);
//			   }
//			
//	}
//	public static void deleteFromIndexUsingURL(String fileLocation,  String fileurl) {
//		boolean modified = false;
//		   try {
//				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//				Document doc = docBuilder.parse(fileLocation);				
//		
//			    NodeList nodes = doc.getElementsByTagName("File");
//
//			    for (int i = 0; i < nodes.getLength(); i++) {
//			      Element file = (Element)nodes.item(i);
//			      if(file.getAttribute("url").equals(fileurl))
//			      {
//			    	  file.getParentNode().removeChild(file);
//			    	  modified = true;
//			      }
//			    }			      
//		 
//				TransformerFactory transformerFactory = TransformerFactory.newInstance();
//				Transformer transformer = transformerFactory.newTransformer();
//				DOMSource source = new DOMSource(doc);
//				StreamResult result = new StreamResult(new File(fileLocation));
//				transformer.transform(source, result);
//				
//				if (modified == true)
//					System.out.println("Index file modified succcessfully");
//				else
//					System.out.println("Specified url does not exists");
//		 
//			   } catch (ParserConfigurationException pce) {
//				   System.out.println("Index file cannot be modified" + pce);
//			   } catch (TransformerException te) {
//				   System.out.println("Index file cannot be modified" + te);
//			   } catch (IOException ioe) {
//				   System.out.println("Index file cannot be modified" + ioe);
//			   } catch (SAXException se) {
//			   System.out.println("Index file cannot be modified" + se);
//			   }
//			
//	}
	
}
