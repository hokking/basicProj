package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import dao.AdminDao;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class AdminService {

	JDBCUtil jdbc = JDBCUtil.getInstance();
	AdminDao adminDao = AdminDao.getinstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	// 싱글톤 패턴
	private AdminService() {
	}

	private static AdminService instance;

	public static AdminService getinstance() {
		if (instance == null) {
			instance = new AdminService();
		}
		return instance;
	}

	// 전체회원 조회
	public int memberList() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━┫ 전체회원조회  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.print("   1.개인회원 List 2.호스트회원 List  ▶  ");
		int input = ScanUtil.nextInt();
		List<Map<String, Object>> member = adminDao.memberSelect(input);
		for (int i = 0; i < member.size(); i++) {
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("    회원 ID : " + member.get(i).get("MEM_ID")
					+ "\t회원이름 : " + member.get(i).get("MEM_NAME")
					+ "\n    생년월일 : "
					+ format.format(member.get(i).get("MEM_BIRTH"))
					+ "\t전화번호 : " + member.get(i).get("MEM_PH") + "\t마일리지 : "
					+ member.get(i).get("MEM_MILE"));
		}
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.HOME_ADMIN; //관리자 홈화면으로 이동
	}

	// 숙소 목록 조회
	public int stayList() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소목록조회  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		List<Map<String, Object>> staylist = adminDao.selectStayList();
		for (int i = 0; i < staylist.size(); i++) {
			System.out.println("    숙소명 : " + staylist.get(i).get("STAY_NAME")
					+ "\n" + "    숙소주소 : " + staylist.get(i).get("ADDR")
					+ "\n    전화번호 : " + staylist.get(i).get("STAY_TEL")
					+ "\n    호스트 아이디 : " + staylist.get(i).get("MEM_ID"));
			System.out
					.println("   ---------------------------------------------------------");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.HOME_ADMIN; //관리자 홈화면으로 이동
	}

	// 숙소 등록요청
	public int requestStay() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소등록  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out
				.println("  ┌───────────────────── 기본 숙소정보 입력  ────────────────────┐");

		System.out
				.println("   · 등록하실 숙소의 지역을 선택해주세요"
						+ "\n   [ 1.서울      2.경기      3.충청      4.강원      5.전라      6.경상      7.제주 ]");
		System.out.print("      입력  ▶  ");
		int region = ScanUtil.nextInt();
		int region2 = region;
		String memId = (String) Controller.loginUser.get("MEM_ID");
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 숙소의 상세 위치를 선택해주세요");
		int sregion = 0;
		switch (region) {
		case 1:
			System.out
					.println("   [ 1.강남      2.마포      3.종로      4.송파      5.서초      6.강북      7.강동 ]");
			System.out.print("      입력  ▶  ");
			int input = ScanUtil.nextInt();
			sregion = input + 7;
			break;
		case 2:
			System.out
					.println("   [ 1.가평      2.파주      3.화성      4.안성      5.수원      6.용인      7.고양 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 14;
			break;
		case 3:
			System.out
					.println("   [ 1.대전      2.태안      3.공주      4.보령      5.청주      6.충주      7.세종 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 21;
			break;
		case 4:
			System.out
					.println("   [ 1.강릉      2.속초      3.동해      4.삼척      5.춘천      6.원주      7.태백 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 28;
			break;
		case 5:
			System.out
					.println("   [ 1.광주      2.전주      3.여수      4.목포      5.보성      6.순천 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 35;
			break;
		case 6:
			System.out
					.println("   [ 1.부산      2.대구      3.울산      4.포항      5.경주      6.울릉 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 41;
			break;
		case 7:
			System.out.println("   [ 1.제주      2.서귀포 ]");
			System.out.print("      입력  ▶  ");
			input = ScanUtil.nextInt();
			sregion = input + 47;
			break;
		}
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 사용하실 숙소의 이름을 입력해주세요");
		System.out.print("      입력  ▶  ");
		String stayName = ScanUtil.nextLine();
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 사업자 등록번호를 입력해주세요");
		System.out.print("      입력  ▶  ");
		String company = ScanUtil.nextLine();
		String exprcn = "\\d{3}-\\d{2}-\\d{5}"; // 사업자 등록번호의 정규표현식
		Pattern pi = Pattern.compile(exprcn);
		Matcher mi = pi.matcher(company); // 정규표현식에 위배하지 않는지 확인
		boolean mm = mi.matches();
		if ( mm == true) {
			System.out.println("    - 사용가능한 사업자 등록번호입니다  -");
		} else{
			System.out.println("    ※ 사용할 수 없는 사업자 등록번호입니다  ※");
			return View.HOME_HOST;
		}
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 숙소의 주소를 입력해주세요[상세주소는 따로 입력]");
		System.out.print("      입력  ▶  ");
		String addr1 = ScanUtil.nextLine();
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 숙소의 상세주소를 입력해주세요");
		System.out.print("      입력  ▶  ");
		String addr2 = ScanUtil.nextLine();
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 숙소의 공지사항을 입력해주세요[생략시 엔터]");
		System.out.print("      입력  ▶  ");
		String notice = ScanUtil.nextLine();
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 숙소의 전화번호를 입력해주세요");
		System.out.print("      입력  ▶  ");
		String tel = ScanUtil.nextLine();
		String exprph = "^0\\d{1,3}-\\d{3,4}-\\d{4}"; // 전화번호의 정규표현식
		pi = Pattern.compile(exprph);
		mi = pi.matcher(tel); // 정규표현식에 위배하지 않는지 확인
		mm = mi.matches();
		if ( mm == true) {
			System.out.println("    - 사용가능한 전화번호입니다  -");
		} else{
			System.out.println("    ※ 사용할 수 없는 전화번호입니다  ※");
			return View.HOME_HOST;
		}
		int result = adminDao.insertStay(region, stayName, company, addr1,
				addr2, memId, sregion, notice, tel);
		System.out
				.println("  └────────────────────────────────────────────────────────┘\n");
		if (result > 0) {

			Map<String, Object> stay = adminDao.requestDetail(tel);
			Controller.selectStay = stay;
			int stayId = ((BigDecimal) stay.get("STAY_ID")).intValue();
			System.out
					.println("  ┌───────────────────── 상세 숙소정보 입력  ────────────────────┐");
			System.out.println("   · 숙소의 편의시설을 입력해주세요[다 고르시면 0번을 입력해주세요]");
			System.out
					.println("   ┌───────────────────────────────────────────────────────┐");
			System.out
					.println("   │ 1.바베큐        2.수영장        3.헬스장           4.무료주자창      5.테라스    │");
			System.out
					.println("   │ 6.라운지        7.셔틀           8.반려동물허용  9.카페              10.레스토랑│");
			System.out
					.println("   └───────────────────────────────────────────────────────┘");

			while (true) {
				System.out.print("      입력  ▶  ");
				int input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					int conven = adminDao.insertConv(stayId, input + 1000);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			while (true) {
				System.out.println("   · 숙소에 있는 방 정보를 입력해주세요."
						+ "\n    [더이상 입력할 방이 없다면 0번을 입력해주세요]");
				System.out.println("   · 방의 이름을 입력해주세요.");
				System.out.print("      입력  ▶  ");
				String name = ScanUtil.nextLine();
				System.out
						.println("   ---------------------------------------------------------");
				if (name.equals("0")) {
					break;
				} else {
					System.out.println("   · 방의 최대인원을 입력해주세요");
					System.out.print("      입력  ▶  ");
					int num = ScanUtil.nextInt();
					System.out
							.println("   ---------------------------------------------------------");
					System.out.println("   · 방의 가격을 입력해주세요[숫자만 입력]");
					System.out.print("      입력  ▶  ");
					int price = ScanUtil.nextInt();
					System.out
							.println("   ---------------------------------------------------------");
					int room = adminDao.insertRoom(price, num, stayId, name);
				}
			}
			System.out.println("   · 방 특성을 추가해주세요");
			List<Map<String, Object>> roomList = adminDao.roomLists(stayId);
			int input = 0;
			System.out.println("   · 오션뷰가 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");

			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1) + "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2004);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("   · 시티뷰가 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");

			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1)+ "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2002);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("   · 욕조가 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");

			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1)+ "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2003);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("   · 주방이 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");

			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1)+ "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2001);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("   · 더블 침대가 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");

			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1) + "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2005);
				}
			}
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println("   · 트윈 침대가 있는 방을 선택해주세요[없다면 0번을 입력해주세요]");
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("   "+(i + 1)+ "." + roomList.get(i).get("ROOM_NAME")
						+ "\t");
			}
			System.out.println();
			for (int i = 0; i < roomList.size(); i++) {
				System.out.print("      입력  ▶  ");
				input = ScanUtil.nextInt();
				if (input == 0) {
					break;
				} else {
					adminDao.insertFeature(
							roomList.get(input - 1).get("ROOM_NAME"), stayId,
							2006);
				}
			}
			System.out
					.println("  └────────────────────────────────────────────────────────┘\n");
			System.out
					.println("  ── 숙소 등록 요청이 완료되었습니다. 관리자 승인 후 예약이 가능해집니다 ──");
		} else {
			System.out
					.println("  └────────────────────────────────────────────────────────┘\n");
			System.out
					.println("  ───────────── 숙소 기본정보 등록에 실패했습니다 ────────────────");
		}
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.HOME_HOST; //호스트 홈화면으로 이동
	}

	// 관리자 - 미승인 숙소 목록
	public int approve() {
		List<Map<String, Object>> nonList = adminDao.nonApprove();
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━┫ 미승인 숙소목록  ┣━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("     숙소아이디\t호스트아이디\t숙소이름\t숙소위치");
		System.out
				.println("\n   ─────────────────────────────────────────────────────────");
		for (Map<String, Object> non : nonList) {

			System.out.println("    "+non.get("STAY_ID") + "\t" + non.get("MEM_ID")
					+ "\t" + non.get("STAY_NAME") + "\t"
					+ non.get("STAY_ADDR1") + " " + non.get("STAY_ADDR2"));
			System.out
					.println("   ---------------------------------------------------------");

		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("    1.숙소정보 보기  0.홈 화면으로 돌아가기  ▶  ");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.DETAIL_CHECK; //미승인 숙소 상세로 이동
		case 0:
			return View.HOME_ADMIN; //관리자 홈화면으로 이동
		}
		return View.HOME_ADMIN; //관리자 홈화면으로 이동
	}

	// 관리자 - 미승인 숙소 상세정보
	public int appDetail() {

		System.out.print("   · 상세 정보를 확인할 숙소의 아이디를 입력해주세요  ▶  ");
		int stayId = ScanUtil.nextInt();
		Map<String, Object> stay = adminDao.naStay(stayId);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━┫ 미승인 숙소 상세 정보  ┣━━━━━━━━━━━━━━━━━━━━━┓\n");
		System.out.println("     숙소 이름  ▷   " + stay.get("STAY_NAME"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out
				.println("     사업자 등록번호  ▷   " + stay.get("STAY_COMPANY_NUM"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("     주소  ▷   " + stay.get("STAY_ADDR1") + " "
				+ stay.get("STAY_ADDR2"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("     호스트 아이디  ▷   " + stay.get("MEM_ID"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("     공지사항  ▷   " + stay.get("STAY_NOTICE"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("     전화번호  ▷   " + stay.get("STAY_TEL"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("       [숙소 편의시설]");
		List<Map<String, Object>> conv = adminDao.naCon(stayId);
		for (int i = 0; i < conv.size(); i++) {
			System.out.print("    " + (i + 1) + "."
					+ conv.get(i).get("FILTER_NAME") + "  ");
			if (conv.size() % 2 == 1) {
				System.out.println();
			}
		}
		System.out
				.println("   ─────────────────────────────────────────────────────────");
		List<Map<String, Object>> rNum = adminDao.rNoList(stayId); //방아이디 목록
		List<Map<String, Object>> rList = adminDao.roomLists(stayId); //전체 방 목록
		System.out.println("       [방 정보]");
		for (int i = 0; i < rList.size(); i++) {
			System.out.println("     " + (i + 1) + "."
					+ rList.get(i).get("ROOM_NAME"));
			System.out.println("      가격 : " + rList.get(i).get("ROOM_PRICE")
					+ "\n      인원 : " + rList.get(i).get("ROOM_NUM_PEOPLE"));
			List<Map<String, Object>> rFeature = adminDao.naRf(rList.get(i)
					.get("ROOM_ID"));
			for (int j = 0; j < rFeature.size(); j++) {
				System.out.println("   (" + (j + 1) + ")"
						+ rFeature.get(j).get("FN"));
			}
			System.out
					.println("   ---------------------------------------------------------");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		
		System.out.print("   · 승인하시겠습니까?[Y|N]  ▶  ");
		String input = ScanUtil.nextLine();
		int result = 0;
		if (input.equals("Y") || input.equals("y")) {
			result = adminDao.apYes(stayId);
			if (result > 0) {
				System.out.println("  ─────────────────────── 승인되었습니다 ───────────────────────");
			}
		} else {
			result = adminDao.apNo(stayId);
			if (result > 0) {
				System.out.println("  ───────────────────── 승인을 거절했습니다 ─────────────────────");
			}
		}
		return View.STAY_CHECK; //미승인 숙소 목록으로 이동
	}

}
