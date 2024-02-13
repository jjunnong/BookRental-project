package mini_4;

public class BookDTO {


	   private String ISBN; //   국제표준도서번호(ISBN)  ex. 111-22-33333-44-5
	   private String bookId; // 도서 아이디
	   private String bookCategory; // 도서분류카테고리 
	   private String bookName; // 도서명
	   private String bookAuthor; // 작가명
	   private String bookPbls; // 출판사
	   private String bookPrice; // 책가격
	   private int bookstatus; // 책대여상태
	   private String bookRentDay; // 책 대여날짜
	   private String bookPblsDay; // 책 출판날짜
	   private String bookRentMember; // 책 대여해간 사람 
	   private String bookNo;		//책번호(프라이머리)
	   
	   private String returnday; //반납예정일 tbl_bookk column아님
	   private int latefee;
	   
	   
	   public int getLatefee() {
		return latefee;
		}
		public void setLatefee(int latefee) {
			this.latefee = latefee;
		}
		public String getBookNo() {
		   return bookNo;
       }
	   public void setBookNo(String bookNo) {
			this.bookNo = bookNo;
	   }
	   public String getReturnday() {
		return returnday;
		}
		public void setReturnday(String returnday) {
			this.returnday = returnday;
		}
		public String getBookAuthor() {
		return bookAuthor;
		}
		public void setBookAuthor(String bookAuthor) {
			this.bookAuthor = bookAuthor;
		}
		public String getISBN() {
	      return ISBN;
	   }
	   public void setISBN(String iSBN) {
	      ISBN = iSBN;
	   }
	   public String getBookId() {
	      return bookId;
	   }
	   public void setBookId(String bookId) {
	      this.bookId = bookId;
	   }
	   public String getBookCategory() {
	      return bookCategory;
	   }
	   public void setBookCategory(String bookCategory) {
	      this.bookCategory = bookCategory;
	   }
	   public String getBookName() {
	      return bookName;
	   }
	   public void setBookName(String bookName) {
	      this.bookName = bookName;
	   }
	   public String getBookPbls() {
	      return bookPbls;
	   }
	   public void setBookPbls(String bookPbls) {
	      this.bookPbls = bookPbls;
	   }
	   public String getBookPrice() {
	      return bookPrice;
	   }
	   public void setBookPrice(String bookPrice) {
	      this.bookPrice = bookPrice;
	   }
	   public int getBookstatus() {
	      return bookstatus;
	   }
	   public void setBookstatus(int bookstatus) {
	      this.bookstatus = bookstatus;
	   }
	   public String getBookRentDay() {
	      return bookRentDay;
	   }
	   public void setBookRentDay(String bookRentDay) {
	      this.bookRentDay = bookRentDay;
	   }
	   public String getBookPblsDay() {
	      return bookPblsDay;
	   }
	   public void setBookPblsDay(String bookPblsDay) {
	      this.bookPblsDay = bookPblsDay;
	   }
	   public String getBookRentMember() {
	      return bookRentMember;
	   }
	   public void setBookRentMember(String bookRentMember) {
	      this.bookRentMember = bookRentMember;
	   }
	   
	   public String viewBookInfo() { // ISBN\t도서아이디\t도서명\t작가명\t출판사\t가격\t대여상태
	         
	         
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
	         
	            String status = (bookstatus==0)? "비치중":"대여중";         
	         
	         String viewInfo = ISBN+"\t"+bookId+"\t"+bookName+"\t"+bookAuthor+"\t"+bookPbls+"\t"+bookPrice+"\t"+status;

	         return viewInfo;
	      }//end of public String viewBookInfo()
	   
	   public String viewNewBookInfo() {
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
		   int bookCategory_length = bookCategory.length();
		   if(bookCategory_length < 11 ) {
		         int length_space = 11 - bookCategory_length;
		         
		         String str_space = "";
		         
		         for(int i=0; i<length_space; i++) {
		            str_space += " ";
		         }
		         
		         bookCategory += str_space;
		      }
		   
			String viewInfo = ISBN+"\t"+bookId+"\t"+bookCategory+"\t"+bookName+"\t"+bookAuthor+"\t"+bookPbls+"\t"+bookPrice+"\t"+bookPblsDay;
			
			return viewInfo;
		}//end of public String viewNewBookInfo()
	   
	   
	   
	   
}
