#FROM webdevops/php-apache:centos-7
#FROM webdevops/php-apache-dev:centos-7
FROM centos:8
RUN mkdir /moodledata
RUN yum -y install epel-release
#RUN yum -y install http://rpms.remirepo.net/enterprise/remi-release-8.rpm
RUN yum -y install php-mbstring mariadb git php-mysqlnd php php-pdo php-soap php-json php-cli php-xml php-xmlrpc php-opcache php-intl php-common php-fpm php-gd httpd
RUN chown apache:apache /moodledata
RUN chown apache:apache /var/www/html
COPY httpd.sh /httpd.sh
COPY php.conf /etc/httpd/conf.d/php.conf
RUN chmod +x /httpd.sh
RUN sed -i 's/^User apache/User root/' /etc/httpd/conf/httpd.conf
RUN sed -i 's/^Group apache/Group root/' /etc/httpd/conf/httpd.conf
#USER apache
RUN git clone git://git.moodle.org/moodle.git /var/www/html
COPY config.php /var/www/html
#RUN cd /var/www/html &&  git checkout MOODLE_39_STABLE
#RUN cd /var/www/html &&  git checkout MOODLE_38_STABLE
#USER root
ENV DBHOST 172.31.13.13
ENV DBNAME moodle
ENV DBUSER moodle
ENV DBPASS c0nygre
ENV URI http://app.eu.conygre.com
VOLUME /moodledata
EXPOSE 80
EXPOSE 443
#ENTRYPOINT ["httpd","-D","foreground"]
ENTRYPOINT ["/httpd.sh"]
