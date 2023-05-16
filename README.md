# lifepuzzle-api

### Project Structure
- domain
    - hero
        - endpoint : 외부 요청을 받는 controller 폴더
            - response : 외부 요청에 대한 응답 데이터[DTO] 관련 클래스 폴더
                - HeroResponse.java
            - request : 외부 요청에 대한 요청 데이터[DTO] 관련 클래스 폴더
                - HeroRequest.java
            - HeroQueryEndpoint.java : [Controller] [예시]
        - entity : 엔티티 클래스 
            - Hero.java 
        - respository : JPA를 통한 DB 데이터 조작 클래스 폴더
        - service : 비즈니스 로직 클래스 폴더
    - question
    ...
        
  - global
      - config : 어플리케이션 전체적으로 영향을 주는 설정 폴더
      - exception : 예외 핸들러 및 커스텀 예외 정의 폴더
          - handler
              - AuthException.java [추상 클래스] [예시]
              - CustomExceptionHandler.java
          - UserNotAccessibleException.java [구체 클레스] [예시]
      - infra : 공용으로 사용되는 유틸 폴더
          - file
          - image 
    
  - LifePuzzleApplication.java

### Versioning

[CalVer](https://calver.org/)을 따릅니다.

Format: `YYYY.MM_DeployNumber`
- `YYYY.MM`: 배포 연월
- `DeployNumber`: 해당 월의 몇번째 배포인지 표시

예시
- `2023.04_1`: 2023년 4월의 첫번째 배포
- `2023.04_5`: 2023년 4월의 다섯번째 배포

### Branch Name, Commit Msg Format

- Branch Name: `<type>/<subject>`
- Commit Msg: `<type>: <subject>`

type 설명

- feat: 새로운 기능 추가/수정/삭제
- fix: 버그 수정
- hotfix: 운영 환경 대상 긴급 버그 수정
- refactor: 리팩토링
- test: 테스트 코드 작성
- build: dependency 추가/수정/삭제
- docs: 문서 수정
- style: 코드 포맷, 스타일 수정
- chore: 위 타입들에 해당하지 않는 기타 작업

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
     