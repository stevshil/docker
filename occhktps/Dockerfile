FROM php:7.0-apache
# Takes the default Docker images for PHP Apache and makes it work on Openshift
MAINTAINER Steve Shilling steve@therapypages.com
RUN apt-get update && apt-get install -y \
	sudo \
        libfreetype6-dev \
        libjpeg62-turbo-dev \
        libmcrypt-dev \
        libpng-dev \
    && docker-php-ext-install -j$(nproc) iconv mcrypt \
    && docker-php-ext-configure gd --with-freetype-dir=/usr/include/ --with-jpeg-dir=/usr/include/ \
    && docker-php-ext-install -j$(nproc) gd
RUN echo "www-data ALL=(root) NOPASSWD:ALL" >/etc/sudoers.d/apache && \
    chmod 0440 /etc/sudoers.d/apache
COPY 000-default.conf /etc/apache2/sites-enabled/000-default.conf
COPY ports.conf /etc/apache2/ports.conf
RUN echo "<H1>It works</h1>" >/var/www/html/index.php
RUN chown -R 1001 /var/www/html /var/run/apache2
RUN mkdir -p /var/run/apache2
RUN touch /var/run/apache2/apache2.pid
RUN chmod -R 777 /var/run/ /var/lock
#COPY index.php /var/www/html/
ADD PHPMailer /var/www/html/PHPMailer/
COPY runall chksite chksite.php sendmsg.php index.php /var/www/html/
EXPOSE 8080
USER 1001
ENV PATH="$PATH:."
ENV MAILPASS=""
ENTRYPOINT ["/var/www/html/runall"]
