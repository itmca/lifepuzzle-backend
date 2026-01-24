현재 브랜치의 변경사항을 분석하고 .github/PULL_REQUEST_TEMPLATE.md 템플릿에 맞춰 PR을 생성해주세요.

## PR 제목 작성 규칙 (Chris Beams 스타일 준수 필수)

1. **명령형(imperative mood) 사용**: Add, Fix, Update, Remove, Refactor (동사원형)
2. **첫 글자 대문자**
3. **50자 이내** (한글의 경우 25자 이내 권장)
4. **마침표 금지**
5. **과거형 금지**: Added(X), Fixed(X) → Add(O), Fix(O)
6. **타입 접두사 금지**: "Refactor:", "Fix:" 등은 사용하지 않음

### 좋은 예시
- Add user authentication feature
- Fix memory leak in image processor
- Update API documentation
- Remove deprecated OAuth endpoints
- Normalize request DTO field naming

### 나쁜 예시
- ❌ Refactor: Request DTO 필드명 통일 (타입 접두사 사용)
- ❌ Added user authentication (과거형)
- ❌ fix bug (소문자 시작)
- ❌ Update the documentation for the API endpoints. (마침표 포함)
- ❌ Request DTO 필드명 네이밍 컨벤션 통일 및 하위 호환성 지원 (너무 긺)

## PR 본문 작성

.github/PULL_REQUEST_TEMPLATE.md 템플릿을 따라 다음 섹션을 작성:
- **작업 배경**: 왜 이 작업이 필요한지
- **작업 내용**: 무엇을 어떻게 변경했는지 (상세하게)
- **참고 사항**: 리뷰어가 알아야 할 추가 정보

GitHub PR을 생성해주세요.
