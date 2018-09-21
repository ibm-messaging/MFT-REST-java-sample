/**
 * © Copyright IBM Corporation 2018, 2020
 * author: Gandhimathy Srinivasan 
 * mail: gsriniva@in.ibm.com, gandhi.srini@gmail.com
 */

package mftRestSample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

public class TransferStatus extends AbstractQuery {
    private static final List<String> attributesRequired = new ArrayList<>();
    private static final List<String> allAttributes = new ArrayList<>();
	
    public TransferStatus() {
		 super();
    }
	
    public TransferStatus(String hostName, String portNumber) {
		 super(hostName,portNumber);
    }

    public TransferStatus(String hostName, String portNumber,Boolean attributeRequired,String queryAttribute) {
		 super(hostName,portNumber,true,queryAttribute);
    }
	 
    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
         attributesRequired.add("id");
         attributesRequired.add("status");
	 attributesRequired.add("sourceAgent");
	 attributesRequired.add("destinationAgent"); 
  	 attributesRequired.add("originator");
  	 attributesRequired.add("statistics");
  	   
	 allAttributes.add("transferSet");
	 allAttributes.add("userProperties");
	 allAttributes.add("job");
	   
  	 switch (args.length) {
  	   case 0:
  	       new TransferStatus().getStatus();
  	       break;
	    case 2: 
		new TransferStatus(args[0],args[1]).getStatus();
		break; 
	    case 4: 
		if( (args[2].equals("true")) && (args[3].equals("all") ) ) {
		     attributesRequired.addAll(allAttributes);
	 	     new TransferStatus(args[0], args[1],true,"*").getStatus();  
                 }
		else if ( (args[2] == "false") || (args[2] == "default") )
		    new TransferStatus(args[0], args[1]).getStatus();
                else
		   printHelp();
                   break; 
	   default: 
	     printHelp();
	     break; 
	} //end of switch-case
     }//end of main

@Override
protected List<String> getBasicAttributeList() {
	return attributesRequired;	 	
    }

private static void printHelp() {
       System.out.println("TransferStatus <HostName/HostIP> <PortNumber> <queryAttribute> <attributesToQuery>");
       System.out.println("================================================================");
       System.out.println("Where -");
       System.out.println("");
       System.out.println("HostName: IP address or the Host name of the server where REST service is running.");
       System.out.println("          It is an optional parameter, but it should be provided with PortNumber");
       System.out.println("");                
       System.out.println("PortNumber: The port, at which the REST service is hosted in the Server.");
       System.out.println("          It is an optional parameter, but it should be provided with HostName");
       System.out.println("");
       System.out.println("queryAttribute: Valid values are true/false. ");
       System.out.println("                It is optional, but it should be provided with attributesToQuery");
       System.out.println("                should be provided as next argument");
       System.out.println("");
       System.out.println("attributsToQuery: Valid values are all/default ");
       System.out.println("                It is optional, but it should be provided with queryAttribute");
       System.out.println("================================================================");
       System.out.println("Examples -");
       System.out.println("");
       System.out.println("1.) To query the default attributes of transfer on a local system, which is configured to port 9080");
       System.out.println("TransferStatus");
       System.out.println("");
       System.out.println("2.) To query the default attributes of transfer on a different server");
       System.out.println("TransferStatus 9.30.10.74 5050" );
       System.out.println("");
       System.out.println("3.) To query the all attributes of transfer on a different server");
       System.out.println("TransferStatus 9.30.10.74 5050 true all" );
       System.out.println("");
       System.out.println("================================================================");
  } //end of printHelp	 
} //end of class
