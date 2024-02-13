package mini_4;

import java.util.*;

public interface InterBookDAO {

	int bookregister(BookDTO bdto);//도서정보등록

	int unitbookRegister(Map<String, String> paraMap);//개별도서등록

	boolean checkISBN(BookDTO bdto);//ISBN유무확인

	int setBookAgain(MemberDTO mdto, Scanner sc); // 대여중에서 비치중으로 변경 하는 메소드

	List<BookDTO> selectAllBook(BookDTO bdto);//나의대여현황보기
	
	int changeBookStatus(BookDTO bdto, MemberDTO mdto, Scanner sc); // 도서 상태 변경

	BookDTO getbookno(Map<String, String> paraMap);//입력한 ISBN과 일치하면서 bookid는 null인 것의 bookno불러오기

	List<BookDTO> bookList(Map<String, String> paraMap);// 검색된 도서 목록 나타내기

	List<BookDTO> newBookList(BookDTO book); //신간도서목록

	BookDTO checkBookId(Map<String, String> paraMap);// 도서 id 조회



	

}
