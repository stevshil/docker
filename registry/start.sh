#!/bin/bash

cat >/var/tmp/dockerreg_config.yml <<_END_
version: 0.1
log:
  fields:
    service: registry
storage:
  cache:
    blobdescriptor: inmemory
  filesystem:
    rootdirectory: /var/lib/registry
  delete:
    enabled: true
http:
  addr: :5000
  headers:
    X-Content-Type-Options: [nosniff]
health:
  storagedriver:
    enabled: true
    interval: 10s
    threshold: 3
_END_

docker run -itd -p 5000:5000 \
	--name dockerreg \
	-m 40m \
	-v /home/share/dockerreg:/var/lib/registry \
	-v certs:/certs \
	--restart=always \
	registry:2
	#-v /var/tmp/dockerreg_config.yml:/etc/docker/registry/config.yml:ro \
	#-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
  	#-e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
