FROM webdevops/php-apache:7.1
EXPOSE 80
ENV WEB_DOCUMENT_ROOT /app
ENV WEB_DOCUMENT_INDEX index.php
COPY index.php /app/index.php
