#!/usr/bin/env bash

DB_PW=$1

cat > docker-compose.yml <<END_TEXT
version: "3.8"
services:
    Nginx:
        container_name: "MyNginx"
        image: "nginx"
        restart: always
        ports:
            - "80:80"
            - "443:443"
        volumes:
            - "/home/nginx/config:/etc/nginx"
            - "/home/nginx/root:/usr/share/nginx/html"
        networks:
            Hygge:
                ipv4_address: 172.18.0.2
    Service-Blog:
        container_name: "Service-Blog"
        build: "Service-Blog/."
        image: "service-blog"
        init: true
        depends_on:
            - "Mysql"
            - "Nginx"
        volumes:
            - "/home/nginx/root/static:/home/nginx/root/static"
            - "/home/log/:/logFile/"
        links:
            - "Mysql:mysql"
            - "Nginx:nginx"
        networks:
            Hygge:
                ipv4_address: 172.18.0.3
    Mysql:
        container_name: "Mysql"
        image: "mysql:5.7"
        restart: always
        environment:
            MYSQL_ROOT_PASSWORD: "${DB_PW}"
        ports:
            - "3306:3306"
        volumes:
            - "/home/mysql/data:/var/lib/mysql"
        networks:
            Hygge:
                ipv4_address: 172.18.0.4
networks:
    Hygge:
        external: true
        name: Hygge
END_TEXT