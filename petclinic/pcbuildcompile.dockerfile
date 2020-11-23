FROM maven:3.6.3-jdk-11 AS compile
RUN git clone https://github.com/spring-projects/spring-petclinic.git /usr/src/mymaven
WORKDIR /usr/src/mymaven
RUN mvn -Dmaven.test.skip=true package
RUN ls /usr/src/mymaven/target

FROM openjdk:9
RUN mkdir /app
RUN apt-get -y update
RUN apt-get -y install netcat
COPY --from=compile /usr/src/mymaven/target/*.jar /app/petclinic.jar
COPY application.properties.tmplt /app/
COPY petclinic.sh /app/
RUN chmod +x /app/petclinic.sh
WORKDIR /app
ENTRYPOINT ["./petclinic.sh"]
