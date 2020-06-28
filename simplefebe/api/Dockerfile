FROM centos:latest
ADD checkdb /checkdb
RUN chmod 755 /checkdb
RUN yum -y install mariadb
ENV DELAY=5
ENTRYPOINT /checkdb
