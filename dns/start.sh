#!/bin/bash

# Check if we have /etc/mydns/named.conf
if [[ -e /etc/mydns/named.conf ]]
then
	mv -f /etc/named.conf /etc/named.conf.internal 
	ln -s /etc/mydns/named.conf /etc/named.conf
else
	if [[ -e /etc/named.conf.internal ]]
	then
		rm -f /etc/named.conf
		ln -sf /etc/named.conf.internal /etc/named.conf
	fi
fi

if [[ -e /etc/mydns/resolv.conf ]]
then
	cp /etc/resolv.conf /etc/resolv.conf.internal
	cat /etc/mydns/resolv.conf >/etc/resolv.conf
else
	if [[ -e /etc/named.conf.internal ]]
	then
		rm -f /etc/named.conf
		ln -sf /etc/named.conf.internal /etc/named.conf
	fi
fi

if [[ -d /var/mydns ]]
then
	mv -f /var/named/forward.data /var/named/forward.data.internal
	ln -sf /var/mydns/forward.data /var/named/forward.data

	mv -f /var/named/localhost.zone /var/named/localhost.zone.internal
	ln -sf /var/mydns/localhost.zone /var/named/localhost.zone

	mv -f /var/named/reverse.data /var/named/reverse.data.internal
	ln -sf /var/mydns/reverse.data /var/named/reverse.data
else
	if [[ -e /var/named/forward.data.internal ]]
	then
		rm -f /var/named/forward.data
		ln -sf /var/named/forward.data.internal /var/named/forward.data

		rm -f /var/named/localhost.zone
		ln -sf /var/named/localhost.zone.internal /var/named/localhost.zone

		rm -f /var/named/reverse.data
		ln -sf /var/named/reverse.data.internal /var/named/reverse.data
	fi
fi

/usr/sbin/named -f -c /etc/named.conf

while :
do
	sleep 360
done
