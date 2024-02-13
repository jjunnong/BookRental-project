package mini_4;

public class MemberDTO {

   
   private String memberseq; // 회원번호
   private String userid; // 회원아이디
   private String passwd; // 회원암호
   private String name; // 회원명
   private String mobile;  // 연락처
   private String rentBook;  // 대여한책
   private int lateFee; // 연체료
   private String egisterday; // 가입일자 
   
   public String getMemberseq() {
      return memberseq;
   }
   
   public void setMemberseq(String memberseq) {
      this.memberseq = memberseq;
   }
   
   public String getUserid() {
      return userid;
   }
   
   public void setUserid(String userid) {
      this.userid = userid;
   }
   
   public String getPasswd() {
      return passwd;
   }
   
   public void setPasswd(String passwd) {
      this.passwd = passwd;
   }
   
   public String getName() {
      return name;
   }
   
   public void setName(String name) {
      this.name = name;
   }
   
   public String getMobile() {
      return mobile;
   }
   
   public void setMobile(String mobile) {
      this.mobile = mobile;
   }
   
   public String getRentBook() {
      return rentBook;
   }
   
   public void setRentBook(String rentBook) {
      this.rentBook = rentBook;
   }
   
   public int getLateFee() {
      return lateFee;
   }
   
   public void setLateFee(int lateFee) {
      this.lateFee = lateFee;
   }
   
   public String getEgisterday() {
      return egisterday;
   }
   
   public void setEgisterday(String egisterday) {
      this.egisterday = egisterday;
   }
   
}