# Git Workflow

## 브랜치 전략

### GitLab Flow 기반

기본적으로 [GitLab Flow](https://docs.gitlab.com/ee/topics/gitlab_flow.html)를 따릅니다.

**브랜치 구조:**

- `main`: 기본 브랜치로 작업 브랜치들이 머지되는 브랜치
- `production`: 배포 브랜치

### 브랜치 네이밍

**형식:** `<type>/<ticket-no>-<subject>`

```bash
feat/LP-1-user-authentication
fix/LP-123-oauth-redirect
docs/LP-456-api-documentation
```

**타입별 예시:**
- `feat/`: 새로운 기능
- `fix/`: 버그 수정
- `hotfix/`: 긴급 수정
- `docs/`: 문서 작업
- `refactor/`: 리팩토링
- `test/`: 테스트 추가/수정

## 기본 워크플로우

### 1. 일반적인 개발 플로우

```bash
# 1. main 브랜치에서 작업 브랜치 생성
git checkout main
git pull origin main
git checkout -b feat/LP-123-new-feature

# 2. 작업 진행 및 커밋
git add .
git commit -m "[LP-123] Add new feature

Implement user authentication with OAuth 2.0 support.
This allows users to sign in with social media accounts.

- Add OAuth configuration
- Create authentication service
- Update user model"

# 3. 작업 브랜치를 원격에 푸시
git push -u origin feat/LP-123-new-feature

# 4. Pull Request 생성
# GitHub UI 또는 CLI 사용

# 5. 코드 리뷰 후 main 브랜치로 머지
# Squash and merge 권장
```

### 2. 배포 플로우

```bash
# main 브랜치에서 production 브랜치로 PR & 머지
git checkout production
git pull origin production
git merge main

# 배포 완료 후 버전 태그 추가
git tag -a v2023.11_1 -m "Release 2023.11_1"
git push origin v2023.11_1
```

### 3. Hotfix 플로우

긴급 수정이 필요한 경우:

```bash
# 1. production 브랜치에서 hotfix 브랜치 생성
git checkout production
git pull origin production
git checkout -b hotfix/LP-999-critical-bug

# 2. 버그 수정 및 커밋
git commit -m "[LP-999] Fix critical authentication bug

Fix null pointer exception in OAuth callback handler.
This was causing login failures for Facebook users.

- Add null checks in callback handler
- Update error handling for OAuth failures"

# 3. production 브랜치로 즉시 머지 (리뷰 권장하지만 생략 가능)
git checkout production
git merge hotfix/LP-999-critical-bug
git push origin production

# 4. 버전 태그 추가
git tag -a v2023.11_2 -m "Hotfix 2023.11_2"
git push origin v2023.11_2

# 5. main 브랜치로도 머지
git checkout main
git merge hotfix/LP-999-critical-bug
git push origin main
```

## 커밋 메시지 가이드라인

### Chris Beams 7가지 규칙

1. **제목과 본문을 빈 줄로 구분**
2. **제목은 50자 이내로 제한**
3. **제목 첫 글자는 대문자**
4. **제목 끝에 마침표 금지**
5. **제목은 명령형 사용** (Add, Fix, Update, Remove...)
6. **본문은 72자에서 줄바꿈**
7. **본문에는 무엇을, 왜 했는지 설명**

### 커밋 메시지 형식

```bash
# 티켓이 있는 경우
[LP-123] Add user authentication feature

Implement OAuth 2.0 integration with Google and Facebook providers.
This allows users to sign in with their social media accounts
instead of creating new credentials.

- Add OAuth configuration for Google/Facebook
- Create user service for social login handling
- Update login UI with social provider buttons

# 티켓이 없는 경우
Add user authentication feature

Implement OAuth 2.0 integration with Google and Facebook providers.
This allows users to sign in with their social media accounts
instead of creating new credentials.
```

### 커밋 타입 (선택사항)

```bash
feat: 새로운 기능 추가/수정/삭제
fix: 버그 수정
hotfix: 운영 환경 대상 긴급 버그 수정
refactor: 리팩토링
test: 테스트 코드 작성
build: 빌드 시스템, 의존성 변경
docs: 문서 수정
style: 코드 포맷, 스타일 수정
chore: 기타 작업
```

### 커밋 분리 원칙

- **각 커밋은 하나의 목적만 가져야 함**
- **논리적으로 관련된 변경사항들만 함께 커밋**
- **관련 없는 변경사항은 별도 커밋으로 분리**
- **가능한 한 작은 단위로 커밋을 나누어 작성**

## Pull Request 가이드라인

### PR 제목 규칙

Chris Beams 스타일을 따르며, GitHub Actions에서 자동 검증됩니다:

```bash
✅ Add user authentication feature
✅ Fix memory leak in image processor
✅ [LP-1234] Update API documentation
✅ Remove deprecated OAuth endpoints

❌ add user authentication feature (소문자 시작)
❌ Added user authentication feature (과거형)
❌ Add user authentication feature. (마침표)
❌ Add user authentication feature that allows users to login with social providers (50자 초과)
```

### PR 설명

`.github/PULL_REQUEST_TEMPLATE.md` 템플릿을 따라 작성합니다.

### 자동 PR 생성

```bash
claude pr
```

이 명령어는 다음을 자동으로 수행합니다:
- 현재 브랜치의 변경사항 분석
- PR 템플릿을 기반으로 제목과 설명 자동 생성
- GitHub PR 생성

## 도구 및 검증

### commitlint

커밋 메시지가 Chris Beams 스타일을 따르는지 자동 검증합니다:

```bash
npm install                    # commitlint 설치
npx commitlint --edit         # 커밋 메시지 검증
```

### GitHub Actions

- **PR Title Check**: PR 제목이 Chris Beams 스타일을 따르는지 검증
- **Commit Lint**: 커밋 메시지 형식 검증

## 모범 사례

### 1. 작업 시작 전

```bash
# 항상 최신 main 브랜치에서 시작
git checkout main
git pull origin main
git checkout -b feat/LP-123-new-feature
```

### 2. 작업 중

```bash
# 자주 커밋하되, 의미 있는 단위로 분리
git add feature1.java
git commit -m "[LP-123] Add user model validation

Implement validation rules for user registration form.

- Add email format validation
- Add password strength requirements"

git add feature2.java
git commit -m "[LP-123] Add user registration service

Implement service layer for user registration process.

- Add user creation logic
- Add duplicate email check
- Add password encryption"
```

### 3. PR 생성 전

```bash
# 최신 main과 sync
git checkout main
git pull origin main
git checkout feat/LP-123-new-feature
git rebase main

# 커밋 정리 (필요시)
git rebase -i HEAD~3
```

### 4. 코드 리뷰

- **모든 PR은 최소 1명 이상의 리뷰 필수**
- **CI/CD 검증 통과 필수**
- **Squash and merge 권장**

이 가이드라인을 따라 일관성 있는 Git 워크플로우를 유지하세요.