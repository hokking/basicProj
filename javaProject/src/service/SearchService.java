package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.SearchDao;

public class SearchService {

	// 싱글톤 패턴
	private SearchService() {
	}

	private static SearchService instance;

	public static SearchService getInstance() {
		if (instance == null) {
			instance = new SearchService();
		}
		return instance;
	}

	private SearchDao searchDao = SearchDao.getInstance();
	Date day1 = new Date(); //체크인시 오늘날짜, 체크아웃시 체크인날짜 삽입
	Date day2 = new Date(); //체크인시 체크인날짜, 체크아웃시 체크아웃날짜 삽입
	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  //날짜 표시하기 위한 format선언

	// 지역정보와 인원, 날짜를 입력받아 숙소 리스트를 저장
	public int searchMain() {		
		Controller.sfeature = new ArrayList<>();  //선택한 편의시설 초기화
		Controller.rfeature = new ArrayList<>();  //선택한 방특성 초기화
		Controller.stayfeature = new ArrayList<>();  //출력을 위한 
		System.out
		.println("\n┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 조 회  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		System.out
				.println("┌────────────────────────────────────────────────────────┐");
		System.out
				.println("│ 1.서울\t 2.경기\t 3.충청\t 4.강원\t 5.전라\t 6.경상\t 7.제주      │");
		System.out
				.println("└────────────────────────────────────────────────────────┘");
		System.out.print("  · 지역을 선택해주세요 ▶ ");
		int input1 = ScanUtil.nextInt();
		Controller.regionLarge = input1;
		List<Map<String, Object>> regions = searchDao.selectRegionList(input1);
		System.out
				.println("┌────────────────────────────────────────────────────────┐");
		for (int i = 0; i < regions.size(); i++) {
			System.out.print("    " + (i + 1) + "."
					+ regions.get(i).get("REGION_NAME") + " ");
		}
		System.out.println();
		System.out
				.println("└────────────────────────────────────────────────────────┘");
		System.out.print("  · 지역을 선택해주세요 ▶ ");
		int input2 = ScanUtil.nextInt();
		System.out.print("  · 인원 수를 입력하세요 ▶ ");
		int peopleNum = ScanUtil.nextInt();
		Controller.peopleNum = peopleNum;
		int compare = 0;
		do {
			System.out.print("  · 체크인 날짜를 입력해주세요[YYYYMMDD] ▶ ");
			Controller.checkInDate = ScanUtil.nextLine();
			try {
				day2 = format.parse(Controller.checkInDate); //day2에 체크인 날짜를 삽입
			} catch (ParseException e) {
				e.printStackTrace();
			}
			compare = day1.compareTo(day2);  
			if (compare > 0) { 			//오늘날짜와 day2날짜를 비교하여 오늘보다 이전날짜면 재입력
				System.out.println("   ※ 날짜를 잘못입력하셨습니다 ※");
			}
		} while (compare > 0);
		do {
			System.out.print("  · 체크아웃 날짜를 입력해주세요[YYYYMMDD] ▶ ");
			Controller.checkOutDate = ScanUtil.nextLine();
			try {
				day1 = format.parse(Controller.checkInDate);    //day1에 입력했던 체크인 날짜 삽입
				day2 = format.parse(Controller.checkOutDate);    //day2에 체크아웃날짜 삽입
			} catch (ParseException e) {
				e.printStackTrace();
			}
			compare = day1.compareTo(day2);
			if (compare > 0) {       			//체크인 날짜보다 체크아웃날짜가 이전이면 재입력
				System.out.println("   ※ 날짜를 잘못입력하셨습니다 ※");
			}
		} while (compare > 0);
		int staynum = Integer.parseInt(String.valueOf(regions.get(input2 - 1).get("REGION_ID")));   //소분류 지역 삽입
		Controller.region = staynum;
		List<Map<String, Object>> stayList = searchDao.selectStayList();   //Dao에서 조건을 입력했던 숙소리스트를 stayList에 삽입
		Controller.stayfeature = stayList;
		return View.STAY_LIST;      //숙소리스트 출력하는 화면으로 리턴
	}

	// 검색 숙소 목록(출력)
	public int searchList() {
		System.out
				.println("┌────────────────────────────────────────────────────────┐");
		if (Controller.stayfeature.size() == 0) { // 숙소리스트가 비어있으면 출력
			System.out.println("   ※ 조건에 맞는 숙소가 없습니다 ※");
		}
		for (int i = 0; i < Controller.stayfeature.size(); i++) {
			Map<String, Object> score = searchDao.score(Controller.stayfeature
					.get(i).get("STAY_ID")); // 숙소별 평점 저장
			double sumScore = Double.parseDouble(String.valueOf(score
					.get("AREL"))) // 숙소별 평점 합계(위치, 청결, 친철)
					+ Double.parseDouble(String.valueOf(score.get("AREC")))
					+ Double.parseDouble(String.valueOf(score.get("AREK")));
			double avgScore = 0;
			if (sumScore != 0) {
				avgScore = Math.round(sumScore / 3 * 10) / 10.0; // 숙소별 전체평점 평균
			}
			if (Integer.parseInt(String.valueOf(Controller.stayfeature.get(i)
					.get("STAY_APPROVE"))) == 1) { // 승인된 숙소만 표시(STAY_APPROVE가
													// 1이면 승인된것)
				System.out.println("  "
						+ (i + 1)
						+ ". " // 각 숙소 출력(숙소명, 주소, 전체평점)
						+ Controller.stayfeature.get(i).get("STAY_NAME") + "\n"
						+ "     위치 : "
						+ Controller.stayfeature.get(i).get("STAY_ADDR1")
						+ "\t★" + avgScore);
				System.out
						.println("  ------------------------------------------------------");
			}
		} // 숙소리스트 출력 끝
		System.out.println("└────────────────────────────────────────────────────────┘");
		System.out.print("  1.숙소 선택 2.필터 적용 0.뒤로가기 ▶ ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			System.out.print("  · 숙소 번호를 적어주세요  ▶ ");
			int input2 = ScanUtil.nextInt();
			Controller.stayNum = input2;
			return View.STAY_DETAIL;  //숙소상세화면으로 리턴
		case 2:
			return View.SELECT_FILTER;  //필터를 적용하는 화면으로 리턴
		case 0:
			if(Controller.loginUser != null){
				return View.HOME_GUEST; //로그인되어있으면 게스트홈
			}
			return View.HOME;  //비회원일시 홈
		}
		return View.HOME;
	}

	// 숙소 상세 출력(숙소정보, 방개수 등)
	public int stayDetail() {

		List<Map<String, Object>> staydetail = searchDao.selectStayDetail(Controller.stayfeature.get(
						Controller.stayNum - 1).get("STAY_ID"));		//선택한 숙소의 정보 받기
		Map<String, Object> score = searchDao.score(Controller.stayfeature.get(
				Controller.stayNum - 1).get("STAY_ID"));				//평점 받기
		List<Map<String, Object>> convenient = searchDao
				.selectCon(Controller.stayfeature.get(Controller.stayNum - 1).get("STAY_ID"));	//편의시설 받기
		System.out
				.println("┌────────────────────────────────────────────────────────┐");
		System.out.println("   - 숙소 이름  ▷   "
				+ Controller.stayfeature.get(Controller.stayNum - 1).get(
						"STAY_NAME"));
		System.out.println("   - 숙소 상세주소  ▷   "
				+ Controller.stayfeature.get(Controller.stayNum - 1).get(
						"STAY_ADDR1")
				+ " "
				+ Controller.stayfeature.get(Controller.stayNum - 1).get(
						"STAY_ADDR2"));
		System.out.println("   - 공지사항  ▷   "
				+ Controller.stayfeature.get(Controller.stayNum - 1).get(
						"STAY_NOTICE"));
		System.out.println("   - 전화번호  ▷   "
				+ Controller.stayfeature.get(Controller.stayNum - 1).get(
						"STAY_TEL"));
		System.out.println("      ★ 위치평점 : " + score.get("AREL")
				+ "   ★ 청결평점 : " + score.get("AREC") + "   ★ 친절평점 : "
				+ score.get("AREK"));
		System.out.print("   - 편의시설  ▷   ");
		for (int i = 0; i < convenient.size(); i++) {
			System.out.print(convenient.get(i).get("FILTER_NAME"));
			if (i != convenient.size() - 1) {
				System.out.print(" / ");
			}
		}
		System.out
				.println("\n  ───────────────────────────────────────────────────────");
		for (int i = 0; i < staydetail.size(); i++) {
			System.out.print("  방번호 : " + (i + 1) + "\t방이름 : "
					+ staydetail.get(i).get("ROOM_NAME"));
			System.out.print("\t최대인원 : "
					+ staydetail.get(i).get("ROOM_NUM_PEOPLE") + "명");
			System.out.println("\t가격 : " + staydetail.get(i).get("ROOM_PRICE")
					+ " / 1박");
			System.out
					.println("  -------------------------------------------------------");
		}
		System.out
				.println("└────────────────────────────────────────────────────────┘");
		System.out.print("  1.방선택하기 2.리뷰보기 0.뒤로가기  ▶  ");
		int input2 = ScanUtil.nextInt();
		switch (input2) {
		case 1:
			System.out.print("  · 방번호를 입력해주세요  ▶  ");
			int input3 = ScanUtil.nextInt();
			Controller.stayId = Integer.parseInt(String
					.valueOf(Controller.stayfeature.get(Controller.stayNum - 1)
							.get("STAY_ID")));
			Controller.roomId = Integer.parseInt(String.valueOf(staydetail.get(
					input3 - 1).get("ROOM_ID")));
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			return View.RESERVE_INFO;		//예약화면으로 이동
		case 2:
			Controller.selectStay = Controller.stayfeature
					.get(Controller.stayNum - 1);
			return View.STAY_REVIEW;		//후기보는 화면으로 이동
		case 0:
			return View.STAY_LIST;		//숙소리스트 출력으로 이동
		}
		return View.STAY_DETAIL; 
	}

	// 필터선택하는 메서드
	public int selectFilter() {
		while (true) {
			System.out.println("┌────────────────────────────────────────────────────────┐");
			List<Map<String, Object>> filterlist = searchDao.selectFilter();		//필터종류 받기
			for (int i = 0; i < filterlist.size(); i++) {
				System.out.print("  " + (i + 1) + ". "
						+ filterlist.get(i).get("FILTER_NAME"));
				if (Integer.parseInt(String.valueOf(filterlist.get(i).get(		//편의시설 선택시 체크와 출력
						"FILTER_ID"))) < 2000) {
					for (int j = 0; j < Controller.sfeature.size(); j++) {
						if (i + 1001 == Integer.parseInt(String
								.valueOf(Controller.sfeature.get(j)))) {
							System.out.print("✔");
						}
					}
				}
				if (Integer.parseInt(String.valueOf(filterlist.get(i).get(		//방특성 선택시 체크와 출력
						"FILTER_ID"))) > 2000) {
					for (int j = 0; j < Controller.rfeature.size(); j++) {
						if (i + 1991 == Integer.parseInt(String
								.valueOf(Controller.rfeature.get(j)))) {
							System.out.print("✔");
						}
					}
				}
				System.out.print("\t\t");
				if (i % 2 == 1) {
					System.out.println();
				}
			}
			System.out.println("└────────────────────────────────────────────────────────┘");
			System.out.print("  · 원하는 필터를 선택해주세요[다 선택했으면 0을 눌러주세요] ▶ ");
			int input = ScanUtil.nextInt();
			int count = 0;
			if (input == 0) {
				break;
			} else if (input <= 10) {			//선택한 편의시설 삭제
				for (int i = 0; i < Controller.sfeature.size(); i++) {
					if (Controller.sfeature.get(i) == input + 1000) {
						Controller.sfeature.remove(i);
						count++;
					}
				}
				if (count == 0) {			//선택한 편의시설 삽입
					Controller.sfeature.add(input + 1000);
				}
			} else {					//선택한 방특성 삭제
				for (int i = 0; i < Controller.rfeature.size(); i++) {
					if (Controller.rfeature.get(i) == input + 1990) {
						Controller.rfeature.remove(i);
						count++;
					}
				}
				if (count == 0) {			//선택한 방특성 삽입
					Controller.rfeature.add(input + 1990);
				}
			}
		}
		return View.SEARCH_FILTER;		//선택된 필터내용 숙소에 적용하는 화면으로 이동
	}

	// 필터를 적용하여 숙소리스트 추출(출력X)
	public int searchFilter() {
		List<Map<String, Object>> stayList = searchDao.selectStayList();
		List<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		if (Controller.rfeature.size() == 0) {					//편의시설만 선택했을때 숙소 리스트
			for (int i = 0; i < Controller.sfeature.size(); i++) {
				for (int j = 0; j < stayList.size(); j++) {
					if (searchDao.selectStayFilter(
							stayList.get(j).get("STAY_ID"),
							Controller.sfeature.get(i)) != null) {
						arraylist.add(searchDao.selectStayFilter(stayList
								.get(j).get("STAY_ID"), Controller.sfeature
								.get(i)));
					}
				}
				stayList = arraylist;
				arraylist = new ArrayList<Map<String, Object>>();
			}
		} else if (Controller.sfeature.size() == 0) {				//방특성만 선택했을때 숙소 리스트
			for (int i = 0; i < Controller.rfeature.size(); i++) {
				for (int j = 0; j < stayList.size(); j++) {
					if (searchDao.selectRoomFilter(
							stayList.get(j).get("STAY_ID"),
							Controller.rfeature.get(i)) != null) {
						arraylist.add(searchDao.selectRoomFilter(stayList
								.get(j).get("STAY_ID"), Controller.rfeature
								.get(i)));
					}
				}
				stayList = arraylist;
				arraylist = new ArrayList<Map<String, Object>>();
			}
		} else {								//편의시설과 방특성이 모두 선택했을때 숙소 리스트
			for (int i = 0; i < Controller.sfeature.size(); i++) {
				for (int k = 0; k < Controller.rfeature.size(); k++) {
					for (int j = 0; j < stayList.size(); j++) {
						if (searchDao.selectAllFilter(
								stayList.get(j).get("STAY_ID"),
								Controller.sfeature.get(i),
								Controller.rfeature.get(k)) != null) {
							arraylist.add(searchDao.selectAllFilter(stayList
									.get(j).get("STAY_ID"), Controller.sfeature
									.get(i), Controller.rfeature.get(k)));
						}
					}
					stayList = arraylist;
					arraylist = new ArrayList<Map<String, Object>>();
				}
			}
		}
		Controller.stayfeature = stayList;	//숙소리스트 저장
		return View.STAY_LIST;  		//숙소리스트 출력하는 화면으로 리턴
	}
}