FROM ubuntu:20.04
RUN apt-get -y update
RUN apt -y install software-properties-common
RUN add-apt-repository ppa:ondrej/php
RUN apt-get -y update
RUN apt-get -y install apache2
RUN apt-get -y install php7.1 php7.1-fpm php7.1-gd libapache2-mod-php7.1 certbot python3-certbot-apache
RUN apt-get -y update
RUN apt-get -y install php7.1-curl curl php7.1-zip
RUN a2enmod php7.1
RUN ln -s /etc/apache2/conf-available/php7.1-fpm.conf /etc/apache2/conf-enabled/php7.1-fpm.conf
# Install GeoIP libraries Apache
RUN apt -y install php7.1-geoip
RUN apt -y install libapache2-mod-geoip
COPY geoip.conf /etc/apache2/mods-available/geoip.conf
RUN curl -sS https://getcomposer.org/installer | php
RUN mv composer.phar /bin/composer
RUN composer require geoip2/geoip2:~2.0
RUN sed -i 's/display_errors = Off/display_errors = On/' /etc/php/7.1/apache2/php.ini
COPY *.mmdb /usr/local/share/GeoIP/
EXPOSE 80
EXPOSE 443
VOLUME /var/www/html
ENV WEB_DOCUMENT_INDEX index.html
ENV PHP_DISPLAY_ERRORS on
ENTRYPOINT ["apachectl","-D","FOREGROUND"]
