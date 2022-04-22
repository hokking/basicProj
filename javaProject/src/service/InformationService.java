package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;
import dao.InformationDao;

public class InformationService {

	// 싱글톤 패턴
	private InformationService() {
	}

	private static InformationService instance;

	public static InformationService getinstance() {
		if (instance == null) {
			instance = new InformationService();
		}
		return instance;
	}

	private InformationDao informationDao = InformationDao.getinstance();
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");

	// 회원정보 조회
	public int memInformation() {
		String memId = (String) Controller.loginUser.get("MEM_ID"); 
		String type = (String) Controller.loginUser.get("MEM_TYPE"); 
		Map<String, Object> information = informationDao.readMemInfo(memId, //현재 로그인된 회원아이디와 타입으로 로그인 정보 불러오기
				type);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 회원정보  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("     아이디  ▷   " + information.get("MEM_ID"));
		String pass = (String) information.get("MEM_PASS"); // 비밀번호 개수만큼 *출력
		System.out.print("     비밀번호  ▷   ");
		for (int i = 0; i < pass.length(); i++) {
			System.out.print("*");
		}
		System.out.println("\n     이름  ▷   " + information.get("MEM_NAME"));
		System.out.println("     생년월일  ▷   "
				+ format.format(information.get("MEM_BIRTH")));
		System.out.println("     전화번호  ▷   " + information.get("MEM_PH"));
		if (information.get("MEM_TYPE").equals("1")) { 						// 게스트일 경우
			System.out.println("     마일리지  ▷   " + information.get("MILE")); // 마일리지 출력
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.print("   1.회원정보 수정 2.예약내역 조회 0.뒤로가기 ▶ "); // 게스트의 정보 메뉴창

			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.MEMBER_MODIFY; //회원정보 수정으로 이동
			case 2:
				return View.RESERVE_LIST; //회원 예약 내역으로 이동(현재까지 예약한 모든 내역 확인 가능)
			case 0:
				return View.HOME_GUEST; //게스트 홈화면으로 이동
			}

			return View.HOME_GUEST;

		} else { // 호스트의 경우 - 마일리지 출력x
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.print("   1.회원정보 수정 2.등록 숙소 조회 0.뒤로가기 ▶ "); // 호스트의 정보 메뉴창

			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.MEMBER_MODIFY; //회원정보 수정으로 이동
			case 2:
				return View.HOST_LIST; //호스트의 숙소 목록으로 이동(미승인 숙소 포함)
			case 0:
				return View.HOME_HOST; //호스트 홈화면으로 이동
			}

			return View.HOME_HOST;
		}
	}

	// 회원정보 수정
	public int InfoUpdate() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━┫ 회원정보수정  ┣━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		System.out.println("   · 수정할 비밀번호를 입력하세요[수정하지 않으려면 0번을 누르세요]");
		System.out.print("    입력 ▶ ");
		String pass = ScanUtil.nextLine(); // 수정할 비밀번호
		String exprpass = "[a-zA-Z0-9-_!@#$%^*+=]{6,20}"; // 비밀번호의 정규표현식
		Pattern pi = Pattern.compile(exprpass);
		Matcher mi = pi.matcher(pass); // 정규표현식에 위배하지 않는지 확인
		boolean mm = mi.matches();
		if (pass.equals("0")) {
			pass = (String) Controller.loginUser.get("MEM_PASS");
		} else if (!pass.equals("0") && mm == true) {
			System.out.println("    - 사용가능한 비밀번호입니다  -");
		} else {
			System.out.println("    ※ 사용할 수 없는 비밀번호입니다  ※");
			return View.INFO_MEMBER; //회원정보로 이동
		}
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   · 수정할 전화번호를 입력하세요[수정하지 않으려면 0번을 누르세요]");
		System.out.print("    입력 ▶ ");
		String ph = ScanUtil.nextLine(); // 수정할 전화번호
		String exprph = "^0\\d{1,3}-\\d{3,4}-\\d{4}"; // 전화번호의 정규표현식
		pi = Pattern.compile(exprph);
		mi = pi.matcher(ph); // 정규표현식에 위배하지 않는지 확인
		mm = mi.matches();
		if (ph.equals("0")) {
			ph = (String) Controller.loginUser.get("MEM_PH");
		} else if (!ph.equals("0") && mm == true) {
			System.out.println("    - 사용가능한 전화번호입니다  -");
		} else {
			System.out.println("    ※ 사용할 수 없는 전화번호입니다  ※");
			return View.INFO_MEMBER; //회원정보로 이동
		}
		String memId = (String) Controller.loginUser.get("MEM_ID");
		int result = informationDao.updateMemInfo(pass, ph, memId);

		if (result > 0) {
			System.out
					.println("  ──────────────────── 회원정보가 수정되었습니다 ────────────────────");
		} else {
			System.out
					.println("  ─────────────────── 회원정보 수정에 실패하였습니다 ──────────────────");
		}

		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.INFO_MEMBER; //회원정보로 이동
	}

	// 호스트의 숙소 목록
	public int hostList() {
		String memId = (String) Controller.loginUser.get("MEM_ID");
		List<Map<String, Object>> stayList = informationDao.hostStayList(memId); 
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소목록  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("   숙소아이디\t숙소이름\t\t숙소위치");
		System.out
				.println("  ───────────────────────────────────────────────────────");
		for (int i = 0; i < stayList.size(); i++) {

			System.out.println("   " + stayList.get(i).get("STAY_ID") + "\t\t"
					+ stayList.get(i).get("STAY_NAME") + "\t\t"
					+ stayList.get(i).get("STAY_ADDR1") + " "
					+ stayList.get(i).get("STAY_ADDR2"));
			System.out
					.println("   ---------------------------------------------------------");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		System.out.print("   1.숙소 상세정보 확인 0.홈화면으로 돌아가기 ▶ ");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.HOST_DETAIL; //숙소 상세정보 확인
		case 0:
			return View.HOME_HOST; //호스트 홈화면으로 이동
		}
		return View.INFO_MEMBER; //회원정보로 이동
	}

	// 숙소 상세 정보
	public int hostDetail() {
		System.out.print("   · 조회할 숙소 아이디를 입력하세요 ▶ ");
		int stayId = ScanUtil.nextInt();
		Map<String, Object> stay = informationDao.hostStayDetail(stayId);
		Controller.selectStay = stay; // 조회한 숙소 정보 저장
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소상세정보  ┣━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("   숙소 이름  ▷   " + stay.get("STAY_NAME"));
		System.out.println("   주소  ▷   " + stay.get("STAY_ADDR1") + " "
				+ stay.get("STAY_ADDR2"));
		System.out.println("   사업자 번호  ▷   " + stay.get("STAY_COMPANY_NUM"));
		System.out.println("   전화번호  ▷   " + stay.get("STAY_TEL"));
		System.out.println("   공지사항  ▷   " + stay.get("STAY_NOTICE"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   [ 등록된 편의시설 목록 ]");
		List<Map<String, Object>> convenient = informationDao.stayCon(stayId); // 숙소의
																				// 편의시설
																				// 리스트

		for (int i = 0; i < convenient.size(); i++) {
			System.out.print("  - " + (i + 1) + "\t"
					+ convenient.get(i).get("FILTER_NAME"));
			System.out.print("\t\t");
			if (i % 2 == 1) {
				System.out.println();
			}

		}
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		System.out.print("   1.숙소정보 수정 2.예약/매출내역 조회 3.후기 보기 0.홈화면으로 돌아가기 ▶");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.HOST_MODIFY; //숙소정보 수정으로 이동
		case 2:
			return View.HOST_RESERVE; //예약/매출내역으로 이동
		case 3:
			return View.STAY_REVIEW; //숙소에 작성된 후기로 이동
		case 0:
			return View.HOME_HOST; //호스트 홈화면으로 이동
		}
		return View.INFO_MEMBER; //회원정보로 이동
	}

	// 숙소 정보 수정
	public int hostUpdate() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소정보수정  ┣━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("   · 수정할 숙소이름을 입력하세요[수정하지 않으려면 0번을 누르세요]");
		System.out.print("     입력 ▶ ");
		String name = ScanUtil.nextLine();
		if (name.equals("0")) {
			name = (String) Controller.selectStay.get("STAY_NAME");
		} else {
			System.out.println("    - 사용가능한 숙소이름입니다  -");
		}
		System.out.println("   · 수정할 숙소 전화번호를 입력하세요[수정하지 않으려면 0번을 누르세요]");
		System.out.print("     입력 ▶ ");
		String tel = ScanUtil.nextLine();
		String exprph = "^0\\d{1,3}-\\d{3,4}-\\d{4}"; // 전화번호의 정규표현식
		Pattern pi = Pattern.compile(exprph);
		Matcher mi = pi.matcher(tel); // 정규표현식에 위배하지 않는지 확인
		boolean mm = mi.matches();
		if (tel.equals("0")) {
			tel = (String) Controller.selectStay.get("STAY_TEL");
		} else if (!tel.equals("0") && mm == true) {
			System.out.println("    - 사용 가능한 전화번호입니다 -");
		} else {
			System.out.println("    ※ 사용할 수 없는 전화번호입니다  ※");
			return View.HOME_HOST;
		}
		System.out
				.println("   · 수정할 공지사항을 입력하세요[수정하지 않으려면 0번/삭제 하려면 1번을 누르세요]");
		System.out.print("     입력 ▶ ");
		String notice = ScanUtil.nextLine();
		if (notice.equals("0")) { 
			notice = (String) Controller.selectStay.get("STAY_NOTICE");
		} else if (notice.equals("1")) { //삭제
			notice = null; //null값 넣어줌
		} else {
			System.out.println("    - 사용 가능한 공지사항입니다 -");
		}
		int stayId = ((BigDecimal) Controller.selectStay.get("STAY_ID"))
				.intValue();
		int result = informationDao.updateDetailstay(name, tel, notice, stayId); //위에서 받은 값으로 update
		if (result > 0) {
			System.out
			.println("  ─────────────────── 숙소정보가 수정되었습니다 ───────────────────");
			System.out
			.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			return View.HOME_HOST; //호스트 홈화면으로 이동
		} else {
			System.out
			.println("  ────────────────── 숙소정보 수정에 실패했습니다 ──────────────────");
			System.out
			.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			
			return View.HOME_HOST; //호스트 홈화면으로 이동
		}
	}

	// 예약, 매출내역 조회
	public int reservePrice() {
		int stayId = ((BigDecimal) Controller.selectStay.get("STAY_ID"))
				.intValue();
		List<Map<String, Object>> reserve = informationDao.reserveHost(stayId);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 예약내역  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("  예약번호\t예약자아이디\t방번호\t체크인\t체크아웃\t결제일\t결제금액\t인원수");
		for (int i = 0; i < reserve.size(); i++) {
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println(reserve.get(i).get("RESERVE_NUM") + "\t"
					+ reserve.get(i).get("MEM_ID") + "\t"
					+ reserve.get(i).get("ROOM_ID") + "\t"
					+ format.format(reserve.get(i).get("RESERVE_IN")) + "\t"
					+ format.format(reserve.get(i).get("RESERVE_OUT")) + "\t"
					+ format.format(reserve.get(i).get("RESERVE_DATE")) + "\t"
					+ reserve.get(i).get("RESERVE_PRICE") + "\t"
					+ reserve.get(i).get("RESERVE_PEOPLE") + "\t");
		}
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 매출내역  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		List<Map<String, Object>> price = informationDao.priceHost(stayId);
		System.out.println("  결제일\t예약번호\t방번호\t예약자아이디\t예약수량\t단가\t결제금액");
		for (int i = 0; i < price.size(); i++) {
			System.out
					.println("   ---------------------------------------------------------");
			System.out.println(format.format(price.get(i).get("RESERVE_DATE"))
					+ "\t" + price.get(i).get("RESERVE_NUM") + "\t"
					+ price.get(i).get("ROOM_ID") + "\t"
					+ price.get(i).get("MEM_ID") + "\t"
					+ price.get(i).get("DAY") + "\t"
					+ price.get(i).get("ROOM_PRICE") + "\t"
					+ price.get(i).get("RESERVE_PRICE") + "\t");
		}
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("   1.회원 정보 확인 0.홈화면으로 돌아가기 ▶ ");

		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			return View.INFO_MEMBER; //회원정보로 이동
		case 0:
			return View.HOME_HOST; //호스트 홈화면으로 이동
		}
		return View.HOME_HOST; //호스트 홈화면으로 이동
	}
}
