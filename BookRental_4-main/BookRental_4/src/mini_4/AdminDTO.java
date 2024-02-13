package mini_4;

public class AdminDTO {

	// field
	private String adminseq; // 사서번호
	private String adminid; // 사서아이디
	private String passwd; // 암호
	private String registerday; // 가입일자 
	
	public String getAdminseq() {
		return adminseq;
	}
	public void setAdminseq(String adminseq) {
		this.adminseq = adminseq;
	}
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRegisterday() {
		return registerday;
	}
	public void setRegisterday(String registerday) {
		this.registerday = registerday;
	}
	
}
