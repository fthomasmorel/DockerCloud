# A basic apache server with PHP. To use either add or bind mount content under /var/www

FROM kstaken/apache2

MAINTAINER ftm

RUN apt-get update && apt-get install -y php5 libapache2-mod-php5 php5-mysql php5-cli && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN rm /var/www/index.html

ADD index.php /var/www/

EXPOSE 80

CMD ["/usr/sbin/apache2", "-D", "FOREGROUND"]
