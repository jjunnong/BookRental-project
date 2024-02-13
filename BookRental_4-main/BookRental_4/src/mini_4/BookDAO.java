package mini_4;

import java.sql.*;
import java.util.*;

import mini_4.singleton.dbconnection.ProjectDBConnection;

public class BookDAO implements InterBookDAO {

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

    // 도서정보 등록하기
   	@Override
   	public int bookregister(BookDTO bdto) {
   		int result = 0;
   		
   		try {
   			conn = ProjectDBConnection.getConn();
   			
   			String sql = " insert into tbl_book(bookNo, ISBN, bookCategory, bookName, bookAuthor, bookPbls, bookPblsDay, bookPrice) "
   					   + " values(bookidseq.nextval,?,?,?,?,?,?,?) ";
   			
   			pstmt = conn.prepareStatement(sql);
   			
   			pstmt.setString(1,bdto.getISBN());
   			pstmt.setString(2,bdto.getBookCategory());
   			pstmt.setString(3,bdto.getBookName());
   			pstmt.setString(4,bdto.getBookAuthor());
   			pstmt.setString(5,bdto.getBookPbls());
   			pstmt.setString(6,bdto.getBookPblsDay());
   			pstmt.setString(7,bdto.getBookPrice());
   			
   			result = pstmt.executeUpdate();
   			//insert가 성공하면 result에는 1이 들어간다
   			   			
   		} catch (SQLException e) {
   			e.printStackTrace();
   		}finally {
   			close();
   		}
		
		return result;
		// result는 1또는 0을 리턴할 것이다
	}// end of public int bookregister(BookDTO bdto)

	//개별도서등록
	@Override
	public int unitbookRegister(Map<String, String> paraMap) {
		int result = 0;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			
			String sql = " insert into tbl_book(ISBN, BookId, BookCategory, BookName, BookAuthor, BookPbls, BookPblsDay"
					   + " , BookPrice, BookNo) "
					   + " values(?,?,?,?,?,?,?,?,bookidseq.nextval) ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,paraMap.get("ISBN"));
			pstmt.setString(2,paraMap.get("BookId"));
			pstmt.setString(3,paraMap.get("BookCategory"));
			pstmt.setString(4,paraMap.get("BookName"));
			pstmt.setString(5,paraMap.get("BookAuthor"));
			pstmt.setString(6,paraMap.get("BookPbls"));
			pstmt.setString(7,paraMap.get("BookPblsDay"));
			pstmt.setString(8,paraMap.get("BookPrice"));
			
			result = pstmt.executeUpdate();
			//insert가 성공하면 result에는 1이 들어간다
			
			
		}catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("이미 등록된 도서id입니다");
			result =-1;
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return result;
	}//end of public int unitbookRegister(Map<String, String> paraMap) 

	//ISBN유무확인
	@Override
	public boolean checkISBN(BookDTO bdto) {
		boolean result = false;
		
		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = " select ISBN "
						+ " from tbl_book "
					    + " where ISBN = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,bdto.getISBN());
			
			rs = pstmt.executeQuery();
		
			if( rs.next())
				result = true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}		
		return result;
	}//end of public int checkISBN(BookDTO bdto)
	
	 // 대여중에서 비치중으로 변경 하는 메소드 <대여중(1)-> 비치중(0)으로 update>
    @Override
    public int setBookAgain(MemberDTO mdto, Scanner sc) {
       int result = 0;
       
       BookDTO bdto = new BookDTO();
       
       try {
          conn = ProjectDBConnection.getConn();
          String sql = "update tbl_book set bookstatus = bookstatus -1, bookrentday = null, bookRentMember = null\n"+
                    "where bookid = ?";// 대여중 -> 비치중 , 빌린날짜 reset, 빌린사람id reset
          
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, mdto.getRentBook());
          
          result = pstmt.executeUpdate();
          //성공시 1
          
          if(result == 1) {
             
                 sql =      " select BOOKSTATUS, BOOKNAME\n"+
                        " from tbl_book\n"+
                        " where bookid =  ? ";
                   pstmt = conn.prepareStatement(sql);
                   pstmt.setString(1, mdto.getRentBook());
                   
                   rs = pstmt.executeQuery(); 
                   
                   
                   rs.next();
                   
                   bdto.setLatefee(rs.getInt(1));
                   bdto.setBookName(rs.getString(2));
                   

                
                if(bdto.getLatefee() < 0) {
                   System.out.println(">>> 이미 반납된 도서입니다! 다시 입력하세요 <<<");
                   conn.rollback();
                   result = -2;
                }
                else {
                   do {
                      String yn = "";
                      
                      System.out.print("\n도서 이름  >>"+ bdto.getBookName()+"<< 를 반납을 완료하시겠습니까? [Y/N] : ");
                   yn = sc.nextLine();
                   
                   if("y".equalsIgnoreCase(yn)) {
                      conn.commit();
                      result = 1;
                      break;
                      
                   }
                   else if("n".equalsIgnoreCase(yn)) {
                      conn.rollback();
                      result = -1;
                      break;
                   }
                   else {
                      System.out.println("!! [Y/N] 중 하나만 입력하세요 !!");
                   }
                   } while (true); // end of do~while(true)-------------------------
                }

          }
          
       } catch (SQLException e) {
          e.printStackTrace();
       } finally {
          close();
       }     
       return result;
    }// end of public int setBookAgain(BookDTO bdto, Scanner sc)----------------

	//나의대여현황보기
	@Override
	public List<BookDTO> selectAllBook(BookDTO bdto) {
		
		List<BookDTO> bookList = new ArrayList<>();
		
		try {
		
		conn = ProjectDBConnection.getConn(); 
		String sql = " select ISBN, BookName, BookAuthor, BookPbls, BookRentMember, to_char(BookRentDay,'yyyy-mm-dd'), bookid "
				+ " ,to_char(BookRentDay+7,'yyyy-mm-dd')"
				+ " from tbl_book "
				+ " where BookRentMember = ? ";
		
		pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, bdto.getBookRentMember());
  
        rs = pstmt.executeQuery();
        
        while(rs.next()) {
        	BookDTO book = new BookDTO();
        	
        	book.setISBN(rs.getString(1));
        	book.setBookName(rs.getString(2));
        	book.setBookAuthor(rs.getString(3));
        	book.setBookPbls(rs.getString(4));
        	book.setBookRentMember(rs.getString(5));
        	book.setBookRentDay(rs.getString(6));
        	book.setBookId(rs.getString(7));
        	book.setReturnday(rs.getString(8));
        	
        	bookList.add(book);
        }//end of while(rs.next())
        
	   } catch (SQLException e) {
             e.printStackTrace();
       } finally {
            close();
       }    
		return bookList;
	}//end of public List<BookDTO> selectAllBook(BookDTO bdto)

	 // 도서 대여 상태 변경
	   @Override
	   public int changeBookStatus(BookDTO bdto, MemberDTO mdto, Scanner sc) {

	      int result =0;
	        
	          try {
	             Connection conn = ProjectDBConnection.getConn();
	               // conn 은 수동commit 으로 되어져 있다.
	               
	               String sql = " update tbl_book set bookstatus = bookstatus + 1 "+ 
	                         "                   , bookrentmember = ? , bookrentday = sysdate "+
	                          " where bookID = ? ";
	               
	               pstmt = conn.prepareStatement(sql);
	               pstmt.setString(1, mdto.getUserid());
	               pstmt.setString(2, bdto.getBookId());
	               
	               result = pstmt.executeUpdate();
	               // update 가 성공되어지면 result 에는 1이 들어간다.
	               
	               String yn = "";
	               
	               if(result == 1) {
	                  
	                  do {
	                     System.out.print("도서 이름  >>"+ bdto.getBookName()+"<< 를 대여 하시겠습니까? [Y/N]");
	                  yn = sc.nextLine();
	                  
	                  if("y".equalsIgnoreCase(yn)) {
	                     conn.commit();
	                     result = 1;
	                     break;
	                     
	                  }
	                  else if("n".equalsIgnoreCase(yn)) {
	                     conn.rollback();
	                     result = -1;
	                     break;
	                  }
	                  else {
	                     System.out.println("!! [Y/N] 중 하나만 입력하세요 !!");
	                     
	                  }
	               } while (true);
	                  
	               }
	               
	         
	          } catch (SQLException e) {
	             e.printStackTrace();
	          }
	          finally {
	             close();
	          }
	      
	      return result;
	   }//end of public int changeBookStatus(BookDTO book)
		
	// bookID 맞는 지 확인 후 select id/bookname
    @Override
    public BookDTO checkBookId(Map<String, String> paraMap) {
    
       BookDTO bdto = null;
        
        try {
           conn = ProjectDBConnection.getConn();
           
           String sql = " select bookid, BOOKNAME, bookstatus "
                       + " from tbl_book "
                        + " where bookid = ? ";
              
              pstmt = conn.prepareStatement(sql);
              
              pstmt.setString(1,paraMap.get("bookId"));
              
              rs = pstmt.executeQuery();
           
              //존재하면 true 없으면 false
             if( rs.next()) {
        
                   bdto = new BookDTO();
                     bdto.setBookId(rs.getString(1));
                     bdto.setBookName(rs.getString(2));
                     bdto.setBookstatus(rs.getInt(3));
                
                
              }

           
        } catch (SQLException e) {
           e.printStackTrace();
        } finally {
           close();
        }      
        
        return bdto;
     }// end of public int checkBookId(BookDTO bdto)-----------------------

	//입력한 ISBN과 일치하면서 bookid는 null인 것의 데이터 불러오기
	@Override
	public BookDTO getbookno(Map<String, String> paraMap) {
		BookDTO bdto = null;
		
		try {
			conn = ProjectDBConnection.getConn();
			String sql = " select  ISBN, BookCategory, BookName, BookAuthor, BookPbls, BookPblsDay"
						+ " , BookPrice, BookNo"+
					 	 " from tbl_book "+
						 " where ISBN = ? and bookID is null";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, paraMap.get("ISBN"));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bdto = new BookDTO();
				bdto.setISBN(rs.getString(1));
				bdto.setBookCategory(rs.getString(2));
				bdto.setBookName(rs.getString(3));
				bdto.setBookAuthor(rs.getString(4));
				bdto.setBookPbls(rs.getString(5));
				bdto.setBookPblsDay(rs.getString(6));
				bdto.setBookPrice(rs.getString(7));
				bdto.setBookNo(rs.getString(8));
				
			}//end of if(rs.next())
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		
		return bdto;
	}//end of public BookDTO getbookno(Map<String, String> paraMap)

	// 검색된 책 목록 나타내주는 메소드 
	@Override
	public List<BookDTO> bookList(Map<String, String> paraMap) {
		// 도서 목록 보기
		List<BookDTO> bookList = new ArrayList<>();

		try {
			conn = ProjectDBConnection.getConn();
			
			String sql = "select ISBN, bookid, bookname, bookauthor, bookpbls, bookprice, bookstatus\n"+
					"from tbl_book\n"+
					"where( upper (bookCategory) like upper (?)\n"+
					"and upper (bookname) like upper (?)\n"+
					"and upper (bookAuthor) like upper (?)\n"+
					"and upper (bookPbls) like upper (?))\n"+
					"and bookid is not null";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, "%"+paraMap.get("bookCategory")+"%");
			pstmt.setString(2, "%"+paraMap.get("bookName")+"%");
			pstmt.setString(3, "%"+paraMap.get("bookAuthor")+"%");
			pstmt.setString(4, "%"+paraMap.get("bookPbls")+"%");

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookDTO bdto = new BookDTO();
				
				bdto.setISBN(rs.getString(1));
				bdto.setBookId(rs.getString(2));
				bdto.setBookName(rs.getString(3));
				bdto.setBookAuthor(rs.getString(4));
				bdto.setBookPbls(rs.getString(5));
				bdto.setBookPrice(rs.getString(6));
				bdto.setBookstatus(rs.getInt(7));
				
				/////////////////////////////////////////////
				
				bookList.add(bdto);
			}// end of if(rs.next())--------------------
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return bookList;
	}//end of public List<BookDTO> bookList(Map<String, String> paraMap)

	// 신간도서 목록
	@Override
	public List<BookDTO> newBookList(BookDTO book) {
		List<BookDTO> bookList = new ArrayList<>();

		try {
			conn = ProjectDBConnection.getConn();


			String sql = "\n"+
			"select ISBN, BOOKID, Bookcategory, bookname, bookAuthor, bookpbls, bookprice, bookpblsday\n"+
			"from tbl_book\n"+
			"where to_date(to_char(sysdate, 'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(bookpblsday, 'yyyy-mm-dd')<7"
			+ " and bookid is not null ";
			
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while(rs.next()) {
				BookDTO bdto = new BookDTO();
				
				bdto.setISBN(rs.getString(1));
				bdto.setBookId(rs.getString(2));
				bdto.setBookCategory(rs.getString(3));
				bdto.setBookName(rs.getString(4));
				bdto.setBookAuthor(rs.getString(5));
				bdto.setBookPbls(rs.getString(6));
				bdto.setBookPrice(rs.getString(7));
				bdto.setBookPblsDay(rs.getString(8));
				
				/////////////////////////////////////////////
				
				bookList.add(bdto);
			}// end of if(rs.next())----------
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return bookList;
	}

		

}
