package mini_4;

import java.sql.*;
import java.util.*;

import mini_4.singleton.dbconnection.ProjectDBConnection;

public class TotalController {

	// attribute, property, 속성
	InterMemberDAO mdao = new MemberDAO();
	InterAdminDAO adao = new AdminDAO();
	InterBookDAO bdao = new BookDAO();
	
	
	// *** 시작메뉴 *** //
	public void menu_start(Scanner sc) {
				
		String s_Choice = "";		
		do {					
			System.out.println("\n ==== > 도서대여 프로그램 < ==== \n"
							 + "1.사서 전용메뉴	   2.일반회원 전용메뉴	   3.프로그램종료   ");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_Choice = sc.nextLine();
			
			switch (s_Choice) {
	         case "1"://사서 전용메뉴
	            adminMenu(sc);
	            break;
	            
	         case "2"://일반회원 전용메뉴
	            memberMenu(sc);
	            break;
	            
	         case "3"://프로그램종료
	            //Connection 객체 자원 반납
	        	 ProjectDBConnection.closeConnection();
	            break;
	            
	         default:
	            System.out.println("\n>>> 메뉴에 없는 번호입니다. 다시 선택하세요 <<<");
	            break;
	         }// end of switch (s_Choice)--------------------------------
			
		} while (!("3".equals(s_Choice)));
		
	}// end of public void menu_start(Scanner sc) ----------------
	
	// 사서 전용 메뉴
	private void adminMenu(Scanner sc) {
		  AdminDTO admin = null;
	      BookDTO bdto = null;
	      
	      String s_Choice = "";
	      
	      do {
	         String loginName = (admin != null)? "[" + admin.getAdminid()+ " 로그인중...]": "";
	         String login_logout = (admin==null)?"로그인":"로그아웃";
	               
	         System.out.println("\n >>> ----- 사서 전용 메뉴 "+loginName+" ----- <<< \n"
	                      + "1.사서가입    2."+login_logout+"  3.도서정보등록    4.개별도서등록\n"
	                      + "5.도서대여해주기    6.대여중인도서조회    7.도서반납해주기    8.나가기");
	         System.out.print("▷메뉴번호 선택 : ");
	         s_Choice = sc.nextLine();
	         
	         switch (s_Choice) {
	         case "1": // 사서가입
	        	 adminResister(sc);
	            break;
	            
	         case "2"://사서 로그인,로그아웃            
	        	 if("로그인".equals(login_logout)) // 로그인시도하기
	                 admin = login(sc);
	             else {
	                 admin = null;// 로그아웃
	                 System.out.println("\n>> 로그아웃 되었습니다. <<");
	             }
	            break;
	            
	         case "3":// 도서정보등록
	        	 if("로그아웃".equals(login_logout)) {//사서 로그인 상태인지 확인하기
	        		 int n = bookRegister(sc);
	        		
		        	if(n== 1) 
						System.out.println(">>> 도서정보등록 성공!! <<<\n");
					else
						System.out.println(">>> 도서정보등록 실패!! <<<\n");
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");	        	
	             break;	
	            
	         case "4"://개별도서등록
	        	 if("로그아웃".equals(login_logout)) {//사서 로그인 상태인지 확인하기
	        		 int n = unitbookRegister(sc,bdto);
	        		
	        		 if(n== 1) 
	 					System.out.println(">>> 개별도서등록 성공!! <<<\n");
	 				 else
	 					System.out.println(">>> 개별도서등록 실패!! <<<\n");

	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
	             break;
	            
	         case "5"://도서대여추가
	        	 if("로그아웃".equals(login_logout)) {//사서 로그인 상태인지 확인하기
	        		 rentBook(sc);
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
	             break;
	            
	         case "6"://대여중인도서조회
	        	 if("로그아웃".equals(login_logout)) {//사서 로그인 상태인지 확인하기
	        		 viewRentalBooks() ;
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
	             break;
	            
	         case "7"://도서반납해주기
	        	 if("로그아웃".equals(login_logout)) {//사서 로그인 상태인지 확인하기
		        	 System.out.println( ">>> 도서 반납하기 <<<");
		        	 returnBooks(sc);
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
	            break;		             
		            
	         default: 
	        	   if(!("8".equals(s_Choice))) {
	               System.out.println("\n>>> 메뉴에 없는 번호입니다. 다시 선택하세요 <<<");
	               break;
	            }
	         }// end of switch (s_Choice)--------------------------------	         
	      } while (!("8".equals(s_Choice)));		
	}//사서전용메뉴


	//1.사서 가입
	private void adminResister(Scanner sc) {
		  System.out.println("\n == 사서 가입 ==");
	      System.out.print("▶  사서ID : ");
	      String adminid = sc.nextLine();
	      
	      System.out.print("▶ 암호 : ");
	      String passwd = sc.nextLine();
	      
	      AdminDTO admin = new AdminDTO();
	      admin.setAdminid(adminid);
	      admin.setPasswd(passwd);
	      
	      int n = adao.adminResister(admin, sc);
	      
         // 1값 리턴 => 정상적인 회원가입
         // -1값 리턴 => 사용자 아이디에 중복이 발생한 것
         // -2값 리턴 => 오류발생
	      switch (n) {
	      case 1:
	         System.out.println("\n >>> 사서등록 성공!! <<<");
	         break;
	         
	      case 0:
	         System.out.println("\n >>> 사서등록을 취소하셨습니다. <<<");
	         break;
	         
	      case -1:
	         System.out.println("\n >>> 아이디가 이미사용중이므로 다른 아이디를 입력하십시오. <<<");
	         break;
	         
	      case -2:
	         System.out.println("\n >>> SQL구문에 오류가 발생하였습니다. <<<");
	         break;
	      }//end of switch()-----------------------
	   }//end of private void adminResister(Scanner sc)----

	//2.사서_로그인
	private AdminDTO login(Scanner sc) {
		AdminDTO admin = null;
		    
	    System.out.println("\n >>> ---- 로그인 ---- <<<");
	    
	    System.out.print("▷ 사서아이디 : ");
	    String adminid = sc.nextLine();
	    System.out.print("▷ 암호 : ");
	    String passwd = sc.nextLine();
	    
	    Map<String, String> paraMap = new HashMap<String, String>();
	    paraMap.put("userid", adminid);
	    paraMap.put("passwd", passwd);
	    
	    admin = adao.login(paraMap);
	    
	    if(admin != null) {
	       System.out.println("\n >>> 로그인 성공!! <<< \n");
	    }
	    else {
	       System.out.println("\n >>> 로그인 실패!! <<< \n");
	    }
	    
	    return admin;
	}//end of private AdminDTO login(Scanner sc)	

	//3.도서정보 등록하기
	private int bookRegister(Scanner sc) {
		int result = 0;
		String ISBN = null;
		
		System.out.println("\n== 도서정보 등록하기 ==");
		
		BookDTO bdto = new BookDTO();
		do {		
			System.out.print("▶ 국제표준도서번호(ISBN) : ");
			ISBN = sc.nextLine();
			bdto.setISBN(ISBN);
			
			if(ISBN.trim().isEmpty()) {
				//엔터 또는 공백일 경우
				System.out.println("~~~ 국제표준도서번호(ISBN)를 입력하세요 !! ~~~\n");
			}
			else {			
				boolean checkISBN = bdao.checkISBN(bdto);
				if(checkISBN) {
					System.out.println("~~~ "+ISBN+" 는 이미 존재하므로 다른 국제표준도서번호(ISBN)를 입력하세요 ~~~");
				}
				else
					break;
			}
		}while (true);
			
		System.out.print("▶ 도서분류카테고리 : ");
		String bookCategory = sc.nextLine();
		System.out.print("▶ 도서명 : ");
		String bookName = sc.nextLine();
		System.out.print("▶ 작가명 : ");
		String bookAuthor = sc.nextLine();
		System.out.print("▶ 출판사 : ");
		String bookPbls = sc.nextLine();
		System.out.print("▶ 출판일자 : ");
		String bookPblsDay = sc.nextLine();
		
		boolean tf = false;
		String bookPrice = null;
		do {
			System.out.print("▶ 가격 : ");
			bookPrice = sc.nextLine();
			
			for(int i= 0; i< bookPrice.length(); i++) {
				char ch_bookPrice = bookPrice.charAt(i);
				if(Character.isDigit(ch_bookPrice) == true)
					tf = true;
				else {
					tf = false;
					System.out.println("~~~ 오류 : 가격은 숫자로만 입력하세요!!! ~~~");
				}
			}
				
		}while(!tf);
		
		bdto.setBookCategory(bookCategory);
		bdto.setBookName(bookName);
		bdto.setBookAuthor(bookAuthor);
		bdto.setBookPbls(bookPbls);
		bdto.setBookPblsDay(bookPblsDay);
		bdto.setBookPrice(bookPrice);

		int n_insert = bdao.bookregister(bdto);
		
		Connection conn = ProjectDBConnection.getConn();
		
		try {
			if(n_insert == 1) {
				conn.commit();
				result = 1;
			}
			else {
				//SQL구문 장애가 발생한 경우
				conn.rollback();
				result = -1;
			}			
		}catch(SQLException e) {			
		}
		return result;
	}//end of private int bookRegister(Scanner sc)

	//4.개별도서등록
	private int unitbookRegister(Scanner sc, BookDTO bdto) {
		
		int result = 0;
		do {
			System.out.println("\n== 개별도서 등록하기==");	
			String ISBN = null;					
			
			System.out.print("▶ 국제표준도서번호(ISBN) : ");
			ISBN = sc.nextLine();	
			if(ISBN.trim().isEmpty()) {
				//엔터 또는 공백일 경우
				System.out.println("~~~ 국제표준도서번호(ISBN)를 입력하세요 !! ~~~\n");
				continue;
			}
			else {	
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("ISBN",ISBN);
				
				BookDTO book = bdao.getbookno(paraMap);
				//현재 paraMap에는 ISBN만 있는 상태이다
				if(book == null) {// ISBN이 book테이블에 존재하지 않는 경우
					System.out.println(">>> 등록된 ISBN이 아닙니다. 도서등록 실패!! <<<");
				}
				else {// ISBN이 book테이블에 존재하는 경우
				  	 //입력한 ISBN과 일치하면서 bookid는 null인 것의 bookno불러오기
					int n_insert =0;
					do {
					System.out.print("▶ 도서아이디 : ");
					String bookId = sc.nextLine();		
											
					paraMap.put("BookId", bookId);
					paraMap.put("BookCategory",book.getBookCategory());
					paraMap.put("BookName",book.getBookName());
					paraMap.put("BookAuthor",book.getBookAuthor());
					paraMap.put("BookPbls",book.getBookPbls());
					paraMap.put("BookPblsDay",book.getBookPblsDay());
					paraMap.put("BookPrice",book.getBookPrice());
				
					n_insert = bdao.unitbookRegister(paraMap);
					// n_insert는 성공시 1 실패시 0이들어온다
					}while(n_insert==-1);
					Connection conn = ProjectDBConnection.getConn();
					try {
						if(n_insert == 1) {
							conn.commit();
							result = 1;
						}
						else {
							// SQL구문 장애가 발생한 경우
							conn.rollback();
						}	
					}catch(SQLException e) {			
					}//end of try ~catch
					}//end of if(book == null) ~ else // ISBN이 book테이블에 존재하는 경우
				break;
				}//end of if(ISBN.trim().isEmpty()) ~ else //엔터 또는 공백일 경우	

		}while(true);
							
		return result;
			
	}// end of private int unitbookRegister(Scanner sc)
	
	 //5. 도서 대여해주기
    private void rentBook(Scanner sc) {
       
       //result==0 등록된 회원 ID가 아닙니다.(회원아이디 다를 경우, 공백 or 엔터)
       //result==1 반납 연체된 회원
       //result==2 대여성공 => 총 대여권수 입력받기 => 
       //               도서ID 입력받기(case나뉨) => 존재하지 않는 도서ID =>다시입력창 뜸(입력창은 총 대여권수만큼 성공할때까지 뜸)
       //            => 대여도서 비치중 -> 대여중으로 변경함

       int n = 0; // 책 대여 상태 대여중이면 1, 대여중아니면 0

       System.out.println("\n>>> 도서대여하기 <<<");
       int cnt = 0;
       int rentNum = 0;
        
          do {
             System.out.print("▶ 회원ID : ");// 회원아이디 입력받기
             String userId= sc.nextLine();
            
             MemberDTO mdto = new MemberDTO();
             mdto.setUserid(userId);
             
             boolean checkmemId = mdao.checkmemId(mdto);
             if(!checkmemId) {
                System.out.println("~~~ 등록된 회원ID가 아닙니다 ~~~");
                continue;
             }

             else{
                // 회원ID가 존재하면 
                // 1. 연체료가 있는 회원의 경우
                
                int result = mdao.howMuchFee(mdto);

                   if(result>0) {
                      System.out.println("~~~~ 반납예정일을 넘긴 미반납된 도서가 존재하므로 도서대여가 불가능합니다.!!!");
                      break;
                   }
                   
                   // 2. 대여가능한 회원의 경우 - 책 대여하기
                   else {
                      System.out.print("▶ 총 대여 권수 : ");
                      rentNum = Integer.parseInt(sc.nextLine());
                       // 총 대여 권수랑 입력한 책 ID 개수가 같으면 반복문 빠져나오기 위해 만듦
                
                      // 도서 ID 받기
                      do {
                         System.out.print("▶ 도서 ID : ");
                         String bookId = sc.nextLine();

                         Map<String, String> paraMap = new HashMap<String, String>();
                         paraMap.put("bookId", bookId);
                         

                         BookDTO bdto = bdao.checkBookId(paraMap);
                         
                         
                         if(!(bdto ==null)) {// 정상적인 id입력
                            if(bdto.getBookstatus() >= 1) {
                               System.out.println("!! 이미 대여된 책입니다. 도서ID입력을 다시 해주세요 !!");
                               
                            }
                            else {
                               cnt++;
                                n = bdao.changeBookStatus(bdto, mdto, sc);
                                
                                if(n == 1) {// 정상적 update
                                  
                                }
                                else if(n == -1) { // update안함
                                   cnt--;
                                  
                                }
                                else if(n == -2) {
                                   // n == 1 이면 정상
                                     // n == -1 이면 롤백
                                     // n == -2면 롤백 이미 대여중인 도서
                                   cnt--;
                                }
                            }
                            
                         }// if 도서아이디 true 일때만
                         else {
                            // 도서아이디가 일치하는 게 없을 때
                            System.out.println("~~~ 존재하지 않는 도서 ID 입니다. 다시 입력하세요!! ~~~");
                         }
                      
                      } while (!(rentNum == cnt));
                      break;
                      

                   }//else
                        
                   }//else


          } while(true);
       
          
          if(n==1) {
          System.out.println("\n>>> 대여 등록 성공 !! <<<");
          System.out.println(">>> 대여도서 비치중에서 대여중으로 변경함 <<<");
          }
          else System.out.println("\n>>> 대여 실패 <<<");
       
    }//end of private void rentBook(Scanner sc)
			
	
       //6.대여중인도서조회
	   private void viewRentalBooks() {
		      

	     System.out.println("=================================================================================================================");
	     System.out.println(" ISBN\t\t\t도서ID\t\t도서명\t\t작가명\t출판사\t회원ID\t회원명\t연락처\t\t대여일자");
	     System.out.println("=================================================================================================================");

	      
	      List<Map<String, String>> mapList = adao.viewRentalBooks();
	      
	      if(mapList.size()>0) {
	         StringBuilder sb = new StringBuilder();
	         for(Map<String, String> map : mapList) {
	         /*   
	            String a = map.get("bookname");
	            
	            if(a != null && a.length()>6) {
	               a = a.substring(0,6)+"..";
	               // 글제목이 10글자보다 크면 8글자만 보여주고 뒤어 ".."을 찍어준다.
	            }
	        */
	            String bookName= map.get("bookname");
	            int bookName_length = map.get("bookname").length();
	          if(bookName_length<3) {
	             bookName+="  ";
	          }
	          if(bookName_length < 12 ) {
	               int length_space = 12 - bookName_length;
	               
	               String str_space = "";
	               
	               for(int i=0; i<length_space; i++) {
	                  str_space += " ";
	               }	               
	               bookName += str_space;
	            }
	            	            
            sb.append(map.get("ISBN") +"\t" +map.get("bookid") + "\t\t"+ bookName +"\t"+ map.get("bookauthor") +"\t"
                  + map.get("bookpbls") +"\t"+ map.get("userid") +"\t"+ map.get("name") +"\t"+map.get("mobile") +"\t"+ map.get("bookrentday") +"\n");

	         }
	         System.out.println(sb.toString());
	      }
	      else {
	         System.out.println(">> 대여중인 도서가 없습니다 <<");
	      }
	      
	   }//end of private void viewRentalBooks()
	
	   //7.도서반납해주기
      private void returnBooks(Scanner sc) {
         
         System.out.print("▶ 총반납권수 : ");
         int NumOfBooks = Integer.parseInt(sc.nextLine());//총반납권수
         int cnt = 0; //정상적인 진행 권수
         int result = 0; //전체적인 흐름

         if(NumOfBooks>0) {
            do {

               System.out.print("▶ 반납도서ID : ");
               String bookid = sc.nextLine();
               
               MemberDTO mdto = new MemberDTO();
               mdto.setRentBook(bookid);
               
               Map<String, String> paraMap = new HashMap<String, String>();
               paraMap.put("bookId", bookid);
               
               BookDTO bdto = bdao.checkBookId(paraMap);
               
               if(!(bdto==null)) {// 정상적인 id입력
                  
                  int num = mdao.returnBooksFee(mdto);// 연체료 확인
                  
                  if(num == 1){ //미납된 연체료가 있을 경우
                     System.out.println("[연체료 납부를 진행해야 반납이 가능합니다 ]");
                     do {
                           String yn = "";
                           
                           System.out.print("\n연체료 납부를 하시겠습니까? [Y/N] : ");
                        yn = sc.nextLine();
                        
                        if("y".equalsIgnoreCase(yn)) {
                           cnt ++;
                           result = bdao.setBookAgain(mdto, sc); // 대여한 도서 비치중으로 업데이트 
                                                              // 1로 return하여 정상적으로 수행
                           
                           break;
                        }
                        else if("n".equalsIgnoreCase(yn)) {
                           result = -1;//연체료 납부 안할시 남은 도서 반납 불가
                           System.out.println("\n>> 연체료 미반납시 나머지 도서 반납불가 <<");
                           break;
                        }
                        else {
                           System.out.println("!! [Y/N] 중 하나만 입력하세요 !!");
                        }
                        } while (true); // end of do~while(true)-------------------------
                     
                  }
                  else if(num == 0) { // 연체료 없을 경우
                     result = bdao.setBookAgain(mdto, sc); // 대여한 도서 비치중으로 업데이트 
                                                   // 1로 return하여 정상적으로 수행
                     cnt ++; // 수행
                     if(result == 2){//반납된 도서일 경우
                        cnt--;
                     }
                     else if(result == -1) {//연체료 납부 안할시 남은 도서 반납 불가
                        cnt = NumOfBooks;
                        break;
                     }
                     else if(result == 1) {//정상반납
                        
                     }
                  }   
                  
                  
               }// if 도서아이디 true 일때만

                  else {
                     // 도서아이디가 일치하는 게 없을 때
                     System.out.println("~~~ 존재하지 않는 도서 ID 입니다. 다시 입력하세요!! ~~~");
                  }
                  
                  
               
            } while (!(NumOfBooks <= cnt)); //입력한 총 반납권수와 진행된 반납 권수가 같을 때까지 반복
               
                  if(result ==1) {
                     System.out.println(">>> 도서 "+NumOfBooks+"권 반납성공!!! <<<");
                     System.out.println(">>> 도서 '대여중'에서 '비치중'으로 반납함 <<<");
                  }
                  else { //연체료 납부 안할시 남은 도서 반납 불가
                     System.out.println(">>> 반납에 실패하였습니다!! <<<");
                  }         
         }// end of if(NumOfBooks>0)---------------------
         else {
            System.out.println(">> 정확한 반납 권수를 입력하세요! (※1 이상의 숫자만 입력) <<");
         }      
      }// end of private void returnBooks()-----------
		
////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// 회원 전용 메뉴 
	private void memberMenu(Scanner sc) {
		MemberDTO member = null;
		BookDTO book = null;
		
		String s_Choice = "";
		
		do {
			
			String login_logout = (member==null)?"로그인":"로그아웃";
			String loginName = (member != null)? "[" + member.getName()+ " 로그인중...]": "";
			
			System.out.println("\n >>> ----- 일반회원 전용 메뉴"+loginName+" ----- <<<\n"
					+ " 1. 일반회원가입   2."+login_logout+"  3.도서검색하기   4.나의대여현황보기   5.신간도서추천   6.나가기\n");
			System.out.print("▷ 메뉴번호 선택 : ");
			s_Choice = sc.nextLine();
			
			switch (s_Choice) {
			case "1"://일반회원가입	
				memberRegister(sc);
				break;
			case "2"://일반회원 로그인, 로그아웃	
				 if("로그인".equals(login_logout)) // 로그인시도하기
	                 member = loginmem(sc);
	               else {
	                  member = null;// 로그아웃
	                   System.out.println("\n>> 로그아웃 되었습니다. <<");
	                }
				break;
			case "3"://도서검색하기		
	        	 if("로그아웃".equals(login_logout)) {//회원 로그인 상태인지 확인하기
	        		 System.out.println( ">>> 도서 검색하기 <<<");
					 bookList(sc, book);
					 break;
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
				break;
			case "4"://나의대여현황보기			
	        	 if("로그아웃".equals(login_logout)) {//회원 로그인 상태인지 확인하기
	        		viewrent(book,member);
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
				break;	
			case "5"://신간도서추천		
	        	 if("로그아웃".equals(login_logout)) {//회원 로그인 상태인지 확인하기
	        		 System.out.println("\n===== 최근 1주일 내에 출간되어 도서관에 구비된 신간도서 목록입니다 =====");
						newBookList(book);
						break;	
	        	 }
	        	 else
	        		 System.out.println(">>> 먼저 로그인 해주세요 <<<");
				break;	
			default: 
				if(!("6".equals(s_Choice))) {
	            System.out.println("\n>>> 메뉴에 없는 번호입니다. 다시 선택하세요 <<<");
	            break;
	         }
		}//end of switch (s_Choice)
		}while(!("6".equals(s_Choice)));
		
		
	}//end of private void memberMenu(Scanner sc)


	//1.일반회원가입	
	private void memberRegister(Scanner sc) {
		
		System.out.println("\n >>> ---- 회원가입 ---- <<<");
		String userid = null;
		MemberDTO member = new MemberDTO();
		
		do {
			System.out.print("1. 아이디 : ");
			userid = sc.nextLine();
					
			member.setUserid(userid);
			
			boolean checkmemId = mdao.checkmemId(member);
			if(checkmemId) {
				System.out.println(">>> "+userid+" 는 이미 존재하므로 다른 회원ID를 입력하세요 <<<");
			}
			else
				break;
		}while(true);
		
		
		System.out.print("2. 비밀번호 : ");
		String passwd = sc.nextLine();
		System.out.print("3. 회원명 : ");
		String name = sc.nextLine();
		System.out.print("4. 연락처 : ");
		String mobile = sc.nextLine();
		
		
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		int n = mdao.memberRegister(member, sc); // 아이디,비밀번호,회원명,연락처가 담아져있는 member를 호출한다
		/*
		   1 : 정상적으로 회원가입
		  -2 : SQL구문에 오류발생
		  -1 : 사용자 아이디에 중복이 발생 
		   0 : 회원가입 취소(롤백)
		 */
		
		if(n==0) {
			System.out.println("\n >>> 회원가입을 취소하셨습니다. <<<");
		}
		else if(n==1)
			System.out.println("\n >>> 회원가입을 축하드립니다. <<<");
		else if(n==-1) {
			System.out.println("\n >>> 아이디가 이미 사용중입니다. 다른 아이디를 입력하세요 <<<");
		}
		else if(n==-2) {
			System.out.println("\n >>> SQL 구문에 오류가 발생함!! <<<");
		}
		
	}//end of private void memberRegister(Scanner sc)

	//2.일반회원 로그인, 로그아웃
	private MemberDTO loginmem(Scanner sc) {
		
		 MemberDTO member = null;
		    
		    System.out.println("\n >>> ---- 로그인 ---- <<<");
		    
		    System.out.print("▷ 회원아이디 : ");
		    String userid = sc.nextLine();
		    System.out.print("▷ 암호 : ");
		    String passwd = sc.nextLine();
		    

		    Map<String, String> paraMap = new HashMap<String, String>();
		    paraMap.put("userid", userid);
		    paraMap.put("passwd", passwd);
		    member = mdao.loginmem(paraMap);

		    if(member != null) {
		       System.out.println("\n >>> 로그인 성공!! <<< \n");
		    }
		    else {
		       System.out.println("\n >>> 로그인 실패!! <<< \n");
		    }
		    
		    return member;
	}//end of private MemberDTO loginmem(Scanner sc)

	//3.도서검색하기	
	private void bookList(Scanner sc, BookDTO book) {
		System.out.println("\n[주의사항] 검색어를 입력치 않고 엔터를 하면 검색 대상에서 제외됩니다.");
				
		System.out.print("▶  도서분류카테고리(Programming, DataBase 등) : ");
		String bookCategory = sc.nextLine();
		if(bookCategory.trim().isEmpty()) {
 			bookCategory = ""; //검색해도 나오지 않을 법한 걸로 바꿔버림
		}
		
		System.out.print("▶  도서명 : ");
		String bookName = sc.nextLine();
		if(bookName.trim().isEmpty()) {
			bookName = "";
		}
		
		System.out.print("▶  작가명 : ");
		String bookAuthor = sc.nextLine();
		if(bookAuthor.trim().isEmpty()) {
			bookAuthor = "";
		}
		
		System.out.print("▶  출판사명 : ");
		String bookPbls = sc.nextLine();
		if(bookPbls.trim().isEmpty()) {
			bookPbls = "";
		}		

		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("bookCategory", bookCategory);
		paraMap.put("bookName", bookName);
		paraMap.put("bookAuthor", bookAuthor);
		paraMap.put("bookPbls", bookPbls);
		
		List<BookDTO> bookList = bdao.bookList(paraMap);
		
		if(bookList.size()>0) {
			System.out.println("\n======================================================================================");
		//	System.out.printf("%-20s %-12s %-10s %-7s %-7s %-7s %-5s","ISBN","도서아이디","도서명", "작가명", "출판사", "가격", "대여상태");
			System.out.println("ISBN\t\t\t도서아이디\t도서명\t\t작가명\t출판사\t가격\t대여상태");

			System.out.println("======================================================================================");

			StringBuilder sb = new StringBuilder();
			
			for(int i=0; i<bookList.size(); i++) {
				sb.append(bookList.get(i).viewBookInfo()+"\n"); //bookList.get(i)는 BookDTO 임.
			}
			System.out.println(sb.toString());

		}
		else System.out.println(">> 일치하는 도서가 없습니다. <<\n");
	
	}//end of private void bookList(Scanner sc, BookDTO book)
	
	//4.나의대여현황보기	
	private void viewrent(BookDTO book, MemberDTO member) {
		System.out.println("\n====================================================================================================================");
		System.out.println("도서ID\t\tISBN\t\t\t도서명\t\t작가명\t출판서\t회원ID\t대여일자\t\t반납예정일\t");
		System.out.println("====================================================================================================================");
		
		BookDTO bdto = new BookDTO();
		bdto.setBookRentMember(member.getUserid());
		
		 List<BookDTO> bookList = bdao.selectAllBook(bdto);
	      
		 if(bookList.size() > 0) {
	            StringBuilder sb = new StringBuilder();
	            
	            for(BookDTO book1 : bookList) {      
	               String bookName=book1.getBookName();
	              int bookName_length = bookName.length();
	              if(bookName_length<3) {
	                 bookName+="  ";
	              }
	             if(bookName_length < 12 ) {
	                   int length_space = 12 - bookName_length;
	                   
	                   String str_space = "";
	                   
	                   for(int i=0; i<length_space; i++) {
	                      str_space += " ";
	                   }
	                   
	                   bookName += str_space;
	                }
	                     
	            sb.append(book1.getBookId()+"\t\t"+
	                    book1.getISBN()+"\t"+
	                    book1.getBookName()+"\t"+
	                    book1.getBookAuthor()+"\t"+
	                    book1.getBookPbls()+"\t"+
	                    book1.getBookRentMember()+"\t"+
	                    book1.getBookRentDay()+"\t"+
	                    book1.getReturnday()+"\n");
	            
	         }// end of for------------------------	         
	         System.out.println(sb.toString());
	      }	      
	      else {
	         System.out.println("~~~ 대여해가신 도서가 없습니다. ~~~");
	      }
	}//private void viewrent(BookDTO book)

	//	*** 신간도서추천	
	private void newBookList(BookDTO book) {
	List<BookDTO> newBookList = bdao.newBookList(book);
	
	if(newBookList.size()>0) {
		System.out.println("\n=========================================================================================================================");
		System.out.println("ISBN\t\t\t도서아이디\t도서카테고리\t도서명\t\t작가명\t출판사\t가격\t출간일");

		System.out.println("=========================================================================================================================");

		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<newBookList.size(); i++) {
			sb.append(newBookList.get(i).viewNewBookInfo()+"\n"); //newBookList.get(i)는 BookDTO 임.
		}
		System.out.println(sb.toString());

	}
	else System.out.println(">> 일치하는 도서가 없습니다. <<\n");
	}
	
	
}
