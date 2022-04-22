package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class InformationDao {
	
	//싱글톤 패턴
	private InformationDao(){}
	private static InformationDao instance;
	public static InformationDao getinstance(){
		if(instance == null){
			instance = new InformationDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//회원 정보 조회
	public Map<String, Object> readMemInfo(String memId, String type){
		String sql = "SELECT MEM_ID,"
				+ "		   	 MEM_PASS,"
				+ "			 MEM_NAME,"
				+ "			 MEM_BIRTH,"
				+ "			 NVL(MEM_MILE,0) AS MILE,"
				+ "			 MEM_TYPE,"
				+ "			 MEM_PH"
				+ "     FROM MEMBER"
				+ "    WHERE MEM_ID = ?"
				+ "      AND MEM_TYPE = ?"; //호스트-게스트-관리자 구분위해 타입도 조건에 넣어줌
		ArrayList<Object> param = new ArrayList<>();
		param.add(memId);
		param.add(type);
		return jdbc.selectOne(sql, param);
	}
	
	//회원 정보 수정
	public int updateMemInfo(String pass,String ph,String memId){
		String sql = "UPDATE MEMBER"
				+ " 	 SET MEM_PASS = ?," //수정가능한 항목 두 가지
				+ "			 MEM_PH = ?"	//비밀번호, 전화번호
				+ "	   WHERE MEM_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(pass);
		param.add(ph);
		param.add(memId);
		return jdbc.update(sql, param);
	}
	
	//호스트의 숙소 목록
	public List<Map<String, Object>> hostStayList(String memId){
		String sql = "SELECT STAY_ID,"
				+ "			 STAY_NAME, "
				+ "          STAY_ADDR1,"
				+ "			 STAY_ADDR2"
				+ "     FROM STAY"		  
				+ "    WHERE MEM_ID = ?"; //아이디로 숙소 목록을 불러옴
		ArrayList<Object> param = new ArrayList<>();
		param.add(memId);
		return jdbc.selectList(sql, param);
	}
	
	//숙소 상세 정보
	public Map<String, Object> hostStayDetail(int stayId){
		String sql = "SELECT STAY_NAME,"
				+ "          STAY_ADDR1,"
				+ "			 STAY_ADDR2,"
				+ "			 STAY_COMPANY_NUM,"
				+ "			 STAY_TEL,"
				+ "			 STAY_ID,"
				+ "			 STAY_NOTICE"
				+ "		FROM STAY"
				+ "    WHERE STAY_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectOne(sql, param);
	}
	
	//속소 편의시설
	public List<Map<String, Object>> stayCon(int stayId){
		String sql = "SELECT A.FILTER_NAME,"
				+ "			 A.FILTER_ID"
				+ "     FROM FILTER A, CONVENIENT B"
				+ "    WHERE A.FILTER_ID=B.FILTER_ID"
				+ "      AND B.STAY_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	//숙소 정보 수정
	public int updateDetailstay(String name, String tel, String notice, int stayId){
		String sql = "UPDATE STAY"
				+ "      SET STAY_NAME = ?," 
				+ "			 STAY_TEL = ?,"
				+ "			 STAY_NOTICE = ?"
				+ "    WHERE STAY_ID = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(name);
		param.add(tel);
		param.add(notice);
		param.add(stayId);
		return jdbc.update(sql, param);
	}
	
	//예약내역 조회
	public List<Map<String, Object>> reserveHost(int stayId){
		String sql = "SELECT A.RESERVE_NUM,"
				+ "          A.MEM_ID,"
				+ "          A.RESERVE_IN,"
				+ "			 A.RESERVE_OUT,"
				+ "			 A.RESERVE_DATE,"
				+ "			 A.RESERVE_PRICE,"
				+ "			 A.RESERVE_PEOPLE,"
				+ "			 B.STAY_ID,"
				+ "			 A.ROOM_ID"
				+ " 	FROM RESERVATION A, ROOM B, STAY C "
				+ "	   WHERE A.ROOM_ID=B.ROOM_ID"
				+ "  	 AND B.STAY_ID=C.STAY_ID"
				+ "  	 AND C.STAY_ID=?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	//매출내역 조회
	public List<Map<String, Object>> priceHost(int stayId){
		String sql = "SELECT A.RESERVE_DATE,"
				+ "          A.RESERVE_NUM,"
				+ "			 A.ROOM_ID,"
				+ " 		 A.MEM_ID,"
				+ "   		 A.RESERVE_OUT-A.RESERVE_IN AS DAY,"
				+ " 		 B.ROOM_PRICE,"
				+ "			 A.RESERVE_PRICE"
				+ "     FROM RESERVATION A, ROOM B, STAY C"
				+ "    WHERE A.ROOM_ID=B.ROOM_ID"
				+ "      AND B.STAY_ID=C.STAY_ID"
				+ "      AND C.STAY_ID=?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
