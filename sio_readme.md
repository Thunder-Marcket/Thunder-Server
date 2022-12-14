
## 22.08.20 
- 서버 구축
  - EC2 인스턴스 생성, dev/prod 서브도메인 적용
- ERD 설계 시작

## 22.08.21
- 서버 구축
  - dev/prod https 적용 중 문제 생김
  - ERD 설계 완료

## 22.08.22
- 서버 구축
  - dev/prod https 적용 문제 해결
  - API 명세서 작성 시작
  - 전화번호로 회원가입 API 구현
  
## 22.08.23
- github로 배포 중 충돌 문제 발생
  - build, .idea, .gradle, log 파일들 .gitignore에 적용하고 sio 브랜치로 업로드 후 빌드 성공
  - master 브랜치 정상화 
  - 찜 목록 조회 API 구현

## 22.08.24
- 최근 본 상품 API 구현
- 최근 검색어 조회 API 구현
- 전화번호로 로그인 API 구현
- 프로필 편집 API 구현
- 마이페이지 유저 정보 조회 API 구현

## 22.08.25
- 유저 탈퇴하기 API 구현
- 결제 수단 등록 API 구현
- 결제 수단 조회 API 구현
- 할부 설정 API 구현
- 결제 수단 수정 API 구현
- 최근 검색어 조회 API 수정
  - 추천 브랜드의 브랜드 게시물 수 response 추가

### 고려해 볼 것
  구현 속도가 괜찮은 것 같은데 이미지 처리를 S3로 시도해볼까

## 22.08.26
- 최근 본 상품 삭제 API 구현
  - 선택해서 배열로 요청을 보내는데 postman에서 list로 요청을 보내본 적이 없어서 이번에 알아봄
  - id만 있는 객체를 만들어서 이것으로 List 만듦 -> Integer 리스트는 어떻게 요청을 보내야 하는지, 위의 방법과 Integer로 보내는 방식 중 뭐가 더 나은지 나중에 질문
  - List로 요청을 보낼 때 jdbcTemplate에서 어떻게 파라미터로 받아야 할지 모르겠음, 일단 service에서 for문 돌려서 update 쿼리 날려서 구현
- log 한번 적용해봄(slf4j에 대해 찾아봄)
- 최근 검색어 삭제하기(1개) API 구현
- 최근 검색어 삭제하기(전체) API 구현

## 22.08.27
- 프론트 요청에 따라 결제 수단 관련 API 4개 수정

## 22.08.28
- S3에 대해 알아봄

## 22.08.29
- 상품 상세 조회 시 최근 본 상품 생성하는 기능 추가

## 22.08.30
- 상품 찜/찜 해제 API 구현
- 내 팔로잉 조회 API 구현
- 내 팔로워 조회 API 구현
- 브랜드 팔로우/팔로우 해제 API 구현
- 유저 검색 API 구현
- 신고하기 API 구현

## 22.08.31
- DB에 카테고리 데이터 저장
- 카테고리에 해당하는 상품리스트 조회 API 구현

## 22.09.01
- 카카오 로그인 API 구현





