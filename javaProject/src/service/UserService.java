package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import dao.UserDao;
import util.ScanUtil;
import util.View;

public class UserService {

	// 싱글톤 패턴
	private UserService() {
	} 

	private static UserService instance;

	public static UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	private UserDao userDao = UserDao.getInstance();

	// 회원가입
	public int join() {
		Map<String, Object> param = new HashMap<>();
		List<Map<String, Object>> memRead = userDao.selectUserId(); // userid
		// 불러올 리스트
		String memId = null; // 회원 아이디
		String memPass = null; // 회원 비밀번호
		String memName = null; // 회원 이름
		String memBirth = null; // 회원 생년월일
		String memPh = null; // 회원 전화번호
		String memType = null; // 회원 타입
		boolean flag = true;
		boolean check = false;
		System.out.println();
		System.out
				.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━┫ 회원가입  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println();
		while (flag) { // 아이디 정규표현식 확인해서 사용할 수 있는 아이디일때 while문 빠져나올 수 있도록
			System.out.print("   · 아이디 ▶ ");
			memId = ScanUtil.nextLine();
			if (checkId(memId) == true) { // 아이디가 정규표현식에 맞으면
				for (Map<String, Object> id : memRead) {
					if (memId.equals(id.get("MEM_ID"))) { // 테이블에 있는 전체 아이디들과 비교해서
						check = false; // 같은 것이 있을 때 false로 리턴
						break;
					} else {
						check = true; // 같은 것이 없을 때 true로 리턴
					}
				}
				if (check == true) {
					System.out.println("    - 사용할 수 있는 아이디입니다  -");
					flag = false;
				} else {
					System.out.println("    ※ 이미 사용중인 아이디입니다  ※");
				}
			} else {
				System.out
						.println("    ※ 6~20자의 영문 소문자, 숫자와 특수기호[_-]만 사용 가능합니다 ※");
			}
		}
		System.out
				.println("  ---------------------------------------------------------");
		flag = true;
		while (flag) { // 비밀번호 정규표현식 확인 후 비밀번호 확인
			System.out.print("   · 비밀번호 ▶ ");
			memPass = ScanUtil.nextLine();

			if (checkPass(memPass) == true) {
				while (true) {
					System.out.print("   · 비밀번호 확인 ▶ ");
					String memPassCheck = ScanUtil.nextLine();
					if (memPassCheck.equals(memPass) == true) {
						System.out.println("    - 비밀번호가 확인되었습니다 -");
						break;
					} else {
						System.out.println("    ※ 비밀번호가 일치하지 않습니다 ※");
					}
				}
				flag = false;
			} else {
				System.out
						.println("    ※ 6~20자의 영문 대·소문자, 숫자와 특수기호[-_!@#$%^*+=]만 사용 가능합니다 ※");
			}
		}
		System.out
				.println("  ---------------------------------------------------------");
		flag = true;
		while (flag) { // 정규표현식에 맞는 이름일때 사용
			System.out.print("   · 이름 ▶ ");
			memName = ScanUtil.nextLine();
			if (checkName(memName) == true) {
				flag = false;
			} else {
				System.out.println("    ※ 2~4자의 한글만 사용 가능합니다 ※");
			}
		}
		System.out
				.println("  ---------------------------------------------------------");
		flag = true;
		while (flag) { // 형식에 맞는 생년월일 사용
			System.out.print("   · 생년월일[0000/00/00] ▶ ");
			memBirth = ScanUtil.nextLine();
			if (checkBirth(memBirth) == true) {
				flag = false;
			} else {
				System.out.println("    ※ 형식에 맞춰서 작성해주세요 ※");
			}
		}
		System.out
				.println("  ---------------------------------------------------------");
		flag = true;
		while (flag) { // 형식에 맞는 휴대폰번호 사용
			System.out.print("   · 전화번호[000-0000-0000] ▶ ");
			memPh = ScanUtil.nextLine();
			if (checkPh(memPh) == true) {
				flag = false;
			} else {
				System.out.println("    ※ 형식에 맞춰서 작성해주세요 ※");
			}
		}
		System.out
				.println("  ---------------------------------------------------------");
		flag = true;
		while (flag) { // 형식에 맞는 타입 사용
			System.out.print("   · 1.게스트  2.호스트  ▶ ");
			memType = ScanUtil.nextLine();

			System.out.println();
			if (checkType(memType) == true)
				flag = false;
		}
		param.put("MEM_ID", memId);
		param.put("MEM_PASS", memPass);
		param.put("MEM_NAME", memName);
		param.put("MEM_BIRTH", memBirth);
		param.put("MEM_TYPE", memType);
		param.put("MEM_PH", memPh);
		int result = userDao.insertUser(param);

		if (0 < result) {
			System.out
					.println("\n  ─────────────────── 회원가입이 완료되었습니다 ───────────────────\n");
		} else {
			System.out
					.println("\n  ─────────────────── 회원가입에 실패하였습니다  ──────────────────\n");
		}

		System.out
				.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		return View.HOME;

	}

	// 로그인
	public int login() {
		
		System.out.print("   · 아이디 ▶ ");
		String memId = ScanUtil.nextLine();
		System.out.print("   · 비밀번호 ▶ ");
		String memPass = ScanUtil.nextLine();
		System.out.println();
		Map<String, Object> user = userDao.selectUser(memId, memPass);

		if (user == null) { // 아이디나 비밀번호가 맞지 않을 때
			System.out.println("    ※ 아이디나 비밀번호 맞지 않습니다 ※");
			System.out
					.println("  ---------------------------------------------------------");
		} else {
			System.out
					.println("  ────────────────────── 로그인 되었습니다  ──────────────────────");
			System.out
					.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			// 로그인한 사람의 정보 기억
			Controller.loginUser = user;
			if (Integer.parseInt(String.valueOf(user.get("MEM_TYPE"))) == 1) { // 회원타입이 게스트일경우
				return View.HOME_GUEST; // 게스트 홈으로 이동
			} else if(Integer.parseInt(String.valueOf(user.get("MEM_TYPE"))) == 2){ // 회원타입이 호스트일경우
				return View.HOME_HOST; // 호스트 홈으로 이동
			}else{ // 회원타입이 관리자일경우
				return View.HOME_ADMIN; // 관리자 홈으로 이동
			}
		}
		return View.LOGIN;
	}

	public boolean checkId(String memId) { // 아이디 정규표현식 확인
		String regex = "^[a-z0-9-_]{6,20}$"; // 6~20자의 영문 소문자, 숫자와 특수기호(_),(-)
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(memId);
		return m.matches();
	}

	public boolean checkPass(String memPass) { // 비밀번호 정규표현식 확인
		String regex = "^[a-zA-Z0-9-_!@#$%^*+=]{6,20}$"; // 6~20자의 영문 대·소문자, 숫자와 특수기호[-_!@#$%^*+=]
		// 숫자와
		// 특수기호
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(memPass);
		return m.matches();
	}

	public boolean checkName(String memName) { // 이름 정규표현식 확인
		String regex = "[가-힣]{2,4}"; // 2~4자의 한글
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(memName);
		return m.matches();
	}

	public boolean checkBirth(String memBirth) { // 생년월일 정규표현식 확인
		String regex = "(19[0-9][0-9]|20[0-2][0-9])/(0[1-9]|1[1-2])/(0[1-9]|1[0-9]|2[0-9]|3[0-1])"; // XXXX/XX/XX
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(memBirth);
		return m.matches();
	}

	public boolean checkPh(String memPh) { // 전화번호 정규표현식
		String regex = "^0\\d{1,3}-\\d{3,4}-\\d{4}"; // 0XX-XXX-XXXX
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(memPh);
		return m.matches();
	}

	public boolean checkType(String memType) { // 회원 타입 정규표현식
		String regex = "[1-2]{1}"; // 1-게스트, 2-호스트
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(memType);
		return m.matches();
	}
}
