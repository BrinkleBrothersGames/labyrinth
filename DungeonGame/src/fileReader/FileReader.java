package fileReader;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileReader { // TODO - make static class?

	Document doc;

	public FileReader()  {
	}

	public FileReader(String filePath)  {
		this.doc = getDocument(filePath);
	}

	public FileReader(Document doc)  {
		this.doc = doc;
	}

	public Document getDocument(String localPath) {
		try {
			String dirPath = FileSystems.getDefault().getPath("src/").toAbsolutePath().toString();
			String path = dirPath + localPath;
			File fXmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(fXmlFile);
		}
		catch (Exception e) {
			return null;
		}
	}

	private Document makeDocument(Node node, DocumentBuilder db) {
		Document newDocument = db.newDocument();
		newDocument.importNode(node, true);
		return newDocument;
	}

	public HashMap<String, FileReader> getDocMap(String filePath) {
		HashMap<String, FileReader> docs = new HashMap<String, FileReader>();
		DocumentBuilder db = db();
		Document doc = getDocument(filePath);
		NodeList nodes = doc.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			docs.put(node.getNodeName(), new FileReader(makeDocument(node, db)));
		}
		return docs;
	}
	
	private DocumentBuilder db() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			return dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String getProperty(Document doc, String propertyName) {
		try {
			Node propertyNode = doc.getElementsByTagName(propertyName).item(0).getFirstChild();
			return propertyNode.getNodeValue();
		}
		catch (Exception e) {
			return null;
		} //TODO log?
	}
	
	public Boolean getBoolean(Document doc, String propertyName) {
		return Boolean.parseBoolean(getProperty(doc, propertyName));
	} //TODO write own function.

	public int getInt(Document doc, String propertyName) {
		return Integer.parseInt(getProperty(doc, propertyName));
	}
	
	
	public double getDouble(Document doc, String propertyName) {
		return Double.parseDouble(getProperty(doc, propertyName));
	}
	
	
	public char getChar(Document doc, String propertyName) {
		String s = getProperty(doc, propertyName);
		if ((s == null) || (s.length() != 1)) {
			return ' ';
		}
		return Character.valueOf(s.charAt(0));
	}
}