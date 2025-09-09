# Photo Reprocessing Script

기존 사진들을 이미지 리사이징 큐로 일괄 처리하는 Go 스크립트입니다.

## 🚀 사용법

### 1. 의존성 설치
```bash
cd scripts/photo-reprocessing
go mod download
```

### 2. 환경 변수 설정
```bash
export DATABASE_URL="username:password@tcp(host:port)/database"
export RABBITMQ_URL="amqp://username:password@host:port/"
```

### 3. 실행

#### 리사이징이 누락된 사진만 처리 (추천)
```bash
go run main.go \
  --missing-only=true \
  --batch-size=50 \
  --delay-ms=1000
```

#### 모든 사진 재처리
```bash
go run main.go \
  --missing-only=false \
  --batch-size=30 \
  --delay-ms=2000
```

#### 특정 범위 처리
```bash
go run main.go \
  --start-id=100 \
  --end-id=500 \
  --batch-size=25
```

#### 드라이런 (실제로 메시지 발송하지 않고 테스트)
```bash
go run main.go \
  --dry-run=true \
  --missing-only=true
```

## 📋 옵션

| 플래그 | 기본값 | 설명 |
|--------|--------|------|
| `--db-url` | `$DATABASE_URL` | 데이터베이스 연결 URL |
| `--rabbitmq-url` | `$RABBITMQ_URL` | RabbitMQ 연결 URL |
| `--exchange` | `image-processing` | RabbitMQ Exchange 이름 |
| `--routing-key` | `image.resize` | RabbitMQ Routing Key |
| `--queue` | `image-resize-queue` | RabbitMQ Queue 이름 |
| `--batch-size` | `50` | 배치당 처리할 사진 수 |
| `--delay-ms` | `1000` | 배치 간 지연 시간 (밀리초) |
| `--dry-run` | `false` | 실제 발송 없이 테스트 |
| `--missing-only` | `true` | 누락된 사이즈만 처리 |
| `--start-id` | `0` | 시작 사진 ID |
| `--end-id` | `0` | 종료 사진 ID |

## 🎯 380개 사진 처리 예시

```bash
# 1. 먼저 드라이런으로 확인
go run main.go --dry-run=true --missing-only=true

# 2. 실제 처리 (안전한 설정)
go run main.go \
  --missing-only=true \
  --batch-size=25 \
  --delay-ms=2000

# 또는 환경변수로
DATABASE_URL="user:pass@tcp(localhost:3306)/lifepuzzle" \
RABBITMQ_URL="amqp://user:pass@localhost:5672/" \
go run main.go --missing-only=true
```

## 🔍 로그 예시

```
2025/01/09 15:30:45 Starting photo reprocessing script with config: ...
2025/01/09 15:30:45 Successfully connected to database
2025/01/09 15:30:45 Successfully connected to RabbitMQ
2025/01/09 15:30:45 Executing query: SELECT id, hero_id, url, resized_sizes, type FROM story_photo WHERE type = 'IMAGE' ORDER BY id
2025/01/09 15:30:45 Loaded 380 photos from database
2025/01/09 15:30:45 Filtered to 127 photos with missing sizes
2025/01/09 15:30:45 Found 127 photos to process
2025/01/09 15:30:45 Processing batch 1-25 of 127
2025/01/09 15:30:45 Sent reprocessing message for photo ID: 15
...
2025/01/09 15:32:10 Processing completed - successful: 127, errors: 0
2025/01/09 15:32:10 Photo reprocessing completed successfully!
```

## ⚠️ 주의사항

- 프로덕션에서 실행 시 `--batch-size`를 작게 하고 `--delay-ms`를 크게 설정하여 시스템 부하 방지
- 먼저 `--dry-run=true`로 테스트 후 실제 실행
- RabbitMQ 큐 상태 모니터링 필요