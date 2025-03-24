# 주문 생성
```shell
$ curl -X POST http://localhost:8083/order   -H "Content-Type: application/json"   -d '{
    "orderId": 0,
    "userId": "string",
    "productId": 0,
    "shoppingAddress": "string",
    "quantity": 0,
    "totalPrice": 0,
    "status": "PENDING",
    "createdAt": "2025-03-24T02:53:04.008Z"
}'
```

# 주문 조회
```shell
$ curl -X GET http://localhost:8083/order/4
```