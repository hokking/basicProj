package service;

import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import dao.ReservationDao;
import util.ScanUtil;
import util.View;

public class ReservationService {
	private ReservationService() {
	}

	private static ReservationService instance;

	public static ReservationService getInstance() {
		if (instance == null) {
			instance = new ReservationService();
		}
		return instance;
	}

	private ReservationDao reservationDao = ReservationDao.getInstance();

	// 결제 할 정보 확인
	public int rsvCheck(int stayId, int roomId, String checkInDate,
			String checkOutDate, int peopleNum) {
		if (Controller.loginUser == null) { // 비회원일 경우
			System.out.println("    ※ 로그인 후 사용가능합니다 ※");
			return View.HOME;
		}

		Map<String, Object> rsv = reservationDao.selectRoomInfo(stayId, roomId,
				checkInDate, checkOutDate);

		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 예약확인  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		System.out.println("    - 숙소  ▷  " + rsv.get("STAY_NAME"));
		System.out.println("    - 방이름  ▷  " + rsv.get("ROOM_NAME"));
		System.out.println("    - 입실날짜  ▷  " + checkInDate);
		System.out.println("    - 퇴실날짜  ▷  " + checkOutDate);
		System.out.println("    - 인원  ▷  " + peopleNum + "명");
		System.out.println("    - 가격  ▷  " + rsv.get("PRICE") + "원");
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");

		System.out.print("   1.결제하기 2.뒤로가기  ▶  ");
		int input = ScanUtil.nextInt();

		if (input == 1) {
			return View.PAY;
		} else {
			return View.STAY_DETAIL;
		}
	}

	// 결제 화면
	public int payReserve(int stayId, int roomId, String checkInDate,
			String checkOutDate, int peopleNum) {
		boolean check = true;
		int payPrice = 0; // 결제 금액
		int mile = 0; // 사용 마일리지
		String memId = (String) Controller.loginUser.get("MEM_ID");
		Map<String, Object> rsv = reservationDao.selectRoomInfo(stayId, roomId,
				checkInDate, checkOutDate);
		Map<String, Object> selectMile = reservationDao.selectMile(memId);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┫ 결제  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		System.out.println("   - 결제금액  ▷  " + rsv.get("PRICE"));
		System.out.println("   - 보유 마일리지  ▷  " + selectMile.get("MEM_MILE"));

		while (check) { // 보유한 마일리지 보다 더 많이 사용할 경우 경고창 출력
			// 적당한 마일리지가 사용되었을 때 반복문 빠져나옴
			System.out.print("       마일리지 사용  ▶  ");
			mile = ScanUtil.nextInt();
			if (mile > Integer.parseInt(String.valueOf(selectMile
					.get("MEM_MILE")))) {
				System.out.println("    ※ 마일리지가 부족합니다 ※");
			} else {
				payPrice = Integer.parseInt(String.valueOf(rsv.get("PRICE")))
						- mile;
				check = false;
			}
		}
		int remainMile = Integer.parseInt(String.valueOf(selectMile
				.get("MEM_MILE"))) - mile; // 결제 후 남은 마일리지
		System.out
				.println("  --------------------------------------------------------");
		System.out.println("     결제 금액  ▷  " + payPrice);
		System.out
				.println("  ─────────────────────────────────────────────────────────");
		System.out.print("   1.신용카드  2.휴대폰  3.무통장입금  ▶  ");
		int payWay = ScanUtil.nextInt();

		Map<String, Object> param = new HashMap<>();
		param.put("ROOM_ID", roomId);
		param.put("MEM_ID", Controller.loginUser.get("MEM_ID"));
		param.put("RESERVE_IN", checkInDate);
		param.put("RESERVE_OUT", checkOutDate);
		param.put("PAY_WAY", payWay);
		param.put("RESERVE_PRICE", payPrice); // 원래 가격 - 사용마일리지
		param.put("RESERVE_PEOPLE_NUM", peopleNum);
		param.put("PAY_MILE", mile); // 사용 마일리지

		int result = reservationDao.insertReserve(param);

		if (0 < result) {
			System.out
					.println("    ────────────────────── 예약 되었습니다  ─────────────────────");
			System.out
			.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			reservationDao.updateMile(remainMile, memId);
			return View.HOME_GUEST;
		} else {
			System.out
					.println("     ─────────────────── 예약에 실패하였습니다  ───────────────────");
			System.out
			.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		}
		return View.SEARCH;
	}

}
