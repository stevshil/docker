FROM fedora:37
RUN dnf -y install https://mirrors.rpmfusion.org/free/fedora/rpmfusion-free-release-37.noarch.rpm https://mirrors.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-37.noarch.rpm
COPY mongodb.repo /etc/yum.repos.d
RUN dnf -y install unifi java-11-openjdk-headless python3 mongodb-org-server
RUN dnf -y install unifi java-17-openjdk-headless python3 mongodb-org-server
#RUN dnf -y install unifi java-11-openjdk-headless mongodb-org-shell mongodb-org-server mongodb-org-mongos mongodb-mongosh mongodb-org
# RUN alternatives --install /usr/bin/java java /usr/lib/jvm/jre-11/bin/java 1
RUN alternatives --install /usr/bin/java java /usr/lib/jvm/jre-17/bin/java 1
RUN alternatives --set java /usr/lib/jvm/jre-17/bin/java
EXPOSE 8080
EXPOSE 8443
RUN ls /usr/lib/jvm
RUN java --version
VOLUME /var/lib/unifi/data
# ENV JAVA_HOME=/usr/lib/jvm/jre-11
ENV JAVA_HOME=/usr/lib/jvm/jre-17
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/unifi/lib/ace.jar", "start"]
# ENTRYPOINT ["/usr/lib/jvm/jre-1.8.0/bin/java", "-jar", "/usr/share/unifi/lib/ace.jar", "start"]
