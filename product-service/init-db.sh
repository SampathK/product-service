#!/usr/bin/env bash
docker exec -i mysql mysql -u root --password=root123 userdb < src/main/resources/init-db.sql