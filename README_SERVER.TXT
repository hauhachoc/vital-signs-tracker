This text document provides short information about the server code
of 'Vital Signs Tracker' application.


Note:
----------------
SERVER
----------------
* Server side uses Java socket programming to listen and respond to 
  client's requests.
* This multithread server can handle many requests from the clients
  simultaneously. Note: server performance has never been tested.
* The server codes consist of:
  - One connection manager class.
  - One constant static class that stores the constant variables for
    client server connection (ip add. and socket port number) and 
    request/respond protocols.
  - Main class that listen and respond to the incoming requests.
  - Sixteen classes that take care the sql queries. These sixteen classes
    extend one abstract class (AbstractSql.java).
  - One hashmap class that translate the request protocols.
* The server codes dynamically call the correct sql queries based on 
  the clients' requests. The class that takes care those requests is
  ClientService.java
* Communication between client and server uses JSON Object.


----------------
DATABASE
----------------
* Database uses PostgreSQL.
* Database scheme design can be found on:
  https://code.google.com/p/vital-signs-tracker/wiki/AppDesignDoc




