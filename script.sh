#! /bin/bash

docker run -d user/apache_v1 /usr/sbin/apache2ctl -D FOREGROUND
