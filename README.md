# ***REMOVED***-lifepuzzle-api



### 폴더 구조

- main.java.io.itmca.lifepuzzle
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
    
        
    
- main.resources : 어플리케이션 설정 파일
    - application.yml