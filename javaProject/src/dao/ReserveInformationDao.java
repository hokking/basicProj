package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ReserveInformationDao {
	private ReserveInformationDao() {}

    private static ReserveInformationDao instance;

    public static ReserveInformationDao getInstance() {
       if (instance == null) {
          instance = new ReserveInformationDao();
       }
       return instance;
    }

    private JDBCUtil jdbc = JDBCUtil.getInstance();

    // 예약 목록 정보 출력
    public List<Map<String, Object>> selectReserveList(String memId) {
       String sql = "SELECT A.RESERVE_NUM, B.STAY_NAME, "
             + "          TO_CHAR(A.RESERVE_IN,'YY-MM-DD') AS RESERVE_IN, "
             + "          TO_CHAR(A.RESERVE_OUT,'YY-MM-DD') AS RESERVE_OUT, "
             + "          A.RESERVE_PRICE"
             + "     FROM RESERVATION A, STAY B, ROOM C"
             + "    WHERE B.STAY_ID = C.STAY_ID"
             + "      AND A.ROOM_ID = C.ROOM_ID"
             + "      AND A.MEM_ID = ?"
             + "    ORDER BY A.RESERVE_DATE DESC";
       
       List<Object> param = new ArrayList<>();
       param.add(memId);
       
       return jdbc.selectList(sql, param);
    }
    
    // 예약 상세 정보 출력
    public Map<String, Object> selectReserveDetail(int rsvNo){
       String sql = "SELECT A.RESERVE_NUM, B.STAY_NAME, "
             + "          TO_CHAR(A.RESERVE_IN,'YYYY-MM-DD') AS RESERVE_IN, "
             + "          TO_CHAR(A.RESERVE_OUT,'YYYY-MM-DD') AS RESERVE_OUT, "
             + "          TO_CHAR(A.RESERVE_DATE,'YYYY-MM-DD') AS RESERVE_DATE,"
             + "          A.RESERVE_PRICE, A.RESERVE_PEOPLE,"
             + "          C.ROOM_NAME, B.STAY_ADDR1, B.STAY_ADDR2, B.STAY_TEL,"
             + "          TO_CHAR(SYSDATE, 'YYYYMMDD') AS NOWDATE,"
             + "          TO_CHAR(A.RESERVE_IN,'YYYYMMDD') AS RSVDATE,"
             + "          A.MEM_ID, A.PAY_MILE"
             + "     FROM RESERVATION A, STAY B, ROOM C"
             + "    WHERE B.STAY_ID = C.STAY_ID"
             + "      AND A.ROOM_ID = C.ROOM_ID"
             + "      AND A.RESERVE_NUM = ?";
       
       List<Object> param = new ArrayList<>();
       param.add(rsvNo);
       
       return jdbc.selectOne(sql, param);
    }
    
    // 예약 정보 삭제
    public int deleteReserve(int input){
       String sql = "DELETE FROM RESERVATION WHERE RESERVE_NUM = ?";
       ArrayList<Object> param = new ArrayList<>();
       param.add(input);
       
       return jdbc.update(sql, param);
    }
    
    // REVIEW 테이블에서 예약번호의 리뷰가 있을 경우 출력
    public Map<String, Object> selectreview(int input) {
        String sql = "SELECT RESERVE_NUM"
              + "     FROM REVIEW"
              + "    WHERE RESERVE_NUM = ?";
        ArrayList<Object> param = new ArrayList<>();
        param.add(input);
        
        return jdbc.selectOne(sql, param);
     }
    
    // (현재 마일리지 + 사용 마일리지) 수정
    public int updateReserve(int rsvNo, String memId){
        String sql = "UPDATE MEMBER A"
              + "       SET A.MEM_MILE = A.MEM_MILE+(SELECT B.PAY_MILE"
              + "                         FROM RESERVATION B"
              + "                        WHERE B.RESERVE_NUM = ?)"
               + "     WHERE A.MEM_ID = ?";
             ArrayList<Object> param = new ArrayList<>();
             param.add(rsvNo);
             param.add(memId);
             return jdbc.update(sql, param);
    }
    

}
