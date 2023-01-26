FROM fedora:35 AS builder
RUN dnf -y install wget xz
RUN wget https://www.torproject.org/dist/torbrowser/12.0.2/tor-browser-linux64-12.0.2_ALL.tar.xz -O /tmp/tor.tar.xz
RUN xz -d /tmp/tor.tar.xz
WORKDIR /
RUN tar xf /tmp/tor.tar

FROM fedora:35
# RUN dnf -y install openssh-clients openssh zenity kdialog xmessage gxmessage
RUN dnf -y install openssh-clients openssh kdialog xmessage gxmessage
RUN dnf -y install https://download-ib01.fedoraproject.org/pub/fedora/linux/releases/36/Everything/x86_64/os/Packages/d/dbus-glib-0.112-3.fc36.x86_64.rpm
RUN useradd -m anon
COPY --from=builder /tor-browser /tor-browser
RUN chown -R anon:anon /tor-browser
USER anon
VOLUME /tmp/.X11-unix
VOLUME /home/anon
ENV DISPLAY=:1
CMD ["/tor-browser/Browser/start-tor-browser"]
