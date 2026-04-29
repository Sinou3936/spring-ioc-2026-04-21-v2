# Spring IoC Container 직접 구현

Spring의 IoC(Inversion of Control) 컨테이너를 밑바닥부터 직접 구현해보는 실습 프로젝트입니다.

## 학습 목표

- Spring IoC/DI의 내부 동작 원리 이해
- 리플렉션(Reflection)을 활용한 클래스 탐색 및 인스턴스 생성
- 싱글톤 빈(Bean) 관리 방식 구현
- 생성자 주입(Constructor Injection) 구현

## 구현 내용

### 스테레오타입 어노테이션

| 어노테이션 | 역할 |
|---|---|
| `@Component` | 일반 컴포넌트 등록 |
| `@Configuration` | 설정 클래스 등록 |
| `@Repository` | 데이터 접근 계층 등록 |
| `@Service` | 비즈니스 로직 계층 등록 |

### ApplicationContext

핵심 IoC 컨테이너 클래스입니다.

```java
ApplicationContext ctx = new ApplicationContext("com.ll");
ctx.init();

MyService myService = ctx.genBean("myService");
```

**동작 흐름:**

1. `init()` — `Reflections` 라이브러리로 베이스 패키지를 스캔, 4종 어노테이션이 붙은 클래스를 수집
2. `genBean(beanName)` — 이름으로 빈 요청 시, 이미 생성된 빈이 있으면 재사용(싱글톤), 없으면 생성
3. `createInstance(clazz)` — 생성자의 파라미터 타입을 분석해 의존 빈을 재귀적으로 주입(생성자 주입)

## 테스트 케이스

| 테스트 | 검증 내용 |
|---|---|
| t1 | `ApplicationContext` 객체 생성 확인 |
| t2 | 이름으로 빈(`testPostService`) 획득 |
| t3 | 동일 이름으로 두 번 요청 시 같은 인스턴스 반환(싱글톤) |
| t4 | `@Repository` 빈(`testPostRepository`) 획득 |
| t5 | `TestPostService`가 `TestPostRepository`를 주입받았는지 확인 |
| t6 | `TestFacadePostService`가 `TestPostService`·`TestPostRepository` 둘 다 주입받았는지 확인 |

## 프로젝트 구조

```
src
├── main/java/com/ll
│   ├── framework/ioc
│   │   ├── ApplicationContext.java      # IoC 컨테이너 구현체
│   │   └── annotations
│   │       ├── Component.java
│   │       ├── Configuration.java
│   │       ├── Repository.java
│   │       └── Service.java
│   └── standard/util/Ut.java
└── test/java/com/ll
    ├── domain/testPost/testPost
    │   ├── repository/TestPostRepository.java
    │   └── service
    │       ├── TestPostService.java         # Repository 의존
    │       └── TestFacadePostService.java   # Service + Repository 의존
    └── framework/ioc/ApplicationContextTest.java
```

## 기술 스택

- Java 17+
- Gradle (Kotlin DSL)
- [Reflections](https://github.com/ronmamo/reflections) 0.10.2 — 클래스패스 스캔
- Lombok — `@RequiredArgsConstructor`로 생성자 자동 생성
- JUnit 5 + AssertJ

## 실행 방법

```bash
# 전체 테스트 실행
./gradlew test
```