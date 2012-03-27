package ru.trur.XMLParseHelper;

import java.util.HashMap;
import java.util.Map;

public class XmlObject {
	protected Map<String, String> mAttributes;
	protected String mContent;

	public XmlObject() {
		clear();
	}

	public XmlObject(Map<String, String> attributes, String content) {
		setAttributes(attributes);
		setContent(content);
	}

	public XmlObject copy() {
		return new XmlObject(this.mAttributes, mContent);
	}
	public XmlObject copy(XmlObject target) {
		this.mAttributes = target.mAttributes;
		this.mContent = target.mContent;
		return this;
	}
	public void clear() {
		this.mAttributes = new HashMap<String, String>();
		this.mContent = "";
	}

	public void setAttributes(Map<String, String> attributes) {
		this.mAttributes = new HashMap<String, String>(attributes);
	}

	public void setContent(String content) {
		this.mContent = new String(content);
	}

	public Map<String, String> getAttributes() {
		return this.mAttributes;
	}

	public String getContent() {
		return this.mContent;
	}
	
	@Override
	public String toString() {
		return mAttributes.toString() + ";" + mContent.toString();
	}
}
