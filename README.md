# WS-Port-Scanner
WS-Port-Scanner is a Webservice created by the Chair for Network and Data Security from the Ruhr-University Bochum for the integration of NMAP in the SIWECOS Project. The Webservice scans a provided URL for various open ports and potential misconfigurations and responds with a JSON report.

You can also run WS-Port-Scanner with Docker. You can build with:
```
docker build . -t port-scanner
```
You can then run it with:
```
docker run -it --network host port-scanner
```
The webservice is then reachable under:
```
http://127.0.0.1:8080/start
```

# Running
The webservice should be up and running and can be called by sending a POST like

```
{
  "url": "google.de",
  "dangerLevel": 0,
  "callbackurls": [
    "http://127.0.0.1:8080"
  ]
}
```
to
```
http://127.0.0.1:8080/WS-Port-Scanner-1.0/start
```

or 

```
http://127.0.0.1:8080/start
```
Depending on your application server.

# Results
TLS-Scanner uses the concept of "checks" which are performed after it collected configuration information. A check which results in "true" is consideres a non optimal choice and is an indicator for a pentester for a possible problem.

An example output may look like this:
```json

{
  "name" : "PORT",
  "hasError" : false,
  "errorMessage" : null,
  "score" : 0,
  "tests" : [ {
    "name" : "IRC",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "success",
    "testDetails" : null
  }, {
    "name" : "MSSQL",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "success",
    "testDetails" : null
  }, {
    "name" : "MYSQL",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 0,
    "scoreType" : "warning",
    "testDetails" : null
  }, {
    "name" : "RDP",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "success",
    "testDetails" : null
  }, {
    "name" : "SMB",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "success",
    "testDetails" : null
  }, {
    "name" : "TELNET",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "hidden",
    "testDetails" : null
  }, {
    "name" : "VNC",
    "hasError" : false,
    "errorMessage" : null,
    "score" : 100,
    "scoreType" : "hidden",
    "testDetails" : null
  }]
}
```


| Check                               | Meaning                                    | 
| ----------------------------------- |:------------------------------------------:|
| TELNET			                  | Checks if the port for Telnet is open      |
| IRC				                  | Checks if the port for IRC is open         |
| RDP				                  | Checks if the port for RDP is open         |
| MSSQL						          | Checks if the port for MS-SQL is open      |
| MYSQL							      | Checks if the port for MY-SQL is open      |
| SMB							      | Checks if the port for SMB is open         |
| VNC							      | Checks if the port for VNC is open         |

