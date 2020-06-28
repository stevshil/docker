FROM openjdk:8
RUN apt-get -y update
RUN apt-get -y install procps mysql-client
RUN mkdir /app
COPY petclinic/target/spring-petclinic-2.0.0.jar /app/petclinic.jar
COPY application.properties.tmplt petclinic.sh /app/
RUN chmod 755 /app/petclinic.sh
EXPOSE 8080
ENV DBSERVERNAME=mysql
ENV DBUSERNAME=root
ENV DBPASSWORD=petclinic
ENTRYPOINT "/app/petclinic.sh"
