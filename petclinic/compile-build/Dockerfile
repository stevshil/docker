FROM maven:3.6.3-jdk-11 AS build
RUN apt-get -y update && apt-get -y install git
RUN git clone https://github.com/spring-projects/spring-petclinic.git /app
WORKDIR /app
RUN mvn -Dmaven.test.skip=true clean package

FROM openjdk:11 AS petclinic
RUN apt-get -y update
RUN apt-get -y install procps # mysql-client
RUN mkdir /app
COPY --from=build /app/target/spring-petclinic-*.jar /app/petclinic.jar
COPY application.properties.tmplt petclinic.sh /app/
RUN chmod 755 /app/petclinic.sh
EXPOSE 8080
ENV DBSERVERNAME=mysql
ENV DBUSERNAME=root
ENV DBPASSWORD=petclinic
ENTRYPOINT "/app/petclinic.sh"
