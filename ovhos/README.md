# Version 2.0

## Ubuntu 20.04 Web Server with ppa:ondrej/php

The version 2.0 image uses ppa:ondrej/php repository to install 7.1 on Ubuntu 20.04 server.

Apache is httpd service and exposes volume /var/www/html.

### Run

```
docker run -itd --restart=always -v "$PWD"=/var/www/html -P --name=webserver steve353/ovh:2.0
```

You can map port 80 and 443.

# Version 1.0

## CentOS Web Server with PHP71 REMI

This is a configuration to test updates to the OS and packages for a web server.

Currently running CentOS 7.7.1908 with REMI PHP 7.1.

Apache is httpd service and exposes volume /var/www/html.

### Run

```
docker run -itd --restart=always -v "$PWD"=/var/www/html -e WEB_DOCUMENT_INDEX=index.html -e PHP_DISPLAY_ERRORS=on -P --name=webserver steve353/ovh:1.0
```

You can map port 80 and 443.
