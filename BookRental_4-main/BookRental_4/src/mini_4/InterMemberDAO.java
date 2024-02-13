package mini_4;

import java.util.*;

public interface InterMemberDAO {

	int memberRegister(MemberDTO member, Scanner sc);//일반회원가입

	MemberDTO loginmem(Map<String, String> paraMap);//일반회원 로그인,로그아웃

	boolean checkmemId(MemberDTO member);// 일반회원 아이디중복확인

	int returnBooksFee(MemberDTO mdto);// 연체료 구하기
	
	int howMuchFee(MemberDTO mdto); // 회원당 연체료 total




}
