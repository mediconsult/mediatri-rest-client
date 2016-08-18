# Mediatri Rest Client

* Mediatri-rest-client is a simple and lightweight REST client library for Java.

* It uses standard jaxrs2 client api so you can use it your favorite application server.

* Goal of the project is to provide *simple* and clean expressions for interacting with Mediatri. This client has few testcases and they are integration tested with Mediatri build process. We are adding more tests and more API and we really should have test status indicator on this page :)

mediatri-rest-client depends on jaxrs2 api and json-simple-1.1.

## Roadmap
  - deploy artifact to maven-central
  - Change API to returned Java POJO's instead of JSONOBjects.  (Our used Java POJO's cannot be opensourced yet)
     - Use jackson2 for unmarshalling
  - Provide simple dummy server for developent. Returning fixed patients etc.
  - Implement more API and document API better.


## Quick Start Example
```java
MediatriRestClient client = new MediatriRestClient("https://mediatri-rest-accesspoint");
AuthenticationDto dto = client.authenticate("username", "password");
client.setRole(5,10);
client.getPatientAPI().getPatientList();
```
