FROM ubuntu:20.04
RUN apt-get -y update
RUN apt -y install software-properties-common
RUN add-apt-repository ppa:ondrej/php
RUN apt-get -y update
RUN apt-get -y install apache2
RUN apt-get -y install php7.1 php7.1-fpm php7.1-gd libapache2-mod-php7.1 certbot python3-certbot-apache
RUN a2enmod php7.1
RUN ln -s /etc/apache2/conf-available/php7.1-fpm.conf /etc/apache2/conf-enabled/php7.1-fpm.conf
EXPOSE 80
EXPOSE 443
VOLUME /var/www/html
ENV WEB_DOCUMENT_INDEX index.html
ENV PHP_DISPLAY_ERRORS on
ENTRYPOINT ["apachectl","-D","FOREGROUND"]
