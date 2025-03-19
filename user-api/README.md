# MSA Product API 
MSA 강좌 중 예제 Product API 소스코드입니다.

# Getting Started
1. 로컬 컴퓨터에서 docker-compose 로 mysql 설치
```shell
docker-compose up --build
```

2. MySQL docker container 접속 후 샘플 데이터 insert
```shell
docker exec -it product-api-product-db-1 /bin/bash

# container
bash-5.1# mysql -u root -p
mysql> insert into products (name, price) values ("coffee coupon", 5000); 
```

3. Application 실행 및 API 접속 테스트 
```shell
$ open http://localhost:8082/api/products/1
```
# Swagger 
```shell
open http://localhost:8082/swagger-ui/index.html
```