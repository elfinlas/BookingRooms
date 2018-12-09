# What is BookingRooms?

---

* 회의실 예약 웹 어플리케이션
* 취미로 만든 토이 프로젝트여서 기능이 많이 부실합니다.
* 점진적으로 기능 추가 및 업그레이드 예정입니다.

---

## 사용 기술

* 언어 : Java 1.8
* Spring boot 2.0.6
* JPA
* Lombok 
* Thymelef
* ModelMapper
* H2
* MariaDB
* [AdminLTE](https://adminlte.io)
* Bootstrap
* jQuery


---

## 기능 

* 회의
   * 전체 회의 조회
   * 월간 회의 조화
* 회의실
   * 회의실 별 사용 현황
* 관리자
	* 회의실 관리
		* 전체 조회, 등록, 수정, 삭제
	* 사용자 관리
		* 전체 조회, 사용자 정보 변경, 비밀번호 초기화, 회원 삭제 
* 로그인 및 회원 가입

--- 

## 사용법

* **가급적 IntelliJ** 환경에서 하실 것을 추천 드립니다.
* 기본적으로 H2를 사용하며, 필요 시 MariaDB 등을 활용할 수 있습니다.
* 기타 자세한 사용법은 Wiki를 참고해주세요.

1. 코드를 다운받고 IntelliJ에서 프로젝트를 불러옵니다.
2. build.gradle에 선언된 라이브러리를 import 해줍니다.
3. Lombok 설정을 진행합니다.
4. Spring boot를 실행하여 확인합니다.

--- 

## 기타

* 제작자 : MHLab (elfinlas@gmail.com)
* 기능 추가 도입 및 기타 문의사항은 메일 문의 주세요.
* FE 부분은 역량이 부족하여 CodePen 등에서 가져와서 사용하였습니다.
	* Login 및 회원가입 화면 (https://codepen.io/bermudaquest/pen/EEvGgW) 

--- 

## 스크린샷

[](https://github.com/elfinlas/BookingRooms/blob/master/git_res/img/login.png?raw=true)
[](https://github.com/elfinlas/BookingRooms/blob/master/git_res/img/main_01.png?raw=true)
