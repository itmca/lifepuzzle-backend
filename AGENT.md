# AGENT.md

AI 코딩 에이전트 (Codex, Antigravity 등)를 위한 LifePuzzle Backend 가이드

## 시작 전 필수

team-config 최신화:
```bash
cd .team && git pull origin main && cd ..
```

## 핵심 규칙

### Git & 커밋 (Chris Beams 스타일)

- **브랜치**: `<type>/<ticket>-<subject>` (예: `feat/LP-123-auth`)
- **커밋**: 명령형, 50자 이내, 첫 글자 대문자, 마침표 금지

```
Good: Add user authentication
Bad:  Added auth.
```

### PR 제목

- 명령형: Add, Fix, Update, Remove
- 50자 이내
- 타입 접두사 금지 (Refactor: X)

## 프로젝트 개요

**서비스**: 인생퍼즐 백엔드
**아키텍처**: Spring Boot API + Go 이미지 처리 마이크로서비스

## 구조

```
services/lifepuzzle-api/     # 메인 API (Spring Boot)
services/image-resizer/      # 이미지 처리 (Go)
shared/java-common/          # Java 공통 라이브러리
infra/docker/                # 로컬 개발 환경
```

## 기술 스택

- **API**: Spring Boot 3.x + JPA + Spring Security
- **이미지**: Go 1.21+
- **데이터**: MySQL 8.0 + RabbitMQ + Redis

## 명령어

```bash
# 개발 환경
./tools/scripts/start-infra.sh     # 인프라
./tools/scripts/start-full.sh      # 전체
./tools/scripts/health.sh          # 상태 확인

# 코드 품질
./gradlew checkstyleMain test      # Java
go fmt ./... && go test ./...      # Go
```

## 상세 가이드

- `.team/base/GIT_WORKFLOW.md` - Git 상세 가이드
- `.team/base/PR_RULES.md` - PR 규칙 상세
- `docs/` - 프로젝트별 상세 문서
