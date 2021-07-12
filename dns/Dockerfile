FROM almalinux:8.4
#FROM centos:7.6.1810

# Update containers packages
RUN dnf -y update

# Install named/bind
RUN dnf -y install bind bind-utils net-tools wget

RUN rm -rf /etc/named /etc/named.rfc1912.zones

RUN rm -f /etc/named.conf
COPY files/var_named/* /var/named/
COPY files/etc/resolv.conf /etc/resolv.conf
RUN chown -R root:root /var/named

VOLUME /etc/mydns
VOLUME /var/named
EXPOSE 53

COPY files/etc/named.conf /etc/named.conf
RUN chown -R root:root /etc/named.conf

#ENTRYPOINT "/usr/sbin/named -f -c /etc/named.conf"

# Below is for debugging
COPY start.sh /start.sh
RUN chmod +x /start.sh
ENTRYPOINT "/start.sh"
