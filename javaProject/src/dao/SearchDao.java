package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;



public class SearchDao {

   // 싱글톤 패턴
      private SearchDao() {}

      private static SearchDao instance;

      public static SearchDao getInstance() {
         if (instance == null) {
            instance = new SearchDao();
         }
         return instance;
      }
      
      private JDBCUtil jdbc = JDBCUtil.getInstance();
      
      public List<Map<String, Object>> selectRegionList(int input) {		//대분류 지역 선택 후 소분류 지역 추출
         String sql = "SELECT REGION_ID, REGION_NAME"
               + "     FROM REGION"
               + "    WHERE REGION_LARGE = ?";
         ArrayList<Object> param = new ArrayList<>();
         param.add(input);
         return jdbc.selectList(sql, param);
      }

      public List<Map<String, Object>> selectStayList() {			//지역,날짜,인원 조건에 맞는 숙소리스트 추출
         String sql = "SELECT DISTINCT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"
               + "     FROM STAY A"
               + "     LEFT OUTER JOIN ROOM B ON A.STAY_ID=B.STAY_ID"
               + "    WHERE A.REGION_ID=?"
               + "      AND B.ROOM_NUM_PEOPLE>=?";
         ArrayList<Object> param = new ArrayList<>();
         param.add(Controller.region);
         param.add(Controller.peopleNum);
         return jdbc.selectList(sql, param);
      }

      public List<Map<String, Object>> selectStayDetail(Object input) {	//방리스트 중 인원조건 만족하고 예약이 없는 방 출력
         String sql = "SELECT A.ROOM_ID, A.ROOM_NAME, A.ROOM_PRICE, A.ROOM_NUM_PEOPLE"
               + "      FROM ROOM A"
               + "    WHERE A.STAY_ID=?"
               + "       AND A.ROOM_NUM_PEOPLE>=?"
               + " MINUS"
               + "   SELECT A.ROOM_ID, A.ROOM_NAME, A.ROOM_PRICE, A.ROOM_NUM_PEOPLE"
               + "     FROM ROOM A, (SELECT ROOM_ID"
               + "                  FROM RESERVATION"
               + "                  WHERE TO_DATE(?) > RESERVE_IN AND TO_DATE(?) < RESERVE_OUT"
               + "                    OR TO_DATE(?) > RESERVE_IN AND TO_DATE(?) < RESERVE_OUT"
               + "                    OR TO_DATE(?) = RESERVE_IN AND TO_DATE(?) = RESERVE_OUT)B"
               + "      WHERE A.STAY_ID=?"
               + "      AND A.ROOM_ID=B.ROOM_ID";
         ArrayList<Object> param = new ArrayList<>();
         param.add(input);
         param.add(Controller.peopleNum);
         param.add(Controller.checkInDate);
         param.add(Controller.checkInDate);
         param.add(Controller.checkOutDate);
         param.add(Controller.checkOutDate);
         param.add(Controller.checkInDate);
         param.add(Controller.checkOutDate);
         param.add(input);
         
         return jdbc.selectList(sql, param);
      }
      
      public Map<String, Object> selectAllFilter(Object id, int snum, int rnum) {	//편의시설과 방특성을 적용한 숙소 추출
         String sql = "SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"               
               + "       FROM STAY A"
               + "    WHERE A.STAY_ID=?"
               + "INTERSECT"
               + "   SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"
               + "     FROM STAY A"
               + "     LEFT OUTER JOIN CONVENIENT B ON A.STAY_ID=B.STAY_ID"
               + "    WHERE B.FILTER_ID=?"
               + "INTERSECT"
               + "   SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"
               + "     FROM STAY A"
               + "     LEFT OUTER JOIN ROOM C ON A.STAY_ID=C.STAY_ID"
               + "     LEFT OUTER JOIN ROOM_FEATURE D ON C.ROOM_ID=D.ROOM_ID"
               + "    WHERE D.FILTER_ID=?";
         ArrayList<Object> param = new ArrayList<>();
         param.add(id);
         param.add(snum);
         param.add(rnum);
         return jdbc.selectOne(sql, param);
      }
      
      public Map<String, Object> selectRoomFilter(Object id, int rnum) {		//방특성만 적용한 숙소 추출
         String sql = "SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"               
               + "       FROM STAY A"
               + "    WHERE A.STAY_ID=?"
               + "INTERSECT"
               + "   SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"
               + "     FROM STAY A"
               + "     LEFT OUTER JOIN ROOM C ON A.STAY_ID=C.STAY_ID"
               + "     LEFT OUTER JOIN ROOM_FEATURE D ON C.ROOM_ID=D.ROOM_ID"
               + "    WHERE D.FILTER_ID=?";
         ArrayList<Object> param = new ArrayList<>();
         param.add(id);
         param.add(rnum);
         return jdbc.selectOne(sql, param);
      }
      public Map<String, Object> selectStayFilter(Object id, int snum) {		//편의시설만 적용한 숙소 추출
         String sql = "SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"               
               + "       FROM STAY A"
               + "    WHERE A.STAY_ID=?"
               + "INTERSECT"
               + "   SELECT A.STAY_ID"
               + "        , A.STAY_NAME"
               + "        , A.STAY_ADDR1"
               + "         , A.STAY_ADDR2"
               + "         , A.STAY_NOTICE"
               + "         , A.STAY_TEL"
               + "         , A.STAY_APPROVE"
               + "     FROM STAY A"
               + "     LEFT OUTER JOIN CONVENIENT B ON A.STAY_ID=B.STAY_ID"
               + "    WHERE B.FILTER_ID=?";
         ArrayList<Object> param = new ArrayList<>();
         param.add(id);
         param.add(snum);
         return jdbc.selectOne(sql, param);
      }

      public List<Map<String, Object>> selectFilter() {		//필터 종류 추출
         String sql = "SELECT FILTER_ID"
               + "         , FILTER_NAME"
               + "        , FILTER_TYPE"
               + "        , FILTER_NAME"
               + "     FROM FILTER"
               + "      ORDER BY 1";         
         return jdbc.selectList(sql);
      }
      
      public Map<String, Object> score(Object object) {		//숙소에 대한 평점 추출
          String sql = "SELECT NVL(ROUND(AVG(A.REVIEW_LOCATION), 1), 0) AS AREL"
                + "        , NVL(ROUND(AVG(A.REVIEW_CLEAN), 1), 0) AS AREC"
                + "        , NVL(ROUND(AVG(A.REVIEW_KIND), 1), 0) AS AREK"
                + "     FROM REVIEW A"
                + "     LEFT OUTER JOIN RESERVATION B ON A.RESERVE_NUM=B.RESERVE_NUM"
                + "     LEFT OUTER JOIN ROOM C ON B.ROOM_ID=C.ROOM_ID"
                + "     LEFT OUTER JOIN STAY D ON C.STAY_ID=D.STAY_ID"
                + "    WHERE D.STAY_ID = ?";
          ArrayList<Object> param = new ArrayList<>();
          param.add(object);
          return jdbc.selectOne(sql, param);
       }

       public List<Map<String, Object>> selectCon(Object object) {	//숙소에 적용한 편의시설명 추출
          String sql = "SELECT B.FILTER_NAME"
                + "     FROM CONVENIENT A"
                + "     LEFT OUTER JOIN FILTER B ON A.FILTER_ID=B.FILTER_ID"
                + "    WHERE A.STAY_ID = ?";
          ArrayList<Object> param = new ArrayList<>();
          param.add(object);
          return jdbc.selectList(sql, param);
       }
}