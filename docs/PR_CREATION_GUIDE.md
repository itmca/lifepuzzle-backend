# PR 생성 가이드 (Claude Code)

## 문제점 분석

### 이전 PR #166의 문제점

**제목:** "Refactor: Request DTO 필드명 네이밍 컨벤션 통일 및 하위 호환성 지원"

**Chris Beams 규칙 위반 사항:**
1. ❌ **타입 접두사 사용**: "Refactor:" 접두사는 사용하지 않아야 함
2. ❌ **제목 길이 초과**: 50자를 훨씬 초과 (약 35자)
3. ❌ **한글 사용**: 일관성을 위해 영어 권장

**올바른 제목 예시:**
- ✅ "Normalize request DTO field naming" (37자)
- ✅ "Add backward compatibility to request DTOs" (45자)

## Chris Beams 7가지 규칙 상세 설명

### 1. 제목과 본문을 빈 줄로 구분
```
Normalize request DTO field naming

Request DTO의 필드명을 Response DTO와 일관성 있게 정규화하고,
@JsonAlias를 사용하여 하위 호환성을 보장합니다.
```

### 2. 제목은 50자 이내로 제한
- 영어: 최대 50자
- 한글: 최대 25자 권장 (한글은 영어보다 정보 밀도가 높음)

### 3. 제목 첫 글자는 대문자
```
✅ Add user authentication
❌ add user authentication
```

### 4. 제목 끝에 마침표 금지
```
✅ Fix memory leak in processor
❌ Fix memory leak in processor.
```

### 5. 제목은 명령형 사용 (Imperative Mood)
명령형은 현재형 동사의 원형을 사용하는 것입니다.

**올바른 동사:**
- Add (추가하다)
- Fix (수정하다)
- Update (업데이트하다)
- Remove (제거하다)
- Refactor (리팩토링하다)
- Normalize (정규화하다)
- Improve (개선하다)
- Optimize (최적화하다)

**잘못된 형태:**
```
❌ Added user authentication (과거형)
❌ Adding user authentication (현재진행형)
❌ Adds user authentication (3인칭 현재형)
✅ Add user authentication (명령형)
```

### 6. 본문은 72자에서 줄바꿈
가독성을 위해 본문은 72자마다 줄바꿈을 권장합니다.

### 7. 본문에는 무엇을, 왜 했는지 설명
- **What**: 무엇을 변경했는가
- **Why**: 왜 변경했는가
- **How**: 어떻게 변경했는가 (선택사항)

## 타입 접두사에 대한 오해

많은 개발자들이 커밋/PR 제목에 타입 접두사를 사용하지만, **Chris Beams 스타일에서는 사용하지 않습니다.**

### ❌ 잘못된 방식 (Conventional Commits 스타일)
```
feat: Add user authentication
fix: Fix memory leak
refactor: Refactor service layer
```

### ✅ 올바른 방식 (Chris Beams 스타일)
```
Add user authentication
Fix memory leak
Refactor service layer
```

**이유:**
- Chris Beams 스타일은 제목 자체가 명령형이므로 타입을 별도로 표시할 필요가 없음
- 명령형 동사(Add, Fix, Refactor)가 이미 변경 타입을 나타냄
- 제목 길이 50자 제한을 지키기 위해 불필요한 접두사 제거

**참고:** Conventional Commits와 Chris Beams는 다른 스타일입니다. 이 프로젝트는 Chris Beams 스타일을 따릅니다.

## Claude Code 자동 PR 생성 사용법

### 1. 기본 사용
```bash
/create-pr
```

Claude가 자동으로:
1. 변경사항 분석
2. Chris Beams 스타일을 준수하는 PR 제목 생성
3. 구조화된 PR 본문 작성
4. GitHub PR 생성

### 2. Claude가 생성하는 PR 제목 예시

**좋은 예시:**
```
변경: Request DTO 필드명 정규화
→ "Normalize request DTO field naming"

변경: 메모리 누수 버그 수정
→ "Fix memory leak in image processor"

변경: 사용자 인증 기능 추가
→ "Add user authentication feature"
```

**피해야 할 패턴:**
```
❌ "Refactor: Request DTO 필드명 통일" (타입 접두사)
❌ "Request DTO 필드명 네이밍 컨벤션 통일 및 하위 호환성 지원" (너무 김)
❌ "request dto 정규화" (소문자 시작)
```

### 3. PR 생성 전 체크리스트

- [ ] PR 제목이 명령형 동사로 시작하는가?
- [ ] PR 제목이 50자 이내인가?
- [ ] PR 제목에 타입 접두사(Feat:, Fix:, Refactor:)가 없는가?
- [ ] PR 제목 첫 글자가 대문자인가?
- [ ] PR 제목에 마침표가 없는가?
- [ ] PR 본문에 작업 배경, 작업 내용, 참고 사항이 포함되어 있는가?

## 일반적인 실수와 해결책

### 실수 1: 타입 접두사 사용
```
❌ "Refactor: Improve code quality"
✅ "Improve code quality"
```

### 실수 2: 과거형 사용
```
❌ "Added new feature"
✅ "Add new feature"
```

### 실수 3: 제목이 너무 긺
```
❌ "Request DTO 필드명 네이밍 컨벤션 통일 및 하위 호환성 지원"
✅ "Normalize request DTO field naming"
```

### 실수 4: 소문자로 시작
```
❌ "fix critical bug in auth"
✅ "Fix critical bug in auth"
```

### 실수 5: 마침표 포함
```
❌ "Update documentation."
✅ "Update documentation"
```

## 참고 자료

- [Chris Beams - How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
- [GIT_WORKFLOW.md](./GIT_WORKFLOW.md) - 프로젝트 Git 워크플로우
- [CLAUDE.md](../CLAUDE.md) - Claude Code 사용 가이드
