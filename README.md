# `product-service`

The goals of this project are:

- Create a simple REST API to manage users called `product-service`. The database used is [`MySQL`](https://www.mysql.com);
- Explore the utilities and annotations that Spring Boot provides when testing applications;
- Used is [`Guava`](https://github.com/google/guava) library, [`LoadingCache`](https://guava.dev/releases/19.0/api/docs/com/google/common/cache/LoadingCache.html) to reduce the latency of remote call;
- Used [`CompletableFuture`](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html) for the aggregating remote call result.

## Start environment

Open a terminal and inside `product-service` root folder run
```
docker-compose up -d
```

Wait a little bit until `MySQL` is `Up (healthy)`. You can check it by running
```
docker-compose ps
```

## Start application

First of all, we need to initialize `MySQL` database. For it, inside `product-service` root folder, run the
following script 
```
./init-db.sh
```

Inside `product-service` root folder, run the command to start the application
```
mvn spring-boot:run
```

The `product-service` endpoints can be access using Swagger website: http://localhost:9090/swagger-ui.html


```

## Shutdown

Run to command below to stop and remove containers, networks and volumes
```
docker-compose down -v
```

## Running Unit Testing

In a terminal and inside of `product-service` root folder, run the command below to run unit and integration
tests
```
mvn clean test
```

- From `product-service` inside of root folder, **Unit Testing Coverage Report** can be found at
```
product-service/target/site/jacoco/index.html
```

- From `product-service` inside of root folder, **Unit Testing Report** can be found at
```
product-service/target/surefire-reports
```

## References

- https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
- http://www.baeldung.com/spring-boot-testing
- https://github.com/ivangfr/springboot-testing-mysql
