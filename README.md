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

<Resource <br><\br>
  name="jdbc/db"<br><\br>
  auth="Container"<br><\br>
  type="java.sql.DataSource"<br><\br>
  username={db username}<br><\br>
  password={db password}<br><\br>
  driverClassName="com.mysql.jdbc.Driver"<br><\br>
  maxActive="100"<br><\br>
  maxIdle="30"<br><\br>
  maxWait="10000"<br><\br>
/>

This is to be used in the context.xml file of a tomcat server and the maxActive/Idle/Wait values can be lowered to reduce the amount of connection in the pool and how long the application waits before closing unneeded connections.

The database can be generated with the .sql files located in the GenerateDatabaseSQL folder.

