#!/bin/bash

cd apache_docker
docker build -t="apache_v1" .

cd ../nginx_docker
docker build -t="nginx_v1" .

echo "Everything is set up ! 🍺 "
