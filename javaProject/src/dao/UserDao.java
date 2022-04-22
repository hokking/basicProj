package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class UserDao {
		   
		   // 싱글톤 패턴
		   private UserDao(){}
		   private static UserDao instance;
		   public static UserDao getInstance() {
		      if (instance == null) {
		         instance = new UserDao();
		      }
		      return instance;
		   }
		   
		   private JDBCUtil jdbc = JDBCUtil.getInstance();
		    // 회원가입할 경우 각각의 항목이 MEMBER 테이블로 삽입
		   public int insertUser(Map<String, Object> param) {
		      String sql = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, 0, ?, ?)";

		      List<Object> p = new ArrayList<>();
		      p.add(param.get("MEM_ID"));
		      p.add(param.get("MEM_PASS"));
		      p.add(param.get("MEM_NAME"));
		      p.add(param.get("MEM_BIRTH"));
		      p.add(param.get("MEM_TYPE"));
		      p.add(param.get("MEM_PH"));

		      return jdbc.update(sql, p);
		   }

		   // 아이디 비밀번호 입력 후 동일한 아이디 비밀번호 출력
		   public Map<String, Object> selectUser(String memId, String memPass) {
			   // 둘 중 하나라도 맞지 않을 경우 null값 반환
		      String sql = "SELECT MEM_ID, MEM_PASS, MEM_TYPE, MEM_PH"
		            + "      FROM MEMBER"
		            + "      WHERE MEM_ID = ?"
		            + "      AND MEM_PASS = ?";
		            
		      List<Object> param = new ArrayList<>();
		      param.add(memId);
		      param.add(memPass);
		      
		      return jdbc.selectOne(sql, param);
		   }
		   
		   // MEMBER 테이블의 기본키인 MEM_ID값을 전체 출력
		   public  List<Map<String, Object>> selectUserId() { 
		      String sql = "SELECT MEM_ID"
		            + "      FROM MEMBER";
		      
		      return jdbc.selectList(sql);
		   }
		   
		   
		   
		}
