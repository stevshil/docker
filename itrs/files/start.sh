#!/bin/bash

# Script to start ITRS Gateway and Netprobe for container

ldconfig /opt/itrs/geneos /opt/itrs/netprobe /usr/lib
export JAVA_HOME=/usr/lib/jvm/jre

cd /opt/itrs/netprobe
#export LOG_FILENAME=/opt/itrs/netprobe/netprobe.log
./netprobe.linux_64 -port 7036 -nopassword &
sleep 10
cd /opt/itrs/gateway
./gateway2.linux_64 -licence gateway2.lic.tmp #-log gateway2.log &

# while :
# do
#   sleep 60
# done
