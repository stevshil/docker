FROM linuxmintd/mint19-amd64
# Takes the default Docker images for PHP Apache and makes it work on Openshift
MAINTAINER Steve Shilling steve@therapypages.com

RUN apt-get -y update

# Install apache
RUN apt-get -y install apache2 apache2-bin apache2-data apache2-utils

# Install Perl
RUN apt-get -y install perl libapache2-mod-perl2 libapache-authenhook-perl libapache-dbi-perl libapache-htpasswd-perl libapache-session-perl libapache2-authcookie-perl libcgi-pm-perl libclass-dbi-perl libdbd-mysql-perl libdbi-perl libwww-perl

# Copy the config
COPY mysites.conf /etc/apache2/sites-enabled/001-mysites.conf
COPY mime.conf /etc/apache2/mods-available/mime.conf
RUN rm /etc/apache2/sites-enabled/000-default.conf

# Set the configuration
RUN cd /etc/apache2/mods-enabled; ln -s ../mods-available/cgi* .
RUN mkdir -p /var/run/mysqld

ENV APACHE_RUN_USER=steve
ENV APACHE_RUN_GROUP=steve
EXPOSE 80 443
VOLUME /accounts
VOLUME /var/log/apache2
VOLUME /var/run/mysqld
CMD ["apache2ctl","-f","/etc/apache2/apache2.conf","-DFOREGROUND"]
#ENTRYPOINT ["/bin/waitforme"]
