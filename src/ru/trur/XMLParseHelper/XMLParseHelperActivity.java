package ru.trur.XMLParseHelper;

import java.net.URL;

import android.app.Activity;
import android.os.Bundle;

public class XMLParseHelperActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
        	URL conn = new URL("http://triphunter.ru/tourpost/api/resorts");
        	XmlParserHelper helper = new XmlParserHelper(conn.openStream(), "TourML");
        	helper.createAttributesObject("resorts", "//references//resorts//resort");
        	helper.parse();
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }
}