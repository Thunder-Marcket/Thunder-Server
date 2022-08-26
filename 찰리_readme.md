https://docs.google.com/spreadsheets/d/1QCRShw8Av8n6jFE01VDI8voo74a2yPCs_GiTj1FAzQY/edit#gid=0

## 22.08.20
  - ERD 설계 50%
  - dev, prod 폴더 나눠서 서브 도메인 적용
  - prod 폴더에 스프링 템플릿 적용

## 22.08.21
  - ERD 설계 100%
  - API index 작성

## 22.08.22
  - 상품 목록 가져오는 API 개발
  - 서버 이슈(깃헙 + https 적용 문제)
  - https 적용 문제 해결

## 22.08.23
  - 검색어로 상품 목록 가져오는 API 개발
  - 상품 상세 정보를 가져오는 API 개발

## 22.08.24
  - API 명세서 중 8-1, 10-3, 4-1, 4-2 개발
  - 2-4, 2-6 API를 클라이언트 요청에 따라 변경(상품 수량 type을 int 에서 String으로 변경 등)

## 22.08.25
  - API 명세서 중 8-2, 8-3, 8-4, 12-1, 12-2 개발
  - 개발 도중 이슈 사항
    - queryForObject는 결과 값이 null이거나 2개 이상이면 IncorrectResultSizeDataAccessException 예외를 발생시킨다.
    - 쿼리 문의 결과 칼럼 이름과 jdbcTemplate의 rs.getString 등의 columnLabel 이름이 같아야 한다...

## 22.08.26
  - API 명세서 중 12-3, 12-4, 12-5 개발
  - 이미지 처리 방식을 모르겠다... 어떻게 할지 감이 안잡힌다...

## 1차 피드백 
  - 환경 이슈 빠르게 해결할 것
  - API 인당 20개 정도는 만들어야 할 것
