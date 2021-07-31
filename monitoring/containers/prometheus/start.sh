#!/bin/bash

# Start node exporter
node_exporter &

# Start prometheus
prometheus --config.file=/etc/prometheus/prometheus.yml
