# LifePuzzle Development Scripts

이 디렉토리에는 LifePuzzle 백엔드 개발을 위한 편의 스크립트들이 있습니다.

## 🚀 빠른 시작 (프론트엔드 개발자용)

### 처음 설정

```bash
# 프로젝트 루트에서 실행
./tools/scripts/setup-dev.sh
```

이 스크립트는 프론트엔드 개발에 필요한 모든 백엔드 서비스를 자동으로 설정하고 시작합니다.

### 일상적인 사용

```bash
# 전체 스택 시작 (프론트엔드 개발용)
./tools/scripts/start-full.sh

# 서비스 상태 확인
./tools/scripts/health.sh

# 로그 확인
./tools/scripts/logs.sh

# 모든 서비스 중지
./tools/scripts/stop.sh
```

## 📋 스크립트 목록

### 🔧 설정 및 시작

| 스크립트 | 설명 | 사용 시점 |
|---------|------|----------|
| `setup-dev.sh` | 프론트엔드 개발을 위한 완전한 환경 설정 | 처음 설정할 때 |
| `start-full.sh` | 전체 애플리케이션 스택 시작 | 프론트엔드 개발 시 |
| `start-infra.sh` | 인프라만 시작 (MySQL + RabbitMQ) | 백엔드 개발 시 |

### 🛠️ 관리

| 스크립트 | 설명 | 사용법 |
|---------|------|--------|
| `stop.sh` | 모든 서비스 중지 | `./tools/scripts/stop.sh` |
| `health.sh` | 서비스 상태 확인 | `./tools/scripts/health.sh` |
| `logs.sh` | 로그 확인 | `./tools/scripts/logs.sh [service] [options]` |
| `cleanup.sh` | Docker 리소스 정리 | `./tools/scripts/cleanup.sh` |

## 🌐 서비스 접근 정보

### 전체 스택 실행 시 (`start-full.sh`)

- **LifePuzzle API**: http://localhost:8080
  - Health Check: http://localhost:8080/hc
- **Image Resizer**: http://localhost:9000  
  - Health Check: http://localhost:9000/health
- **MySQL**: localhost:3306
  - Database: `lifepuzzle`
  - Username: `lifepuzzle`
  - Password: `lifepuzzlepass`
- **RabbitMQ**: localhost:5672
  - Management UI: http://localhost:15672
  - Username: `lifepuzzle`
  - Password: `lifepuzzlepass`

### 인프라만 실행 시 (`start-infra.sh`)

- **MySQL**: localhost:3306
- **RabbitMQ**: localhost:5672
- **RabbitMQ Management**: http://localhost:15672

## 📖 상세 사용법

### setup-dev.sh

프론트엔드 개발을 위한 완전한 백엔드 환경을 설정합니다.

```bash
./tools/scripts/setup-dev.sh
```

**수행 작업:**
- Docker 실행 상태 확인
- `.env` 파일 생성 (없을 경우)
- 모든 서비스 빌드 및 시작
- 서비스 상태 확인
- 접속 정보 표시

### logs.sh

서비스별 로그를 확인할 수 있습니다.

```bash
# 모든 서비스 로그 (최근 100줄)
./tools/scripts/logs.sh

# 특정 서비스 로그
./tools/scripts/logs.sh api          # API 로그
./tools/scripts/logs.sh resizer      # 이미지 리사이저 로그
./tools/scripts/logs.sh mysql        # MySQL 로그
./tools/scripts/logs.sh rabbitmq     # RabbitMQ 로그

# 실시간 로그 확인
./tools/scripts/logs.sh api -f

# 특정 줄 수만 확인
./tools/scripts/logs.sh api -t 50
```

### health.sh

모든 서비스의 상태를 한번에 확인합니다.

```bash
./tools/scripts/health.sh
```

**확인 항목:**
- Docker 컨테이너 상태
- HTTP 엔드포인트 응답 상태
- 서비스별 헬스체크 결과

### cleanup.sh

Docker 리소스를 정리합니다.

```bash
./tools/scripts/cleanup.sh
```

**정리 옵션:**
1. 컨테이너만 제거
2. 컨테이너 + 이미지 제거  
3. 컨테이너 + 이미지 + 볼륨 제거 (⚠️ 데이터 손실)
4. 전체 정리 (⚠️ 데이터 손실)

## 🔧 환경 설정

### .env 파일

`infra/docker/.env` 파일에서 다음 설정을 변경할 수 있습니다:

```bash
# AWS S3 설정 (이미지 리사이저용)
AWS_REGION=us-west-2
AWS_ACCESS_KEY_ID=your-aws-access-key-id
AWS_SECRET_ACCESS_KEY=your-aws-secret-access-key
S3_BUCKET=lifepuzzle-dev

# 포트 설정
LIFEPUZZLE_API_PORT=8080
IMAGE_RESIZER_PORT=8081
```

## 🚨 문제 해결

### 자주 발생하는 문제

**1. 포트 충돌**
```bash
# 사용 중인 포트 확인
lsof -i :8080
lsof -i :3306

# 해결: 다른 서비스 중지 또는 포트 변경
```

**2. Docker 공간 부족**
```bash
# Docker 시스템 정리
docker system prune -f

# 또는 완전 정리
./tools/scripts/cleanup.sh
```

**3. 서비스가 시작되지 않음**
```bash
# 상태 확인
./tools/scripts/health.sh

# 로그 확인
./tools/scripts/logs.sh [service]

# 재시작
./tools/scripts/stop.sh
./tools/scripts/start-full.sh
```

**4. 데이터베이스 연결 오류**
```bash
# MySQL 컨테이너 상태 확인
docker ps | grep mysql

# MySQL 로그 확인
./tools/scripts/logs.sh mysql

# 새로 시작
./tools/scripts/cleanup.sh  # 옵션 3 선택
./tools/scripts/setup-dev.sh
```

## 💡 팁

### 프론트엔드 개발 워크플로우

1. **처음 설정**: `./tools/scripts/setup-dev.sh`
2. **매일 시작**: `./tools/scripts/start-full.sh`
3. **문제 발생시**: `./tools/scripts/health.sh` → `./tools/scripts/logs.sh`
4. **작업 종료**: `./tools/scripts/stop.sh`

### 백엔드 개발 워크플로우

1. **인프라만 시작**: `./tools/scripts/start-infra.sh`
2. **IDE에서 애플리케이션 실행**
3. **문제 해결**: `./tools/scripts/logs.sh mysql` / `./tools/scripts/logs.sh rabbitmq`

### 유용한 별칭 설정

`~/.bashrc` 또는 `~/.zshrc`에 추가:

```bash
alias lp-setup='./tools/scripts/setup-dev.sh'
alias lp-start='./tools/scripts/start-full.sh'
alias lp-stop='./tools/scripts/stop.sh'
alias lp-health='./tools/scripts/health.sh'
alias lp-logs='./tools/scripts/logs.sh'
```