# Versioning Strategy

## Calendar Versioning (CalVer)

LifePuzzle 백엔드는 [CalVer](https://calver.org/) 방식을 사용합니다.

### 버전 형식

```
YYYY.MM_DeployNumber
```

**구성 요소:**
- `YYYY.MM`: 배포 연월 (4자리 년도.2자리 월)
- `DeployNumber`: 해당 월의 몇 번째 배포인지 표시 (1부터 시작)

### 버전 예시

```bash
2023.04_1    # 2023년 4월의 첫 번째 배포
2023.04_5    # 2023년 4월의 다섯 번째 배포
2023.11_1    # 2023년 11월의 첫 번째 배포
2024.01_3    # 2024년 1월의 세 번째 배포
```

## 태그 생성 규칙

### 정규 배포

```bash
# production 브랜치에서 배포 후 태그 생성
git checkout production
git tag -a v2023.11_1 -m "Release 2023.11_1

Features:
- Add user authentication with OAuth 2.0
- Implement image processing API
- Add RabbitMQ integration

Bug Fixes:
- Fix memory leak in image resizer
- Resolve OAuth redirect issues"

git push origin v2023.11_1
```

### Hotfix 배포

Hotfix도 동일한 버저닝 전략을 사용합니다:

```bash
# 예: 2023.11_1에서 긴급 버그 발견
git checkout production
git tag -a v2023.11_2 -m "Hotfix 2023.11_2

Critical Bug Fixes:
- Fix null pointer exception in OAuth callback
- Resolve database connection timeout"

git push origin v2023.11_2
```

## 배포 프로세스

### 1. 배포 준비

```bash
# main 브랜치 최신 상태 확인
git checkout main
git pull origin main

# 릴리스 노트 준비 (CHANGELOG.md 업데이트)
# 버전 번호 결정 (현재 월의 다음 배포 번호)
```

### 2. production 배포

```bash
# main → production 머지
git checkout production
git pull origin production
git merge main
git push origin production

# 배포 스크립트 실행 (CI/CD 또는 수동)
# 배포 검증
```

### 3. 태그 생성 및 릴리스

```bash
# 버전 태그 생성
git tag -a v$(date +%Y.%m)_1 -m "Release $(date +%Y.%m)_1"
git push origin v$(date +%Y.%m)_1

# GitHub Release 생성 (선택사항)
gh release create v$(date +%Y.%m)_1 --title "Release $(date +%Y.%m)_1" --notes-file CHANGELOG.md
```

## 버전 관리 모범 사례

### 1. 월별 배포 계획

- **월 초 (1-5일)**: 주요 기능 배포
- **월 중 (6-20일)**: 개선 사항 및 버그 수정
- **월 말 (21-31일)**: 긴급 수정만 배포

### 2. 배포 번호 관리

```bash
# 현재 월의 마지막 배포 번호 확인
git tag -l "v$(date +%Y.%m)*" | sort -V | tail -1

# 다음 배포 번호 계산
# 예: v2023.11_3 → v2023.11_4
```

### 3. 릴리스 노트 작성

각 배포마다 다음 내용을 포함:

```markdown
## v2023.11_1 (2023-11-15)

### 새로운 기능
- OAuth 2.0 인증 시스템 구현
- 이미지 처리 API 추가
- RabbitMQ 메시지 큐 통합

### 개선 사항
- API 응답 시간 30% 개선
- 데이터베이스 쿼리 최적화
- 로그 형식 표준화

### 버그 수정
- 이미지 리사이저 메모리 누수 수정
- OAuth 리다이렉트 오류 해결
- 데이터베이스 연결 타임아웃 문제 수정

### 기술적 변경사항
- Spring Boot 3.1.5 업그레이드
- MySQL 8.0.34 업그레이드
- Docker 이미지 최적화

### 브레이킹 체인지
- `/api/v1/auth` 엔드포인트 변경
- 응답 형식 변경: `data` 필드 추가

### 알려진 이슈
- 대용량 이미지 처리 시 간헐적 타임아웃 (다음 버전에서 수정 예정)
```

## 지원 및 유지보수

### 지원 버전

- **현재 버전**: 최신 배포 버전
- **이전 버전**: 현재 월의 이전 배포들
- **LTS**: 분기별 주요 버전 (필요시)

### 보안 패치

보안 이슈 발견 시 즉시 hotfix 배포:

```bash
# 보안 패치 태그 예시
git tag -a v2023.11_4-security -m "Security patch 2023.11_4

Security Fixes:
- CVE-2023-xxxx: Fix SQL injection vulnerability
- Update dependencies with security vulnerabilities"
```

이 버저닝 전략을 통해 배포 이력을 명확하게 추적하고 관리할 수 있습니다.