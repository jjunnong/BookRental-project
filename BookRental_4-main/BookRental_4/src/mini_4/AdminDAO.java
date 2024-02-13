package mini_4;

import java.sql.*;
import java.util.*;

import mini_4.singleton.dbconnection.ProjectDBConnection;

public class AdminDAO implements InterAdminDAO{

    // attribute, property, 속성
    Connection conn;// 초기 null값을 주지않아도 자동으로 null 값이 주어짐
    PreparedStatement pstmt;// 초기 null값을 주지않아도 자동으로 null 값이 주어짐
    ResultSet rs;// 초기 null값을 주지않아도 자동으로 null 값이 주어짐
    
    // === 자원반납 메소드 === //
    public void close() {
       
       try {
          
          if(rs != null) rs.close();
          if(pstmt != null) pstmt.close();
          
       } catch (SQLException e) {
          e.printStackTrace();
       }
       
    }// end of private void close()------

 // 사서 가입
    @Override
    public int adminResister(AdminDTO admin, Scanner sc) {
       
       int result= 0;
       
       try {
          
          conn = ProjectDBConnection.getConn();
          
          String sql = " insert into tbl_admin(adminseq, adminid, passwd) "+
                     " values (adminseq.nextval, ?, ?)";
       
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, admin.getAdminid());
          pstmt.setString(2, admin.getPasswd());
          
          result = pstmt.executeUpdate();
          
          if(result == 1) {
                String yn = "";
                do {
                   System.out.print(">> 사서가입을 정말로 하시겠습니까?[Y/N] ");
                   yn = sc.nextLine();
                   
                   if("Y".equalsIgnoreCase(yn)) {
                      conn.commit(); // 커밋
                      result = 1;
                      break;
                   }
                   else if("N".equalsIgnoreCase(yn)) {
                      conn.rollback(); // 롤백
                      result = 0;
                      break;
                   }
                   else {
                      System.out.println(">>> Y 또는 N 만 입력하세요!! \n");
                   }
                   
                } while (true);
             }// end of if()---------------
          
       } catch (SQLIntegrityConstraintViolationException e) {
          if(e.getErrorCode() == 1) {
             result = -1;
          }
       } catch (SQLException e) {
          result = -2;
       } finally {
          close();
       }
       
       return result; // 0값 리턴 => 회원가입 취소
                   // 1값 리턴 => 정상적인 회원가입
                   // -1값 리턴 => 사용자 아이디에 중복이 발생한 것
                   // -2값 리턴 => 오류발생
       
    }// end of public int adminResister(AdminDTO admin, Scanner sc)------------

	//사서_로그인
	@Override
	public AdminDTO login(Map<String, String> paraMap) {
		  AdminDTO admin = null;
	         
	         try {
	            
	            conn = ProjectDBConnection.getConn(); 
	            
	            String sql = "select adminseq, adminid, passwd \n"+
	                    "from tbl_admin \n"+
	                    "where adminid = ? and passwd = ? ";
	         
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, paraMap.get("userid"));
	            pstmt.setString(2, paraMap.get("passwd"));
	      
	            rs = pstmt.executeQuery();
	            
	            if(rs.next()) {
	               admin = new AdminDTO();
	               admin.setAdminseq(rs.getString(1));
	               admin.setAdminid(rs.getString(2));
	               admin.setPasswd(rs.getString(3));
	             }
	         } catch (SQLException e) {
	             e.printStackTrace();
	         } finally {
	            close();
	         }
	         
	         return admin;
	      }// end of public AdminDTO login(Map<String, String> paraMap)---------------

	// 6. 대여중인 도서 조회
    @Override
    public List<Map<String, String>> viewRentalBooks() {
       List<Map<String, String>> mapList =  new ArrayList<Map<String,String>>();
       
       try {
          conn = ProjectDBConnection.getConn();
          
          String sql = " select B.bookid, B.ISBN, B.bookname, B.bookauthor, B.bookpbls, M.userid, M.name, M.mobile , to_char(B.bookrentday, 'yyyy-mm-dd') as bookrentday \n"+
                    " from tbl_member M join tbl_book B\n"+
                    " on M.userid =  B.bookrentmember\n"+
                    " order by bookrentday desc";
          
          
          pstmt = conn.prepareStatement(sql);
          
          rs = pstmt.executeQuery();
          
          while(rs.next()) {
             Map<String, String> map = new HashMap<String, String>();
             map.put("bookid", rs.getString(1));
             map.put("ISBN", rs.getString(2));
             map.put("bookname", rs.getString(3));
             map.put("bookauthor", rs.getString(4));
             map.put("bookpbls", rs.getString(5));
             map.put("userid", rs.getString(6));
             map.put("name", rs.getString(7));
             map.put("mobile", rs.getString(8));
             map.put("bookrentday", rs.getString(9));
                        
             mapList.add(map);
          }//end of while(rs.next())
         
       } catch (SQLException e) {
          e.printStackTrace();
       } finally {
          close();
       }      
       
       return mapList;
    }// end of public List<Map<String, String>> viewRentalBooks()------------------------

   
    
}
