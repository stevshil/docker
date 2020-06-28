FROM steve353/php:7.0-apache-gd
MAINTAINER Steve Shilling steve@therapypages.com
COPY ./ /var/www/html
EXPOSE 8080
