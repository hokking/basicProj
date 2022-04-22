package util;

public interface View {

	int HOME = 0;             //조회 로그인 회원가입
	   int LOGIN = 1;
	   int JOIN = 2;
	   int HOME_GUEST = 3;  
	   int HOME_HOST = 4;
	   int HOME_ADMIN = 5;
	   int SEARCH = 6;         //검색화면(호스트제외)
	   int STAY_LIST = 7;         //검색 숙소 리스트
	   int STAY_DETAIL = 8;     //검색 후 선택 숙소 내용
	   int STAY_REVIEW = 9;      //숙소의 리뷰
	   int RESERVE_INFO = 10;     //예약 화면
	   int PAY = 11;            //결제 화면
	   int INFO_MEMBER = 12;      //회원 정보
	   int MEMBER_MODIFY = 13;     //회원 정보 수정 
	   int RESERVE_LIST = 14;      //게스트 예약 내역
	   int RESERVE_DETAIL = 15;  //게스트 예약 상세
	   int RESERVE_DELETE = 16;  //게스트 예약 취소
	   int RIVIEW_WRITE = 17;    //게스트  후기 작성
	   int HOST_LIST = 18;        //호스트의 숙소 목록
	   int HOST_DETAIL = 19;     //호스트의 숙소 상세
	   int HOST_MODIFY = 20;     //호스트 숙소 정보 수정
	   int HOST_REQUEST = 21;    //호스트 숙소 등록 요청
	   int RE_REVIEW = 22;      //호스트 후기 답글
	   int HOST_RESERVE = 23;    //호스트 숙소의 예약 내역
	   int MEMBER_READ = 24;     //관리자 회원 조회
	   int STAY_CHECK = 25;      //관리자 숙소 승인
	   int SEARCH_FILTER = 26;   //검색필터화면
	   int SELECT_FILTER = 27;     //필터 적용한 목록
	   int DETAIL_CHECK = 28;      //미승인 숙소 상세정보
	   int STAY_ADMIN_LIST = 29; //관리자숙소 목록 조회
	
}
