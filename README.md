# Spring IoC Container

## 프로젝트 소개

Spring의 핵심 개념인 **IoC(Inversion of Control)** 와
**DI(Dependency Injection)** 를 직접 구현한 프로젝트입니다.

v1에서는 하드코딩 방식으로 Bean을 생성하고 관리했으며,
v2에서는 Reflections를 활용해 **컴포넌트 스캔 기반 자동 Bean 생성 및 DI**를 구현했습니다.

---

## 구현 기능

* Reflections 기반 컴포넌트 스캔
* 애노테이션 기반 Bean 등록 (@Component, @Service 등)
* 생성자 기반 DI 자동화
* Singleton 유지

---

## 테스트

ApplicationContextTest 기준

* ApplicationContext 생성
* testPostService 빈 생성
* Singleton 검증
* testPostRepository 빈 생성
* testPostService 의존성 주입
* testFacadePostService 의존성 주입

---

## 핵심 구조

### ApplicationContext

* 컴포넌트 스캔
* Bean 생성 및 관리
* 의존성 주입 수행

---

## 핵심 코드

```java
args[i] = createBean(parameterTypes[i]);
```

 -> 생성자 파라미터를 기반으로 의존성을 재귀적으로 생성

---

## 실행 방법

```bash
./gradlew test
```

---

## 느낀 점

* IoC와 DI 개념을 직접 구현하며 객체 생성과 의존성 관리 구조를 명확히 이해할 수 있었다
* v2에서 하드코딩을 제거하고 자동화된 구조로 확장하면서 Spring의 내부 동작 원리를 더 깊이 이해할 수 있었다
