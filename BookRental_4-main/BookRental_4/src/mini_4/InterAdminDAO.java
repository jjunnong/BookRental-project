package mini_4;

import java.util.*;

public interface InterAdminDAO {

	int adminResister(AdminDTO admin, Scanner sc); // 사서가입
		
	AdminDTO login(Map<String, String> paraMap);// 로그인처리(select) 메소드

	List<Map<String, String>> viewRentalBooks();// 대여중인도서조회 


	
}
