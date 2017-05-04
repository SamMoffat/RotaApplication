# RotaApplication
RESTful web application written in Jersey. 

The project was written in Jersey to create a RESTful application with HATEOAS functionality.

Originally intended to have an oauth 2.0 authentication flow through the use of keycloak and google's Google+ API. Due to issues most 
likely caused by standard web browser's lack of support for CORS, I was unable to implement this in the timeframe. I have gone with basic 
authentication, despite it's lack of security. This has also affected the setup for my HATEOAS implementation. As a user's token and 
information would be held in the header of all GET/POST requests. This means that a lot of the intial HATEOAS hrefs lack arguments needed 
to make the requests.

MySQL was used to store the data from the database. Due to the use of connection pooling, the server that this application is ran on 
requires a resource reference to point to the location of the database, details of the database, and authorization for the database. This 
can be done like this:

<Resource
  name="jdbc/db"
  auth="Container"
  type="java.sql.DataSource"
  username={db username}
  password={db password}
  driverClassName="com.mysql.jdbc.Driver"
  maxActive="100" <!-- maxActive and maxIdle values can be altered for lower server load -->
  maxIdle="30"
  maxWait="10000"
/>

This is to be used in the context.xml file of a tomcat server.
the database can be generated with the .sql files located in the GenerateDatabaseSQL folder.

