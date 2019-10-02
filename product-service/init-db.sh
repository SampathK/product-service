#!/usr/bin/env bash
docker exec -i mysql mysql -u root --password=root123 productdb < src/main/resources/init-db.sql