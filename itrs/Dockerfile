FROM centos:centos8.3.2011

# Copy mysql connector for ITRS to use with MySQL dbs
COPY files/ITRS/mysql-connector-c-shared-6.1.10-1.el7.x86_64.rpm /tmp
RUN yum -y install /tmp/mysql-connector-c-shared-6.1.10-1.el7.x86_64.rpm
RUN ln -s /usr/lib64/libmysqlclient.so /usr/lib64/libmysqlclient_r.so
RUN ln -s /usr/lib64/libnsl.so.2 /usr/lib64/libnsl.so.1

# Add Java
RUN yum -y install java-1.8.0-openjdk
COPY files/ITRS/jdk-8u131-linux-x64.rpm /tmp
RUN yum -y install /tmp/jdk-8u131-linux-x64.rpm

# Install the Gateway and Netprobe
COPY files/ITRS/geneos-gateway-4.1.0.linux-x64.tar.gz /tmp
COPY files/ITRS/geneos-netprobe-4.1.0.linux-x64.tar.gz /tmp
RUN mkdir -p /opt/itrs/gateway
RUN cd /opt/itrs; tar xvf /tmp/geneos-gateway-4.1.0.linux-x64.tar.gz
RUN mkdir -p /opt/itrs/netprobe
RUN cd /opt/itrs; tar xvf /tmp/geneos-netprobe-4.1.0.linux-x64.tar.gz

# Copy the samples
#COPY files/ITRS/gateway.setup.xml /opt/itrs/gateway
COPY files/setup/ITRS/* /opt/itrs/gateway/

# Set up license
RUN mkdir /opt/itrs/gateway/licensedir
COPY files/ITRS/gateway2.lic.tmp /opt/itrs/gateway/licensedir/gateway2.lic.tmp
RUN ln -s /opt/itrs/gateway/licensedir/gateway2.lic.tmp /opt/itrs/gateway/gateway2.lic.tmp
# Remove tmp files
RUN rm /tmp/*.gz /tmp/*.rpm

# Start up
COPY files/start.sh /start.sh
RUN chmod +x /start.sh
VOLUME /opt/itrs/gateway/licensedir
EXPOSE 7038
EXPOSE 7036
ENTRYPOINT ["/start.sh"]
