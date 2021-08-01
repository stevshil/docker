# FROM centos:centos8.3.2011
FROM centos:centos7.8.2003

# Copy mysql connector for ITRS to use with MySQL dbs
COPY files/ITRS/mysql-connector-c-shared-6.1.10-1.el7.x86_64.rpm /tmp
RUN yum -y install /tmp/mysql-connector-c-shared-6.1.10-1.el7.x86_64.rpm
RUN ln -s /usr/lib64/libmysqlclient.so /usr/lib64/libmysqlclient_r.so
#RUN ln -s /usr/lib64/libnsl.so.2 /usr/lib64/libnsl.so.1

# Add Java
RUN yum -y install java-1.8.0-openjdk
COPY files/ITRS/jdk-8u131-linux-x64.rpm /tmp
RUN yum -y install /tmp/jdk-8u131-linux-x64.rpm

# Install the Gateway, Netprobe and License Daemon
COPY files/ITRS/geneos-gateway-5.8.2-linux-x64.tar.gz /tmp
COPY files/ITRS/geneos-netprobe-5.8.2-linux-x64.tar.gz /tmp
COPY files/ITRS/geneos-licd-5.7.1-linux-x64.tar.gz /tmp
RUN mkdir -p /opt/itrs/gateway
RUN cd /opt/itrs; tar xvf /tmp/geneos-gateway-5.8.2-linux-x64.tar.gz
RUN mkdir -p /opt/itrs/netprobe
RUN cd /opt/itrs; tar xvf /tmp/geneos-netprobe-5.8.2-linux-x64.tar.gz
RUN mkdir -p /opt/itrs/
RUN cd /opt/itrs/; tar xvf /tmp/geneos-licd-5.7.1-linux-x64.tar.gz

# Set the libraries
COPY files/licd.conf /etc/ld.so.conf.d/licd.conf
COPY files/itrs.conf /etc/ld.so.conf.d/itrs.conf
COPY files/netprobe.conf /etc/ld.so.conf.d/netprobe.conf

# Copy the samples
#COPY files/ITRS/gateway.setup.xml /opt/itrs/gateway
COPY files/setup/ITRS/* /opt/itrs/gateway/

# Set up license
RUN mkdir /opt/itrs/gateway/licensedir
COPY files/ITRS/1fac2b17.lic /opt/itrs/licensedir/gateway.lic
RUN ln -s /opt/itrs/licensedir/gateway.lic /opt/itrs/licd/geneos.lic
# Remove tmp files
RUN rm /tmp/*.gz /tmp/*.rpm

# Start up
COPY files/start.sh /start.sh
RUN chmod +x /start.sh
VOLUME /opt/itrs/gateway/licensedir
EXPOSE 7038
EXPOSE 7036
ENTRYPOINT ["/start.sh"]
