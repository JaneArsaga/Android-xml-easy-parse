package ru.trur.XMLParseHelper;

import java.net.URL;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class XMLParseHelperActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
        	URL conn = new URL("http://habrahabr.ru/rss/hubs/");
        	XmlParserHelper helper = new XmlParserHelper(conn.openStream(), "rss");
        	
        	final String TITLES = "guid";
        	XmlParseObject object = helper.createObject(TITLES, "//channel//item//guid");
        	object.setParseAttributes();
        	object.setParseContent();
        	
			Map<String, List<XmlObject>> results = helper.parse();
        	List<XmlObject> titlesList = results.get(TITLES);
        	for (XmlObject title : titlesList) 
        	{
        		Log.d("Guid:", title.toString());
        	}
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
}