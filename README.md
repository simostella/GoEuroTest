# GoEuroTest

This Project was built for test purposes and it implements an API Query to the GoEuro rest Service available on http://www.goeuro.com/GoEuroAPI/rest/api/v2/position/suggest/en/STRING
It takes STRING as a parameter to build a JSONArray response with founded locations. 
The user can call the service through a standalone jar built on this project, passing the following arguments
- args[0] MANDATORY - the query string
- args[1] the local particle
- args[2] the filename

If no locale is provided the default "en" is used;
It produces a csv file with the result provided by the service, in the filename specified at the third parameter; if not specified a filename is associated to query string and timestamp.
