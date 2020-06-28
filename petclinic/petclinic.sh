#!/bin/bash

echo "DBSERVERNAME=$DBSERVERNAME"
echo "DBUSERNAME=$DBUSERNAME"
echo "DBPASSWORD=$DBPASSWORD"

cd /app
mkdir config

sed -e "s/DBSERVERNAME/$DBSERVERNAME/" \
    -e "s/DBUSERNAME/$DBUSERNAME/" \
    -e "s/DBPASSWORD/$DBPASSWORD/" application.properties.tmplt >config/application.properties

cat config/application.properties

# Check DB connection is up

while ! mysql -u$DBUSERNAME -p$DBPASSWORD -h$DBSERVERNAME -e 'show databases;'
do
	sleep 5
done

java -jar ./petclinic.jar

while :
do
	sleep 30
done
