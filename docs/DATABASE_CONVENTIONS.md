# Database Conventions

LifePuzzle 프로젝트의 데이터베이스 스키마 및 마이그레이션 작성 규칙

---

## 테이블 명명 규칙

- **소문자 + 언더스코어(_)**: `user`, `story_gallery`
- **단수형 사용**: `user` (O), `users` (X)
- **명확하고 간결한 이름**

```sql
-- Good
CREATE TABLE `user` (...);
CREATE TABLE `story_gallery` (...);
CREATE TABLE `content_like` (...);

-- Bad
CREATE TABLE `users` (...);  -- 복수형
CREATE TABLE `storyGallery` (...);  -- camelCase
```

---

## 컬럼 명명 규칙

- **소문자 + 언더스코어(_)**: `user_id`, `created_at`
- **타입 접미사 지양**: `user_id` (O), `user_id_number` (X)

### 공통 컬럼

```sql
-- Primary Key
`id` bigint NOT NULL AUTO_INCREMENT

-- Timestamp
`created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
`updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
`deleted_at` datetime DEFAULT NULL  -- soft delete

-- Foreign Key (참조용, 제약조건 미사용)
`user_id` bigint NOT NULL
`hero_id` bigint NOT NULL
```

---

## 인덱스 명명 규칙

### 형식

```
{타입}_{테이블명}_{컬럼명들}
```

### 타입별 접두사

| 타입 | 접두사 | 예시 |
|------|--------|------|
| 일반 인덱스 | `idx_` | `idx_comment_story_id` |
| UNIQUE 제약 | `uk_` | `uk_user_email` |

### 단일 컬럼 인덱스

```sql
KEY `idx_comment_story_id` (`story_id`)
KEY `idx_comment_writer_id` (`writer_id`)
```

### 복합 컬럼 인덱스

```sql
-- 검색 조건 순서대로 컬럼 나열
KEY `idx_gallery_hero_age_created` (`hero_id`, `age_group`, `created_at` DESC)
KEY `idx_story_writer_created` (`writer_id`, `created_at` DESC)
```

### UNIQUE 제약조건

```sql
UNIQUE KEY `uk_user_email` (`email`)
UNIQUE KEY `uk_content_like_user_type_content` (`user_id`, `type`, `content_id`)
```

---

## 제약조건

### PRIMARY KEY
```sql
PRIMARY KEY (`id`)
PRIMARY KEY (`story_id`, `gallery_id`)  -- 복합 PK
```

### UNIQUE KEY
```sql
UNIQUE KEY `uk_user_login_id` (`login_id`)
```

### Foreign Key
- **사용하지 않음** (운영 복잡도 상 애플리케이션 레벨에서 관리)

---

## 마이그레이션 작성 규칙

### 파일명

```
V{버전}__{설명}.sql
```

예시:
- `V1__Initial_schema.sql`
- `V2__add_audio_duration_to_story.sql`

### DDL 작성 규칙

#### 1. 타입 일관성
```sql
-- datetime 사용 (timestamp X)
`created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP

-- varchar 사이즈는 여유있게
`email` varchar(256)
`nick_name` varchar(64)
```

#### 2. COMMENT 필수
```sql
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '회원 ID',
  `email` varchar(256) NOT NULL COMMENT '이메일',
  ...
) COMMENT='회원 정보';
```

#### 3. 문자셋 및 Collation
```sql
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
```

#### 4. NULL 처리
```sql
`email` varchar(256) NOT NULL  -- 필수
`birthday` date DEFAULT NULL   -- 선택
```

---

## 체크리스트

- [ ] 테이블명이 단수형인가?
- [ ] 소문자 + 언더스코어를 사용했는가?
- [ ] 인덱스명이 규칙(`idx_`, `uk_`)을 따르는가?
- [ ] 모든 테이블/컬럼에 COMMENT가 있는가?
- [ ] datetime 타입을 사용했는가?
- [ ] varchar 사이즈가 여유있게 설정되었는가?
- [ ] NULL 가능 여부가 명시되었는가?

---

## 관련 문서

- [Git Workflow](./GIT_WORKFLOW.md)
- [Versioning](./VERSIONING.md)
