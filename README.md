# LifePuzzle Backend

> 소중한 사람들과의 추억을 모으고 기록하는 인생퍼즐 서비스의 백엔드 시스템

### 핵심 기능
- **사용자 인증**: OAuth 2.0 기반 소셜 로그인 (Kakao, Apple)
- **이미지 처리**: 실시간 리사이징, 크롭, 포맷 변환
- **추억 관리**: 스토리 생성, 수정, 공유 기능
- **비동기 처리**: RabbitMQ 기반 백그라운드 작업

## 요구사항

### 필수 도구
- **Docker** & **Docker Compose**: 로컬 환경 실행
- **Git**: 버전 관리
- **Java 17+**: Spring Boot 개발 시
- **Go 1.21+**: Image Resizer 개발 시

## Quick Start

### Frontend 개발자용
백엔드 서비스를 로컬에서 실행하여 프론트엔드 개발:

```bash
# 초기 설정 (한 번만 실행)
./tools/scripts/setup-dev.sh

# 매일 사용하는 명령어
./tools/scripts/start-full.sh    # 모든 백엔드 서비스 시작
./tools/scripts/health.sh        # 서비스 상태 확인
./tools/scripts/stop.sh          # 작업 완료 후 중지
```

**서비스 접근:**
- API Server: http://localhost:8080
- Image Resizer: http://localhost:9000
- RabbitMQ Management: http://localhost:15672

### Backend 개발자용
인프라만 실행하고 IDE에서 애플리케이션 개발:

```bash
./tools/scripts/start-infra.sh   # MySQL + RabbitMQ만 시작
# 이후 IDE에서 각 서비스를 개별 실행
./tools/scripts/stop.sh          # 작업 완료 후 중지
```

> 자세한 가이드: [tools/scripts/README.md](./tools/scripts/README.md)

## 아키텍처

### 마이크로서비스 구조

```bash
lifepuzzle-backend/
├── services/                    # 마이크로서비스들
│   ├── lifepuzzle-api/         # 메인 API (Spring Boot)
│   └── image-resizer/          # 이미지 처리 (Go)
├── shared/                      # 공통 라이브러리
│   ├── java-common/            # Java 공통 유틸리티
│   └── go-common/              # Go 공통 패키지
├── infra/                       # 인프라 설정
│   ├── docker/                 # 로컬 개발 환경
│   └── helm/                   # Kubernetes 배포
└── tools/                       # 개발 도구
    ├── checkstyle/             # 코드 스타일 설정
    └── scripts/                # 편의 스크립트
```

### 주요 서비스

| 서비스 | 기술 스택 | 포트 | 설명 |
|--------|----------|------|------|
| **[lifepuzzle-api](./services/lifepuzzle-api/README.md)** | Spring Boot 3.x | 8080 | 메인 REST API, 인증, 사용자 관리 |
| **[image-resizer](./services/image-resizer/README.md)** | Go 1.21+ | 9000 | 이미지 리사이징 및 처리 |

### 외부 의존성

- **MySQL 8.0**: 메인 데이터베이스
- **RabbitMQ**: 비동기 메시지 처리
- **Redis**: 캐싱 및 세션 관리 (선택사항)

## 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Spring Boot 3.x, Spring Security, JPA |
| **Image Processing** | Go 1.21+, Image processing libraries |
| **Database** | MySQL 8.0 |
| **Message Queue** | RabbitMQ |
| **Caching** | Redis (optional) |
| **Container** | Docker, Docker Compose |
| **Orchestration** | Kubernetes + Helm |
| **CI/CD** | GitHub Actions |

### 환경 설정

주요 환경 변수들을 `.env` 파일로 설정:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=lifepuzzle
DB_USERNAME=lifepuzzle
DB_PASSWORD=password

# OAuth
KAKAO_CLIENT_ID=your_kakao_client_id
FACEBOOK_CLIENT_ID=your_facebook_client_id
```

## 개발 가이드

### 새로운 기능 개발

```bash
# 1. 브랜치 생성
git checkout main && git pull origin main
git checkout -b feat/LP-123-new-feature

# 2. 개발 및 테스트
./tools/scripts/start-infra.sh
# IDE에서 개발 진행

# 3. PR 생성
claude pr  # 자동 PR 생성
```

### 코드 품질 도구

```bash
# Java (Spring Boot)
./gradlew checkstyleMain          # 코드 스타일 검사
./gradlew test                    # 단위 테스트
./gradlew bootRun                 # 로컬 실행

# Go (Image Resizer)
go fmt ./...                      # 코드 포맷팅
go test ./...                     # 테스트 실행
go run cmd/main.go               # 로컬 실행
```

## 문서

### 개발 가이드
- **[Git Workflow](./docs/GIT_WORKFLOW.md)** - 브랜치 전략, 커밋 컨벤션, PR 가이드
- **[Versioning](./docs/VERSIONING.md)** - CalVer 기반 버전 관리 전략
- **[CLAUDE.md](./CLAUDE.md)** - Claude AI 작업 가이드

### 인프라 & 배포
- **[Docker 환경](./infra/README.md)** - 로컬 개발 환경 구성
- **[Kubernetes 배포](./infra/helm/README.md)** - 프로덕션 환경 배포
- **[개발 스크립트](./tools/scripts/README.md)** - 편의 스크립트 사용법

### API 문서
- **API 문서**: http://localhost:8080/swagger-ui.html (로컬 실행 시)
- **OpenAPI 스펙**: `./services/lifepuzzle-api/docs/`

## 상태 확인

```bash
# 서비스 헬스체크
./tools/scripts/health.sh

# 개별 서비스 확인
curl http://localhost:8080/actuator/health    # API 서비스
curl http://localhost:9000/health             # Image Resizer
```

## 기여하기

1. **이슈 생성**: 새로운 기능이나 버그 발견 시
2. **브랜치 생성**: `feat/description` 형식으로
3. **개발**: 테스트 코드와 함께 구현
4. **PR 생성**: claude code에서 `/create-pr` 명령어 또는 수동 생성
5. **코드 리뷰**: 최소 1명 이상의 승인 필요