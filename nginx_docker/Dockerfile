# A basic apache server with PHP. To use either add or bind mount content under /var/www

FROM ubuntu

MAINTAINER ftm

RUN apt-get update && apt-get install -y nginx && apt-get clean && rm -rf /var/lib/apt/lists/*
RUN rm /etc/nginx/sites-enabled/default

EXPOSE 80

