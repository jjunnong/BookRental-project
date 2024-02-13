show user;
-- USER이(가) "SEMIUSER_4"입니다.


create table tbl_admin
(adminseq       number        not null    -- 사서번호
,adminid        varchar2(30)  not null    -- 사서아이디
,passwd        varchar2(30)  not null    -- 암호
,registerday   date default sysdate      -- 가입일자 
,constraint PK_tbl_admin primary key(adminseq)
,constraint UQ_tbl_admin unique(adminid)
);



create sequence adminseq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

select *
from tbl_admin;

select *
from tbl_member;

select *
from tbl_book;

alter table tbl_book modify(bookPrice varchar2(30));

select *
from user_tab_columns

select *
from all_tab_columns
where table_name = 'TBL_ADMIN';

insert into tbl_member(memberseq, userid,passwd,name,mobile)
values(memberseq.nextval, 'eomjh','1234','엄정화','010-1234-1234');

rollback

alter table tbl_book add whorent varchar2(30) ;

alter table tbl_admin drop column test;
                       
alter table tbl_book add test varchar2(30) ;     


select ISBN
from tbl_book
where ISBN = 'qq'


-- 책 테이블
create table tbl_book
(bookNo         varchar2(30)                    -- 고유번호
,ISBN           varchar2(30)                    -- 국제표준도서번호(ISBN)
,bookId         varchar2(30)                    -- 도서 아이디
,bookCategory   varchar2(30) not null           -- 도서분류카테고리  
,bookName       varchar2(100) not null          -- 도서명
,bookAuthor     varchar2(30) not null           -- 작가명
,bookPbls       varchar2(30) not null           -- 출판사
,bookPrice      number       not null           -- 책가격
,bookstatus     number(1) default 0             -- status 컬럼의 값이 0 이면 비치중, 1 이면 대여중
,bookRentDay    date                            -- 책 대여날짜
,bookPblsDay    varchar2(30) not null           -- 책 출판날짜
,bookRentMember varchar2(30)                    -- 책 대여해간 사람

,constraint PK_tbl_book primary key(bookNo)
,constraint UK_tbl_book unique key(bookId)
);

create sequence bookidseq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


insert into tbl_book(ISBN, bookCategory, bookName, bookAuthor, bookPbls, bookPblsDay, bookPrice, bookNo)
values ('111-22-33333-44-88','Programmin','JAVA기','서영','쌍출판사','2021-03-11',22000, bookidseq.nextval)

alter table tbl_book add constraints ISBN unique(ISBN);

-- 행지우기
delete from tbl_book
where bookno = 25;

commit;

select *
from tbl_book
order by 2, 3;

insert into tbl_book(ISBN, bookCategory, bookName, bookAuthor, bookPbls, bookPblsDay, bookPrice) 
values('111-22-33333-01-2','IT','Oracle재밌다','서영학','강북출판사','2021-03-12',30000)


commit;

alter table tbl_book drop column ISBN;

alter table tbl_book
drop column bookid;

alter table tbl_book add bookId varchar2(30);

alter table tbl_book add constraints UQ_bookId unique (bookId);

drop table tbl_book;

select to_char(BookRentDay+7,'yyyy-mm-dd')
from tbl_book

update tbl_book set bookrentday = '2021-02-12', bookrentmember = 'eomjh', bookstatus = 1
where bookid = '011-001'

commit;

update tbl_book set bookId='033-001'
where bookno = '14';

select  to_number(to_date(to_char(sysdate, 'yyyy-mm-dd')) - (to_date(to_char( B.bookrentday, 'yyyy-mm-dd')) +14) ) * 100 as LATEFEE
from tbl_member M JOIN tbl_book B
ON  M.userid =  B.bookrentmember
where B.bookid = '001-003';

desc user_constraints;