#!/bin/bash

docker run -itd -p 5000:5000 \
	--name dockerreg \
	-v /home/share/dockerreg:/var/lib/registry \
	-v certs:/certs \
	--restart=always \
	registry:2
	#-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
  	#-e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
