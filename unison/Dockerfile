FROM fedora:35
RUN dnf -y install https://mirrors.rpmfusion.org/free/fedora/rpmfusion-free-release-35.noarch.rpm https://mirrors.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-35.noarch.rpm
RUN dnf -y install https://download-ib01.fedoraproject.org/pub/epel/7/x86_64/Packages/u/unison240-gtk-2.40.128-5.el7.x86_64.rpm https://download-ib01.fedoraproject.org/pub/epel/7/x86_64/Packages/u/unison240-2.40.128-5.el7.x86_64.rpm openssh-clients openssh
RUN groupadd -g 1000 steve
RUN useradd -m -u 1000 -g 1000 steve
COPY start.sh /start.sh
RUN chmod +x /start.sh
VOLUME /tmp/.X11-unix
VOLUME /home
USER steve
ENV DISPLAY=:1
CMD ["/start.sh"]
