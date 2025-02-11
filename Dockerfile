# 1. JDK 17을 기반 이미지로 사용
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (Spring Boot 빌드 후 생성된 JAR)
COPY build/libs/*.jar app.jar
 #🔥 로컬에서 빌드한 JAR 파일을 컨테이너로 복사

# 4. 컨테이너에서 실행될 명령어 지정
CMD ["java", "-jar", "app.jar"]

# 5. Spring Boot가 사용할 포트 열기
EXPOSE 8080