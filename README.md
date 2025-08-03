# lifepuzzle-backend

## Quick Start

### For Frontend Developers
백엔드 서비스를 로컬에서 실행하여 프론트엔드 개발 및 테스트:

```bash
# 처음 한 번만 실행 (완전 자동화)
./tools/scripts/setup-dev.sh

# 이후 매일 사용
./tools/scripts/start-full.sh    # 모든 백엔드 서비스 시작
./tools/scripts/health.sh        # 서비스 상태 확인
./tools/scripts/stop.sh          # 작업 완료 후 중지
```

**서비스 접근:**
- API: http://localhost:8080
- Image Resizer: http://localhost:9000
- RabbitMQ Management: http://localhost:15672

### For Backend Developers
인프라만 실행하고 IDE에서 애플리케이션 개발:

```bash
./tools/scripts/start-infra.sh   # MySQL + RabbitMQ만 시작
# IDE에서 애플리케이션 실행
./tools/scripts/stop.sh          # 작업 완료 후 중지
```

> 📖 자세한 스크립트 사용법: [tools/scripts/README.md](./tools/scripts/README.md)

## Services

This monorepo contains the following services:

- **[lifepuzzle-api](./services/lifepuzzle-api/README.md)** - Spring Boot REST API service
- **[image-resizer](./services/image-resizer/README.md)** - Go-based image processing service

## Shared Libraries

- **shared/java-common** - Common Java utilities and configurations
- **shared/go-common** - Common Go packages

## Project Structure

```bash
lifepuzzle-backend/
├── services/
│   ├── lifepuzzle-api/          # Spring Boot API service
│   └── image-resizer/           # Go image processing service
├── shared/
│   ├── java-common/             # Shared Java libraries
│   └── go-common/               # Shared Go libraries
├── infra/
│   ├── docker/                  # Docker Compose configurations
│   └── helm/                    # Kubernetes Helm charts
└── tools/
    ├── checkstyle/              # Code style configuration
    └── scripts/                 # Development convenience scripts
```

## Infrastructure

- **[Docker Compose](./infra/README.md)** - Local development with MySQL and RabbitMQ
- **[Kubernetes Helm Charts](./infra/helm/README.md)** - Production deployment

### Versioning [수정 필요]

[CalVer](https://calver.org/)을 따릅니다.

Format: `YYYY.MM_DeployNumber`

- `YYYY.MM`: 배포 연월
- `DeployNumber`: 해당 월의 몇번째 배포인지 표시

예시

- `2023.04_1`: 2023년 4월의 첫번째 배포
- `2023.04_5`: 2023년 4월의 다섯번째 배포

### Branch Name, Commit Msg Format

Branch Name: `<type>/<ticket no>-<subject>`

    feat/LP-1-foo

Commit Msg: `[<ticket no>] <type>: <subject>`

    [LP-0] feat: foo

Type Component

```bash
feat: 새로운 기능 추가/수정/삭제
fix: 버그 수정
hotfix: 운영 환경 대상 긴급 버그 수정
refactor: 리팩토링
test: 테스트 코드 작성
build: dependency 추가/수정/삭제
docs: 문서 수정
style: 코드 포맷, 스타일 수정
chore: 위 타입들에 해당하지 않는 기타 작업
```

참고 사항

- 여러 성격을 가지는 커밋 또는 브랜치 명인 경우 대표하는 type 하나 사용
- 커밋의 경우 최대한 적절한 타입으로 나누어 커밋

### Git Strategy

기본적으로 [GitLab FLow](https://docs.gitlab.com/ee/topics/gitlab_flow.html)를 따릅니다.

Branches

- `main`: 기본 브랜치로 작업 브랜치들이 머지 되는 브랜치
- `production`: 배포 브랜치

Basic Flow

1. `main` 브랜치에서 작업 브랜치 생성 및 작업 진행
2. 작업 완료 후 `main` 브랜치로 PR, 리뷰 후 머지
3. 배포 시 `main` 브랜치에서 `production` 브랜치로 PR & 머지
4. 배포 완료 후 버전 tag 추가

Hotfix Flow

1. 배포 버전에서 버그 발생하여 긴급 수정 필요 시 `production` 브랜치에서 `hotfix` 브랜치 생성
2. `hotfix` 브랜치 작업 후 `production` 브랜치로 PR & 머지 (리뷰 권장되지만 생략 가능)
3. 버그 해결 완료 후 버전 tag 추가 (hotfix도 기본 버저닝 전략 사용 e.g. `2023_04_1` -> `2023_04_2`)
4. `hotfix` 브랜치를 `main` 브랜치로 PR & 머지
