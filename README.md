# 문화숲 (Culture Forest)

자유로운 문화공간 커뮤니티 웹 애플리케이션
세종사이버대학교 2024학년도 가을학기

## 프로젝트 소개

문화숲은 예술, 음악, 영화, 공연 등 다양한 문화적 경험과 생각을 나누는 커뮤니티 플랫폼입니다. 사용자들이 자유롭게 문화 관련 콘텐츠를 공유하고 소통할 수 있는 공간을 제공합니다.

## 주요 기능

- 게시판 CRUD 기능
- 댓글 시스템
- 회원 관리
- 검색 기능 (제목, 내용, 작성자)
- 반응형 디자인

## 기술 스택

### Backend

- Java 11
- Spring Boot 2.7.x
- Spring Data JPA
- QueryDSL
- MariaDB
- Lombok

### Frontend

- Thymeleaf
- Bootstrap 4.5.0
- jQuery
- Font Awesome

## 프로젝트 구조

### 엔티티 구조

- BaseEntity: 공통 날짜 필드 관리
- Board: 게시글 엔티티
- Member: 회원 엔티티
- Reply: 댓글 엔티티

### 주요 특징

- JPA Auditing을 활용한 생성/수정 시간 자동 관리
- QueryDSL을 활용한 동적 쿼리 처리
- 페이징 처리
- 모달을 활용한 댓글 CRUD
- Bootstrap을 활용한 반응형 디자인

## 실행 방법

1. MariaDB 설치 및 데��터베이스 생성
2. application.properties 설정
3. 프로젝트 빌드
