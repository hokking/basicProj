package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDao {
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//싱글톤 패턴
	private AdminDao(){}
	private static AdminDao instance;
	public static AdminDao getinstance(){
		if(instance == null){
			instance = new AdminDao();
		}
		return instance;
	}
	
	//숙소 등록요청 - 기본정보 등록
	/*
	 * 1~7번으로 지역대분류값 입력받고 거기에 숙소번호 더하기
	 * 지역소분류도 입력받기
	 */
	public int insertStay(int region, String stayName,
						  String company, String addr1, String addr2,
						  String memId, int sregion, String notice, String tel){
		String sql = "INSERT INTO STAY"
				+ "		VALUES (FN_CREATE_NUM(?),"
				+ "				?, ?, ?, ?, ?, ?, 0, ?, ?)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(region);
		param.add(stayName);
		param.add(company);
		param.add(addr1);
		param.add(addr2);
		param.add(memId);
		param.add(sregion);
		param.add(notice);
		param.add(tel);
		return jdbc.update(sql, param);
	}
	
	//방금 입력한 정보로 숙소 상세 정보 저장하기
	public Map<String, Object> requestDetail(String tel){
		String sql = "SELECT STAY_NAME,"
				+ "          STAY_ADDR1,"
				+ "			 STAY_ADDR2,"
				+ "			 STAY_COMPANY_NUM,"
				+ "			 STAY_TEL,"
				+ "			 STAY_ID,"
				+ "			 STAY_NOTICE"
				+ "		FROM STAY"
				+ "    WHERE STAY_TEL = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(tel);
		return jdbc.selectOne(sql, param);
	}
	
	public List<Map<String, Object>> selectStayList() {
	      String sql = "SELECT STAY_ID"
	            + "        , STAY_NAME"
	            + "        , STAY_ADDR1||' '||STAY_ADDR2 AS ADDR"
	            + "        , STAY_TEL"
	            + "        , MEM_ID"
	            + "     FROM STAY"
	            + "    ORDER BY 1";
	      return jdbc.selectList(sql);
	   }
	
	//숙소 편의시설과 방 특성
	public int insertConv(int stayId, int filter){
		String sql = "INSERT INTO CONVENIENT"
				+ "		 VALUES (?, ?)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		param.add(filter);
		return jdbc.update(sql, param);
	}
	
	//방 추가하기
	public int insertRoom(int price, int num,int stayId, String name){
		String sql= "INSERT INTO ROOM"
				+ "	  VALUES ((SELECT MAX(ROOM_ID)+1 FROM ROOM),"
				+ "			  ?,?,?,?)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(price);
		param.add(num);
		param.add(stayId);
		param.add(name);
		return jdbc.update(sql, param);
	}
	
	//방금 추가한 방 리스트 보기
	public List<Map<String, Object>> roomLists(int stayId){
		String sql = "SELECT *"
				+ "		FROM ROOM"
				+ "	   WHERE STAY_ID=?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	//방 특성 추가
	public int insertFeature(Object name, int stayId, int feature){
		String sql = " INSERT INTO ROOM_FEATURE"
				+ "			VALUES ((SELECT ROOM_ID"
				+ "					   FROM ROOM"
				+ "					  WHERE ROOM_NAME=?"
				+ "					    AND STAY_ID=?),"
				+ "					 ?)";
		ArrayList<Object> param = new ArrayList<>();
		param.add(name);
		param.add(stayId);
		param.add(feature);
		return jdbc.update(sql, param);
	}
	
	//미승인 숙소 리스트
	   public List<Map<String, Object>> nonApprove(){
	      String sql = "SELECT *"
	            + "      FROM STAY"
	            + "      WHERE STAY_APPROVE=0";
	      return jdbc.selectList(sql);
	   }
	   
	   //숙소 상세정보 출력 - 숙소
	   public Map<String, Object> naStay(int stayId){
	      String sql = "SELECT *"
	            + "      FROM STAY"
	            + "    WHERE STAY_APPROVE=0"
	            + "        AND STAY_ID=?";
	      ArrayList<Object> param = new ArrayList<>();
	      param.add(stayId);
	      return jdbc.selectOne(sql, param);
	   }
	   
	   //숙소 상세정보 출력 - 방아이디
	   public List<Map<String, Object>> rNoList(int stayId){
		   String sql = "SELECT ROOM_ID AS RI"
				   + "		   FROM ROOM"
				   + "		  WHERE STAY_ID=?";
		   ArrayList<Object> param = new ArrayList<>();
		   param.add(stayId);
		   return jdbc.selectList(sql, param);
	   }
	   
	   //숙소 상세정보 출력 - 편의시설
	   public List<Map<String, Object>> naCon(int stayId){
		   String sql = "SELECT DISTINCT B.FILTER_NAME"
		   		+ "		   FROM CONVENIENT A, FILTER B"
		   		+ "		  WHERE A.FILTER_ID=B.FILTER_ID"
		   		+ "		    AND A.STAY_ID=?";
		   ArrayList<Object> param = new ArrayList<>();
		   param.add(stayId);
		   return jdbc.selectList(sql, param);
	   }
	   
	   //숙소 상세정보 출력 - 방 특성
	   public List<Map<String, Object>> naRf(Object object){
		   String sql = "SELECT DISTINCT C.FILTER_NAME AS FN"
		   		+ "		   FROM ROOM_FEATURE B, FILTER C"
		   		+ "		  WHERE B.FILTER_ID=C.FILTER_ID"
		   		+ "         AND B.ROOM_ID=?";
		   ArrayList<Object> param = new ArrayList<>();
		   param.add(object);
		   return jdbc.selectList(sql, param);
	   }
	   
	   //숙소 승인 허가
	   public int apYes(int stayId){
	      String sql = "UPDATE STAY"
	            + "       SET STAY_APPROVE=1"
	            + "      WHERE STAY_ID=?";
	      ArrayList<Object> param = new ArrayList<>();
	      param.add(stayId);
	      return jdbc.update(sql, param);
	   }
	   
	   //숙소 승인 불허
	   public int apNo(int stayId){
		      String sql = "UPDATE STAY"
		            + "       SET STAY_APPROVE=2"
		            + "      WHERE STAY_ID=?";
		      ArrayList<Object> param = new ArrayList<>();
		      param.add(stayId);
		      return jdbc.update(sql, param);
		   }
	   
	   //전체회원 조회
	   public List<Map<String, Object>> memberSelect(int input) {
		      String sql = "SELECT MEM_ID"
		            + "        , MEM_NAME"
		            + "       , MEM_BIRTH"
		            + "         , MEM_MILE"
		            + "         , MEM_PH"
		            + "     FROM MEMBER"
		            + "    WHERE MEM_TYPE = ?";
		      ArrayList<Object> param = new ArrayList<>();
		      param.add(input);
		      return jdbc.selectList(sql, param);
		   }

}
