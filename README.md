## 클로버추얼패션 사전과제
### 필수 구현 대상 API
- 직원 목록 조회 API
  - GET : /api/employee?page=0&pageSize=10 
  - 페이징처리 포함
- 직원 단건 조회 API
  - GET : /api/employee/{name}
  - 대상 존재하지 않을 시 404 에러처리
- 직원 정보 생성 API
  - POST : /api/employee
  - file과 text 정보가 공존 할 경우 실제 사용해야 하는 값을 판단하기 어려우므로 에러처리
  - 데이터 파싱 실패시 에러처리
  - All or Nothing 방식으로 전체 트랜젝션 처리
  - email 값을 UK로 사용
 
### 기타
- 테스트케이스 추가
  - 파싱로직에 대한 기능테스트 및 MockMVC 사용한 컨트롤러 테스트
- 로깅 추가
  - AOP사용한 전체 API request param, response body 로그.
  - 과제의 범위 안에서는 별도의 logback 설정은 하지 않고 slf4j 사용한 출력만 구현.
- swagger 추가.
  - http://localhost:8080/swagger-ui/index.html
