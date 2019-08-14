package net.etfbl.main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.lang.RuntimeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.logging.*;

public class Main {

	public static String BASE_URL = null;
	static final Logger LOGGER = Logger.getLogger("Logger");
    static FileHandler handler;
    
    static {
    	try {
    	handler=new FileHandler("error.log");
        LOGGER.addHandler(handler);
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		setErrorLog(e);
    	}
    }

	 public static void setErrorLog(Exception exception)
	    {
	        StackTraceElement elements[] = exception.getStackTrace();
	        for (StackTraceElement element:elements) 
	            LOGGER.log(Level.WARNING, element.toString());
	    }

}
