# Rebook Trade Service

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.5-blue)
![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-336791)
![Redis](https://img.shields.io/badge/Redis-6+-DC382D)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.x-FF6600)
![AWS S3](https://img.shields.io/badge/AWS-S3-569A31)
![Gradle](https://img.shields.io/badge/Gradle-8.14.2-02303A)

**Rebook 마이크로서비스 아키텍처의 거래 관리 서비스**

중고 도서 거래 등록, 관리, 찜하기, 알림 발행을 담당하는 핵심 백엔드 서비스

</div>

---

## 1. 개요

**Rebook Trade Service**는 중고 도서 거래 플랫폼 Rebook의 핵심 백엔드 마이크로서비스로, 사용자 간 도서 거래를 관리하고 조율합니다. Spring Boot 기반으로 구현된 본 서비스는 **서비스 디스커버리**, **중앙화된 설정 관리**, **비동기 메시징**을 통한 확장 가능한 구조를 제공합니다.


### 서비스 역할

본 서비스는 Rebook 플랫폼 내에서 다음과 같은 역할을 담당합니다:

- **거래 관리**: 중고 도서 거래 등록, 수정, 삭제 및 상태 관리
- **찜하기 시스템**: 사용자별 관심 거래 북마킹 기능
- **추천 시스템**: Book Service 연동을 통한 개인화 거래 추천
- **알림 발행**: RabbitMQ를 통한 실시간 거래 알림 전송
- **이미지 관리**: AWS S3 기반 거래 이미지 업로드 및 관리

---

## 2. 목차
- [주요 기능](#3-주요-기능)
- [기술 스택](#4-기술-스택)
- [아키텍처](#5-아키텍처)
- [API 문서](#6-api-문서)
- [프로젝트 구조](#7-프로젝트-구조)
---

## 3. 주요 기능

### 3.1 거래 관리

#### 거래 등록 및 수정
- ✅ 중고 도서 거래 등록 (이미지 업로드 포함)
- ✅ 거래 정보 수정 (이미지 교체 가능)
- ✅ 거래 삭제 (소유자 권한 검증)
- ✅ AWS S3 기반 이미지 저장 및 URL 관리

#### 거래 조회
- ✅ 거래 상세 정보 조회 (찜하기 상태 포함)
- ✅ 내 거래 목록 조회 (페이지네이션 지원)
- ✅ 특정 도서의 모든 거래 목록 조회
- ✅ 타 사용자의 거래 목록 조회
- ✅ 사용자 맞춤 거래 추천 (Book Service 연동)

#### 거래 상태 관리
- ✅ 거래 상태 변경 (판매중 → 예약중 → 판매완료)
- ✅ 소유자 권한 검증을 통한 안전한 상태 관리
- ✅ 상태별 거래 필터링 및 조회

### 3.2 찜하기 (북마크) 시스템

- ✅ 거래 찜하기/찜 해제 토글 기능
- ✅ 찜한 거래 목록 조회 (페이지네이션 지원)
- ✅ 거래 조회 시 현재 사용자의 찜하기 상태 표시
- ✅ 복합키(Composite Key) 기반 효율적인 찜하기 관리

### 3.3 추천 시스템

- ✅ 사용자 맞춤 도서 거래 추천
- ✅ Book Service와 OpenFeign 연동을 통한 개인화 추천
- ✅ 추천 도서에 해당하는 거래 목록 자동 필터링

### 3.4 알림 시스템 (Event-Driven)

- ✅ 새로운 거래 등록 시 해당 도서를 찜한 사용자에게 알림 발행
- ✅ 거래 가격 변동 시 찜한 사용자에게 알림 발행
- ✅ RabbitMQ 기반 비동기 메시징 (Notification Service 연동)
- ✅ JSON 메시지 직렬화를 통한 안정적 메시지 전송


---

## 4. 기술 스택

### 4.1 백엔드 프레임워크

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Boot** | 3.3.13 | 애플리케이션 프레임워크 |
| **Java** | 17 | 프로그래밍 언어 |
| **Spring Data JPA** | - | ORM 및 데이터 접근 계층 |
| **Spring Validation** | - | 요청 데이터 유효성 검증 |
| **Lombok** | - | 보일러플레이트 코드 제거 |

### 4.2 데이터베이스 & 캐싱

| 기술 | 버전 | 용도 |
|------|------|------|
| **PostgreSQL** | 14+ | 관계형 데이터베이스 (메인 데이터 저장소) |
| **Redis** | 6+ | 분산 캐싱 및 세션 관리 |

### 4.3 마이크로서비스 인프라 (Spring Cloud)

| 기술 | 버전 | 용도 |
|------|------|------|
| **Eureka Client** | 2023.0.5 | 서비스 디스커버리 및 등록 |
| **Spring Cloud Config** | 2023.0.5 | 중앙화된 설정 관리 (외부 Config Server) |
| **OpenFeign** | 2023.0.5 | 선언적 HTTP 클라이언트 (Book Service 연동) |

### 4.4 메시징 & 이벤트

| 기술 | 버전 | 용도 |
|------|------|------|
| **RabbitMQ (AMQP)** | 3.x | 비동기 메시징 및 이벤트 발행 |
| **Spring AMQP** | - | RabbitMQ 통합 및 메시지 컨버터 |

### 4.5 클라우드 & 스토리지

| 기술 | 버전 | 용도 |
|------|------|------|
| **AWS S3** | SDK 2.27.21 | 거래 이미지 파일 저장소 |

### 4.6 모니터링 & 로깅

| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Actuator** | - | 헬스체크 및 메트릭 엔드포인트 |
| **Prometheus** | - | 메트릭 수집 및 모니터링 |
| **Sentry** | 8.13.2 | 실시간 에러 트래킹 및 알림 |
| **SLF4J & Logback** | - | 애플리케이션 로깅 |

### 4.7 API 문서화

| 기술 | 버전 | 용도 |
|------|------|------|
| **SpringDoc OpenAPI 3** | 2.6.0 | Swagger UI 기반 REST API 문서 자동 생성 |

### 4.8 빌드 & 배포

| 기술 | 버전 | 용도 |
|------|------|------|
| **Gradle** | 8.14.2 | 빌드 자동화 도구 |
| **Docker** | - | 컨테이너화 및 배포 |
| **Jacoco** | - | 테스트 커버리지 분석 |
| **JUnit 5** | - | 단위 테스트 프레임워크 |

---

## 5. 아키텍처
![ai거래묶음등록](~~~)
![ai거래상태측정](~~~)


## 6. API 문서

### 6.1 API 엔드포인트 상세

#### 6.1.1 거래 관리 API (`TradeController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **POST** | `/api/trades` | 거래 등록 |
| **GET** | `/api/trades/{tradeId}` | 거래 상세 조회 |
| **PATCH** | `/api/trades/{tradeId}` | 거래 상태 수정 |
| **PUT** | `/api/trades/{tradeId}` | 거래 정보 수정 |
| **DELETE** | `/api/trades/{tradeId}` | 거래 삭제 |
| **GET** | `/api/trades/me` | 내 거래 목록 조회 |
| **GET** | `/api/trades/books/{bookId}` | 특정 도서 거래 목록 |
| **GET** | `/api/trades/recommendations` | 추천 거래 목록 |
| **GET** | `/api/trades/others/{userId}` | 타인의 거래 목록 |

#### 6.1.2 찜하기 API (`TradeUserController`)

| Method | Endpoint | Summary |
|--------|----------|---------|
| **POST** | `/api/trades/{tradeId}/marks` | 거래 찜하기/취소 (토글) |
| **GET** | `/api/trades/marks` | 찜한 거래 목록 조회 |


## 7. 프로젝트 구조

```
src/main/java/com/example/rebooktradeservice/
├── advice/                   # 전역 예외 처리
├── common/                   # 응답 표준화
├── config/                   # RabbitMQ, S3, JPA 설정
├── controller/               # REST API 엔드포인트
├── enums/                    # 상태 관리 enum
├── exception/                # 커스텀 예외
├── feigns/                   # OpenFeign 클라이언트 (Book Service)
├── model/
│   ├── entity/              # JPA 엔티티 (Trade, TradeUser)
│   └── message/             # RabbitMQ 메시지 DTO
├── repository/              # Spring Data JPA 리포지토리
└── service/                 # 비즈니스 로직 (Trade, S3, Notification)
```

