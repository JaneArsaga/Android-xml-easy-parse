package ru.trur.XMLParseHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.sax.RootElement;
import android.util.Xml;

public class XmlParserHelper 
{
	protected String mRootName = "";
	protected InputStream mXmlStream = null;
	protected List<XmlParseObject> mParseList = new ArrayList<XmlParseObject>();
	
	/**
	 * Initialize xml helper with file stream
	 * @param xmlStream input xml stream
	 */
	public XmlParserHelper(InputStream xmlStream, String rootName) 
	{
		this.mXmlStream = xmlStream;
		this.mRootName = rootName;
	}
	/**
	 * Point parse all attributes for XPath
	 * @param objectName key for attributes list in response
	 * @param XPath path to tag
	 */
	public void createAttributesObject(String objectName, String XPath) 
	{
		XmlParseObject currentObject = new XmlParseObject(objectName, XPath);
		currentObject.setParseAttributes();
		mParseList.add(currentObject);
	}
	public void createContentObject(String objectName, String XPath) 
	{
		XmlParseObject currentObject = new XmlParseObject(objectName, XPath);
		currentObject.setParseContent();
		mParseList.add(currentObject);
	}
	public XmlParseObject createObject(String objectName, String XPath) 
	{
		XmlParseObject currentObject = new XmlParseObject(objectName, XPath);
		mParseList.add(currentObject);
		return currentObject;
	}
	public Map<String, List<XmlObject>> parse() throws Exception 
	{
		if (mRootName.equals("")) 
		{
			throw new Exception("Root tag must be defined");
		}
		RootElement root = new RootElement(mRootName);
		for (XmlParseObject parselable : mParseList) 
		{
			parselable.configurateListenersForRoot(root);
		}
		try {
            Xml.parse(mXmlStream, Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
		Map<String, List<XmlObject>> result = new HashMap<String, List<XmlObject>>();
		for (XmlParseObject parselable : mParseList) 
		{
			result.put(parselable.getName(), parselable.getResults());
		}
		return result;
	}
}
