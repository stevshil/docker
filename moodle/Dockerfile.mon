FROM bitnami/moodle:3.9.2
ENV MOODLE_USERNAME=admin
ENV MOODLE_PASSWORD=admin123
ENV MOODLE_DATABASE_USER=root
ENV MOODLE_DATABASE_PASSWORD=petclinic
ENV MOODLE_DATABASE_NAME=moodle
ENV MOODLE_DATABASE_HOST=database
ENV MYSQL_CLIENT_DATABASE_HOST=database
ENV MYSQL_CLIENT_DATABASE_ROOT_PASSWORD=petclinic
ENV BITNAMI_DEBUG=false

RUN apt-get -y update
RUN apt-get -y install wget

# Install monitoring
RUN wget https://github.com/ncabatoff/process-exporter/releases/download/v0.7.5/process-exporter-0.7.5.linux-amd64.tar.gz -O /tmp/process-exporter-0.7.5.linux-amd64.tar.gz
RUN mkdir /opt/exporters
RUN cd /opt/exporters;
RUN tar xvf /tmp/process-exporter-0.7.5.linux-amd64.tar.gz
RUN mv process-exporter-0.7.5.linux-amd64/process-exporter /opt/exporters/process-exporter
RUN rm -rf process-exporter-0.7.5.linux-amd64
RUN rm -f process-exporter-0.7.5.linux-amd64.tar.gz
COPY process-exporter.conf /opt/exporters
RUN wget https://github.com/prometheus/node_exporter/releases/download/v1.2.0/node_exporter-1.2.0.linux-amd64.tar.gz -O /tmp/node_exporter-1.2.0.linux-amd64.tar.gz
RUN cd /opt/exporters; tar xvf /tmp/node_exporter-1.2.0.linux-amd64.tar.gz; mv node*/node_exporter /opt/exporters/node_exporter
RUN rm -rf /tmp/node_exporter-1.2.0.linux-amd64.tar.gz /opt/exporters/node_exporter-*
RUN wget https://github.com/dundee/disk_usage_exporter/releases/download/v0.1.0/disk_usage_exporter_linux_amd64.tgz -O /tmp/disk_usage_exporter_linux_amd64.tgz
RUN cd /opt/exporters; tar xvf /tmp/disk_usage_exporter_linux_amd64.tgz; mv disk* disk_usage_exporter
COPY disk_usage_exporter.yaml /opt/exporters/disk_usage_exporter.yaml

# Update entrypoint
RUN sed -i '/exec "$@"/i\cd \/opt\/exporters' /opt/bitnami/scripts/moodle/entrypoint.sh
RUN sed -i '/exec "$@"/i\.\/disk_usage_exporter &' /opt/bitnami/scripts/moodle/entrypoint.sh
RUN sed -i '/exec "$@"/i\.\/node_exporter &' /opt/bitnami/scripts/moodle/entrypoint.sh
RUN sed -i '/exec "$@"/i\.\/process-exporter -config.path process-exporter.conf &' /opt/bitnami/scripts/moodle/entrypoint.sh
RUN sed -i '/exec "$@"/i\cd -' /opt/bitnami/scripts/moodle/entrypoint.sh
