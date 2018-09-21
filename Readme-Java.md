## About sample application.

The base class ```AbstractQuery``` can is extended by the REST query applications ```AgentStatus``` and 
```TransferStatus```  which quires agent and transfer status respectively.

The sample application can be extended to query default attributes or all attributes of agent and transfer.

The base class can be extended for any new MFT REST GET api, just by setting the values for the variables
```attributesRequired``` and ```allattributes``` it can be developed quickly.

## Options: 

1.) An attempt to execute with out any optional value will take the 'localhost' and the port '9080' and execute the command.

```AgentStatus```

```TransferStatus```

> **NOTE**: Depending on the class from which the api is invoked, the base class will pick the response response data template which is expected from the REST API will be picked.  

2.) An attempt to execute the REST service which is hosted on a different server.

```AgentStatus  hostName/hostIP  portNumber```

```TransferStatus  hostName/hostIP  portNumber```

> **NOTE**: The response will have only the default values provided by the corresponding REST api.

3.) An attempt to execute the REST service which is hosted on a different server and expected to have all the details in as response.

```AgentStatus  hostName/hostIP  portNumber  true all```

```TransferStatus  hostName/hostIP  portNumber  true all```

> **NOTE**: if false is given in the above command, then only default value will be included in the response irrespective of the next argument value.
