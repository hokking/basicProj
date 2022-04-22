package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.ReviewDao;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class ReviewService {

	// 싱글톤 패턴
	private ReviewService() {
	}

	private static ReviewService instance;

	public static ReviewService getinstance() {
		if (instance == null) {
			instance = new ReviewService();
		}
		return instance;
	}

	private JDBCUtil jdbc = JDBCUtil.getInstance();
	private ReviewDao reviewDao = ReviewDao.getinstance();

	// 숙소 후기 조회
	public int readReview() {
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┫ 숙소후기  ┣━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
		int stayId = ((BigDecimal) Controller.selectStay.get("STAY_ID"))
				.intValue();
		List<Map<String, Object>> reviewList = reviewDao.readReviewStay(stayId);
		if (reviewDao.avgRe(stayId) != null) {
			Map<String, Object> avgReview = reviewDao.avgRe(stayId);
			System.out
					.println("  ┌───────────────────────── 평균평점  ───────────────────────┐");

			System.out.println("\t\t위치\t\t청결\t\t친절");
			System.out.println("\t\t" + avgReview.get("LOC") + "\t\t"
					+ avgReview.get("CL") + "\t\t" + avgReview.get("KI"));
			System.out
					.println("  └────────────────────────────────────────────────────────┘\n");
			System.out
					.println("  ┌───────────────────────── 후기목록  ───────────────────────┐");
			System.out.println("       번호\t\t후기번호\t위치\t청결\t친절\t예약자아이디");
			System.out
					.println("    ──────────────────────────────────────────────────────");
			for (int i = 0; i < reviewList.size(); i++) {
				System.out.println("     " + (i + 1) + "\t\t"
						+ reviewList.get(i).get("REVIEW_NUM") + "\t"
						+ reviewList.get(i).get("REVIEW_LOCATION") + "\t"
						+ reviewList.get(i).get("REVIEW_CLEAN") + "\t"
						+ reviewList.get(i).get("REVIEW_KIND") + "\t"
						+ reviewList.get(i).get("MEM_ID"));
				System.out
						.println("     -----------------------------------------------------");
				System.out.println("\t내용  ▷   "
						+ reviewList.get(i).get("REVIEW_CON"));

				if (!reviewList.get(i).get("RECON").equals("0")) {
					System.out.println("            └─> "
							+ reviewList.get(i).get("RECON"));

				}
				System.out
						.println("     ─────────────────────────────────────────────────────");
			}
			System.out
					.println("  └────────────────────────────────────────────────────────┘\n");
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			if (Controller.loginUser != null) {
				String type = (String) Controller.loginUser.get("MEM_TYPE");
				if (type.equals("1")) { // 게스트일 경우
					System.out.print("   1.뒤로가기 0.홈화면으로 돌아가기  ▶  ");
					int input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						return View.STAY_DETAIL; //선택한 숙소 화면으로 이동
					case 0:
						return View.HOME_GUEST; //홈화면으로 이동
					}
				} else if(type.equals("2")){ // 호스트일 경우
					System.out.print("   1.후기 답글 달기 0.홈화면으로 돌아가기  ▶  ");
					int input = ScanUtil.nextInt();
					switch (input) {
					case 1:
						return View.RE_REVIEW; //후기 답글다는 화면으로 이동
					case 0:
						return View.HOME_HOST; //호스트 홈화면으로 이동
					}
				}
			} else { //로그인하지 않았을 경우
				System.out.print("   1.뒤로가기  ▶  ");
				int input = ScanUtil.nextInt();
				if (input == 1) {
					return View.STAY_DETAIL; //선택한 숙소 화면으로 이동
				}
			}
		} else {
			System.out.println("\t\t\t[ 등록된 리뷰가 없습니다 ]\n");
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		}
		if (Controller.loginUser != null) {
			String type = (String) Controller.loginUser.get("MEM_TYPE");
			if (type.equals("1")) { // 게스트일 경우
				System.out.print("   1.뒤로가기 0.홈화면으로 돌아가기  ▶  ");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					return View.STAY_DETAIL; //선택한 숙소 화면으로 이동
				case 0:
					return View.HOME_GUEST; //홈화면으로 이동
				}
			} else if(type.equals("2")){ // 호스트일 경우
				System.out.print("   0.홈화면으로 돌아가기  ▶  ");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 0:
					return View.HOME_HOST; //호스트 홈화면으로 이동
				}
			}
		} else { //로그인하지 않았을 경우
			System.out.print("   1.뒤로가기  ▶  ");
			int input = ScanUtil.nextInt();
			if (input == 1) {
				return View.STAY_DETAIL; //선택한 숙소 화면으로 이동
			}
		}
		String type = (String) Controller.loginUser.get("MEM_TYPE");
		if(type.equals("2")){ //호스트일 경우
			return View.HOME_HOST; //홈화면으로 이동
		}else{
			return View.STAY_DETAIL; //호스트 제외 숙소 상세로 이동
		}
	}

	// 호스트의 답글 작성
	public int updateRe() {
		int stayId = ((BigDecimal) Controller.selectStay.get("STAY_ID"))
				.intValue();
		List<Map<String, Object>> reviewList = reviewDao.nonRecon(stayId);
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━┫ 후기답글작성  ┣━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.println("    후기번호\t예약자아이디\t위치\t청결\t친절\t후기내용");
		for (int i = 0; i < reviewList.size(); i++) {
			System.out
					.println("    ------------------------------------------------------");
			System.out
					.println("   ────────────────────────────────────────────────────────");
			System.out.println("    " + reviewList.get(i).get("REVIEW_NUM")
					+ "\t" + reviewList.get(i).get("MEM_ID") + "\t"
					+ reviewList.get(i).get("REVIEW_LOCATION") + "\t"
					+ reviewList.get(i).get("REVIEW_CLEAN") + "\t"
					+ reviewList.get(i).get("REVIEW_KIND") + "\t"
					+ reviewList.get(i).get("REVIEW_CON"));
		}
		System.out
				.println("   ────────────────────────────────────────────────────────");
		System.out.print("   · 답글을 작성할 후기번호를 입력해주세요  ▶  ");
		int num = ScanUtil.nextInt();
		System.out.println("   · 답글의 내용을 입력해주세요");
		System.out.print("      입력  ▶  ");
		String recon = ScanUtil.nextLine();
		int result = reviewDao.updateRecon(recon, num);
		if (result > 0) {
			System.out.println("    - 답글을 등록했습니다 -");
		} else {
			System.out.println("    ※ 답글 등록을 실패하였습니다 ※");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.INFO_MEMBER; //회원정보로 이동
	}

	// 게스트의 후기 작성
	public int insertReview() {
		String memId = (String) Controller.loginUser.get("MEM_ID");
		int renum = Controller.reserveNo;
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 후기작성  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");

		System.out.print("   · 위치는 어떠셨나요?[1~5사이의 숫자로 평가해주세요]  ▶  ");
		int loc = ScanUtil.nextInt();
		System.out.print("   · 숙소 청결은 어떠셨나요?[1~5사이의 숫자로 평가해주세요]  ▶  ");
		int cl = ScanUtil.nextInt();
		System.out.print("   · 호스트의 친절함은 어떠셨나요?[1~5사이의 숫자로 평가해주세요]  ▶  ");
		int ki = ScanUtil.nextInt();
		System.out.println("   · 숙소에 대한 전체적인 후기를 남겨주세요");
		System.out.print("      입력  ▶  ");
		String con = ScanUtil.nextLine();

		int result = reviewDao.inRe(con, loc, cl, ki, memId, renum);
		if (result > 0) {
			System.out.println("    - 후기를 등록하였습니다 -");
		} else {
			System.out.println("    ※ 후기 등록에 실패하였습니다 ※");
		}
		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.INFO_MEMBER; //회원정보로 이동
	}

}
