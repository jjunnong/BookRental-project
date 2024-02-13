--------------------------------------------------------------------------------
-- 미니프로젝트(도서대여) --

-- 사서 테이블(tbl_admin)
drop sequence adminseq;

create table tbl_admin
(adminseq       number        not null    -- 사서번호
,adminid        varchar2(30)  not null    -- 사서아이디
,passwd        varchar2(30)  not null    -- 암호
,name          varchar2(20)  not null    -- 사서이름
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



