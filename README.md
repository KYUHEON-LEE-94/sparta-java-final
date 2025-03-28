# sparta-java-project-final
이 프로젝트는 `Product`, `Order`, `User` 도메인을 가진 **MSA 아키텍처**입니다.  
각 서비스는 개별 데이터베이스를 사용하고, Kafka를 통한 이벤트 기반 통신과  
ELK 스택 및 Prometheus + Grafana 기반 모니터링을 구성하고 있습니다.

---

## ⚙️ 서비스 구성

| 서비스 이름     | 설명                            | 포트        |
|----------------|----------------------------------|-------------|
| `product-api`  | 상품 API 서비스                 | 8082        |
| `order-api`    | 주문 API 서비스                 | 8083        |
| `user-api`     | 사용자 API 서비스               | 8084        |
| `product-db`   | 상품 DB (MySQL)                | 3306        |
| `order-db`     | 주문 DB (MySQL)                | 3307        |
| `user-db`      | 사용자 DB (MySQL)              | 3308        |
| `zookeeper`    | Kafka coordination              | 2181        |
| `kafka`        | 메시지 브로커                   | 9093        |
| `elasticsearch`| 로그 저장소                    | 9200        |
| `logstash`     | 로그 수집기                    | 5001, 9600  |
| `kibana`       | 로그 시각화 UI                 | 5601        |
| `prometheus`   | 메트릭 수집기                  | 9090        |
| `grafana`      | 메트릭 시각화 대시보드         | 3000        |

---

## 🔗 네트워크 구조

모든 컨테이너는 동일한 Docker 네트워크 (`elk`) 상에 연결되어 있어  
컨테이너 이름으로 서로 통신할 수 있습니다.

## 아키텍처
```text
                           +----------------+
                           |    Grafana     | (3000)
                           | (Dashboard UI) |
                           +--------+-------+
                                    |
                                    ↓
                           +--------+-------+
                           |   Prometheus   | (9090)
                           | (Metrics 수집) |
                           +--------+-------+
                                    |
       ┌────────────────────────────┼
       ↓                            ↓                            
+-------------+            +----------------+           
| product-api |───→ DB ───→|  product-db     |          
|   (8082)    |            |  (MySQL 3306)   |           
+-------------+            +----------------+           
       ↑
       │    (Kafka 이벤트 발행)
       ↓
+-------------+             +----------------+
|  order-api  |───→ DB ───→ | order-db       |
|   (8083)    |             | (MySQL 3306)   |
+-------------+             +----------------+
       ↑
       │
       ↓
+-------------+             +----------------+
|  user-api   |───→ DB ───→ |   user-db      |
|   (8084)    |             +----------------+
+-------------+

[공통 로그 디렉토리로 로그 수집]
       ↓
   +--------+
   | Logstash | (9600 / 5001)
   +----+-----+
        |
        ↓
+----------------+
| Elasticsearch  | (9200)
+----------------+
        |
        ↓
+----------------+
|    Kibana      | (5601)
+----------------+

```

### Grafana
Prometheus를 데이터 소스로 연결하여 메트릭을 시각화합니다.

#### 기본 접속 정보:

```text
URL: http://localhost:3000

ID / PW: admin / admin
```


JVM, Spring Boot, Kafka 등 다양한 템플릿 대시보드를 가져와 사용할 수 있습니다.

### 📝 로그 수집 흐름 (ELK)
- 각 API 서비스는 컨테이너 내부 로그를 ./shared-logs/<서비스> 디렉토리로 남깁니다.
- Logstash가 해당 로그를 수집해 Elasticsearch로 전송합니다.
- Kibana를 통해 수집된 로그를 시각화하거나 필터링할 수 있습니다.

###  실행 방법
```shell
docker-compose up
```
실행 후 다음 경로에서 모니터링 도구를 사용할 수 있습니다:

- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000
- Kibana: http://localhost:5601