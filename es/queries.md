# 데이터 삽입
```shell
curl -X POST "http://localhost:9200/sales/_bulk" -H 'Content-Type: application/json' -d'
{"index":{}}
{"product": "Laptop", "category": "electronics", "price": 1500, "quantity": 2, "date": "2025-01-01"}
{"index":{}}
{"product": "Smartphone", "category": "electronics", "price": 800, "quantity": 5, "date": "2025-02-02"}
{"index":{}}
{"product": "Tablet", "category": "electronics", "price": 600, "quantity": 3, "date": "2025-03-03"}
{"index":{}}
{"product": "TV", "category": "electronics", "price": 1200, "quantity": 1, "date": "2025-04-04"}
{"index":{}}
{"product": "Headphones", "category": "accessories", "price": 200, "quantity": 4, "date": "2025-05-05"}
'
```

# 쿼리
## 데이터 조회
### 제품 검색
```shell
curl -X GET "http://localhost:9200/sales/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "product": "Laptop"
    }
  }
}'

```

### 카테고리 검색
```shell
curl -X GET "http://localhost:9200/sales/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "term": {
      "category.keyword": "electronics"
    }
  }
}'
```
### 가격이 1000 이상인 제품 검색
```shell
curl -X GET "http://localhost:9200/sales/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "range": {
      "price": { "gte": 1000 }
    }
  }
}'
```
### 카테고리 + 가격 
```shell
curl -X GET "http://localhost:9200/sales/_search" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "category.keyword": "electronics" } },
        { "range": { "price": { "gte": 1000 } } }
      ]
    }
  }
}'
```

## Aggregation
### 제품별 매출
```shell
curl -X GET "http://localhost:9200/sales/_search?size=0" -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "total_sales_per_product": {
      "terms": { "field": "product.keyword" },
      "aggs": {
        "total_sales": { "sum": { "field": "price" } }
      }
    }
  }
}'
```

### 월별 매출
```shell
curl -X GET "http://localhost:9200/sales/_search?size=0" -H 'Content-Type: application/json' -d'
{
  "aggs": {
    "monthly_sales": {
      "date_histogram": {
        "field": "date",
        "calendar_interval": "month"
      },
      "aggs": {
        "total_sales": { "sum": { "field": "price" } }
      }
    }
  }
}'
```