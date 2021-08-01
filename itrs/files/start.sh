#!/bin/bash

# Script to start ITRS Gateway and Netprobe for container
# ldconfig /opt/itrs/gateway /opt/itrs/netprobe /opt/itrs/licd /usr/lib
export JAVA_HOME=/usr/lib/jvm/jre

# Start the license daemon
cd /opt/itrs/licd
./licd.linux_64 &
# Current error - ./licd.linux_64: symbol lookup error: /lib64/libk5crypto.so.3: undefined symbol: EVP_KDF_ctrl, version OPENSSL_1_1_1b

cd /opt/itrs/netprobe
#export LOG_FILENAME=/opt/itrs/netprobe/netprobe.log
./netprobe.linux_64 -port 7036 -nopassword &
sleep 10
cd /opt/itrs/gateway
./gateway2.linux_64 #-log gateway2.log &

while :
do
  sleep 60
done
