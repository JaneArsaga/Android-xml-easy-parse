package ru.trur.XMLParseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

public class XmlParseObject 
{
	final public static String ELEMENT_CONTENT = "ELEMENT_CONTENT";
	
	protected boolean mParseAttributes = false;
	protected boolean mParseContent = false;
	
	protected String mInMapName = "";
	protected String mXpath = "";
	protected String[] mXpathComponents;
	
	protected List<Map<String, String>> mElementsAttributes;
	protected List<Map<String, String>> mElementsContent;
	
	public XmlParseObject(String inMapName, String XPath) 
	{
		this.mInMapName = inMapName;
		this.mXpath = XPath;
		this.mXpathComponents = mXpath.split("#//#");	
		
	}
	public String getName() { return mInMapName; }
	public void setParseAttributes() { mParseAttributes = true; this.mElementsAttributes = new ArrayList<Map<String,String>>(); }
	public void setParseContent() { mParseContent = true; this.mElementsContent = new ArrayList<Map<String,String>>(); }
	
	protected Element findElementWithXPath(Element parent, int nextComponentNum) 
	{
		String nextComponentName = "";
		do {
			nextComponentName = mXpathComponents[nextComponentNum++];
		} while (nextComponentName.equals(""));
		if (nextComponentNum == mXpathComponents.length) return parent.getChild(nextComponentName);
		return findElementWithXPath(parent.getChild(nextComponentName), nextComponentNum);
	}
	
	protected void pushNewAttributeSet(Map<String, String> attrSet) 
	{
		Map<String, String> attributeSet = new HashMap<String, String>(attrSet);
		this.mElementsAttributes.add(attributeSet);
	}
	protected void pushNewContent(String content) 
	{
		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put(ELEMENT_CONTENT, content);
		this.mElementsContent.add(contentMap);
	}
	
	public void configurateListenersForRoot(RootElement xmlRoot) throws Exception
	{
		if (this.mParseAttributes && this.mParseContent) 
		{
			throw new Exception("Only one kind of parsing can be done with XmlParseObject");
		}
		final Map<String, String> currentElementAttributes = new HashMap<String, String>();
		Element thisElement = findElementWithXPath(xmlRoot, 0);
		if (this.mParseAttributes) {
			thisElement.setStartElementListener(new StartElementListener() {
				@Override
				public void start(Attributes attributes) {
					currentElementAttributes.clear();
					int len = attributes.getLength();
					for (int i = 0; i < len; i++) 
					{
						currentElementAttributes.put(attributes.getLocalName(i), attributes.getValue(i));
					}
					pushNewAttributeSet(currentElementAttributes);
				}
			});
		}
		else if (this.mParseContent) {
			thisElement.setEndTextElementListener(new EndTextElementListener() {
				@Override
				public void end(String body) {
					pushNewContent(body);
				}
			});
		}
	}
	
	public List<Map<String, String>> getResults() 
	{
		if (this.mParseAttributes) {
			return this.mElementsAttributes;
		}
		else if (this.mParseContent) {
			return this.mElementsContent;
		}
		return null;
	}
}
