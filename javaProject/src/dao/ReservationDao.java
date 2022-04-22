package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ReservationDao {
	// 싱글톤 패턴
    private ReservationDao(){}
    private static ReservationDao instance;
    public static ReservationDao getInstance() {
       if (instance == null) {
          instance = new ReservationDao();
       }
       return instance;
    }
    
    private JDBCUtil jdbc = JDBCUtil.getInstance();
    
    // 숙소명, 방이름, 숙박일수만큼의 방 가격, 마일리지 출력
    public Map<String, Object> selectRoomInfo(int stayId, int roomId, String checkInDate, String checkOutDate) {
       String sql = "SELECT A.STAY_NAME,"
             + "          B.ROOM_NAME,"
             + "          (B.ROOM_PRICE*(TO_DATE(?)-TO_DATE(?))) AS PRICE,"
             + "          NVL(C.MEM_MILE,0) AS MILE"
             + "     FROM STAY A, ROOM B, MEMBER C"
             + "      WHERE A.STAY_ID = B.STAY_ID"
             + "       AND A.MEM_ID = C.MEM_ID"
             + "        AND A.STAY_ID = ?"
             + "      AND B.ROOM_ID = ?";
             
       List<Object> param = new ArrayList<>();
       
       param.add(checkOutDate);
       param.add(checkInDate);
       param.add(stayId);
       param.add(roomId);
       
       return jdbc.selectOne(sql, param);
    }
    
    // 로그인 한 게스트가 보유하고 있는 마일리지 출력
    public Map<String, Object> selectMile(String memId){
       String sql = "SELECT NVL(MEM_MILE,0) AS MEM_MILE"
             + "     FROM MEMBER"
             + "    WHERE MEM_ID = ?";
       List<Object> param = new ArrayList<>();
       param.add(memId);
       
       return jdbc.selectOne(sql, param);
    }
    
    // 결제 후 RESERVATION 테이블에 정보 삽입
    public int insertReserve(Map<String, Object> param) {
       String sql = "INSERT INTO RESERVATION"
             + "      VALUES((SELECT NVL(MAX(RESERVE_NUM),0)+1 FROM RESERVATION),"
             + "            ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?)";

       List<Object> p = new ArrayList<>();
       p.add(param.get("ROOM_ID"));
       p.add(param.get("MEM_ID"));
       p.add(param.get("RESERVE_IN"));
       p.add(param.get("RESERVE_OUT"));
       p.add(param.get("PAY_WAY"));
       p.add(param.get("RESERVE_PRICE"));
       p.add(param.get("RESERVE_PEOPLE_NUM"));
       p.add(param.get("PAY_MILE"));

       return jdbc.update(sql, p); // 몇 개의 행이 리턴받았는지 리턴
    }
    
    // 변경 된 마일리지 수정
    public int updateMile(int remainMile, String memId){

       String sql = "UPDATE MEMBER"
             + "       SET MEM_MILE = ?"
             + "    WHERE MEM_ID = ?";
       
       List<Object> param = new ArrayList<>();
       param.add(remainMile);
       param.add(memId);
       
       return jdbc.update(sql, param);
    }

}
