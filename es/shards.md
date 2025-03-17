```shell
$ curl -X PUT "http://localhost:9200/my-index" -H "Content-Type: application/json" -d '
{
  "settings": {
    "index": {
      "number_of_shards": 3,
      "number_of_replicas": 1
    }
  }
}'
```
결과
```shell
{"acknowledged":true,"shards_acknowledged":true,"index":"my-index"}
```