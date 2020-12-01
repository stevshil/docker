FROM centos:centos7.7.1908
RUN yum -y install https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm http://rpms.remirepo.net/enterprise/remi-release-7.rpm
RUN yum -y install yum-utils
RUN yum-config-manager --enable remi-php71
#COPY yum.repos.d/remi-php*.repo /etc/yum.repos.d/
#COPY yum.repos.d/epel.repo /etc/yum.repos.d/
RUN yum -y install php71-php-fpm php71-php-common php71-php-mbstring php71-php-json php71-php-gd php71-php-xml php71-runtime php71-php php71-php-cli httpd httpd-tools mod_ssl authconfig
COPY httpd.sh /usr/sbin/
RUN chmod +x /usr/sbin/httpd.sh
EXPOSE 80
EXPOSE 443
VOLUME /var/www/html
ENV WEB_DOCUMENT_INDEX index.html
ENV PHP_DISPLAY_ERRORS on
ENTRYPOINT ["/usr/sbin/httpd.sh"]
