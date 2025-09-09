# CLAUDE.md

Claude AI 어시스턴트 개발 작업 가이드

## Git 워크플로우

### 브랜치 관리
- main 브랜치로 전환 시 `git pull origin main` 실행

### 커밋 컨벤션
Conventional Commits 규칙 + 티켓 번호 추가

```bash
# 티켓이 있는 경우
[TICKET-123] feat: 새로운 기능 추가
[JIRA-456] fix: 버그 수정

# 티켓이 없는 경우  
feat: 새로운 기능 추가
fix: 버그 수정
```

**커밋 타입:**
- `feat`: 새 기능
- `fix`: 버그 수정
- `docs`: 문서 변경
- `refactor`: 리팩토링
- `test`: 테스트
- `build`: 빌드 시스템, 의존성 변경
- `chore`: 기타 작업

**커밋 분리 원칙:**
- 각 커밋은 하나의 목적만 가져야 함
- 기능, 버그수정, 문서, 빌드 등 성격별로 분리하여 커밋
- 관련 없는 변경사항은 별도 커밋으로 분리

## Pull Request

`.github/PULL_REQUEST_TEMPLATE.md` 템플릿 참고해서 작성

## 개발 지원

- 코드 작성 및 리뷰
- 테스트 케이스 작성
- 문서 작성/업데이트
- 보안 및 성능 검토
- 프로젝트 구조 분석