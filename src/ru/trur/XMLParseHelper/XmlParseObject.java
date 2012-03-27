package ru.trur.XMLParseHelper;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

public class XmlParseObject 
{
	protected boolean mParseAttributes = false;
	protected boolean mParseContent = false;
	
	protected String mInMapName = "";
	protected String mXpath = "";
	protected String[] mXpathComponents;
	
	protected List<XmlObject> mItems;
	
	public XmlParseObject(String inMapName, String XPath) 
	{
		this.mInMapName = inMapName;
		this.mXpath = XPath;
		this.mXpathComponents = mXpath.split("//");	
		this.mItems = new ArrayList<XmlObject>();
	}
	public String getName() { return mInMapName; }
	public void setParseAttributes() { mParseAttributes = true;  }
	public void setParseContent() { mParseContent = true; }
	
	protected Element findElementWithXPath(Element parent, int nextComponentNum) 
	{
		String nextComponentName = "";
		do {
			nextComponentName = mXpathComponents[nextComponentNum++];
		} while (nextComponentName.equals(""));
		if (nextComponentNum == mXpathComponents.length) return parent.getChild(nextComponentName);
		return findElementWithXPath(parent.getChild(nextComponentName), nextComponentNum);
	}
	
	protected void pushItemToPosition(XmlObject item, int position) 
	{
		if (mItems.size() == position) 
		{
			mItems.add(item.copy());
		}
		else 
		{
			mItems.get(position).copy(item);
		}
	}

	
	public void configurateListenersForRoot(RootElement xmlRoot) throws Exception
	{
		final XmlObject currentElement = new XmlObject();
		Element thisElement = findElementWithXPath(xmlRoot, 0);
		if (this.mParseAttributes) {
			thisElement.setStartElementListener(new StartElementListener() {
				int currentElementNum = 0;
				@Override
				public void start(Attributes attributes) {
					currentElement.clear();
					int len = attributes.getLength();
					for (int i = 0; i < len; i++) 
					{
						currentElement.getAttributes().put(attributes.getLocalName(i), attributes.getValue(i));
					}
					pushItemToPosition(currentElement, currentElementNum++);
				}
			});
		}
		if (this.mParseContent) {
			thisElement.setEndTextElementListener(new EndTextElementListener() {
				int currentElementNum = 0;
				@Override
				public void end(String body) {
					currentElement.setContent(body);
					pushItemToPosition(currentElement, currentElementNum++);
				}
			});
		}
	}
	
	public List<XmlObject> getResults() 
	{
		return this.mItems;
	}
}
