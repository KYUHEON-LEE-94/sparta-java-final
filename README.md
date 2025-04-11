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
| `redis`        | In-memory 캐시 서버            | 6379        |
| `zookeeper`    | Kafka coordination              | 2181        |
| `kafka`        | 메시지 브로커                   | 9093        |
| `elasticsearch`| 로그 저장소                    | 9200        |
| `logstash`     | 로그 수집기                    | 5001, 9600  |
| `kibana`       | 로그 시각화 UI                 | 5601        |
| `prometheus`   | 메트릭 수집기                  | 9090        |
| `grafana`      | 메트릭 시각화 대시보드         | 3000        |
| `zipkin`       | 분산 추적 시스템               | 9411        |
| `eureka-server`  | 서비스 레지스트리 (Service Discovery) | 8761   |
| `gateway-service`| API Gateway (Spring Cloud Gateway + Eureka Client) | 8080 |

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
     ┌──────────────────────────────┼────────────────────────────────┐
     ↓                              ↓                                ↓
+----------------+        +----------------+                +----------------+
| product-api    |───────→|  product-db     |                |   Redis Cache  |
| (8082)         |        |  (MySQL 3306)   |                |     (6379)     |
+----------------+        +----------------+                +----------------+
       ↑
       │ Kafka 이벤트 발행
       ↓
+----------------+        +----------------+
|  order-api     |───────→|  order-db       |
|  (8083)        |        |  (MySQL 3307)   |
+----------------+        +----------------+
       ↑
       ↓
+----------------+        +----------------+
|  user-api      |───────→|   user-db       |
|  (8084)        |        |   (MySQL 3308)  |
+----------------+        +----------------+

   ┌────────────── 로그 수집 ───────────────┐
   ↓                                       ↓
+--------+                          +----------------+
| Logstash|────→ ElasticSearch →──→|    Kibana      |
| (5001)  |                         |    (5601)      |
+--------+                          +----------------+

   +------------------------+
   |   gateway-service      | (8080)
   |  - Spring Cloud Gateway
   |  - Eureka Client       |
   +-----------+------------+
               |
               ↓
        [ Eureka Server ]
            (8761)

```

---

## 📈 메트릭 수집 및 모니터링 (Prometheus + Grafana)

각 서비스는 `Micrometer`와 `Prometheus`를 활용하여 **애플리케이션의 내부 상태와 비즈니스 메트릭을 수집**합니다.

### 💡 Micrometer 기본 메트릭

Spring Boot Actuator와 Micrometer를 통해 아래와 같은 기본 메트릭이 수집됩니다:

| 메트릭 이름           | 설명                             |
|------------------------|----------------------------------|
| `jvm.memory.used`      | JVM 힙/논힙 메모리 사용량         |
| `jvm.gc.pause`         | GC 시간 및 횟수                   |
| `http.server.requests` | HTTP 요청 수, 응답 시간           |
| `process.cpu.usage`    | 애플리케이션의 CPU 사용량         |
| `logback.events`       | 로그 레벨별 로그 수 (info, error 등) |

### 🛠 커스텀 비즈니스 메트릭 예시

각 서비스에는 도메인 특화 메트릭도 정의되어 있습니다:

#### order-api

| 메트릭 이름                  | 설명                                |
|------------------------------|-------------------------------------|
| `order_created_total`        | 주문 생성 성공 횟수                 |
| `order_failed_total`         | 주문 생성 실패 횟수                 |
| `order_processing_time`      | 주문 생성/조회 소요 시간 (타이머)   |
| `order_get_by_id_success`    | 주문 조회 성공 카운터               |
| `order_get_by_id_failure`    | 주문 조회 실패 카운터               |
| `order_get_by_id_duration`   | 주문 조회 처리 시간 타이머          |

#### product-api

| 메트릭 이름                        | 설명                                |
|-------------------------------|-------------------------------------|
| `product_created_total`       | 상품 생성 성공 횟수               |
| `product_create_failed_total` | 상품 생성 실패 횟수              |
| `product_create_duration`     | 상품 생성 처리 시간  |
| `product_view_total`          | 단일 상품 조회 수              |
| `product_list_total`          | 전체 상품 목록 호출 수             |
| `product_stock_decreased_total`    | 재고 감소 성공 횟수         |
| `product_stock_failed_total`    | 재고 부족으로 실패한 횟수         |

#### AOP 기반 메트릭 수집

서비스 메서드에 `@TimedMetric("이름")` 어노테이션을 붙이면 자동으로 아래 메트릭이 수집됩니다:

- `${이름}_success` : 메서드 성공 횟수
- `${이름}_failure` : 메서드 실패 횟수
- `${이름}_duration` : 메서드 실행 시간

### 📊 Grafana 대시보드

Grafana에서는 다음과 같은 대시보드를 구성할 수 있습니다:

- 전체 주문 성공/실패 추이
- 서비스별 HTTP 응답 시간 분포
- Kafka 전송 성공률
- 컨테이너 리소스 사용량 (CPU, Memory)
- JVM GC 동작 현황

### 🔌 Prometheus 엔드포인트

각 서비스는 `/actuator/prometheus` 경로를 통해 Prometheus가 메트릭을 수집할 수 있도록 노출합니다:

- `http://product-api:8082/actuator/prometheus`
- `http://order-api:8083/actuator/prometheus`
- `http://user-api:8084/actuator/prometheus`

Prometheus는 `prometheus.yml`의 `scrape_configs` 설정을 통해 위 엔드포인트를 주기적으로 수집합니다.

### 📍 Grafana 기본 접속 정보

```text
URL: http://localhost:3000
ID / PW: admin / admin
```

