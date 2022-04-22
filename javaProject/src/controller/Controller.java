package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import service.AdminService;
import service.InformationService;
import service.ReservationService;
import service.ReserveInformationService;
import service.ReviewService;
import service.SearchService;
import service.UserService;
import util.JDBCUtil;
import util.ScanUtil;
import util.View;

public class Controller {

	public static void main(String[] args) {
		new Controller().start();
	}
	
	static JDBCUtil jdbc = JDBCUtil.getInstance();
	
	
	private InformationService infoService = InformationService.getinstance();
	private ReviewService reviewService = ReviewService.getinstance();
	private UserService userService = UserService.getInstance();
	private ReservationService rsvService = ReservationService.getInstance();
	private ReserveInformationService rsvInfoService = ReserveInformationService.getInstance();
	private SearchService searchService = SearchService.getInstance();
	private AdminService adminService = AdminService.getinstance();
   
	
	public static Map<String, Object> loginUser = null;
	public static Map<String, Object> selectStay = null;
	public static int reserveNo = 0;
	public static int regionLarge = 0;
	public static int region = 0;
	public static int peopleNum = 0;
	public static String checkInDate = null;
	public static String checkOutDate = null;
	public static int stayId = 0;
	public static int roomId = 0;
	public static ArrayList<Integer> sfeature = new ArrayList<>();
	public static ArrayList<Integer> rfeature = new ArrayList<>();
	public static List<Map<String, Object>> stayfeature = new ArrayList<>();
	public static int stayNum = 0;


	private void start() {
		int view = View.HOME;
		
		while(true){
            switch (view){
            case View.HOME: view = home(); break;
            case View.LOGIN: view = userService.login(); break;
            case View.JOIN: view = userService.join(); break;
            case View.HOME_GUEST: view = homeG(); break;
            case View.HOME_HOST: view = homeH(); break;
            case View.HOME_ADMIN: view = homeA(); break;
            case View.SEARCH: view = searchService.searchMain(); break;
            case View.STAY_LIST: view = searchService.searchList(); break;
            case View.STAY_DETAIL: view = searchService.stayDetail(); break;
            case View.STAY_REVIEW: view = reviewService.readReview(); break;
            case View.RESERVE_INFO: view = rsvService.rsvCheck(stayId, roomId, checkInDate, checkOutDate, peopleNum); break;
            case View.PAY: view = rsvService.payReserve(stayId, roomId, checkInDate, checkOutDate, peopleNum); break;
            case View.INFO_MEMBER: view = infoService.memInformation(); break;
            case View.MEMBER_MODIFY: view = infoService.InfoUpdate(); break;
            case View.RESERVE_LIST: view = rsvInfoService.reserveList();  break;
            case View.RESERVE_DETAIL: view = rsvInfoService.reserveDetail(); break;
            case View.RESERVE_DELETE: view = rsvInfoService.reserveCancel(); break;
            case View.RIVIEW_WRITE: view = reviewService.insertReview(); break;
            case View.HOST_LIST: view = infoService.hostList(); break;
            case View.HOST_DETAIL: view = infoService.hostDetail(); break;
            case View.HOST_MODIFY: view = infoService.hostUpdate(); break;
            case View.HOST_REQUEST: view = adminService.requestStay(); break;
            case View.RE_REVIEW: view = reviewService.updateRe(); break;
            case View.HOST_RESERVE: view = infoService.reservePrice(); break;
            case View.MEMBER_READ: view = adminService.memberList(); break;
            case View.STAY_CHECK: view = adminService.approve(); break;
            case View.SEARCH_FILTER: view = searchService.searchFilter(); break;
            case View.SELECT_FILTER: view = searchService.selectFilter(); break;
            case View.DETAIL_CHECK: view = adminService.appDetail(); break;
            case View.STAY_ADMIN_LIST: view = adminService.stayList(); break;
            }
         }
	}
	
	
	//홈화면(전체)
	private int home(){ 
	      System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
	      System.out.println("┃                         BLUE SPACE                        ┃");
	      System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
	      
	      System.out.print("   1.로그인 2.회원가입 3.조회 0.프로그램 종료 ▶ ");
	      int input = ScanUtil.nextInt();

	      switch (input) {
	      case 1:
	    	  System.out.println("\n┏━━━━━━━━━━━━━━━━━━━━━━━━━━┫ 로그인  ┣━━━━━━━━━━━━━━━━━━━━━━━━━┓\n");
	         return View.LOGIN;
	      case 2:
	         return View.JOIN;
	      case 3:
	         return View.SEARCH;
	      case 0:
	    	  System.out.println(" ─────────────────── 프로그램이 종료되었습니다 ────────────────────");
	    	  
	         System.exit(0);
	      }
	      return View.HOME;
	      }
	      
	
	//홈화면(게스트)
	private int homeG(){
		System.out.print("   1.조회 2.내 정보 0.로그아웃 ▶ ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1:
			return View.SEARCH;
		case 2:
			return View.INFO_MEMBER;
		case 0:
			loginUser = null;
			selectStay = null;
			reserveNo = 0;
			regionLarge = 0;
			region = 0;
			peopleNum = 0;
			checkInDate = null;
			checkOutDate = null;
			stayId = 0;
			roomId = 0;
			sfeature = new ArrayList<>();
			rfeature = new ArrayList<>();
			stayfeature = new ArrayList<>();
			stayNum = 0;
			System.out.println(" ────────────────────── 로그아웃 되었습니다 ──────────────────────");
			return View.HOME;
		}
		return View.HOME_GUEST;
	}
	
	//홈화면(호스트)
	private int homeH(){
		System.out.print("   1.숙소 등록요청 2.내 정보 0.로그아웃 ▶ ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1:
			return View.HOST_REQUEST;
		case 2:
			return View.INFO_MEMBER;
		case 0:
			loginUser = null;
			selectStay = null;
			reserveNo = 0;
			regionLarge = 0;
			region = 0;
			peopleNum = 0;
			checkInDate = null;
			checkOutDate = null;
			stayId = 0;
			roomId = 0;
			sfeature = new ArrayList<>();
			rfeature = new ArrayList<>();
			stayfeature = new ArrayList<>();
			stayNum = 0;
			System.out.println(" ────────────────────── 로그아웃 되었습니다 ──────────────────────");
			return View.HOME;
		}
		return View.HOME_HOST;
	}
	
	//홈화면(관리자)
	private int homeA(){
		System.out.print("   1.숙소 등록요청 확인 2.회원 정보 3.숙소정보 0.로그아웃 ▶ ");
		int input = ScanUtil.nextInt();
		switch(input){
		case 1:
			return View.STAY_CHECK;
		case 2:
			return View.MEMBER_READ;
		case 3:
			return View.STAY_ADMIN_LIST;
		case 0:
			loginUser = null;
			selectStay = null;
			reserveNo = 0;
			regionLarge = 0;
			region = 0;
			peopleNum = 0;
			checkInDate = null;
			checkOutDate = null;
			stayId = 0;
			roomId = 0;
			sfeature = new ArrayList<>();
			rfeature = new ArrayList<>();
			stayfeature = new ArrayList<>();
			stayNum = 0;
			System.out.println(" ────────────────────── 로그아웃 되었습니다 ──────────────────────");
			return View.HOME;
		}
		return View.HOME_ADMIN;
	}

	
	

}
