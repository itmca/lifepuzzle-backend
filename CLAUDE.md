# CLAUDE.md

Claude AI 작업 가이드 - LifePuzzle Backend

## 프로젝트 개요

**서비스**: 소중한 사람들과의 추억을 기록하는 인생퍼즐 백엔드
**아키텍처**: Spring Boot API + Go 이미지 처리 마이크로서비스
**주요 기능**: OAuth 인증, 이미지 처리, 스토리 관리, 비동기 처리

## 빠른 시작

### 개발 환경 실행
```bash
# 백엔드 개발자 (인프라만)
./tools/scripts/start-infra.sh

# 프론트엔드 개발자 (전체 서비스)
./tools/scripts/start-full.sh

# 상태 확인
./tools/scripts/health.sh
```

### 새 기능 개발
```bash
# 브랜치 생성: feat/LP-123-description
git checkout main && git pull origin main
git checkout -b feat/LP-123-new-feature

# 작업 후 PR 생성
/create-pr  # Claude Code 명령어
```

## 프로젝트 구조 이해

### 핵심 디렉토리
```
services/lifepuzzle-api/     # 메인 API (Spring Boot)
services/image-resizer/      # 이미지 처리 (Go)
shared/java-common/          # Java 공통 라이브러리
infra/docker/                # 로컬 개발 환경
tools/scripts/               # 개발 편의 스크립트
```

### 기술 스택
- **API**: Spring Boot 3.x + JPA + Spring Security
- **이미지**: Go 1.21+ + 이미지 처리 라이브러리
- **데이터**: MySQL 8.0 + RabbitMQ + Redis
- **인프라**: Docker + Kubernetes + Helm

## 개발 패턴

### Spring Boot API 작업 시
```bash
# 공통 응답 형식 사용
ResponseEntity<ApiResponse<T>>

# 엔티티 설계 시 고려사항
- JPA 연관관계 최적화 (FetchType.LAZY)
- 공통 BaseEntity 상속 (created/updated 필드)
- 인덱스 및 제약조건 명시적 설정
```

### Go 이미지 처리 작업 시
```bash
# 표준 디렉토리 구조
cmd/        # 메인 애플리케이션
internal/   # 내부 패키지
pkg/        # 공개 패키지
```

### 코드 품질 체크
```bash
# Java (Spring Boot)
./gradlew checkstyleMain test

# Go (Image Resizer)
go fmt ./... && go test ./...
```

## 자주 사용하는 명령어

### Claude Code 명령어
```bash
/create-pr          # PR 자동 생성
/new-feature 설명    # 새 브랜치 생성 후 작업
```

### Git 워크플로우
```bash
# 커밋 형식: [LP-123] Add feature description
# 브랜치: feat/LP-123-description

# 상세 가이드: docs/GIT_WORKFLOW.md 참조
```

### 서비스 관리
```bash
# 개발 환경
./tools/scripts/start-infra.sh     # 인프라만
./tools/scripts/start-full.sh      # 전체 서비스
./tools/scripts/health.sh          # 상태 확인
./tools/scripts/stop.sh            # 중지

# 접근 URL
API Server: http://localhost:8080
Image Resizer: http://localhost:9000
RabbitMQ Management: http://localhost:15672
```

## 빠른 참조

### 핵심 문서
- **[Git Workflow](./docs/GIT_WORKFLOW.md)** - 브랜치 전략 및 커밋 컨벤션
- **[Versioning](./docs/VERSIONING.md)** - CalVer 버전 관리
- **[README.md](./README.md)** - 프로젝트 전체 개요

### API 문서
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: `services/lifepuzzle-api/docs/`

## 작업 효율성 팁

### 새 기능 개발 시
1. **기존 패턴 확인** - 유사 기능 구현 찾기
2. **공통 라이브러리 활용** - shared/ 디렉토리 확인
3. **테스트 작성** - 기능과 함께 테스트 코드 작성
4. **API 문서 업데이트** - Swagger 주석 추가

### 성능 최적화
- **N+1 쿼리 방지** - @EntityGraph, JOIN FETCH 활용
- **캐싱 전략** - Redis 활용한 적절한 캐싱
- **비동기 처리** - RabbitMQ 메시지 큐 활용
- **이미지 최적화** - Go 서비스에서 효율적 처리

### 보안 고려사항
- **민감 정보 로깅 금지** - 비밀번호, 토큰 등
- **OAuth 토큰 관리** - 적절한 만료시간 설정
- **입력값 검증** - @Valid, 커스텀 validator 사용