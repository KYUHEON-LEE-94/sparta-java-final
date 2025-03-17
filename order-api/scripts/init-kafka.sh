#!/bin/bash

# Kafka 컨테이너 이름 (docker-compose.yml에 정의된 이름과 일치해야 함)
KAFKA_CONTAINER="kafka"

TOPIC_NAME="order-created"

# 토픽 생성
echo "Creating Kafka topics..."
echo "🔹 Checking if Kafka topic '$TOPIC_NAME' exists..."
if docker exec -it $KAFKA_CONTAINER kafka-topics --describe --topic $TOPIC_NAME --bootstrap-server kafka:9092 >/dev/null 2>&1; then
  echo "✅ Topic '$TOPIC_NAME' already exists!"
else
  echo "🔹 Creating Kafka topic '$TOPIC_NAME'..."
  docker exec -it $KAFKA_CONTAINER kafka-topics  --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 3 --topic $TOPIC_NAME
  echo "✅ Successfully created topic '$TOPIC_NAME'!"
fi

# 토핑 생성 확인
echo "Successfully topic created"
docker exec -it kafka kafka-topics  --list --bootstrap-server kafka:9092

