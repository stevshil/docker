# Simple Frontend Backend Application

This application creates 2 Docker images;

* Database
  * Launched with a simple SQL script to build and populate the database
  * Located in the **db** directory
* API
  * Launched with a simple shell script that continually polls the database and displays the output to stdout every 5 seconds by default, or every DELAY seconds
  * If you wish to change the delay set the DELAY variable
    * e.g. ```docker run -it --name api -e DELAY=20 simplebefe/api:0.0.1```
