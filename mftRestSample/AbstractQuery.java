package mftRestSample;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public abstract class AbstractQuery {
	
	/* Formats for URLs */
	private static final String ALL_BASIC_URL_PATTERN = "http://%s:%s/ibmmq/rest/v1/admin/mft/%s";

	
	protected String hostName; // Host where the API is exposed
	protected String port; // Port on which the API is exposed
	protected String serviceRequest;
	protected boolean attributeRequired=false; 
	protected String queryAttributes=null;
	
	/*protected Class<? extends TransferStatus> transferObject;*/
	
	protected String specificEntity;

/*Default Constructor */
 public AbstractQuery() {
		
		hostName = "localhost";
		port = "9080";
		attributeRequired = false;
		queryAttributes=null;
		getServiceName();
		printDetails();
	    
	    
} //end of default constractor
 /**
	 * Subclasses must implement this to identify the basic attributes expected
	 * in responses
	 * 
	 * @return an array of attribute names
	 */
	abstract protected List<String> getBasicAttributeList();
	
	
 /*Constructor different Host and port*/
	
 public AbstractQuery(String hostName, String portNumber) {
		
		this.hostName = hostName;
		this.port = portNumber;
		getServiceName();
		printDetails();
} //end of default constractor

 public AbstractQuery(String hostName, String portNumber,Boolean attributeRequired,String queryAttribute) {
		
		this.hostName = hostName;
		this.port = portNumber;
		getServiceName();
		if(attributeRequired)
		{
		   this.attributeRequired = attributeRequired;
		   this.queryAttributes=queryAttribute;
		}
		printDetails();
} //end of default constractor
 
 /**
	 * Actually performs the query
	 */
  protected void getStatus() throws MalformedURLException, IOException, JSONException {
	  
		String url = String.format(ALL_BASIC_URL_PATTERN, this.hostName, this.port, this.serviceRequest);
						
		if (this.attributeRequired) {
			url += "?attributes="+this.queryAttributes;
		}

		System.out.format("Opening URL '%s'%n", url);
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
		urlConnection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.name());

		if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			reportSuccess(urlConnection);
		} else {
			System.out.format("Retrieval failed with HTTP response code %d%n", urlConnection.getResponseCode());
			reportFailure(urlConnection);
		}
	}

  /**
	 * Report failures on the query
	 */
	private void reportFailure(HttpURLConnection urlConnection) throws IOException, JSONException {

		// get the error response string
		InputStream responseStream = urlConnection.getErrorStream();
		String responseString = null;
		try (Scanner scanner = new Scanner(responseStream)) {
			responseString = scanner.useDelimiter("\\A").next();
		}

		JSONObject fullResponse = new JSONObject(responseString);
		JSONArray allErrors = fullResponse.getJSONArray("error");
		if (allErrors != null) {
			int errorCount = allErrors.length(); // almost certainly 1!
			for (int i = 0; i < errorCount; i++) {
				System.out.println("REST API Reported error:");
				JSONObject currentError = allErrors.getJSONObject(i);
				for (String attribute : new String[] { "type", "msgId", "message", "explanation", "action" }) {
					String value = currentError.optString(attribute, null);
					if (value != null) {
						System.out.format("\t%-12.12s : %s%n", attribute, value);
					}
				}
			}
		}
	}
	
  /**
	 * Report results of the query
	 */
	private void reportSuccess(HttpURLConnection urlConnection) throws IOException, JSONException {

		// Get the response string
		InputStream responseStream = urlConnection.getInputStream();
		String responseString = null;
		try (Scanner scanner = new Scanner(responseStream)) {
			responseString = scanner.useDelimiter("\\A").next();
		}
		// Try to get it as a JSONObject
				JSONObject fullResponse = new JSONObject(responseString);

				// Results are be a JSONArray of JSONObjects named by entity type
				JSONArray allEntities = fullResponse.getJSONArray(this.serviceRequest);
				int entityCount = allEntities.length();
				for (int i = 0; i < entityCount; i++) {
					JSONObject currentEntity = allEntities.getJSONObject(i);
					// Dump out the basic attributes - listed by the subclass
					for (String attribute : getBasicAttributeList()) {
						String value = currentEntity.optString(attribute, null);
						if (value != null) {
							System.out.format("%-8.8s : %s%n", attribute, value);
						}
					}

					// If we have extended data, report that
					JSONObject extended = currentEntity.optJSONObject("extended");
					if (extended != null) {
						Iterator<String> allExtendedAttributes = extended.keys(); // Unordered!
						while (allExtendedAttributes.hasNext()) {
							String attribute = allExtendedAttributes.next();
							String value = extended.optString(attribute, null);
							if (value != null) {
								System.out.format("\t%-20.20s : %s%n", attribute, value);
								/* 20 is a little arbitrary! */
							}
						}
					}
					System.out.println();
				}
			}
 
 private void getServiceName()
 {
	 String strClassName=this.getClass().toString();
	 
	 switch (strClassName) { 
       case "class mftRestSample.AgentStatus": 
     	  serviceRequest = "agent"; 
       break; 
       case "class mftRestSample.TransferStatus": 
     	  serviceRequest = "transfer";
       break;  
       default: 
     	  serviceRequest = "agent"; 
     	  break; 
     } 
	this.serviceRequest=serviceRequest;
 }
 
 private void printDetails()
 {
	 System.out.println("================================================================");
	 System.out.println("hostName = "+this.hostName);
	 System.out.println("port = "+this.port);
	 System.out.println("serviceRequest = "+this.serviceRequest);
	 System.out.println("attributeRequired = "+this.attributeRequired);
	 System.out.println("queryAttributes = "+this.queryAttributes);
	 System.out.println("================================================================");
 }
} //end of Class