package mini_4;

import java.sql.*;
import java.util.*;

import mini_4.singleton.dbconnection.ProjectDBConnection;


public class MemberDAO implements InterMemberDAO {

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

    //일반회원가입
	@Override
	public int memberRegister(MemberDTO member, Scanner sc) {
		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
		  String sql = " insert into tbl_member(memberseq, userid, passwd, name, mobile) "
		                + " values(memberseq.nextval, ?, ?, ?, ?) ";
		
		   pstmt = conn.prepareStatement(sql);
		   pstmt.setString(1, member.getUserid());
		   pstmt.setString(2, member.getPasswd());
		   pstmt.setString(3, member.getName());
		   pstmt.setString(4, member.getMobile());
			
		   result = pstmt.executeUpdate();
		   
		   if(result == 1) {
	            String yn = "";
	            do {
	               System.out.print(">> 회원가입을 정말로 하시겠습니까?[Y/N] ");
	               yn = sc.nextLine();
	               
	               if("Y".equalsIgnoreCase(yn)) {
	                  conn.commit(); // 커밋
	               }
	               else if("N".equalsIgnoreCase(yn)) {
	                  conn.rollback(); // 롤백
	                  result = 0;
	               }
	               else {
	                  System.out.println(">>> Y 또는 N 만 입력하세요!! \n");
	               }
	               
	            } while (!("Y".equalsIgnoreCase(yn) || "N".equalsIgnoreCase(yn)));
	         }//end of if(result == 1)
		      
		}catch(SQLIntegrityConstraintViolationException e) {
			if(e.getErrorCode() == 1)
			result = -1;
		}catch(SQLException e) {
			result = -2;
		}finally {
			close();
		}
		
		return result; 
		/*
		   1 : 정상적으로 회원가입
		  -2 : SQL구문에 오류발생
		  -1 : 사용자 아이디에 중복이 발생 
		   0 : 회원가입 취소(롤백)
		 */
	}//end of public int memberRegister(MemberDTO member, Scanner sc)


	//일반회원 로그인,로그아웃
	@Override
	public MemberDTO loginmem(Map<String, String> paraMap) {
		 MemberDTO member = null;
         
         try {
            
            conn = ProjectDBConnection.getConn(); 
            
            String sql = "select memberseq, userid, passwd, name\n"+
                    "from tbl_member \n"+
                    "where userid = ? and passwd = ? ";
         
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, paraMap.get("userid"));
            pstmt.setString(2, paraMap.get("passwd"));
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
               member = new MemberDTO();
               member.setMemberseq(rs.getString(1));
               member.setUserid(rs.getString(2));
               member.setPasswd(rs.getString(3));
               member.setName(rs.getString(4));
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
            close();
         }
         
         return member;
	}//end of public MemberDTO loginmem(Map<String, String> paraMap)

	//일반회원 아이디 중복확인
	@Override
	public boolean checkmemId(MemberDTO member) {
		boolean result = false;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " select userid "
						+ " from tbl_member "
					    + " where userid = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,member.getUserid());
			
			rs = pstmt.executeQuery();
			
			if(rs.next())
				result = true;
			else
				result = false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}//end of public int checkmemId(MemberDTO member)
	
	// 연체료 구하기
		@Override
		public int returnBooksFee(MemberDTO mdto) {
			int result = 0;
			
			List<BookDTO> bookList = new ArrayList<>();
			
			try {
	            
	            conn = ProjectDBConnection.getConn(); 
	            
	            String sql = " select BOOKNAME, to_number(to_date(to_char(sysdate, 'yyyy-mm-dd')) - (to_date(to_char( BOOKRENTDAY, 'yyyy-mm-dd')) +14) )*100 as LATEFEE\n"+
		            		 " from tbl_book \n"+
		            		 " where BOOKID = ?";
	         
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, mdto.getRentBook());
	            
	      
	            rs = pstmt.executeQuery();
	            
	            while(rs.next()) {
	            	
	            	BookDTO book = new BookDTO();
	            	
	            	book.setBookName(rs.getString(1));
	            	book.setLatefee(rs.getInt(2));
	            	
	            	bookList.add(book);
	            	
	            	if(book.getLatefee() > 0) {
	            		System.out.println("\n[** 경고 **]");
	            		System.out.println("- 연체된 도서 이름 : "+ book.getBookName());
	            		System.out.println("- 연체료 : "+ book.getLatefee() + "원 \n");
	            		
	            		result = 1; // 연체료가 있을 경우
	            		
	            	}
	            	else if(book.getLatefee() <= 0) { // 연체료가 0일경우 혹은 마이너스(연체료가 아님)
	            		result = 0;
	            	}
	            	else {
	            		result = 2; // rs에 아무것도 담기지 않았을 경우 
	            	}
	            	
	            }
	            	
	            	
	            
			} catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	           close();
	        }
			
			return result;
		}// end of public int returnBooksFee(MemberDTO mdto)-------	

	   // 연체료 구하기
	   @Override
	   public int howMuchFee(MemberDTO mdto) {
	      
	      int result = 0;
	      List<BookDTO> bookList = new ArrayList<>();
	      
	      try {
	            
	            conn = ProjectDBConnection.getConn(); 
	            
	            String sql = " select B.BOOKNAME, to_number(to_date(to_char(sysdate, 'yyyy-mm-dd')) - (to_date(to_char( B.BOOKRENTDAY, 'yyyy-mm-dd')) +14) )*100 as LATEFEE\n"+
	                      " from tbl_member M JOIN tbl_book B\n"+
	                      " ON M.userid =  B.bookrentmember\n"+
	                      " where M.USERID = ?";
	         
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, mdto.getUserid());
	            
	      
	            rs = pstmt.executeQuery();
	            
	            while(rs.next()) {
	               
	               BookDTO book = new BookDTO();
	               
	               book.setBookName(rs.getString(1));
	               book.setLatefee(rs.getInt(2));
	               
	               bookList.add(book);
	               
	               if(book.getLatefee() > 0) {
	                  System.out.println("\n[** 경고 **]");
	                  System.out.println("- 연체된 도서 이름 : "+ book.getBookName());
	                  System.out.println("- 연체료 : "+ book.getLatefee() + "원 \n");
	                  
	                  result = 1;
	               }
	               else {
	                  result = 0;
	               }
	            }
	      } catch (SQLException e) {
	            result = 2;
	            
	        } finally {
	           close();
	        }
	      
	      
	      return result;
	   }
	
}
