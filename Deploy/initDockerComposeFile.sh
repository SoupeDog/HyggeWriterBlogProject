#!/usr/bin/env bash

DB_Pw=$1

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
            - "Elasticsearch_Single"
        environment:
            - "dbPw=${DB_Pw}"
        volumes:
            - "/home/nginx/root/static:/home/nginx/root/static"
            - "/home/log/:/logFile/"
        links:
            - "Mysql:mysql"
            - "Nginx:nginx"
            - "Elasticsearch_Single:elasticsearch"
        networks:
            Hygge:
                ipv4_address: 172.18.0.3
    Mysql:
        container_name: "Mysql"
        image: "mysql:5.7"
        restart: always
        environment:
          - "MYSQL_ROOT_PASSWORD: ${DB_Pw}"
        command: "--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci"
        ports:
            - "3306:3306"
        volumes:
            - "/home/mysql/data:/var/lib/mysql"
        networks:
            Hygge:
                ipv4_address: 172.18.0.4
    Elasticsearch_Single:
        image: docker.elastic.co/elasticsearch/elasticsearch:7.8.0
        container_name: "elasticsearch_single"
        environment:
          - "discovery.type=single-node"
          - "bootstrap.memory_lock=true"
          - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
        ulimits:
          memlock:
            soft: -1
            hard: -1
        volumes:
          - elasticsearch_volume:/usr/share/elasticsearch/data
        ports:
          - 9200:9200
        networks:
            Hygge:
                ipv4_address: 172.18.0.5

volumes:
  elasticsearch_volume:
    driver: local

networks:
    Hygge:
        external: true
        name: Hygge
END_TEXT