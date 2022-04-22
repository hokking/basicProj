package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ReviewDao {
	
	//싱글톤 패턴
	private ReviewDao(){}
	private static ReviewDao instance;
	public static ReviewDao getinstance(){
		if(instance == null){
			instance = new ReviewDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//숙소 후기 조회
	public List<Map<String, Object>> readReviewStay(int stayId){
		String sql = "SELECT A.REVIEW_NUM,"
				+ "          A.REVIEW_CON,"
				+ "          A.REVIEW_LOCATION,"
				+ "          A.REVIEW_CLEAN,"
				+ "			 A.REVIEW_KIND,"
				+ "			 A.MEM_ID,"
				+ "			 A.RESERVE_NUM,"
				+ "          NVL(A.REVIEW_CONCON,0) AS RECON"
				+ " 	FROM REVIEW A, ROOM B, RESERVATION C"
				+ "	   WHERE A.RESERVE_NUM=C.RESERVE_NUM"
				+ "		 AND B.ROOM_ID=C.ROOM_ID"
				+ "		 AND B.STAY_ID= ?";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	//숙소 후기 항목별 평균 평점
	public Map<String, Object> avgRe(int stayId){
		String sql = "SELECT B.STAY_ID,"
				+ " 	     ROUND(NVL(AVG(A.REVIEW_LOCATION),0),1) AS LOC,"
				+ "			 ROUND(NVL(AVG(A.REVIEW_CLEAN),0),1) AS CL,"
				+ "			 ROUND(NVL(AVG(A.REVIEW_KIND),0),1) AS KI"
				+ "		FROM REVIEW A, ROOM B, RESERVATION C"
				+ "    WHERE A.RESERVE_NUM=C.RESERVE_NUM"
				+ "		 AND B.ROOM_ID=C.ROOM_ID"
				+ "		 AND B.STAY_ID = ?"
				+ "	   GROUP BY B.STAY_ID";
		
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectOne(sql, param);
	}
	
	//호스트의 답글 작성
	public int updateRecon(String recon, int num){
		String sql = "UPDATE REVIEW"
				+ "      SET REVIEW_CONCON = ?"
				+ "	   WHERE REVIEW_NUM = ?";
		ArrayList<Object> param = new ArrayList<>();
		param.add(recon);
		param.add(num);
		return jdbc.update(sql, param);
	}
	
	//현재 답글이 달리지 않은 후기 보기
	public List<Map<String, Object>> nonRecon(int stayId){
		String sql = "SELECT A.REVIEW_NUM,"
				+ " 		 A.REVIEW_CON,"
				+ "			 A.REVIEW_LOCATION,"
				+ "			 A.REVIEW_CLEAN,"
				+ "			 A.REVIEW_KIND,"
				+ " 		 A.MEM_ID,"
				+ "			 A.RESERVE_NUM"
				+ "		FROM REVIEW A, ROOM B, RESERVATION C"
				+ "	   WHERE A.MEM_ID=C.MEM_ID"
				+ "		 AND B.ROOM_ID=C.ROOM_ID"
				+ "		 AND B.STAY_ID=?"
				+ "MINUS"
				+ "	  SELECT A.REVIEW_NUM,"
				+ " 	 	 A.REVIEW_CON,"
				+ " 	 	 A.REVIEW_LOCATION,"
				+ " 		 A.REVIEW_CLEAN,"
				+ " 		 A.REVIEW_KIND,"
				+ "		     A.MEM_ID,"
				+ "		 	 A.RESERVE_NUM"
				+ "		FROM REVIEW A, ROOM B, RESERVATION C"
				+ "    WHERE A.MEM_ID=C.MEM_ID"
				+ "		 AND B.ROOM_ID=C.ROOM_ID"
				+ "		 AND A.REVIEW_CONCON!='NULL'";
		ArrayList<Object> param = new ArrayList<>();
		param.add(stayId);
		return jdbc.selectList(sql, param);
	}
	
	//게스트의 후기 작성
	public int inRe(String con, int loc, int cl, int ki, String memId, int renum){
		String sql = "INSERT INTO REVIEW"
				+ "		VALUES ((SELECT NVL(MAX(REVIEW_NUM),0) + 1 FROM REVIEW),"
				+ "			    ?,?,?,?,?,?,'')";
		ArrayList<Object> param = new ArrayList<>();
		param.add(con);
		param.add(loc);
		param.add(cl);
		param.add(ki);
		param.add(memId);
		param.add(renum);
		return jdbc.update(sql, param);
	}

}
