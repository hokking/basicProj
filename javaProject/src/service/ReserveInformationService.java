package service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle.Control;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.ReserveInformationDao;

public class ReserveInformationService {

	private ReserveInformationService() {
	}

	private static ReserveInformationService instance;

	public static ReserveInformationService getInstance() {
		if (instance == null) {
			instance = new ReserveInformationService();
		}
		return instance;
	}

	int rsvNo;

	private ReserveInformationDao rsvInfoDao = ReserveInformationDao
			.getInstance();

	// 현재까지 게스트가 예약한 목록
	public int reserveList() {
		String memId = (String) Controller.loginUser.get("MEM_ID");
		List<Map<String, Object>> reserveList = rsvInfoDao
				.selectReserveList(memId);

		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 예약목록  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		for (Map<String, Object> reserve : reserveList) {
			System.out.println("   - 예약번호  ▷  " + reserve.get("RESERVE_NUM")
					+ "\t\t   - 숙소명  ▷  " + reserve.get("STAY_NAME"));
			System.out.println("   - 입·퇴실날짜  ▷  " + reserve.get("RESERVE_IN")
					+ "/" + reserve.get("RESERVE_OUT") + "\t - 결제금액  ▷  "
					+ reserve.get("RESERVE_PRICE"));
			System.out
					.println("  ----------------------------------------------------------");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		System.out.print("   1.예약상세조회  2.뒤로가기  ▶  ");
		int input = ScanUtil.nextInt();

		switch (input) {
		case 1:
			return View.RESERVE_DETAIL; // 예약상세조회 화면으로 이동
		case 2:
			return View.INFO_MEMBER; // 회원 정보 화면으로 이동
		}

		return View.SEARCH; // 조회 화면으로 이동
	}

	// 예약상세화면
	public int reserveDetail() {

		System.out.print("   · 조회 할 예약번호 입력  ▶  ");
		rsvNo = ScanUtil.nextInt();
		Map<String, Object> rsvDetail = rsvInfoDao.selectReserveDetail(rsvNo);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━┫ 예약상세조회  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("   - 예약번호  ▷  " + rsvDetail.get("RESERVE_NUM"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   - 주소  ▷  " + rsvDetail.get("STAY_ADDR1") + " "
				+ rsvDetail.get("STAY_ADDR2"));
		System.out.println("   - 전화번호  ▷  " + rsvDetail.get("STAY_TEL"));
		System.out.println("   - 숙소명  ▷  " + rsvDetail.get("STAY_NAME"));
		System.out.println("   - 방이름  ▷  " + rsvDetail.get("ROOM_NAME"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   - 입실날짜  ▷  " + rsvDetail.get("RESERVE_IN"));
		System.out.println("   - 퇴실날짜  ▷  " + rsvDetail.get("RESERVE_OUT"));
		System.out
				.println("   ---------------------------------------------------------");
		System.out.println("   - 결제금액  ▷  " + rsvDetail.get("RESERVE_PRICE"));
		System.out.println("   - 결제날짜  ▷  " + rsvDetail.get("RESERVE_DATE"));
		System.out.println("   - 사용마일리지  ▷  " + rsvDetail.get("PAY_MILE"));
		System.out
				.println("\n┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		// 현재 날짜가 입실날짜 이전일 경우 예약취소 가능
		if (Integer.parseInt(String.valueOf(rsvDetail.get("NOWDATE"))) < Integer
				.parseInt(String.valueOf(rsvDetail.get("RSVDATE")))) {
			System.out.print("   1.예약취소  2.뒤로가기  ▶  ");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				return View.RESERVE_DELETE; // 예약 취소 화면으로 이동
			case 2:
				return View.RESERVE_LIST; // 예약 목록 화면으로 이동
			}
		} else {
			if (rsvInfoDao.selectreview(rsvNo) != null) { // 후기 작성을 하였을 경우
				if (Integer.parseInt(String.valueOf(rsvInfoDao.selectreview(rsvNo).get("RESERVE_NUM"))) == rsvNo) {
					System.out.print("   1.뒤로가기  ▶  "); // 뒤로가기만 출력
					int input = ScanUtil.nextInt();
					if (input == 1) {
						return View.RESERVE_LIST; // 예약 목록 화면으로 이동
					}
				}
			  
			}// 후기작성을 하지 않았을 경우
			System.out.print("   1.후기작성  2.뒤로가기  ▶  "); // 후기작성과 뒤로가기를 출력
			int input1 = ScanUtil.nextInt();
			if (input1 == 1) {
				Controller.reserveNo=rsvNo;
				return View.RIVIEW_WRITE; // 후기 작성 화면으로 이동
			} else {
				return View.RESERVE_LIST; // 예약 목록 화면으로 이동
			}

		}
		return View.RESERVE_LIST;
	}

	// 예약 취소
	public int reserveCancel() {
		String memId = (String) Controller.loginUser.get("MEM_ID");
		Map<String, Object> rsvDetail = rsvInfoDao.selectReserveDetail(rsvNo);
		if (memId.equals(rsvDetail.get("MEM_ID"))) { // 현재 로그인한 회원과 예약한 회원의 아이디가 같을경우
			System.out.print("   예약을 취소하시겠습니까?[Y|N]  ▶  ");
			String input = ScanUtil.nextLine();
			if (input.equals("Y") || input.equals("y")) {
				rsvInfoDao.updateReserve(rsvNo, memId); // 마일리지 수정(사용 마일리지+현재 마일리지)
				int result = rsvInfoDao.deleteReserve(rsvNo); // 예약된 정보 삭제
				if (0 < result) {

					System.out
							.println("    ─────────────────── 예약이 취소되었습니다  ───────────────────");

				} else {
					System.out
							.println("    ────────────────── 예약 취소에 실패했습니다  ──────────────────");
				}
			} else {
				System.out
						.println("  ─────────────────── 홈화면으로 이동합니다 ───────────────────");
				return View.HOME_GUEST; // 게스트 홈 화면으로 이동
			}
		}
		return View.RESERVE_LIST; // 예약 목록 화면으로 이동
	}
}
