#!/bin/bash
curl -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' http://admin:admin123@localhost:3000/api/dashboards/db -d '{
  "dashboard": {
    "id": 12515,
    "uid": null,
    "title": "Node Exporter Dashboard",
    "tags": [ "templated" ],
    "timezone": "browser",
    "schemaVersion": 22,
    "version": 1,
    "refresh": "25s"
  },
  "folderId": 0,
  "message": "Loaded Exporter Dashboard",
  "overwrite": true
}'
