# 1. JDK 17을 기반 이미지로 사용
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY build/libs/*.jar app.jar

# 4. Spring Boot가 사용할 포트 열기
EXPOSE 8080

# 5. ENTRYPOINT를 사용하여 컨테이너 시작 시 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
