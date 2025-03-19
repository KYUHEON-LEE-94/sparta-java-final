# 1단계: 빌드
FROM openjdk:11-jdk-slim as builder
WORKDIR /app

# Gradle 빌드 캐시를 활용하기 위해 필요한 파일만 먼저 복사
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# Gradle Wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 먼저 다운로드 (빌드 캐시 활용)
RUN ./gradlew dependencies --no-daemon || true

# 전체 프로젝트 복사
COPY . .

# JAR 파일 빌드 (clean은 필요 없음)
RUN ./gradlew bootJar --no-daemon

# 2단계: 실행 환경 구성
FROM openjdk:11-jre-slim
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar user-api.jar

# 로그 디렉토리 생성 (권한 문제 방지)
RUN mkdir -p /app/logs && chmod -R 777 /app/logs

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8084

# 포트 노출
EXPOSE 8084

# 실행 명령
ENTRYPOINT ["java", "-jar", "user-api.jar"]
