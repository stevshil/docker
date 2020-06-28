#!/bin/bash

docker run -itd -p 5000:5000 \
	--name dockerreg \
	-v /home/shared/dockerreg:/var/lib/registry \
	-v certs:/certs \
	registry:2
	#-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
  	#-e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
