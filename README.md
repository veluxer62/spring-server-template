# REST Server Template

Spring Boot와 Kotlin을 기반으로 한 REST API 서버 템플릿입니다.

## Stack

### Framework
- Spring Boot
- Kotlin
- Spring Data JPA
- Spring MVC

### Database
- PostgreSQL
- Flyway

### Documentation
- Swagger
- OpenAPI

### Test
- Kotest
- Mockk
- Testcontainers

## Getting Started

### Environment
- JDK 21
- PostgreSQL 13
- Docker Compose

### Runs
- Start Database
  ```bash
  docker-compose up -d
  ```
  
- Start Server
  ```bash
  ./gradlew bootRun
  ```

### Documentation

애플리케이션 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs

## Structures

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/example/template/
│   │       ├── application/
│   │       │   ├── configuration/     # 설정 클래스들
│   │       │   ├── facade/            # 파사드 레이어
│   │       │   └── presentation/      # 컨트롤러 및 웹 레이어
│   │       ├── domain/                # 도메인 모델 및 비즈니스 로직
│   │       └── Application.kt         # 메인 애플리케이션 클래스
│   └── resources/
│       ├── db/migration/              # Flyway 마이그레이션 파일들
│       └── application.yml            # 애플리케이션 설정
├── test/                              # 테스트 코드
│   └── kotlin/
│   │   └── com/example/template/
│   │       ├── FunctionalTestBase     # 기능 테스트 관련 베이스 클래스
│   │       ├── TestConfiguration      # 기능 테스트 관련 베이스 클래스
│   │       ├── FunctionalTestBase     # 기능 테스트 관련 베이스 클래스
│   │       ├── RepositoryTestBase     # Repository 테스트 관련 베이스 클래스
│   │       ├── UnitTestBase           # 단위 테스트 관련 베이스 클래스
│   │       ├── Functions              # 테스트 도움 함수 모음
│   │       └── TestHelper             # 기능 테스트 관련 도움 함수 모음
│   └── resources/
│           └── io/mockk/              # mockk 관련 설정
├── build.gradle.kts                   # 의존성 설정
└── docker-compose.yml                 # Docker compose 설정
```

## Other Templates

- [GraphQL Server Template](https://github.com/veluxer62/spring-server-template-with-graphql)
