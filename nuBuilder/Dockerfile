FROM webdevops/php-apache:7.1
RUN rm -rf /app
RUN git clone https://github.com/nuBuilder/nuBuilder-4.5.git /app
COPY nuconfig.php /app
RUN chown -R www-data /app
ENV DBUSER=root
ENV DBPASS=secret123
ENV DBHOSTNAME=nudb
ENV DBNAME=nubuilder4
ENV GLOBALADMINPW=nu123