FROM almalinux:8.4
RUN dnf -y install curl wget
RUN wget https://packagecloud.io/install/repositories/prometheus-rpm/release/script.rpm.sh -O /tmp/prom.rpm.sh
RUN chmod +x /tmp/prom.rpm.sh
RUN os=el dist=8 /tmp/prom.rpm.sh
RUN dnf -y install node_exporter
RUN dnf -y install prometheus
COPY prometheus.yml /etc/prometheus/prometheus.yml
RUN mkdir /app
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh
# Node exporter
EXPOSE 9100
# Server HTTP
EXPOSE 9090
# Push Gateway HTTP
EXPOSE 9091
# Avoids collision with Kafka
EXPOSE 9092
# Alertmanager
EXPOSE 9093
 # Alertmanager clustering
EXPOSE 9094
# StatsD exporter
EXPOSE 9102
# Collectd exporter
EXPOSE 9103
ENTRYPOINT ["/app/start.sh"]
